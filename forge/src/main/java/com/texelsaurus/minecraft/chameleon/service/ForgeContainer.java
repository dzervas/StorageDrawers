package com.texelsaurus.minecraft.chameleon.service;

import com.texelsaurus.minecraft.chameleon.inventory.ContainerContent;
import com.texelsaurus.minecraft.chameleon.inventory.ContainerContentSerializer;
import com.texelsaurus.minecraft.chameleon.inventory.ContentMenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;

import java.util.Optional;
import java.util.function.Supplier;

public class ForgeContainer implements ChameleonContainer
{
    @Override
    public <T extends AbstractContainerMenu, C extends ContainerContent<C>> Supplier<MenuType<T>> getContainerSupplier (ChameleonContainerFactory<T, C> factory, ContainerContentSerializer<C> serializer) {
        return () -> IForgeMenuType.create((id, inventory, data) -> {
            if (serializer != null)
                return factory.create(id, inventory, Optional.ofNullable(serializer.from(data)));
            return factory.create(id, inventory, Optional.empty());
        });
    }

    @Override
    public <C extends ContainerContent<C>> void openMenu (Player player, ContentMenuProvider<C> menuProvider) {
        NetworkHooks.openScreen((ServerPlayer)player, menuProvider, buf -> {
            C content = menuProvider.createContent((ServerPlayer) player);
            if (content != null)
                content.serializer().to(buf, content);
        });
    }
}
