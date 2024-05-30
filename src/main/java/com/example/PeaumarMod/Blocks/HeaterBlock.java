package com.example.PeaumarMod.Blocks;

import com.example.PeaumarMod.PeaumarMod;
import com.example.PeaumarMod.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

enum LitState implements StringRepresentable {
    OFF("off"),
    FIRE("fire"),
    SOUL("soul");

    private final String name;

    LitState(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}

public class HeaterBlock extends Block implements EntityBlock {
    public static final String NAME = "heater";

    public static final DirectionProperty FACING_PROPERTY = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<LitState> LIT_STATE_PROPERTY = EnumProperty.create("lit", LitState.class);

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

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        // TODO(mathias): this should probably be more sophisticated
        // TODO like holding shift to input the entire stack?
        
        int burnTime = Utils.getBurnTime(stack.getItem());
        if (!level.isClientSide && burnTime > 0) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof HeaterBlockEntity heaterBlockEntity) {
                heaterBlockEntity.addFuel(burnTime);
                level.setBlockAndUpdate(pos, state.setValue(LIT_STATE_PROPERTY, LitState.FIRE));

                // consume the item from the players inventory
                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                return ItemInteractionResult.CONSUME_PARTIAL;
            }
        }

        return ItemInteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HeaterBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == PeaumarMod.HEATER_BLOCK_ENTITY.get() ? HeaterBlockEntity::tick : null;
    }
}