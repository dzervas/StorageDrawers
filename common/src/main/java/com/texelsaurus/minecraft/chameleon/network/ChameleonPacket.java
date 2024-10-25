package com.texelsaurus.minecraft.chameleon.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public interface ChameleonPacket<T extends ChameleonPacket<T>>
{
    ResourceLocation getId();

    ChameleonPacketHandler<T> getHandler();
}
