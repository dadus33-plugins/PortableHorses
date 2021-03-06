package net.nordicraft.phorses.implementations.v1_11_R1;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftDonkey;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftMule;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftSkeletonHorse;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftZombieHorse;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mule;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_11_R1.EntityHorse;
import net.minecraft.server.v1_11_R1.EntityHorseDonkey;
import net.minecraft.server.v1_11_R1.EntityHorseMule;
import net.minecraft.server.v1_11_R1.EntityHorseSkeleton;
import net.minecraft.server.v1_11_R1.EntityHorseZombie;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.nordicraft.phorses.api.NMSHandler;

public class Handler1_11_R1 extends NMSHandler {

	@Override
	public void spawnFromSaddle(ItemStack saddle, LivingEntity h) {
		final NBTTagCompound saddleTag = CraftItemStack.asNMSCopy(saddle).getTag();
		if (h instanceof Horse) {
			final EntityHorse spawned = ((CraftHorse) h).getHandle();
			final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
			spawned.a(saddleHorseTag);
			spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
			if (saddleTag.hasKey("cname"))
				spawned.setCustomName(saddleTag.getString("cname"));
			return;
		}
		if (h instanceof Mule) {
			final EntityHorseMule spawned = (EntityHorseMule) ((CraftMule) h).getHandle();
			final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
			spawned.a(saddleHorseTag);
			spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
			if (saddleTag.hasKey("cname"))
				spawned.setCustomName(saddleTag.getString("cname"));
		}
		if (h instanceof Donkey) {
			final EntityHorseDonkey spawned = (EntityHorseDonkey) ((CraftDonkey) h).getHandle();
			final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
			spawned.a(saddleHorseTag);
			spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
			if (saddleTag.hasKey("cname"))
				spawned.setCustomName(saddleTag.getString("cname"));
		}
		if (h instanceof SkeletonHorse) {
			final EntityHorseSkeleton spawned = (EntityHorseSkeleton) ((CraftSkeletonHorse) h).getHandle();
			final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
			spawned.a(saddleHorseTag);
			spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
			if (saddleTag.hasKey("cname"))
				spawned.setCustomName(saddleTag.getString("cname"));
		}
		if (h instanceof ZombieHorse) {
			final EntityHorseZombie spawned = (EntityHorseZombie) ((CraftZombieHorse) h).getHandle();
			final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
			spawned.a(saddleHorseTag);
			spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
			if (saddleTag.hasKey("cname"))
				spawned.setCustomName(saddleTag.getString("cname"));
		}
	}

	@Override
	public ItemStack setSpecialSaddle(ItemStack saddle) {
		net.minecraft.server.v1_11_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
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
	public EntityType getEntityType(ItemStack saddle) {
		net.minecraft.server.v1_11_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
		EntityType t = EntityType.valueOf(nmsSaddle.getTag().getString("entype"));
		if (t == null) {
			NBTTagCompound tag = nmsSaddle.getTag();
			int type = ((NBTTagCompound) tag.get("horsetag")).getInt("Type");
			switch (type) {
			case 0:
				return EntityType.HORSE;
			case 1:
				return EntityType.DONKEY;
			case 2:
				return EntityType.MULE;
			case 3:
				return EntityType.ZOMBIE_HORSE;
			case 4:
				return EntityType.SKELETON_HORSE;
			default:
				return null;
			}
		}
		return t;
	}
}
