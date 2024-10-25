package com.jaquadro.minecraft.storagedrawers.core;

import com.jaquadro.minecraft.storagedrawers.network.CountUpdateMessage;
import com.jaquadro.minecraft.storagedrawers.network.PlayerBoolConfigMessage;
import com.texelsaurus.minecraft.chameleon.api.ChameleonInit;
import com.texelsaurus.minecraft.chameleon.service.ChameleonNetworking;

public class ModNetworking implements ChameleonInit
{
    public static final ModNetworking INSTANCE = new ModNetworking();

    @Override
    public void init (InitContext context) {
        ChameleonNetworking.registerPacket(CountUpdateMessage.ID, CountUpdateMessage.HANDLER);
        ChameleonNetworking.registerPacket(PlayerBoolConfigMessage.ID, PlayerBoolConfigMessage.HANDLER);
    }
}
