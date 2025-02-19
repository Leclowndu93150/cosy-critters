package com.leclowndu93150.cosycritters.mixin;

import com.leclowndu93150.cosycritters.CosyCritters;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Shadow @Final private Holder.Reference<Block> builtInRegistryHolder;

    @Unique
    public void spawnCritters(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (this.builtInRegistryHolder.is(CosyCritters.SPIDER_SPAWNABLE)) {
            CosyCritters.trySpawnSpider(level, pos);
        }
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (!level.isClientSide()) return;
        this.spawnCritters(state, level, pos, random);
    }
}
