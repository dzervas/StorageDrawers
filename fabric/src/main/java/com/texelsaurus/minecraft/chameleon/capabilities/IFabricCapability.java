package com.texelsaurus.minecraft.chameleon.capabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Function;

public interface IFabricCapability<T> extends ChameleonCapability<T>
{
    <BE extends BlockEntity> T getCapability(BE blockEntity);

    <BE extends BlockEntity> void register (BlockEntityType<BE> entity, Function<BE, T> provider);
}
