package com.example.PeaumarMod.Blocks;

import net.minecraft.util.StringRepresentable;

public enum LitState implements StringRepresentable {
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
