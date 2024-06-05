package com.mathiasyde.AssembleMod;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Utils {
    public static int getBurnTime(Item item) {
        // cannot use a switch statement here as the items are not constant
        // TODO(mathias): add all fuel sources

        if (item == Items.CHARCOAL || item == Items.COAL) {
            return 1600;
        }

        if (item == Items.STICK) {
            return 80; // four seconds
        }

        return 0;
    }
}
