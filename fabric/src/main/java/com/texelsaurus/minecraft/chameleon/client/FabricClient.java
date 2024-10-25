package com.texelsaurus.minecraft.chameleon.client;

import com.texelsaurus.minecraft.chameleon.network.ChameleonClientPacketType;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketHandler;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FabricClient
{
    public static <P extends ChameleonPacket<P>> void registerPacket(ResourceLocation id, ChameleonPacketHandler<P> packetType) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, responseSender) -> {
            P decode = packetType.decode(buf);
            packetType.handle(decode, client.player, client::execute);
        });
    }

    public static <P extends ChameleonPacket<P>> void sendToServer(P packet) {
        ChameleonPacketHandler<P> handler = packet.getHandler();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        handler.encode(packet, buf);

        ClientPlayNetworking.send(packet.getId(), buf);
    }
}
