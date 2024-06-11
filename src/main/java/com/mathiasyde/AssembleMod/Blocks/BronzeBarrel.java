package com.mathiasyde.AssembleMod.Blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BronzeBarrel extends Block {
    public final static DirectionProperty FACING_PROPERTY = BlockStateProperties.FACING;

    public BronzeBarrel() {
        super(BlockBehaviour.Properties.of());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_PROPERTY, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_PROPERTY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_PROPERTY, context.getClickedFace());
    }
}
