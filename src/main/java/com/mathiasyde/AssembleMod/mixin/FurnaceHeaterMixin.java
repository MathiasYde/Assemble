package com.mathiasyde.AssembleMod.mixin;

import com.mathiasyde.AssembleMod.Blocks.HeaterBlock;
import com.mathiasyde.AssembleMod.Datamodels.LitState;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FurnaceHeaterMixin {
    @Mixin(AbstractFurnaceBlockEntity.class)
    private static class AbstractFurnaceBlockEntityMixin {

        @Inject(method="getTotalCookTime", at=@At("RETURN"), cancellable = true)
        private static void modifyTotalCookTime(Level level, AbstractFurnaceBlockEntity entity, CallbackInfoReturnable<Integer> info) {
            int ticks = info.getReturnValue();

            BlockState blockStateBelow = level.getBlockState(entity.getBlockPos().below());
            System.out.println("Got BlockState: " + blockStateBelow.getBlock());
            if (blockStateBelow.getBlock() instanceof HeaterBlock heaterBlock) {
                LitState litState = blockStateBelow.getValue(HeaterBlock.LIT_STATE_PROPERTY);

                if (litState == LitState.FIRE) {
                    ticks = ticks / 2;
                } else if (litState == LitState.SOUL) {
                    ticks = ticks / 4;
                }
            }

            info.setReturnValue(ticks);
        }
    }
}
