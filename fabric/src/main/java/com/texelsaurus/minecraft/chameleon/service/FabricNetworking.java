package com.texelsaurus.minecraft.chameleon.service;

import com.texelsaurus.minecraft.chameleon.ChameleonServices;
import com.texelsaurus.minecraft.chameleon.client.FabricClient;
import com.texelsaurus.minecraft.chameleon.network.ChameleonClientPacketType;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketType;
import com.texelsaurus.minecraft.chameleon.network.ChameleonServerPacketType;
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
    public <P extends ChameleonPacket<P>> void registerPacketInternal (ChameleonPacketType<P> payloadType) {
        if (payloadType instanceof ChameleonClientPacketType<P> type) {
            if (ChameleonServices.PLATFORM.isPhysicalClient())
                FabricClient.registerPacket(type);
        } else if (payloadType instanceof ChameleonServerPacketType<P> type) {
            ResourceLocation id = type.id();
            ResourceLocation loc = new ResourceLocation(payloadType.modId(), id.getNamespace() + "/" + id.getPath());

            ServerPlayNetworking.registerGlobalReceiver(loc, (server, player, handler, buf, responseSender) -> {
                P decode = type.decode(buf);
                server.execute(() -> type.handle(decode).accept(player));
            });
        }
    }

    @Override
    public <P extends ChameleonPacket<P>>  void sendToPlayer (P packet, ServerPlayer player) {
        ChameleonPacketType<P> type = packet.type();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        type.encode(packet, buf);

        ResourceLocation id = type.id();
        ResourceLocation loc = new ResourceLocation(type.modId() + "/" + id.getNamespace() + "/" + id.getPath());
        ServerPlayNetworking.send(player, loc, buf);
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
