package com.mathiasyde.AssembleMod.Blocks;

import com.mathiasyde.AssembleMod.AssembleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class BronzeCauldronBlockEntity extends BlockEntity implements Container {
    private NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    public BronzeCauldronBlockEntity(BlockPos pos, BlockState state) {
        super(AssembleMod.BRONZE_CAULDRON_BLOCK_ENTITY.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
        // TODO(mathias): add a transfer cooldown, just like a hopper
        if (entity instanceof BronzeCauldronBlockEntity cauldronBlockEntity) {
            List<ItemEntity> itemEntities = getItemsToSuckInFromAbove(level, pos);
            for (ItemEntity itemEntity : itemEntities) {
                // i don't know why the particle isn't showing up
                level.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                HopperBlockEntity.addItem(cauldronBlockEntity, itemEntity);
            }
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

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int quantity) {
        return ContainerHelper.removeItem(items, slot, quantity);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.items.set(slot, itemStack);
        if (itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }
}
