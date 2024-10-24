package com.jaquadro.minecraft.storagedrawers.client.model;

import com.jaquadro.minecraft.storagedrawers.ModConstants;
import com.jaquadro.minecraft.storagedrawers.block.tile.modelprops.DrawerModelProperties;
import com.jaquadro.minecraft.storagedrawers.block.tile.modelprops.FramedModelProperties;
import com.jaquadro.minecraft.storagedrawers.client.model.context.DrawerModelContext;
import com.jaquadro.minecraft.storagedrawers.client.model.context.FramedModelContext;
import com.jaquadro.minecraft.storagedrawers.client.model.decorator.CombinedModelDecorator;
import com.jaquadro.minecraft.storagedrawers.client.model.decorator.DrawerModelDecorator;
import com.jaquadro.minecraft.storagedrawers.client.model.decorator.MaterialModelDecorator;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ModelLoadPlugin implements ModelLoadingPlugin
{
    @Override
    public void initialize (Context pluginContext) {
        DrawerModelGeometry.loadGeometryData();
        pluginContext.modifyModelAfterBake().register((original, context) -> {
            if (context.topLevelId() == null)
                return original;

            ResourceLocation blockId = context.topLevelId().id();
            if (!blockId.getNamespace().equals(ModConstants.MOD_ID))
                return original;

            DrawerModelStore.tryAddModel(context.topLevelId(), original);
            if (!DrawerModelStore.INSTANCE.isTargetedModel(context.topLevelId()))
                return original;

            if (blockId.equals(ModBlocks.FRAMED_FULL_DRAWERS_1.getId()))
                return makeFramedStandardDrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_FULL_DRAWERS_2.getId()))
                return makeFramedStandardDrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_FULL_DRAWERS_4.getId()))
                return makeFramedStandardDrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_HALF_DRAWERS_1.getId()))
                return makeFramedStandardDrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_HALF_DRAWERS_2.getId()))
                return makeFramedStandardDrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_HALF_DRAWERS_4.getId()))
                return makeFramedStandardDrawerModel(original);

            if (blockId.equals(ModBlocks.FRAMED_COMPACTING_DRAWERS_2.getId()))
                return makeFramedComp2DrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_COMPACTING_HALF_DRAWERS_2.getId()))
                return makeFramedComp2DrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_COMPACTING_DRAWERS_3.getId()))
                return makeFramedComp3DrawerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_COMPACTING_HALF_DRAWERS_3.getId()))
                return makeFramedComp3DrawerModel(original);

            if (blockId.equals(ModBlocks.FRAMED_TRIM.getId()))
                return makeFramedTrimModel(original);
            if (blockId.equals(ModBlocks.FRAMED_CONTROLLER.getId()))
                return makeFramedControllerModel(original);
            if (blockId.equals(ModBlocks.FRAMED_CONTROLLER_IO.getId()))
                return makeFramedControllerIOModel(original);

            return makeStandardDrawerModel(original);
        });
    }

    static BakedModel makeStandardDrawerModel(BakedModel parentModel) {
        DrawerModelDecorator decorator = new DrawerModelDecorator(DrawerModelStore.INSTANCE);
        return new PlatformDecoratedModel<>(parentModel, decorator, DrawerModelProperties.INSTANCE);
    }

    static BakedModel makeFramedDrawerModel (BakedModel parentModel, DrawerModelStore.FrameMatSet matSet) {
        CombinedModelDecorator<DrawerModelContext> decorator = new CombinedModelDecorator<>();
        decorator.add(new DrawerModelDecorator(DrawerModelStore.INSTANCE));
        decorator.add(new MaterialModelDecorator.FacingSizedSlotted<>(matSet, true));

        return new PlatformDecoratedModel<>(parentModel, decorator, DrawerModelProperties.INSTANCE);
    }

    static BakedModel makeFramedStandardDrawerModel(BakedModel parentModel) {
        return makeFramedDrawerModel(parentModel, DrawerModelStore.FramedStandardDrawerMaterials);
    }

    static BakedModel makeFramedCompDrawerModel (BakedModel parentModel, DrawerModelStore.FrameMatSet matSet) {
        CombinedModelDecorator<DrawerModelContext> decorator = new CombinedModelDecorator<>();
        decorator.add(new DrawerModelDecorator(DrawerModelStore.INSTANCE));
        decorator.add(new MaterialModelDecorator.FacingSizedOpen<>(matSet, true));

        return new PlatformDecoratedModel<>(parentModel, decorator, DrawerModelProperties.INSTANCE);
    }

    static BakedModel makeFramedComp2DrawerModel(BakedModel parentModel) {
        return makeFramedCompDrawerModel(parentModel, DrawerModelStore.FramedComp2DrawerMaterials);
    }

    static BakedModel makeFramedComp3DrawerModel(BakedModel parentModel) {
        return makeFramedCompDrawerModel(parentModel, DrawerModelStore.FramedComp3DrawerMaterials);
    }

    static BakedModel makeFramedTrimModel(BakedModel parentModel) {
        MaterialModelDecorator<FramedModelContext> decorator =
            new MaterialModelDecorator.Single<>(DrawerModelStore.FramedTrimMaterials, true);
        return new PlatformDecoratedModel<>(parentModel, decorator, FramedModelProperties.INSTANCE);
    }

    static BakedModel makeFramedControllerModel(BakedModel parentModel) {
        MaterialModelDecorator<FramedModelContext> decorator =
            new MaterialModelDecorator.Facing<>(DrawerModelStore.FramedControllerMaterials, true);
        return new PlatformDecoratedModel<>(parentModel, decorator, FramedModelProperties.INSTANCE);
    }

    static BakedModel makeFramedControllerIOModel(BakedModel parentModel) {
        MaterialModelDecorator<FramedModelContext> decorator =
            new MaterialModelDecorator.Single<>(DrawerModelStore.FramedControllerIOMaterials, true);
        return new PlatformDecoratedModel<>(parentModel, decorator, FramedModelProperties.INSTANCE);
    }
}
