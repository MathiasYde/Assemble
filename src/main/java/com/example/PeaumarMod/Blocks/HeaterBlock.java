package com.example.PeaumarMod.Blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

enum LitState implements StringRepresentable {
    OFF,
    FIRE,
    SOUL;

    @Override
    public @NotNull String getSerializedName() {
        return switch (this.name()) {
            case "OFF" -> "off";
            case "FIRE" -> "fire";
            case "SOUL" -> "soul";
            default -> throw new IllegalStateException("Unexpected value: " + this.name());
        };
    }
}

public class HeaterBlock extends Block {
    public static final String NAME = "heater";

    private static final DirectionProperty FACING_PROPERTY = BlockStateProperties.HORIZONTAL_FACING;
    private static final EnumProperty<LitState> LIT_STATE_PROPERTY = EnumProperty.create("lit", LitState.class);

    public HeaterBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_PROPERTY, Direction.NORTH));    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_PROPERTY);
        builder.add(LIT_STATE_PROPERTY);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_PROPERTY, context.getHorizontalDirection().getOpposite());
    }
}
