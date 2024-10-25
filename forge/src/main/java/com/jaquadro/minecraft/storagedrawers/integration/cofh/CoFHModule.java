package com.jaquadro.minecraft.storagedrawers.integration.cofh;

import com.jaquadro.minecraft.storagedrawers.core.ModSecurity;
import com.jaquadro.minecraft.storagedrawers.integration.IntegrationModule;

public class CoFHModule extends IntegrationModule
{
    @Override
    public String getModID () {
        return "cofh_core";
    }

    @Override
    public void init () throws Throwable {

    }

    @Override
    public void postInit () {
        ModSecurity.registry.registerProvider(new CoFHSecurityProvider());
    }
}
