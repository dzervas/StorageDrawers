package com.jaquadro.minecraft.storagedrawers.block.tile.modelprops;

import com.jaquadro.minecraft.storagedrawers.api.framing.IFramedBlockEntity;
import com.jaquadro.minecraft.storagedrawers.api.framing.IFramedMaterials;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.MaterialData;
import com.jaquadro.minecraft.storagedrawers.client.model.ModelContextSupplier;
import com.jaquadro.minecraft.storagedrawers.client.model.context.FramedModelContext;
import com.jaquadro.minecraft.storagedrawers.components.item.FrameData;
import com.jaquadro.minecraft.storagedrawers.core.ModDataComponents;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FramedModelProperties implements ModelContextSupplier<FramedModelContext>
{
    public static final FramedModelProperties INSTANCE = new FramedModelProperties();

    public final IFramedMaterials material;

    private FramedModelProperties () {
        material = null;
    }

    private FramedModelProperties (IFramedBlockEntity blockEntity) {
        material = blockEntity.material();
    }

    public static FramedModelProperties getModelData (IFramedBlockEntity blockEntity) {
        return new FramedModelProperties(blockEntity);
    }

    @Override
    public FramedModelContext makeContext (@Nullable BlockState state, @Nullable Direction side, RandomSource rand, Object renderData, @Nullable RenderType type) {
        FramedModelContext context = new FramedModelContext(state, side, rand, type);
        if (renderData instanceof FramedModelProperties props) {
            context.materialData(new MaterialData(props.material));
        }

        return context;
    }

    @Override
    public FramedModelContext makeContext (ItemStack stack) {
        MaterialData data = stack.getOrDefault(ModDataComponents.FRAME_DATA.get(), FrameData.EMPTY).asMaterialData();

        Block block = Blocks.AIR;
        if (stack.getItem() instanceof BlockItem blockItem)
            block = blockItem.getBlock();

        return new FramedModelContext(block.defaultBlockState())
            .materialData(data);
    }
}