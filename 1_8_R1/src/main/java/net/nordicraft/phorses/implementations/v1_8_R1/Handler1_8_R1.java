package net.nordicraft.phorses.implementations.v1_8_R1;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftHorse;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHorse;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;
import net.nordicraft.phorses.api.NMSHandler;

public class Handler1_8_R1 extends NMSHandler {

	private MethodHandle onEntityAdded;

	public Handler1_8_R1(Plugin instance) {

		try {
			Method onEntityAdded = World.class.getDeclaredMethod("a", Entity.class);

			onEntityAdded.setAccessible(true);

			this.onEntityAdded = MethodHandles.lookup().unreflect(onEntityAdded);
		} catch (IllegalAccessException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ItemStack transferTag(LivingEntity horse, ItemStack saddle) {
		NBTTagCompound saddleTag;
		net.minecraft.server.v1_8_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
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
			if (horseTag.getFloat("Health") <= 0.5D || horseTag.getFloat("HealF") <= 0.5D) {
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
	public double getSpeedOfHorse(LivingEntity h) {
		EntityHorse nmsHorse = ((CraftHorse) h).getHandle();
		AttributeInstance speed = nmsHorse.getAttributeInstance(GenericAttributes.d);
		return speed.getValue();
	}

	@Override
	public boolean isPHorse(ItemStack saddle) {
		NBTTagCompound saddleTag;
		net.minecraft.server.v1_8_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
		if (!nmsSaddle.hasTag()) {
			return false;
		}
		saddleTag = nmsSaddle.getTag();
		if (saddleTag.hasKey("phorse")) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isSpecialSaddle(ItemStack saddle) {
		net.minecraft.server.v1_8_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
		if (!nmsSaddle.hasTag()) {
			return false;
		}
		if (nmsSaddle.getTag().hasKey("spsaddle")) {
			return true;
		}
		return false;
	}

	@Override
	public ItemStack setSpecialSaddle(ItemStack saddle) {
		net.minecraft.server.v1_8_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
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
	public LivingEntity forceSpawn(EntityType type, Location spawnLocation) {
		EntityLiving nmsEntity = createEntity(spawnLocation);
		try {
			loadEntity(nmsEntity, (CraftWorld) spawnLocation.getWorld());
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		return (LivingEntity) nmsEntity.getBukkitEntity();
	}

	@SuppressWarnings("unchecked")
	@Override
	public LivingEntity spawn(EntityType type, Location spawnLocation) {
		Class<? extends LivingEntity> entityClass = (Class<? extends LivingEntity>) type.getEntityClass();

		return ((CraftWorld) spawnLocation.getWorld()).spawn(spawnLocation, entityClass,
				CreatureSpawnEvent.SpawnReason.CUSTOM);
	}

	@Override
	public boolean isFakeSaddle(ItemStack saddle) {
		net.minecraft.server.v1_8_R1.ItemStack nmsSaddle = CraftItemStack.asNMSCopy(saddle);
		return !nmsSaddle.hasTag() ? false : nmsSaddle.getTag().hasKey("fake-saddle");
	}

	@SuppressWarnings("unchecked")
	private void loadEntity(Entity entity, CraftWorld craftWorld) throws Throwable {
		World world = craftWorld.getHandle();
		((EntityInsentient) entity).prepare(world.E(new BlockPosition(entity)), null);

		int i = MathHelper.floor(entity.locX / 16.0D);
		int j = MathHelper.floor(entity.locZ / 16.0D);
		world.getChunkAt(i, j).a(entity);
		world.entityList.add(entity);
		onEntityAdded.invoke(world, entity);
	}

	private EntityLiving createEntity(Location location) {
		EntityLiving entity = new EntityHorse(((CraftWorld) location.getWorld()).getHandle());
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		float pitch = location.getPitch();
		float yaw = location.getYaw();
		entity.setLocation(x, y, z, yaw, pitch);
		return entity;
	}
}
