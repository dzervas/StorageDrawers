package com.jaquadro.minecraft.storagedrawers.item;

import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.util.WorldUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ItemFramedDrawers extends ItemDrawers
{
    public ItemFramedDrawers (Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock (BlockPlaceContext context, BlockState state) {
        if (!super.placeBlock(context, state))
            return false;

        BlockEntityDrawers blockEntity = WorldUtils.getBlockEntity(context.getLevel(), context.getClickedPos(), BlockEntityDrawers.class);
        ItemStack stack = context.getItemInHand();
        if (blockEntity != null && !stack.isEmpty())
            blockEntity.material().read(context.getItemInHand().getOrCreateTag());

        return true;
    }
}
