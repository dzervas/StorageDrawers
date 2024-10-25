package com.jaquadro.minecraft.storagedrawers.block.tile.tiledata;

import net.minecraft.nbt.CompoundTag;

public abstract class BlockEntityDataShim
{
    public abstract void read (CompoundTag tag);

    public abstract CompoundTag write (CompoundTag tag);

    public CompoundTag serializeNBT () {
        CompoundTag tag = new CompoundTag();
        return write(tag);
    }

    public void deserializeNBT (CompoundTag nbt) {
        read(nbt);
    }

    public void invalidateCaps() {}
}
