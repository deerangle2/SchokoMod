package com.deerangle.items;

import com.deerangle.main.ISchokoBar;
import com.deerangle.main.SchokoMod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemSchokoBar extends ItemFood implements ISchokoBar{

	protected int effect = 0;

	public ItemSchokoBar(String type, int food) {
		this(type, food, false, 0);
	}

	public ItemSchokoBar(String type) {
		this(type, 4, false, 0);
	}

	public ItemSchokoBar(String type, int food, boolean wolf) {
		this(type, food, wolf, 0);
	}

	public ItemSchokoBar(String type, int food, boolean wolf, int effect) {
		super(food, wolf);
		type = type.substring(0, 1).toUpperCase() + type.substring(1);
		this.effect = effect;
		this.setUnlocalizedName("schokoBar" + type);
		this.setCreativeTab(SchokoMod.bars);
		this.setTextureName(SchokoMod.MODID + ":bars/schokoBar" + type);
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(SchokoMod.schokoPotion.id, 10 * 20, effect));
		if(!world.isRemote){
			player.getEntityData().setInteger("Diabetis", player.getEntityData().getInteger("Diabetis") + 1);
		}
		return super.onEaten(stack, world, player);
	}

}
