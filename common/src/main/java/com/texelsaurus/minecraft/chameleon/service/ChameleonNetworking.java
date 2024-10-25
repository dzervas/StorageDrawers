package com.texelsaurus.minecraft.chameleon.service;

import com.texelsaurus.minecraft.chameleon.ChameleonServices;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketHandler;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface ChameleonNetworking
{
    static <B extends FriendlyByteBuf, P extends ChameleonPacket<P>> void registerPacket(ResourceLocation id, ChameleonPacketHandler<P> payloadType) {
        ChameleonServices.NETWORK.registerPacketInternal(id, payloadType);
    }

    <P extends ChameleonPacket<P>> void registerPacketInternal(ResourceLocation id, ChameleonPacketHandler<P>  payloadType);

    <P extends ChameleonPacket<P>> void sendToPlayer(P packet, ServerPlayer player);

    <P extends ChameleonPacket<P>> void sendToPlayersNear(P packet, ServerLevel level, double x, double y, double z, double radius);

    <P extends ChameleonPacket<P>> void sendToServer(P packet);
}
