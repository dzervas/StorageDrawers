package com.jaquadro.minecraft.storagedrawers.api.framing;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IFramedMaterials
{
    @NotNull
    ItemStack getHostBlock ();

    void setHostBlock (@NotNull ItemStack stack);

    @NotNull
    ItemStack getMaterial (FrameMaterial material);

    void setMaterial (FrameMaterial material, @NotNull ItemStack stack);
}
