package com.jaquadro.minecraft.storagedrawers.network;

import com.jaquadro.minecraft.storagedrawers.ModConstants;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacket;
import com.texelsaurus.minecraft.chameleon.network.ChameleonPacketHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record CountUpdateMessage(int x, int y, int z, int slot, int count) implements ChameleonPacket<CountUpdateMessage>
{
    public static final ResourceLocation ID = new ResourceLocation(ModConstants.MOD_ID, "count_update");
    public static final Handler HANDLER = new Handler();

    public CountUpdateMessage (BlockPos pos, int slot, int count) {
        this(pos.getX(), pos.getY(), pos.getZ(), slot, count);
    }

    @Override
    public ResourceLocation getId () {
        return ID;
    }

    @Override
    public ChameleonPacketHandler<CountUpdateMessage> getHandler () {
        return HANDLER;
    }

    private static class Handler implements ChameleonPacketHandler<CountUpdateMessage>
    {
        @Override
        public Class<CountUpdateMessage> type () {
            return CountUpdateMessage.class;
        }

        @Override
        public void encode (CountUpdateMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.x);
            buffer.writeShort(message.y);
            buffer.writeInt(message.z);
            buffer.writeByte(message.slot);
            buffer.writeInt(message.count);
        }

        @Override
        public CountUpdateMessage decode (FriendlyByteBuf buffer) {
            int x = buffer.readInt();
            int y = buffer.readShort();
            int z = buffer.readInt();
            int slot = buffer.readByte();
            int count = buffer.readInt();
            return new CountUpdateMessage(new BlockPos(x, y, z), slot, count);
        }

        @Override
        public void handle (CountUpdateMessage message, Player player, Consumer<Runnable> workQueue) {
            if (!(player instanceof ServerPlayer)) {
                workQueue.accept(() -> {
                    new CountUpdateMessageHandler().handle(message);
                });
            }
        }
    }
}
