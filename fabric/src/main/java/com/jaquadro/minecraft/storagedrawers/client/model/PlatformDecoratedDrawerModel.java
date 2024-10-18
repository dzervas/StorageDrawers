package com.jaquadro.minecraft.storagedrawers.client.model;

import com.jaquadro.minecraft.storagedrawers.client.model.context.DrawerModelContext;
import com.jaquadro.minecraft.storagedrawers.client.model.context.ModelContext;
import com.jaquadro.minecraft.storagedrawers.client.model.decorator.ModelDecorator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class PlatformDecoratedDrawerModel<C extends ModelContext> extends ParentModel implements FabricBakedModel
{
    private final ModelDecorator<C> decorator;
    private final ModelContextSupplier<C> contextSupplier;

    public PlatformDecoratedDrawerModel (BakedModel parent, ModelDecorator<C> decorator, ModelContextSupplier<C> contextSupplier) {
        super(parent);
        this.decorator = decorator;
        this.contextSupplier = contextSupplier;
    }

    @Override
    public boolean isVanillaAdapter () {
        return false;
    }

    @Override
    public void emitItemQuads (ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        mainModel.emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public void emitBlockQuads (BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        if (state == null) {
            parent.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            return;
        }

        FabricBlockView fabricView = blockView;
        if (fabricView == null)
            return;

        Object renderData = fabricView.getBlockEntityRenderData(pos);
        Supplier<C> supplier = () -> contextSupplier.makeContext(state, null, randomSupplier.get(), renderData, null);

        if (decorator.shouldRenderBase(supplier))
            parent.emitBlockQuads(blockView, state, pos, randomSupplier, context);

        Consumer<BakedModel> emitModel = model -> {
            if (model != null)
                model.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        };

        try {
            decorator.emitQuads(supplier, emitModel);
        } catch (Exception e) { }
    }
}
