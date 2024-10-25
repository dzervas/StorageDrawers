package com.texelsaurus.minecraft.chameleon.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface ChameleonPacketType<T extends ChameleonPacket<T>>
{
    Class<T> type ();

    String modId ();

    ResourceLocation id ();

    void encode (T message, FriendlyByteBuf buffer);

    T decode (FriendlyByteBuf buffer);
}
