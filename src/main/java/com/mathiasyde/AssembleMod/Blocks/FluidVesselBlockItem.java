package com.mathiasyde.AssembleMod.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidVesselBlockItem extends BlockItem {
    public FluidVesselBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        final int WHITE = 0xFFFFFF;
        final int GRAY = 0x666666; // TODO: match the gray with Create mod's gray

        final int fluidMilliBuckets = 27000;
        final int maxMilliBuckets = 32000;

        pTooltipComponents.add(Component.literal("Holds " + maxMilliBuckets + "mB of fluid").withStyle(s -> s.withItalic(true)));

        // this adds a line of vertical bars to indicate how much fluid is in the vessel
        // example -> Water: (in white)||||||||||||||||||||| (in gray)|||||||||||
        MutableComponent levelIndicator = Component.literal("Water: ").append(
                Component.literal("|".repeat(fluidMilliBuckets / 1000))
                        .withStyle(s -> s.withColor(WHITE))
                        .append(
                                Component.literal("|".repeat((maxMilliBuckets - fluidMilliBuckets) / 1000))
                                        .withStyle(s -> s.withColor(GRAY))
                        )
        );

        if (pIsAdvanced.isAdvanced()) {
            levelIndicator.append(Component.literal(" (" + fluidMilliBuckets + "mB)"));
        }

        pTooltipComponents.add(levelIndicator);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
