package net.nordicraft.phorses.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.nordicraft.phorses.utils.Maths;
import net.nordicraft.phorses.utils.PacketUtils;
import net.nordicraft.phorses.utils.ReflectionUtils;
import net.nordicraft.phorses.utils.Version;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class NMSHandler {

	protected Object speedAttribute;
	protected Method getterAttribute, addEntityInWorld, prepareWorld, worldDamageScaler, getBukkitEntity,
			worldCreateEntity, getChunkAt, registerEntity;
	protected Constructor<?> blockPositionConstructor;
	protected Field entityLocX, entityLocZ, entityListField;
	
	public NMSHandler() {
		try {
			Version ver = Version.getVersion();
			// load class
			Class<?> entityClass = PacketUtils.getNmsClass("Entity");
			Class<?> entityLivingClass = PacketUtils.getNmsClass("EntityLiving");
			Class<?> worldClass = PacketUtils.getNmsClass("World");
			Class<?> worldServerClass = PacketUtils.getNmsClass("WorldServer");
			Class<?> attributesClass = PacketUtils.getNmsClass("GenericAttributes");
			Class<?> blockPosClass = PacketUtils.getNmsClass("BlockPosition");
	        Class<?> craftWorldClass = PacketUtils.getObcClass("CraftWorld");
			// get objects
			this.speedAttribute = attributesClass.getDeclaredField(ver.isNewerOrEquals(Version.V1_8_R3) ? "MOVEMENT_SPEED" : "d").get(attributesClass);
			// load method
			this.getterAttribute = entityLivingClass.getDeclaredMethod("getAttributeInstance", PacketUtils.getNmsClass("IAttribute"));
			if(ver.isNewerOrEquals(Version.V1_14_R1))
				this.addEntityInWorld = PacketUtils.getNmsClass("IWorldWriter").getDeclaredMethod("addEntity", entityClass);
			else
				this.addEntityInWorld = worldClass.getDeclaredMethod("b", entityClass);
			this.addEntityInWorld.setAccessible(true);
			if(ver.isNewerOrEquals(Version.V1_14_R1)) {
				this.registerEntity = worldServerClass.getDeclaredMethod("registerEntity", entityClass);
				this.registerEntity.setAccessible(true);
			}
			this.prepareWorld = ReflectionUtils.getMethodWithName(PacketUtils.getNmsClass("EntityInsentient"), "prepare");
			String worldDamagerScalerName = ver.isNewerOrEquals(Version.V1_13_R1) ? "getDamageScaler" : (ver.isNewerOrEquals(Version.V1_9_R1) ? "D" : "E");
			this.worldDamageScaler = worldClass.getDeclaredMethod(worldDamagerScalerName, blockPosClass);
			this.getChunkAt = worldClass.getDeclaredMethod("getChunkAt", int.class, int.class);
			this.getBukkitEntity = entityClass.getDeclaredMethod("getBukkitEntity");
			for(Method method : craftWorldClass.getDeclaredMethods()) { // for all method in craftworld
				if(method.getName().equalsIgnoreCase("createEntity") && method.getParameterCount() == 2) { // searching for Location/Class<? extends Entity>
			        this.worldCreateEntity = method; // set this as method
			        break;
				}
			}
			
			// load constructors
			this.blockPositionConstructor = blockPosClass.getDeclaredConstructor(entityClass);
			
			// load fields
			this.entityLocX = entityClass.getDeclaredField("locX");
			this.entityLocX.setAccessible(true);
			this.entityLocZ = entityClass.getDeclaredField("locZ");
			this.entityLocZ.setAccessible(true);
			if(!ver.isNewerOrEquals(Version.V1_14_R1))
				this.entityListField = worldClass.getDeclaredField("entityList");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public abstract ItemStack transferTag(LivingEntity horse, ItemStack saddle);

	public abstract void spawnFromSaddle(ItemStack saddle, LivingEntity h);

    public boolean isPHorse(ItemStack saddle) {
        return hasTagWithKey(saddle, "phorse");
    }

    public boolean isSpecialSaddle(ItemStack saddle) {
        return hasTagWithKey(saddle, "spsaddle");
    }
    
    protected boolean hasTagWithKey(ItemStack item, String tagKey) {
    	try {
	    	Object nmsTag = getItemTag(item);
	        if(nmsTag == null){ // no tag
	            return false;
	        }
	        return (boolean) nmsTag.getClass().getMethod("hasKey", String.class).invoke(nmsTag, tagKey);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
        return false;
    }
    
    /**
     * Get tag of the given item
     * 
     * @param item the item which have tag
     * @return the tag or null if don't have tag or error
     */
    protected Object getItemTag(ItemStack item) {
    	try {
	    	Class<?> craftItemClass = PacketUtils.getObcClass("inventory.CraftItemStack");
	    	Object nmsSaddle = craftItemClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, item);
	    	return nmsSaddle.getClass().getMethod("getTag").invoke(nmsSaddle);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
		}
    }

	public abstract ItemStack setSpecialSaddle(ItemStack saddle);

	public double getSpeedOfHorse(LivingEntity h) {
		try {
			Object nmsHorse = PacketUtils.getNMSEntity(h);
			Object speed = getterAttribute.invoke(nmsHorse, speedAttribute);
			return (double) ReflectionUtils.callMethod(speed, "getValue");
		} catch (Exception e) {
			return -1;
		}
	}

	public LivingEntity forceSpawn(EntityType type, Location spawnLocation){
        try {
	        World world = spawnLocation.getWorld();
	        Object entity = createEntity(type, spawnLocation);
	    	Object worldServer = PacketUtils.getWorldServer(world);

	    	int prepareWorldCountParam = prepareWorld.getParameterCount();
	    	if(prepareWorldCountParam == 1)
	    		prepareWorld.invoke(entity, (Object) null);
	    	else if(prepareWorldCountParam == 2)
	    		prepareWorld.invoke(entity, null, null);
	    	else if(prepareWorldCountParam == 3)
	    		prepareWorld.invoke(entity, null, null, null);
	    	else if(prepareWorldCountParam == 4)
	    		prepareWorld.invoke(entity, null, null, null, null);
	    	
	        int i = Maths.floor(entityLocX.getDouble(entity) / 16.0D);
	        int j = Maths.floor(entityLocZ.getDouble(entity) / 16.0D);
	        
	        Object nmsChunk = getChunkAt.invoke(worldServer, i, j);
	        nmsChunk.getClass().getDeclaredMethod("a", PacketUtils.getNmsClass("Entity")).invoke(nmsChunk, entity);
	        if(entityListField != null)
	        	((List) entityListField.get(worldServer)).add(entity);
	        if(registerEntity != null)
	        	registerEntity.invoke(worldServer, entity);
	        addEntityInWorld.invoke(worldServer, entity);
	        
	        return (LivingEntity) getBukkitEntity.invoke(entity);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return null;
		}
    }

	public boolean isFakeSaddle(ItemStack saddle) {
		return hasTagWithKey(saddle, "fake-saddle");
	}

	public EntityType getEntityType(ItemStack saddle) {
		return EntityType.HORSE;
	}
	
	public Object createEntity(EntityType type, Location loc) throws Exception {
        Class<? extends Entity> entityClass = (Class<? extends Entity>)type.getEntityClass();
        World world = loc.getWorld();
		return worldCreateEntity.invoke(world, loc, entityClass);
	}
}
