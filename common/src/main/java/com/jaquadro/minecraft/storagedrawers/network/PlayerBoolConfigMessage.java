package com.jaquadro.minecraft.storagedrawers.network;

import com.google.common.collect.Maps;
import com.jaquadro.minecraft.storagedrawers.ModConstants;
import com.jaquadro.minecraft.storagedrawers.config.PlayerConfig;
import com.jaquadro.minecraft.storagedrawers.config.PlayerConfigSetting;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public record PlayerBoolConfigMessage(String uuid, String key, boolean value) implements ChameleonPacket<PlayerBoolConfigMessage>
{
    public static final ResourceLocation ID = new ResourceLocation(ModConstants.MOD_ID, "player_bool_config");
    public static final PlayerBoolConfigMessage.Handler HANDLER = new PlayerBoolConfigMessage.Handler();

    @Override
    public ResourceLocation getId () {
        return ID;
    }

    @Override
    public ChameleonPacketHandler<PlayerBoolConfigMessage> getHandler () {
        return HANDLER;
    }

    private static class Handler implements ChameleonPacketHandler<PlayerBoolConfigMessage>
    {
        @Override
        public Class<PlayerBoolConfigMessage> type () {
            return PlayerBoolConfigMessage.class;
        }

        @Override
        public void encode (PlayerBoolConfigMessage message, FriendlyByteBuf buffer) {
            buffer.writeUtf(message.uuid);
            buffer.writeUtf(message.key);
            buffer.writeBoolean(message.value);
        }

        @Override
        public PlayerBoolConfigMessage decode (FriendlyByteBuf buffer) {
            String uuid = buffer.readUtf();
            String key = buffer.readUtf();
            boolean value = buffer.readBoolean();
            return new PlayerBoolConfigMessage(uuid, key, value);
        }

        @Override
        public void handle (PlayerBoolConfigMessage message, Player player, Consumer<Runnable> workQueue) {
            if (player instanceof ServerPlayer) {
                workQueue.accept(() -> {
                    UUID playerUniqueId;
                    try {
                        playerUniqueId = UUID.fromString(message.uuid);
                    } catch (IllegalArgumentException e) {
                        return;
                    }

                    Map<String, PlayerConfigSetting<?>> clientMap = PlayerConfig.serverPlayerConfigSettings.get(playerUniqueId);
                    if (clientMap == null) {
                        clientMap = Maps.newHashMap();
                    }

                    clientMap.put(message.key, new PlayerConfigSetting<>(message.key, message.value, playerUniqueId));
                    PlayerConfig.serverPlayerConfigSettings.put(playerUniqueId, clientMap);
                });
            }
        }
    }
}
