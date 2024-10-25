package com.texelsaurus.minecraft.chameleon.service;

import com.texelsaurus.minecraft.chameleon.api.ChameleonInit;
import com.texelsaurus.minecraft.chameleon.client.ClientUtil;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketHandler;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;

public class ForgeNetworking implements ChameleonNetworking
{
    private static final String PROTOCOL_VERSION = "1";
    private static final Map<String, SimpleChannel> CHANNELS = new HashMap<>();
    private static final Map<String, Integer> INDEXES = new HashMap<>();

    public static void init (ChameleonInit init, ChameleonInit.InitContext context) {
        init.init(context);
    }

    @Override
    public <P extends ChameleonPacket<P>> void registerPacketInternal (ResourceLocation id, ChameleonPacketHandler<P> payloadType) {
        String modId = id.getNamespace();
        if (!CHANNELS.containsKey(modId)) {
            CHANNELS.put(modId, NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(modId, "main"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel());
            INDEXES.put(modId, 0);
        }

        SimpleChannel channel = CHANNELS.get(modId);
        int index = INDEXES.get(modId);

        channel.registerMessage(index, payloadType.type(), payloadType::encode, payloadType::decode, (packet, ctx) -> {
            NetworkEvent.Context context = ctx.get();
            Player player = context.getSender();
            if (player == null && context.getDirection() == NetworkDirection.PLAY_TO_CLIENT)
                player = ClientUtil.getLocalPlayer();

            packet.getHandler().handle(packet, player, context::enqueueWork);
            context.setPacketHandled(true);
        });

        INDEXES.put(modId, index + 1);
    }

    @Override
    public <P extends ChameleonPacket<P>> void sendToPlayer (P packet, ServerPlayer player) {
        SimpleChannel channel = CHANNELS.getOrDefault(packet.getId().getNamespace(), null);
        if (channel != null)
            channel.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public <P extends ChameleonPacket<P>> void sendToPlayersNear(P packet, ServerLevel level, double x, double y, double z, double radius) {
        SimpleChannel channel = CHANNELS.getOrDefault(packet.getId().getNamespace(), null);
        if (channel != null)
            channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, radius, level.dimension())), packet);
    }

    @Override
    public <P extends ChameleonPacket<P>> void sendToServer (P packet) {
        SimpleChannel channel = CHANNELS.getOrDefault(packet.getId().getNamespace(), null);
        if (channel != null)
            channel.send(PacketDistributor.SERVER.noArg(), packet);
    }
}
