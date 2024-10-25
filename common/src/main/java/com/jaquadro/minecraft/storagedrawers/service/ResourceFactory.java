package com.jaquadro.minecraft.storagedrawers.service;

import com.jaquadro.minecraft.storagedrawers.block.tile.*;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface ResourceFactory
{
    BlockEntityType.BlockEntitySupplier<BlockEntityDrawersStandard> createBlockEntityDrawersStandard (int slotCount);
    BlockEntityType.BlockEntitySupplier<BlockEntityDrawersComp> createBlockEntityDrawersComp (int slotCount);
    BlockEntityType.BlockEntitySupplier<BlockEntityController> createBlockEntityController ();
    BlockEntityType.BlockEntitySupplier<BlockEntitySlave> createBlockEntityControllerIO ();
    BlockEntityType.BlockEntitySupplier<BlockEntityFramingTable> createBlockEntityFramingTable ();
    BlockEntityType.BlockEntitySupplier<BlockEntityTrim> createBlockEntityTrim ();
}
