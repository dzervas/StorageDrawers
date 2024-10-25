package com.texelsaurus.minecraft.chameleon.client;

import com.texelsaurus.minecraft.chameleon.network.ChameleonClientPacketType;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class FabricClient
{
    public static <P extends ChameleonPacket<P>> void registerPacket(ChameleonClientPacketType<P> packetType) {
        ResourceLocation id = packetType.id();
        ResourceLocation loc = new ResourceLocation(packetType.modId(), id.getNamespace() + "/" + id.getPath());
        ClientPlayNetworking.registerGlobalReceiver(loc, (client, handler, buf, responseSender) -> {
            P decode = packetType.decode(buf);
            client.execute(() -> packetType.handle(decode));
        });
    }

    public static <P extends ChameleonPacket<P>> void sendToServer(P packet) {
        ChameleonPacketType<P> type = packet.type();
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        type.encode(packet, buf);

        ResourceLocation id = type.id();
        ResourceLocation loc = new ResourceLocation(type.modId() + "/" + id.getNamespace() + "/" + id.getPath());
        ClientPlayNetworking.send(loc, buf);
    }
}
