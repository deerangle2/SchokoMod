package com.deerangle.item;

import org.apache.http.client.CredentialsProvider;

import com.deerangle.block.ModBlocks;
import com.deerangle.effect.ModPotions;
import com.deerangle.main.NoahsChocolate;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemChorusFruit;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;

public class ItemChocolateBar extends ItemFood {

	public static String[] types = new String[] { "normal", "dark", "light", "full", "lite", "cookie", "nut", "smartie",
			"joghurt", "colored_white", "colored_orange", "colored_magenta", "colored_light_blue", "colored_yellow",
			"colored_lime", "colored_pink", "colored_gray", "colored_silver", "colored_cyan", "colored_purple",
			"colored_blue", "colored_brown", "colored_green", "colored_red", "colored_black", "bed", "mushroom",
			"flower", "lilypad", "gold", "steve", "lsd", "troll", "fish", "quartz", "cobble", "spider", "creeper",
			"ender", "wither", "fire", "firework", "glass", "windows", "apple", "glowstone", "redstone", "illuminati",
			"cow", "rainbow", "book", "portal", "christmas", "halloween" };

	SoundEvent SOUND_WINDOWS = new SoundEvent(new ResourceLocation(NoahsChocolate.MODID + ":windows"));
	SoundEvent SOUND_SCREAM = new SoundEvent(new ResourceLocation(NoahsChocolate.MODID + ":scream"));
	SoundEvent SOUND_SANTA = new SoundEvent(new ResourceLocation(NoahsChocolate.MODID + ":santa"));
	SoundEvent SOUND_ILLUMINATI = new SoundEvent(new ResourceLocation(NoahsChocolate.MODID + ":illuminati"));

	public ItemChocolateBar() {
		super(0, false);
		this.setRegistryName("chocolate_bar");
		this.setCreativeTab(NoahsChocolate.bars);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (playerIn.canEat(canAlwaysBeEaten(itemstack.getMetadata()))) {
			playerIn.setActiveHand(handIn);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		} else {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			for (int i = 0; i < types.length; i++) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public int getMetadata(ItemStack stack) {
		return this.getMetadata(stack.getItemDamage());
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item.chocolate_bar_" + types[stack.getMetadata()];
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			int meta = stack.getMetadata();
			player.getFoodStats().addStats(new ItemFood(getHungerAmount(meta), isWolfFood(meta)), stack);
			worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			executeEffect(meta, stack, worldIn, player);
			player.addStat(StatList.getObjectUseStats(this));

			if (player instanceof EntityPlayerMP) {
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) player, stack);
			}
		}

