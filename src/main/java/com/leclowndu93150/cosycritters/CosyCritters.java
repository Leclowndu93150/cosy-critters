package com.leclowndu93150.cosycritters;

import com.leclowndu93150.cosycritters.particle.MothParticle;
import com.leclowndu93150.cosycritters.registry.ParticleRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

@Mod(CosyCritters.MODID)
public class CosyCritters {
    public static final String MODID = "cosycritters";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean wasSleeping = false;
    public static int birdCount = 0;
    public static int maxBirdCount = 10;
    public static int mothCount = 0;
    public static int maxMothCount = 10;
    public static ArrayList<MothParticle> moths = new ArrayList<>();
    public static final TagKey<Block> SPIDER_SPAWNABLE = TagKey.create(Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(CosyCritters.MODID, "spider_spawnable"));

    public CosyCritters(IEventBus modEventBus, ModContainer modContainer) {
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        Config.register(modContainer);
    }

    public static boolean isDayButNotBroken(Level level) {
        return (level.dayTime() % 24000 < 13000);
    }

    public static void tickHatManSpawnConditions(Minecraft minecraft) {
        if (!Config.enableHatMan.get()) return;
        if (minecraft.level.dimensionType().moonPhase(minecraft.level.dayTime()) == 4) {
            if (minecraft.player.isSleeping()) {
                if (!wasSleeping) {
                    trySpawnHatman(minecraft);
                    wasSleeping = true;
                }
            } else if (wasSleeping) {
                wasSleeping = false;
            }
        }
    }

    private static void trySpawnHatman(Minecraft minecraft) {
        final Optional<BlockPos> sleepingPos = minecraft.player.getSleepingPos();
        if (sleepingPos.isPresent()) {
            BlockState state = minecraft.level.getBlockState(sleepingPos.get());
            Property property = BlockStateProperties.HORIZONTAL_FACING;
            if (state.hasProperty(property)) {
                Direction direction = (Direction) state.getValue(property);
                BlockPos blockPos = BlockPos.containing(minecraft.player.position()).relative(direction.getOpposite(), 2);
                Vec3 pos = blockPos.getCenter();
                RandomSource random = minecraft.player.getRandom();
                Vec3 randomPos = new Vec3(pos.x + random.nextInt(2) - 1, pos.y, pos.z + random.nextInt(2) - 1);
                if (minecraft.level.getBlockState(BlockPos.containing(randomPos)).isAir()) {
                    minecraft.particleEngine.createParticle(ParticleRegistry.HAT_MAN.get(), randomPos.x, randomPos.y + 0.5, randomPos.z, 0, 0, 0);
                }
            }
        }
    }

    public static void trySpawnBird(BlockState state, Level level, BlockPos blockPos) {
        if (!Config.enableBirds.get()) return;
        if (isDayButNotBroken(level)
                && birdCount < Config.maxBirds.get()
                && level.getBlockState(blockPos.above()).isAir()
                && !Minecraft.getInstance().player.position().closerThan(blockPos.getCenter(), 10)) {
            Vec3 pos = blockPos.getCenter();
            pos = state.getCollisionShape(level, blockPos).clip(pos.add(0, 2, 0), pos.add(0, -0.6, 0), blockPos).getLocation();
            Vec3 spawnFrom = pos.add(level.random.nextInt(10) - 5, level.random.nextInt(5), level.random.nextInt(10) - 5);
            if (level.clip(new ClipContext(spawnFrom, pos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty())).getType().equals(HitResult.Type.MISS)) {
                level.addParticle(ParticleRegistry.BIRD.get(), spawnFrom.x, spawnFrom.y, spawnFrom.z, pos.x, pos.y, pos.z);
            }
        }
    }

    public static void trySpawnMoth(Level level, BlockPos blockPos) {
        if (!Config.enableMoths.get()) return;
        if (!isDayButNotBroken(level)
                && mothCount < Config.maxMoths.get()
                && level.canSeeSky(blockPos)) {
            level.addParticle(ParticleRegistry.MOTH.get(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0, 0, 0);
        }
    }

    public static void trySpawnSpider(Level level, BlockPos blockPos) {
        if (!level.isClientSide()) return;
        if (!Config.enableSpiders.get()) return;
        if (Minecraft.getInstance().player.position().closerThan(blockPos.getCenter(), 2)) return;

        Direction direction = Direction.getRandom(level.random);
        blockPos = blockPos.relative(direction);
        BlockState state = level.getBlockState(blockPos);

        if (state.isFaceSturdy(level, blockPos, direction.getOpposite())) {
            if(level.getBlockState(blockPos.above()).is(Blocks.COBWEB)){
                final Vec3 spawnPos = blockPos.getCenter().add(new Vec3(direction.step()).multiply(-0.6f, -0.6f, -0.6f));
                level.addParticle(ParticleRegistry.SPIDER.get(), spawnPos.x, spawnPos.y, spawnPos.z, direction.get3DDataValue(), 0, 0);
            } else {
                Direction randomOffset = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
                final Vec3 spawnPos = blockPos.getCenter().add(new Vec3(direction.step()).multiply(-0.6f, -0.6f, -0.6f))
                        .add(new Vec3(randomOffset.step()).multiply(0.5f, 0, 0.5f));
                level.addParticle(ParticleRegistry.SPIDER.get(), spawnPos.x, spawnPos.y, spawnPos.z, direction.get3DDataValue(), 0, 0);
            }
        }
    }
}