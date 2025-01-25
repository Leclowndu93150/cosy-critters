package com.leclowndu93150.cosycritters.mixin;

import com.leclowndu93150.cosycritters.CosyCritters;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WebBlock.class)
public class WebBlockMixin extends BlockMixin {

    @Override
    public void spawnCritters(BlockState state, Level level, BlockPos pos, RandomSource random) {
        CosyCritters.trySpawnSpider(level, pos);
    }
}