package com.jaquadro.minecraft.storagedrawers.block.tile;

import com.jaquadro.minecraft.storagedrawers.block.tile.modelprops.DrawerModelProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class PlatformBlockEntityDrawersComp extends BlockEntityDrawersComp
{
    public PlatformBlockEntityDrawersComp (BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public static class Slot2 extends BlockEntityDrawersComp.Slot2 {
        public Slot2 (BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @Nullable Object getRenderData () {
            return DrawerModelProperties.getModelData(this);
        }
    }

    public static class Slot3 extends BlockEntityDrawersComp.Slot3 {
        public Slot3 (BlockPos pos, BlockState state) {
            super(pos, state);
        }

        @Override
        public @Nullable Object getRenderData () {
            return DrawerModelProperties.getModelData(this);
        }
    }
}
