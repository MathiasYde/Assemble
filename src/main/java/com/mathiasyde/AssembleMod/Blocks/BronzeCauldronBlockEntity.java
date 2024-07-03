package com.mathiasyde.AssembleMod.Blocks;

import com.mathiasyde.AssembleMod.AssembleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.stream.Collectors;

public class BronzeCauldronBlockEntity extends BlockEntity {
    public BronzeCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(AssembleMod.BRONZE_CAULDRON_BLOCK_ENTITY.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        if (entity instanceof BronzeCauldronBlockEntity cauldronBlockEntity) {
            List<ItemEntity> itemEntities = getItemsToSuckInFromAbove(level, pos);
        }
    }

    private static List<ItemEntity> getItemsToSuckInFromAbove(Level level, BlockPos pos) {
        return BronzeCauldronBlock.SUCK.toAabbs().stream().flatMap(
                (aabb) -> level.getEntitiesOfClass(
                        ItemEntity.class,
                        aabb.move(pos),
                        EntitySelector.ENTITY_STILL_ALIVE).stream()
            ).collect(Collectors.toList());
    }
}
