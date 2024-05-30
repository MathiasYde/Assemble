package com.example.PeaumarMod.Blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class HeaterBlock extends Block {
    public static final String NAME = "heater";

    public HeaterBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    }
}
