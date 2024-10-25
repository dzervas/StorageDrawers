package com.jaquadro.minecraft.storagedrawers.block.tile;

import com.jaquadro.minecraft.storagedrawers.block.tile.modelprops.FramedModelProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PlatformBlockEntityController extends BlockEntityController
{
    public PlatformBlockEntityController (BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public @Nullable Object getRenderData () {
        return FramedModelProperties.getModelData(this);
    }
}