		stack.shrink(1);
		return stack;
	}
	
	private boolean executeEffect(int meta, ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote) {
			applyStandardEffects(player, 0);
			switch (types[meta]) {
			case "full":
				applyStandardEffects(player, 2);
				return false;
			case "lite":
				player.getEntityData().setInteger("Diabetis", 0);
				player.removePotionEffect(ModPotions.schoko);
				return false;
			case "bed":
				worldIn.setWorldTime(1000);
				return true;
			case "mushroom":
				worldIn.setWorldTime(18000);
				return true;
			case "flower":
				worldIn.getWorldInfo().setRainTime(0);
				worldIn.getWorldInfo().setThunderTime(0);
				worldIn.getWorldInfo().setRaining(false);
				worldIn.getWorldInfo().setThundering(false);
				return true;
			case "lilypad":
				worldIn.getWorldInfo().setRainTime(0);
				worldIn.getWorldInfo().setRaining(true);
				return true;
			case "gold":
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 5 * 20, 1));
				player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 120 * 20, 0));
				return true;
			case "steve":
				player.addPotionEffect(new PotionEffect(MobEffects.POISON, 10 * 20, 2));
				return true;
			case "lsd":
				player.addPotionEffect(new PotionEffect(ModPotions.lsd, 30 * 20, 0));
				return true;
			case "troll":
				player.addPotionEffect(new PotionEffect(MobEffects.POISON, 10 * 20, 2));
				return false;
			case "fish":
				player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 10 * 20, 2));
				return false;
			case "quartz":
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10 * 20, 4));
				int lvl = player.getFoodStats().getFoodLevel() - 4;
				player.getFoodStats().setFoodLevel((lvl < 0 ? 0 : lvl));
				return false;
			case "cobble":
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10 * 20, 4));
				int lvl2 = player.getFoodStats().getFoodLevel() - 4;
				player.getFoodStats().setFoodLevel((lvl2 < 0 ? 0 : lvl2));
				return false;
			case "christmas":
				player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.present, 1 + worldIn.rand.nextInt(3)));
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SOUND_SANTA, SoundCategory.PLAYERS, 1, 1);
				return true;
			case "fire":
				player.setFire(5);
				return true;
			case "book":
				player.addExperience(30);
				return true;
			case "portal":
				player.setPortal(player.getPosition());
				player.changeDimension(-1);
				return true;
			case "glass":
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 1);
				player.attackEntityFrom(new DamageSource("eatGlass"), 4);
				return true;
			case "cow":
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_COW_HURT, SoundCategory.PLAYERS, 1, 1);
				return true;
			case "creeper":
				worldIn.createExplosion(player, player.posX + 0.1, player.posY, player.posZ - 0.1, 3F, true);
				player.attackEntityFrom(new DamageSource("eatTnt"), 10);
				return true;
			case "wither":
				player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 10 * 20, 2));
				if (worldIn.rand.nextFloat() < 0.03) {
					player.addItemStackToInventory(new ItemStack(Items.SKULL, 1, 1));
				}
				return true;
			case "windows":
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SOUND_WINDOWS, SoundCategory.PLAYERS, 1, 1);
				return true;
			case "halloween":
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SOUND_SCREAM, SoundCategory.PLAYERS, 1, 1);
				ItemStack head_armor_stack = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
				if (head_armor_stack.isEmpty()){
					player.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Blocks.PUMPKIN));
				}
				return true;
			case "ender":
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
				for (int i = 0; i < 16; ++i) {
					double d3 = player.posX + (player.getRNG().nextDouble() - 0.5D) * 16.0D;
					double d4 = MathHelper.clamp(player.posY + (double) (player.getRNG().nextInt(16) - 8), 0.0D, (double) (worldIn.getActualHeight() - 1));
					double d5 = player.posZ + (player.getRNG().nextDouble() - 0.5D) * 16.0D;

					if (player.isRiding()) {
						player.dismountRidingEntity();
					}

					if (player.attemptTeleport(d3, d4, d5)) {
						break;
					}
				}
				return true;
			case "spider":
				worldIn.setBlockState(player.getPosition(), Blocks.WEB.getDefaultState());
				player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 10 * 20, 4));
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10 * 20, 4));
				return true;
			case "illuminati":
				worldIn.playSound((EntityPlayer) null, player.posX, player.posY, player.posZ, SOUND_ILLUMINATI, SoundCategory.PLAYERS, 1, 1);
				player.addPotionEffect(new PotionEffect(ModPotions.schoko, 26 * 20, 100));
				player.addPotionEffect(new PotionEffect(ModPotions.lsd, 26 * 20, 0));
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 26 * 20, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 26 * 20, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 26 * 20, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 26 * 20, 2));
				player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 26 * 20, 4));
				return true;
			case "firework":
				return true;
				
			case "redstone":
				return true;
			case "glowstone":
				return true;
			case "rainbow":
				return true;
			}
		} else {
			if(types[meta].equals("firework")) {
				NBTTagCompound fireworks = new NBTTagCompound();
				NBTTagList explosions = new NBTTagList();
				NBTTagCompound explosion = new NBTTagCompound();

				fireworks.setByte("Flight", (byte) 1);
				explosion.setByte("Type", (byte) 0);
				explosion.setIntArray("Colors", new int[] { 9533525 });
				explosions.appendTag(explosion);

				fireworks.setTag("Explosions", explosions);
				worldIn.makeFireworks(player.posX, player.posY + player.eyeHeight, player.posZ, 0, 0, 0, fireworks);
				return true;
			}
		}
		
		return false;
	}

	private void applyStandardEffects(EntityPlayer player, int effect) {
		player.addPotionEffect(new PotionEffect(ModPotions.schoko, 10 * 20, effect));
		player.getEntityData().setInteger("Diabetis", player.getEntityData().getInteger("Diabetis") + 1);
	}

	private boolean canAlwaysBeEaten(int meta) {
		return true;
	}

	private boolean isWolfFood(int meta) {
		switch (types[meta]) {
		case "cow":
			return true;
		default:
			return false;
		}
	}

	private int getHungerAmount(int meta) {
		switch (types[meta]) {
		case "nut":
			return 6;
		case "apple":
			return 6;
		case "full":
			return 6;
		case "lite":
			return 2;
		case "cookie":
			return 6;
		case "joghurt":
			return 6;
		case "smartie":
			return 6;
		case "quartz":
			return 0;
		case "cobble":
			return 0;
		case "fire":
			return 0;
		default:
			return 4;
		}
	}

}
