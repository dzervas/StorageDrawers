package com.jaquadro.minecraft.storagedrawers.config;

import com.texelsaurus.minecraft.chameleon.ChameleonServices;
import com.texelsaurus.minecraft.chameleon.config.ConfigSpec;
import com.texelsaurus.minecraft.chameleon.service.ChameleonConfig;

public class ModClientConfig extends ConfigSpec
{
    public static ModClientConfig INSTANCE = new ModClientConfig();

    private final ChameleonConfig commonConfig;
    public General GENERAL;
    public Integration INTEGRATION;
    public Render RENDER;

    private ModClientConfig () {
        commonConfig = ChameleonServices.CONFIG.create(this);
    }

    public ChameleonConfig context() {
        return commonConfig;
    }

    @Override
    public void init() {
        GENERAL = new General();
        INTEGRATION = new Integration();
        RENDER = new Render();
    }

    public class General {
        public final ChameleonConfig.ConfigEntry<Boolean> invertShift;
        public final ChameleonConfig.ConfigEntry<Boolean> invertClick;

        public General() {
            commonConfig.pushGroup("General");

            invertShift = commonConfig.define("invertShift", false)
                .comment("Invert the behavior of the shift key for extracting items")
                .build();
            invertClick = commonConfig.define("invertClick", false)
                .comment("Invert left and right click action on drawers")
                .build();

            commonConfig.popGroup();
        }
    }

    public class Integration {
        public final ChameleonConfig.ConfigEntry<Boolean> enableWaila;
        public final ChameleonConfig.ConfigEntry<Boolean> enableTheOneProbe;

        public Integration() {
            commonConfig.pushGroup("Integration");

            enableWaila = commonConfig.define("enableWaila", true)
                .comment("Enable extended data display in WAILA if present")
                .build();
            enableTheOneProbe = commonConfig.define("enableTheOneProbe", true)
                .comment("Enable extended data display in The One Probe if present")
                .build();

            commonConfig.popGroup();
        }
    }

    public class Render {
        public final ChameleonConfig.ConfigEntry<Double> labelRenderDistance;
        public final ChameleonConfig.ConfigEntry<Double> quantityRenderDistance;
        public final ChameleonConfig.ConfigEntry<Double> quantityFadeDistance;

        public Render() {
            commonConfig.pushGroup("Render");

            labelRenderDistance = commonConfig.define("labelRenderDistance", 20.0)
                .comment("Distance in blocks before item labels stop rendering")
                .build();
            quantityRenderDistance = commonConfig.define("quantityRenderDistance", 10.0)
                .comment("Distance in blocks before quantity numbers stop rendering")
                .build();
            quantityFadeDistance = commonConfig.define("quantityFadeDistance", 20.0)
                .comment("Distance in blocks before quantity numbers begin to fade out")
                .build();

            commonConfig.popGroup();
        }
    }
}
