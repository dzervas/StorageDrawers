package com.texelsaurus.minecraft.chameleon.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public interface ChameleonPacketHandler<T extends ChameleonPacket<T>>
{
    Class<T> type ();

    void encode (T message, FriendlyByteBuf buffer);

    T decode (FriendlyByteBuf buffer);

    void handle (T message, Player player, Consumer<Runnable> workQueue);
}
