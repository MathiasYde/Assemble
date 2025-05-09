package com.mathiasyde.AssembleMod.Blocks;

import com.mathiasyde.AssembleMod.AssembleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidVesselBlockEntity extends BlockEntity {
    private final FluidTank fluidTank = new FluidTank(8000) {
        @Override
        protected void onContentsChanged() {
            System.out.println("FluidVesselBlockEntity.fluidTank.onContentsChanged");
            System.out.println("fluidTank.getFluidAmount() = " + fluidTank.getFluidAmount());
            setChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return stack.getFluid() == Fluids.WATER;
        }
    };

    public FluidVesselBlockEntity(BlockPos pos, BlockState state) {
        super(AssembleMod.BRONZE_BARREL_BLOCK_ENTITY.get(), pos, state);
    }


    private LazyOptional<IFluidHandler> fluidHandler = LazyOptional.empty();

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        fluidHandler = LazyOptional.of(() -> fluidTank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        fluidHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag = fluidTank.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        fluidTank.readFromNBT(pTag);
    }

    public boolean tryFillFluidTank(@NotNull IFluidHandlerItem itemFluidHandler, int drain) {
        int availableSpace = fluidTank.getCapacity() - fluidTank.getFluidAmount();
        if (availableSpace < drain) {
            return false;
        }

        if (fluidTank.isFluidValid(
                itemFluidHandler.drain(drain, IFluidHandler.FluidAction.SIMULATE)
        )) {
            FluidStack fluidStack = itemFluidHandler.drain(drain, IFluidHandler.FluidAction.EXECUTE);
            fluidTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            return true;
        }

        return false;
    }

    public int getFluidAmount() {
        return fluidTank.getFluidAmount();
    }
}
