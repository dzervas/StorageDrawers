package com.texelsaurus.minecraft.chameleon.service;

import com.texelsaurus.minecraft.chameleon.inventory.ContainerContent;
import com.texelsaurus.minecraft.chameleon.inventory.ContainerContentSerializer;
import com.texelsaurus.minecraft.chameleon.inventory.ContentMenuProvider;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class FabricContainer implements ChameleonContainer
{
    @Override
    public <T extends AbstractContainerMenu, C extends ContainerContent<C>> Supplier<MenuType<T>> getContainerSupplier (ChameleonContainerFactory<T, C> factory, ContainerContentSerializer<C> serializer) {
        return () -> new ExtendedScreenHandlerType<>((id, inventory, data) -> {
            if (serializer != null)
                return factory.create(id, inventory, Optional.ofNullable(serializer.from(data)));

            return factory.create(id, inventory, Optional.empty());
        });
    }

    @Override
    public <C extends ContainerContent<C>> void openMenu (Player player, ContentMenuProvider<C> menuProvider) {
        player.openMenu(new PlatformContainerFactory<>(menuProvider));
    }

    private record PlatformContainerFactory<T extends ContainerContent<T>> (ContentMenuProvider<T> provider) implements ExtendedScreenHandlerFactory
    {
        @Override
        public Component getDisplayName () {
            return provider.getDisplayName();
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu (int i, Inventory inventory, Player player) {
            return provider.createMenu(i, inventory, player);
        }

        @Override
        public boolean shouldCloseCurrentScreen () {
            return true;
        }

        @Override
        public void writeScreenOpeningData (ServerPlayer player, FriendlyByteBuf buf) {
            T content = provider.createContent(player);
            if (content != null)
                content.serializer().to(buf, content);
        }
    }
}
