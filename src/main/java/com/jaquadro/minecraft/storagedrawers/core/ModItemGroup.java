package com.jaquadro.minecraft.storagedrawers.core;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemGroup
{
    public static final CreativeModeTab STORAGE_DRAWERS = (new CreativeModeTab("storagedrawers")
    {
        @Override
        @NotNull
        public ItemStack makeIcon () {
            return new ItemStack(ModBlocks.OAK_FULL_DRAWERS_2.get());
        }
    });
}
