package com.jaquadro.minecraft.storagedrawers.components.item;

import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.MaterialData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record FrameData (ItemStack base, ItemStack side, ItemStack trim, ItemStack front)
{
    public static final FrameData EMPTY = new FrameData();

    public static final Codec<FrameData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            ItemStack.CODEC.fieldOf("base").forGetter(FrameData::base),
            ItemStack.CODEC.fieldOf("side").forGetter(FrameData::side),
            ItemStack.CODEC.fieldOf("trim").forGetter(FrameData::trim),
            ItemStack.CODEC.fieldOf("front").forGetter(FrameData::front)
        ).apply(instance, FrameData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FrameData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC,
        FrameData::base,
        ItemStack.OPTIONAL_STREAM_CODEC,
        FrameData::side,
        ItemStack.OPTIONAL_STREAM_CODEC,
        FrameData::trim,
        ItemStack.OPTIONAL_STREAM_CODEC,
        FrameData::front,
        FrameData::new
    );

    public FrameData () {
        this(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public FrameData (MaterialData data) {
        this(data.getFrameBase(), data.getSide(), data.getTrim(), data.getFront());
    }

    public MaterialData asMaterialData() {
        return new MaterialData(base, side, front, trim);
    }
}
