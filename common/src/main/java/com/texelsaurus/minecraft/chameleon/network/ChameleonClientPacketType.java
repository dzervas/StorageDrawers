package com.texelsaurus.minecraft.chameleon.network;

public interface ChameleonClientPacketType<P extends ChameleonPacket<P>> extends ChameleonPacketType<P>
{
    Runnable handle (P message);
}
