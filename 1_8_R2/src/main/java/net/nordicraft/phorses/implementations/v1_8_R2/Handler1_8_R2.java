package net.nordicraft.phorses.implementations.v1_8_R2;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R2.EntityHorse;
import net.minecraft.server.v1_8_R2.EntityLiving;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.nordicraft.phorses.api.NMSHandler;

public class Handler1_8_R2 extends NMSHandler {

	@Override
	public ItemStack transferTag(LivingEntity horse, ItemStack saddle) {
		NBTTagCompound saddleTag;
		net.minecraft.server.v1_8_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
		saddleTag = nmsSaddle.getTag();
		if (saddleTag != null) {
			saddleTag.setBoolean("phorse", true);
		} else {
			saddleTag = new NBTTagCompound();
			saddleTag.setBoolean("phorse", true);
		}
		NBTTagCompound horseTag = new NBTTagCompound();
		EntityHorse nmsHorse = ((CraftHorse) horse).getHandle();
		nmsHorse.b(horseTag);
		if (horse.getHealth() <= 0.5D)
			if (horseTag.getFloat("Health") <= 0.5F || horseTag.getFloat("HealF") <= 0.5F) {
				horseTag.setFloat("Health", 1F);
				if (horseTag.hasKey("HealF"))
					horseTag.setFloat("HealF", 1F);
			}
		saddleTag.set("horsetag", horseTag);
		saddleTag.setBoolean("phorse", true);
		saddleTag.setBoolean("iscnameviz", horse.isCustomNameVisible());
		if (horse.getCustomName() != null)
			saddleTag.setString("cname", horse.getCustomName());
		nmsSaddle.setTag(saddleTag);
		saddle = CraftItemStack.asCraftMirror(nmsSaddle);
		return saddle;
	}

	@Override
	public void spawnFromSaddle(ItemStack saddle, LivingEntity h) {
		final NBTTagCompound saddleTag = CraftItemStack.asNMSCopy(saddle).getTag();
		final EntityHorse spawned = ((CraftHorse) h).getHandle();
		final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
		spawned.a(saddleHorseTag);
		spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
		if (saddleTag.hasKey("cname"))
			spawned.setCustomName(saddleTag.getString("cname"));
	}

	@Override
	public ItemStack setSpecialSaddle(ItemStack saddle) {
		net.minecraft.server.v1_8_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
		ItemStack returnItem;
		if (nmsSaddle.hasTag()) {
			nmsSaddle.getTag().setBoolean("spsaddle", true);
			returnItem = CraftItemStack.asCraftMirror(nmsSaddle);
			return returnItem;
		}
		NBTTagCompound toSet = new NBTTagCompound();
		toSet.setBoolean("spsaddle", true);
		nmsSaddle.setTag(toSet);
		return CraftItemStack.asCraftMirror(nmsSaddle);
	}
	
	@Override
	public Object createEntity(EntityType type, Location loc) throws Exception {
		EntityLiving entity = new EntityHorse(((CraftWorld) loc.getWorld()).getHandle());
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float pitch = loc.getPitch();
		float yaw = loc.getYaw();
		entity.setLocation(x, y, z, yaw, pitch);
		return entity;
	}
}
