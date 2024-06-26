package com.mathiasyde.AssembleMod.Blocks;

import com.mathiasyde.AssembleMod.AssembleMod;
import com.mathiasyde.AssembleMod.Datamodels.LitState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class HeaterBlockEntity extends BlockEntity {
    private int fuel;

    public HeaterBlockEntity(BlockPos pos, BlockState state) {
        super(AssembleMod.HEATER_BLOCK_ENTITY.get(), pos, state);
        this.fuel = 0;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T entity) {
        if (entity instanceof HeaterBlockEntity heaterEntity) {
            heaterEntity.tick(level, blockPos, blockState);
        }
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (fuel > 1) {
            fuel--;
            setChanged();
        } else if (fuel == 1) { // only update the block state if the fuel is exactly 1
            // preventing the block from updating every tick and also so you can change the block states
            // with the debug stick
            level.setBlockAndUpdate(blockPos, blockState.setValue(HeaterBlock.LIT_STATE_PROPERTY, LitState.OFF));
            fuel = 0;
        }
    }

    public void addFuel(int fuel) {
        this.fuel += fuel;
        setChanged();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("fuel", this.fuel);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.fuel = tag.getInt("fuel");
    }
}
