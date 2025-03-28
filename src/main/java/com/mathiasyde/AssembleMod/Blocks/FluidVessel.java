package com.mathiasyde.AssembleMod.Blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class FluidVessel extends Block implements EntityBlock {
    public static final String NAME = "bronze_barrel";

    public final static DirectionProperty FACING_PROPERTY = BlockStateProperties.FACING;

    public FluidVessel() {
        super(BlockBehaviour.Properties.of());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_PROPERTY, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING_PROPERTY);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // for debug purposes
        if (player.isCrouching() && level.getBlockEntity(pos) instanceof FluidVesselBlockEntity fluidVesselBlockEntity) {
            int fluidAmount = fluidVesselBlockEntity.getFluidAmount();
            player.displayClientMessage(Component.literal("Fluid Amount = " + fluidAmount + "mB"), true);
        }

        System.out.println("FluidVessel.use");
        System.out.println("state = " + state + ", level = " + level + ", pos = " + pos + ", player = " + player + ", hand = " + hand + ", hit = " + hit);

        if (level.isClientSide == false && (level.getBlockEntity(pos) instanceof FluidVesselBlockEntity entity)) {
            ItemStack itemStack = player.getItemInHand(hand);
            System.out.println("itemStack = " + itemStack);

//            if (itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM) instanceof IFluidHandlerItem itemFluidHandler) {
//                System.out.println("itemFluidHandler = " + itemFluidHandler);
//
//                int drain = 1000;
//                if (entity.getFluidTank().isFluidValid(
//                        itemFluidHandler.drain(drain, IFluidHandler.FluidAction.SIMULATE)
//                )) {
//                    System.out.println("fluid was valid");
//                    FluidStack fluidStack = itemFluidHandler.drain(drain, IFluidHandler.FluidAction.EXECUTE);
//                    entity.getFluidTank().fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
//                    return InteractionResult.SUCCESS;
//                }
//            }

            AtomicReference<InteractionResult> interactionResult = new AtomicReference<>(InteractionResult.PASS);

            itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(itemFluidHandler -> {
                int drain = 1000;

                if (entity.tryFillFluidTank(itemFluidHandler, drain)) {
                    interactionResult.set(InteractionResult.SUCCESS);
                }
            });

            return interactionResult.get();
        }

        return InteractionResult.PASS;
    }

    static InteractionResult emptyBucket(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack filledStack, BlockState state, SoundEvent emptySound) {
        if (level.isClientSide == false) {
            Item item = filledStack.getItem();
            player.setItemInHand(hand, ItemUtils.createFilledResult(filledStack, player, new ItemStack(Items.BUCKET)));
            player.awardStat(Stats.ITEM_USED.get(item));
            level.setBlockAndUpdate(pos, state);
//            level.playSound(null, pos, emptySound, SoundSource.BLOCKS, 1.0F, 1.0F);
//            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING_PROPERTY, context.getClickedFace());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluidVesselBlockEntity(pos, state);
    }

//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//        return level.isClientSide ? null : type == AssembleMod.BRONZE_BARREL_BLOCK_ENTITY.get() ? FluidVesselBlockEntity::tick : null;
//    }
}
