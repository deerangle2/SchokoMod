package com.deerangle.main;

import java.lang.reflect.InvocationTargetException; 
import java.lang.reflect.Method;

import com.deerangle.block.ModBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{

	@Override
	public void preinit(FMLPreInitializationEvent event) {
		super.preinit(event);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postinit(FMLPostInitializationEvent event) {
		super.postinit(event);
	}

}
