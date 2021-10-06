package net.nordicraft.phorses.implementations.v1_13_R2;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.ChatMessage;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.nordicraft.phorses.api.NMSHandler;

public class Handler1_13_R2 extends NMSHandler {

	@Override
	public void spawnFromSaddle(ItemStack saddle, LivingEntity h) {
		try {
			final NBTTagCompound saddleTag = CraftItemStack.asNMSCopy(saddle).getTag();
			final Entity spawned = ((CraftEntity) h).getHandle();
			final NBTTagCompound saddleHorseTag = (NBTTagCompound) saddleTag.get("horsetag");
			spawned.getClass().getDeclaredMethod("a", NBTTagCompound.class).invoke(spawned, saddleHorseTag);
			spawned.setCustomNameVisible(saddleTag.getBoolean("iscnameviz"));
			if (saddleTag.hasKey("cname"))
				spawned.setCustomName(new ChatMessage(saddleTag.getString("cname")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ItemStack setSpecialSaddle(ItemStack saddle) {
		net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
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
		net.minecraft.server.v1_13_R2.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
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
