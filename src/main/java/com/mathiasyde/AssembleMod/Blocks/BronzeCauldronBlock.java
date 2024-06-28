package com.mathiasyde.AssembleMod.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BronzeCauldronBlock extends Block {
    public static final String NAME = "bronze_cauldron";
    private static final VoxelShape SHAPE = Block.box(2, 1, 2, 14, 10, 14);

    public BronzeCauldronBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
