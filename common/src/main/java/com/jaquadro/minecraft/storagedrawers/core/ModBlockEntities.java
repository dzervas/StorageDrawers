package com.jaquadro.minecraft.storagedrawers.core;

import com.jaquadro.minecraft.storagedrawers.ModConstants;
import com.jaquadro.minecraft.storagedrawers.ModServices;
import com.jaquadro.minecraft.storagedrawers.block.BlockCompDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockDrawers;
import com.jaquadro.minecraft.storagedrawers.block.BlockStandardDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.*;
import com.texelsaurus.minecraft.chameleon.ChameleonServices;
import com.texelsaurus.minecraft.chameleon.api.ChameleonInit;
import com.texelsaurus.minecraft.chameleon.registry.ChameleonRegistry;
import com.texelsaurus.minecraft.chameleon.registry.RegistryEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.*;
import java.util.stream.Stream;

public final class ModBlockEntities {
    public static final ChameleonRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ChameleonServices.REGISTRY.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModConstants.MOD_ID);

    public static final Set<RegistryEntry<? extends BlockEntityType<? extends BlockEntityDrawers>>> DRAWER_TYPES = new HashSet<>();
    public static final Set<RegistryEntry<? extends BlockEntityType<? extends BlockEntityDrawers>>> FRAMING_TABLE_TYPES = new HashSet<>();

    public static final RegistryEntry<BlockEntityType<BlockEntityDrawersStandard>> STANDARD_DRAWERS_1 = registerDrawerBlockEntityType("standard_drawers_1", ModServices.RESOURCE_FACTORY.createBlockEntityDrawersStandard(1), BlockStandardDrawers.class, 1);
    public static final RegistryEntry<BlockEntityType<BlockEntityDrawersStandard>> STANDARD_DRAWERS_2 = registerDrawerBlockEntityType("standard_drawers_2", ModServices.RESOURCE_FACTORY.createBlockEntityDrawersStandard(2), BlockStandardDrawers.class, 2);
    public static final RegistryEntry<BlockEntityType<BlockEntityDrawersStandard>> STANDARD_DRAWERS_4 = registerDrawerBlockEntityType("standard_drawers_4", ModServices.RESOURCE_FACTORY.createBlockEntityDrawersStandard(4), BlockStandardDrawers.class, 4);
    public static final RegistryEntry<BlockEntityType<BlockEntityDrawersComp>> FRACTIONAL_DRAWERS_2 = registerDrawerBlockEntityType("fractional_drawers_2", ModServices.RESOURCE_FACTORY.createBlockEntityDrawersComp(2), BlockCompDrawers.class, 2);
    public static final RegistryEntry<BlockEntityType<BlockEntityDrawersComp>> FRACTIONAL_DRAWERS_3 = registerDrawerBlockEntityType("fractional_drawers_3", ModServices.RESOURCE_FACTORY.createBlockEntityDrawersComp(3), BlockCompDrawers.class, 3);

    public static final RegistryEntry<BlockEntityType<BlockEntityController>> CONTROLLER = registerControllerBlockEntityType("controller", ModServices.RESOURCE_FACTORY.createBlockEntityController());
    public static final RegistryEntry<BlockEntityType<BlockEntityControllerIO>> CONTROLLER_IO = registerControllerIOBlockEntityType("controller_io", ModServices.RESOURCE_FACTORY.createBlockEntityControllerIO());

    public static final RegistryEntry<BlockEntityType<BlockEntityTrim>> TRIM = BLOCK_ENTITIES.register("trim", () ->
        new BlockEntityType<>(ModServices.RESOURCE_FACTORY.createBlockEntityTrim(), Set.of(ModBlocks.FRAMED_TRIM.get())));

    public static final RegistryEntry<BlockEntityType<BlockEntityFramingTable>> FRAMING_TABLE = BLOCK_ENTITIES.register("framing_table", () ->
        new BlockEntityType<>(ModServices.RESOURCE_FACTORY.createBlockEntityFramingTable(), Set.of(ModBlocks.FRAMING_TABLE.get())));

    private ModBlockEntities() {}

    private static <BE extends BlockEntityDrawers, B extends BlockDrawers> RegistryEntry<BlockEntityType<BE>> registerDrawerBlockEntityType(String name, BlockEntityType.BlockEntitySupplier<BE> blockEntitySupplier, Class<B> drawerBlockClass, int size) {
        RegistryEntry<BlockEntityType<BE>> ro = registerBlockEntityType(name, blockEntitySupplier, drawerBlockClass, size);
        DRAWER_TYPES.add(ro);
        return ro;
    }

    private static <BE extends BaseBlockEntity, B extends BlockDrawers> RegistryEntry<BlockEntityType<BE>> registerBlockEntityType(String name, BlockEntityType.BlockEntitySupplier<BE> blockEntitySupplier, Class<B> drawerBlockClass, int size) {
        return BLOCK_ENTITIES.register(name, () ->
            new BlockEntityType<>(blockEntitySupplier, Set.of(ModBlocks.getDrawersOfTypeAndSize(drawerBlockClass, size).toArray(Block[]::new))));
    }

    private static <BE extends BaseBlockEntity, B extends BlockDrawers> RegistryEntry<BlockEntityType<BE>> registerControllerBlockEntityType(String name, BlockEntityType.BlockEntitySupplier<BE> blockEntitySupplier) {
        return BLOCK_ENTITIES.register(name, () ->
            new BlockEntityType<>(blockEntitySupplier, Set.of(ModBlocks.getControllers().toArray(Block[]::new))));
    }

    private static <BE extends BaseBlockEntity, B extends BlockDrawers> RegistryEntry<BlockEntityType<BE>> registerControllerIOBlockEntityType(String name, BlockEntityType.BlockEntitySupplier<BE> blockEntitySupplier) {
        return BLOCK_ENTITIES.register(name, () ->
            new BlockEntityType<>(blockEntitySupplier, Set.of(ModBlocks.getControllerIOs().toArray(Block[]::new))));
    }

    public static void init(ChameleonInit.InitContext context) {
        BLOCK_ENTITIES.init(context);
    }

    //public static Set<RegistryEntry<? extends BlockEntityType<? extends BlockEntityDrawers>>> getBlockEntityTypesWithRenderers() {
    //    return Collections.unmodifiableSet(BLOCK_ENTITY_TYPES_WITH_RENDERERS);
    //}

    public static Stream<BlockEntityType<? extends BlockEntityDrawers>> getDrawerTypes() {
        return DRAWER_TYPES.stream().map(RegistryEntry::get);
    }
}
