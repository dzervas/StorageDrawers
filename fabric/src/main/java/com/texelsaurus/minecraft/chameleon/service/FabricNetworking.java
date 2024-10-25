package com.texelsaurus.minecraft.chameleon.service;

import com.texelsaurus.minecraft.chameleon.ChameleonServices;
import com.texelsaurus.minecraft.chameleon.client.FabricClient;
import com.texelsaurus.minecraft.chameleon.network.*;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworking implements ChameleonNetworking
{
    @Override
    public <P extends ChameleonPacket<P>> void registerPacketInternal (ResourceLocation id, ChameleonPacketHandler<P> payloadType, boolean clientBound) {
        if (clientBound) {
            if (ChameleonServices.PLATFORM.isPhysicalClient())
                FabricClient.registerPacket(id, payloadType);
        } else  {
            ServerPlayNetworking.registerGlobalReceiver(id, (server, player, handler, buf, responseSender) -> {
                P decode = payloadType.decode(buf);
                payloadType.handle(decode, player, server::execute);
            });
        }
    }

    @Override
    public <P extends ChameleonPacket<P>>  void sendToPlayer (P packet, ServerPlayer player) {
        ChameleonPacketHandler<P> type = packet.getHandler();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        type.encode(packet, buf);

        ServerPlayNetworking.send(player, packet.getId(), buf);
    }

    @Override
    public <P extends ChameleonPacket<P>>  void sendToPlayersNear (P packet, ServerLevel level, double x, double y, double z, double radius) {
        for (ServerPlayer player : level.players()) {
            if (player.blockPosition().distSqr(new BlockPos((int)x, (int)y, (int)z)) <= radius)
                sendToPlayer(packet, player);
        }
    }

    @Override
    public <P extends ChameleonPacket<P>> void sendToServer (P packet) {
        FabricClient.sendToServer(packet);
    }
}
