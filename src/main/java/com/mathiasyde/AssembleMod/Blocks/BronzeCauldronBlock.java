package com.mathiasyde.AssembleMod.Blocks;

import com.mathiasyde.AssembleMod.AssembleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BronzeCauldronBlock extends Block implements EntityBlock {
    public static final String NAME = "bronze_cauldron";
    private static final VoxelShape SHAPE = Block.box(2, 1, 2, 14, 10, 14);
    public static final VoxelShape SUCK = Block.box(4, 4, 4, 12, 12, 12);

    public BronzeCauldronBlock() {
        super(BlockBehaviour.Properties.of()
                .noOcclusion()
                .requiresCorrectToolForDrops()
                .strength(1.2F, 4.0F)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        ItemStack itemStack = player.getItemInHand(hand);
        return InteractionResult.PASS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        List<ItemStack> drops = super.getDrops(pState, pParams);
        drops.add(new ItemStack(this));
        return drops;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (oldState.is(newState.getBlock()) == false) {
            if (level.getBlockEntity(pos) instanceof BronzeCauldronBlockEntity cauldronBlockEntity) {
                Containers.dropContents(level, pos, cauldronBlockEntity);
            }

            super.onRemove(oldState, level, pos, newState, movedByPiston);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BronzeCauldronBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : type == AssembleMod.BRONZE_CAULDRON_BLOCK_ENTITY.get() ? BronzeCauldronBlockEntity::tick : null;
    }
}
