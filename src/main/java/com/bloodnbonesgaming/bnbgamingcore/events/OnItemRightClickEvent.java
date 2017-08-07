package com.bloodnbonesgaming.bnbgamingcore.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnItemRightClickEvent extends Event {
	
	private final World world;
	private final EntityPlayer player;
	private final ItemStack itemStack;
	private final EnumHand hand;
	private EnumActionResult result;

	public OnItemRightClickEvent(final World world, final EntityPlayer player, final ItemStack itemStack, final EnumHand hand)
	{
		this.world = world;
		this.player = player;
		this.itemStack = itemStack;
		this.hand = hand;
		this.result = EnumActionResult.FAIL;
	}

	public EnumActionResult getEnumResult() {
		return this.result;
	}

	public void setEnumResult(EnumActionResult result) {
		this.result = result;
	}

	public World getWorld() {
		return world;
	}

	public EntityPlayer getPlayer() {
		return player;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public EnumHand getHand() {
		return hand;
	}
}