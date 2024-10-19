package com.jaquadro.minecraft.storagedrawers.block.tile;

import com.jaquadro.minecraft.storagedrawers.block.tile.modelprops.FramedModelProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlatformBlockEntityTrim extends BlockEntityTrim
{
    public PlatformBlockEntityTrim (BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @Nullable Object getRenderData () {
        return FramedModelProperties.getModelData(this);
    }
}
