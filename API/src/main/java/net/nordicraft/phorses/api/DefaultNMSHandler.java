package net.nordicraft.phorses.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class DefaultNMSHandler extends NMSHandler {

	@Override
	public void spawnFromSaddle(ItemStack saddle, LivingEntity h) {
		
	}

	@Override
	public ItemStack setSpecialSaddle(ItemStack saddle) {
		return saddle;
	}

}
