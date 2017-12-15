package com.deerangle.items.bars;

import com.deerangle.main.ISchokoBar;
import com.deerangle.main.SchokoMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemSchokoBarLite extends ItemFood implements ISchokoBar {

	public ItemSchokoBarLite() {
		super(1, false);
		this.setUnlocalizedName("schokoBarLite");
		this.setCreativeTab(SchokoMod.bars);
		this.setTextureName(SchokoMod.MODID + ":bars/schokoBarLite");
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(SchokoMod.schokoPotion.id, 0, 0));
		if(!world.isRemote){
			player.getEntityData().setInteger("Diabetis", 0);
		}
		return super.onEaten(stack, world, player);
	}

}
