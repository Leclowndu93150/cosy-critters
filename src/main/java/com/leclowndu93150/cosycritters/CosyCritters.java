package com.leclowndu93150.cosycritters;

import com.leclowndu93150.cosycritters.particle.BirdParticle;
import com.leclowndu93150.cosycritters.registry.ParticleRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.joml.Vector3d;
import org.slf4j.Logger;

import java.util.Optional;

@Mod(CosyCritters.MODID)
public class CosyCritters {
    public static final String MODID = "cosycritters";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static boolean wasSleeping = false;
    public static int mothCount = 0;
    public static int spiderCount = 0;
    public static final TagKey<Block> SPIDER_SPAWNABLE = TagKey.create(Registries.BLOCK,
            Identifier.fromNamespaceAndPath(CosyCritters.MODID, "spider_spawnable"));

    private static final TagKey<Biome> C_IS_FOREST = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_forest"));
    private static final TagKey<Biome> C_IS_TAIGA = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_taiga"));
    private static final TagKey<Biome> C_IS_JUNGLE = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_jungle"));

    public CosyCritters(IEventBus modEventBus, ModContainer modContainer) {
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        Config.register(modContainer);
    }

    public static void tickHatManSpawnConditions(Minecraft minecraft) {
        if (!Config.enableHatMan.get()) return;
        if (isNewMoon(minecraft.level)) {
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

    private static boolean isNewMoon(ClientLevel level) {
        return Minecraft.getInstance().gameRenderer.getGameRenderState()
                .levelRenderState.skyRenderState.moonPhase
                .equals(net.minecraft.world.level.MoonPhase.NEW_MOON);
    }

    private static void trySpawnHatman(Minecraft minecraft) {
        if (minecraft.level.getRandom().nextFloat() < 0.1) return;
        Optional<BlockPos> sleepingPos = minecraft.player.getSleepingPos();
        if (sleepingPos.isPresent()) {
            BlockState state = minecraft.level.getBlockState(sleepingPos.get());
            Property<Direction> property = BlockStateProperties.HORIZONTAL_FACING;
            if (state.hasProperty(property)) {
                Direction direction = state.getValue(property);
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

    public static void trySpawnBird(ClientLevel level) {
        if (!Config.enableBirds.get()) return;
        if (!level.isBrightOutside()) return;
        if (BirdParticle.birds.size() >= Config.maxBirds.get()) return;
        if (level.isRaining() && level.getRandom().nextFloat() > 0.01) return;

        int despawnDist = Config.birdDespawnDistance.get();
        int reactionDist = Config.birdReactionDistance.get();
        Vec3 player = Minecraft.getInstance().player.position();
        int x = level.getRandom().nextIntBetweenInclusive((int) (player.x - despawnDist), (int) (player.x + despawnDist));
        int z = level.getRandom().nextIntBetweenInclusive((int) (player.z - despawnDist), (int) (player.z + despawnDist));
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        if (Vector3d.distance(x, y, z, player.x, player.y, player.z) > reactionDist
                && isBirdBiome(level, BlockPos.containing(x, y, z))) {
            Minecraft.getInstance().particleEngine.add(new BirdParticle(level, x + 0.5F, y + 0.5F, z + 0.5F));
        }
    }

    private static boolean isBirdBiome(ClientLevel level, BlockPos pos) {
        var biome = level.getBiome(pos);
        return biome.is(C_IS_FOREST) || biome.is(C_IS_TAIGA) || biome.is(C_IS_JUNGLE);
    }

    public static void trySpawnMoth(Level level, BlockPos blockPos) {
        if (!Config.enableMoths.get()) return;
        if (!level.isBrightOutside()
                && mothCount < Config.maxMoths.get()
                && level.getBiome(blockPos).is(BiomeTags.IS_OVERWORLD)
                && level.getBrightness(LightLayer.BLOCK, blockPos) > 13
                && level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) <= blockPos.getY()) {
            level.addParticle(ParticleRegistry.MOTH.get(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0, 0, 0);
        }
    }

    public static void trySpawnSpider(Level level, BlockPos blockPos) {
        if (!level.isClientSide()) return;
        if (!Config.enableSpiders.get()) return;
        if (spiderCount >= Config.maxSpiders.get()) return;
        if (Minecraft.getInstance().player.position().closerThan(blockPos.getCenter(), 2)) return;

        Direction direction = Direction.getRandom(level.getRandom());
        blockPos = blockPos.relative(direction);
        BlockState state = level.getBlockState(blockPos);

        if (state.isFaceSturdy(level, blockPos, direction.getOpposite())) {
            Vec3 spawnPos = blockPos.getCenter().add(new Vec3(direction.step()).multiply(-0.6f, -0.6f, -0.6f));
            level.addParticle(ParticleRegistry.SPIDER.get(), spawnPos.x, spawnPos.y, spawnPos.z, direction.get3DDataValue(), 0, 0);
        }
    }
}
