package com.jaquadro.minecraft.storagedrawers.client.model;

import com.jaquadro.minecraft.storagedrawers.client.model.context.ModelContext;
import com.jaquadro.minecraft.storagedrawers.client.model.decorator.ModelDecorator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class PlatformDecoratedModel<C extends ModelContext> extends ParentModel implements FabricBakedModel
{
    private final ModelDecorator<C> decorator;
    private final ModelContextSupplier<C> contextSupplier;

    private static Map<BakedModel, Mesh> meshCache = new HashMap<>();
    private static RenderMaterial cutoutMat;
    private static RenderMaterial transMat;

    public PlatformDecoratedModel (BakedModel parent, ModelDecorator<C> decorator, ModelContextSupplier<C> contextSupplier) {
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
        Supplier<C> supplier = () -> contextSupplier.makeContext(stack);

        if (decorator.shouldRenderBase(supplier, stack))
            parent.emitItemQuads(stack, randomSupplier, context);

        RandomSource randomSource = randomSupplier.get();

        BiConsumer<BakedModel, RenderType> emitModel = (model, renderType) -> {
            if (model != null) {
                if (renderType == RenderType.translucent()) {
                    if (stack.getItem() instanceof BlockItem bi) {
                        Mesh mesh = getMesh(model, bi.getBlock().defaultBlockState(), randomSource, renderType);
                        mesh.outputTo(context.getEmitter());
                    }
                } else
                    model.emitItemQuads(stack, randomSupplier, context);
            }
        };

        try {
            decorator.emitItemQuads(supplier, emitModel, stack);
        } catch (Exception e) { }
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

        RandomSource randomSource = randomSupplier.get();

        Object renderData = fabricView.getBlockEntityRenderData(pos);
        Supplier<C> supplier = () -> contextSupplier.makeContext(state, null, randomSource, renderData, null);

        if (decorator.shouldRenderBase(supplier))
            parent.emitBlockQuads(blockView, state, pos, randomSupplier, context);

        BiConsumer<BakedModel, RenderType> emitModel = (model, renderType) -> {
            if (model != null) {
                if (renderType == RenderType.translucent()) {
                    Mesh mesh = getMesh(model, state, randomSource, renderType);
                    mesh.outputTo(context.getEmitter());
                } else
                    model.emitBlockQuads(blockView, state, pos, randomSupplier, context);
            }
        };

        try {
            decorator.emitQuads(supplier, emitModel);
        } catch (Exception e) { }
    }

    private Mesh getMesh (BakedModel model, BlockState state, RandomSource randomSource, RenderType renderType) {
        if (meshCache.containsKey(model))
            return meshCache.get(model);

        Mesh mesh = buildMesh(model, state, randomSource, renderType);
        meshCache.put(model, mesh);
        return mesh;
    }

    private Mesh buildMesh (BakedModel model, BlockState state, RandomSource randomSource, RenderType renderType) {
        Renderer render = RendererAccess.INSTANCE.getRenderer();
        RenderMaterial mat = null;

        if (renderType == RenderType.cutoutMipped()) {
            if (cutoutMat == null)
                cutoutMat = render.materialFinder().blendMode(BlendMode.CUTOUT_MIPPED).find();
            mat = cutoutMat;
        }
        else if (renderType == RenderType.translucent()) {
            if (transMat == null)
                transMat = render.materialFinder().blendMode(BlendMode.TRANSLUCENT).find();
            mat = transMat;
        }

        if (mat == null)
            return null;

        MeshBuilder builder = render.meshBuilder();
        QuadEmitter quadEmit = builder.getEmitter();

        for (var d : Direction.values()) {
            for (var quad : model.getQuads(state, d, randomSource)) {
                quadEmit.fromVanilla(quad, mat, d).emit();
            }
        }
        for (var quad : model.getQuads(state, null, randomSource)) {
            quadEmit.fromVanilla(quad, mat, null).emit();
        }

        return builder.build();
    }
}
