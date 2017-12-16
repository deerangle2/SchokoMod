package com.deerangle.gui;

import com.deerangle.items.ModItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotSchokoMixerButter extends Slot {

	public SlotSchokoMixerButter(IInventory inv, int id, int x, int y) {
		super(inv, id, x, y);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return stack.getItem() == ModItems.cocoaButter || stack.getItem() == ModItems.cocoaButterMilk;
	}
	
}