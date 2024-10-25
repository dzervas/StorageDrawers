package com.texelsaurus.minecraft.chameleon.network;

import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public interface ChameleonServerPacketType<P extends ChameleonPacket<P>> extends ChameleonPacketType<P>
{
    Consumer<Player> handle (P message);
}
