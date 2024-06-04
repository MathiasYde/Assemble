package com.example.PeaumarMod.mixin;

import com.example.PeaumarMod.Blocks.HeaterBlock;
import com.example.PeaumarMod.Blocks.LitState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FurnaceHeaterMixin {
    @Mixin(AbstractFurnaceBlockEntity.class)
    private static class AbstractFurnaceBlockEntityMixin {

        @Inject(method="getTotalCookTime", at=@At("RETURN"), cancellable = true)
        private static void injected(CallbackInfoReturnable<Integer> info) {
            info.setReturnValue(10);
        }
    }

    @Mixin(FurnaceBlock.class)
    public static class FurnaceBlockMixin {
        @Unique
        public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
            BlockState blockStateBelow = world.getBlockState(pos.below());
            if (blockStateBelow.getBlock() instanceof HeaterBlock heaterBlock) {
                LitState litState = blockStateBelow.getValue(HeaterBlock.LIT_STATE_PROPERTY);
                System.out.println("Furnace: Heater below is " + litState.getSerializedName() + "!");
            }
        }
    }
}