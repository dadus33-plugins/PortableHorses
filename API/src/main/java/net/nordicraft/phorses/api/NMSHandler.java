package net.nordicraft.phorses.api;

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

	protected Object speedAttribute; // speed attribute from enum
	protected Method addEntityInWorld, prepareWorld, worldDamageScaler, getBukkitEntity, worldCreateEntity, getChunkAt; // world method
	protected Method getterAttribute, registerEntity, itemAsNMSCopy, itemAsCraftMirror; // item method
	protected Method saveNbtData, nbtSetTag, nbtGetTag; // nbt method
	protected Field entityLocX, entityLocZ, entityListField, entityLoc, vecLocX, vecLocZ; // all field
	
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
	        Class<?> itemStackClass = PacketUtils.getNmsClass("ItemStack");
	    	Class<?> craftItemClass = PacketUtils.getObcClass("inventory.CraftItemStack");
	    	Class<?> entityHorse = PacketUtils.getNmsClass("EntityHorseAbstract");
	    	Class<?> nbtTagComp = PacketUtils.getNmsClass("NBTTagCompound");
	    	
			// get objects
			this.speedAttribute = attributesClass.getDeclaredField(ver.isNewerOrEquals(Version.V1_8_R3) ? "MOVEMENT_SPEED" : "d").get(attributesClass);
			// load method
			this.getterAttribute = entityLivingClass.getDeclaredMethod("getAttributeInstance", PacketUtils.getNmsClass(ver.isNewerOrEquals(Version.V1_16_R1) ? "AttributeBase" : "IAttribute"));
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
	    	
	    	this.itemAsNMSCopy = craftItemClass.getMethod("asNMSCopy", ItemStack.class);
	        this.itemAsCraftMirror = craftItemClass.getMethod("asCraftMirror", itemStackClass);
	        
	    	this.saveNbtData = entityHorse.getDeclaredMethod(ver.isNewerOrEquals(Version.V1_16_R1) ? "saveData" : "b", nbtTagComp);
	    	this.nbtGetTag = itemStackClass.getMethod("getTag");
	    	this.nbtSetTag = itemStackClass.getMethod("setTag", nbtTagComp);
			
			// load fields
			if(ver.isNewerOrEquals(Version.V1_16_R1)) {
				this.entityLoc = entityClass.getDeclaredField("loc");
				this.entityLoc.setAccessible(true);
				Class<?> vec3dClass = PacketUtils.getNmsClass("Vec3D");
				this.vecLocX = vec3dClass.getDeclaredField("x");
				this.vecLocZ = vec3dClass.getDeclaredField("z");
			} else {
				this.entityLocX = entityClass.getDeclaredField("locX");
				this.entityLocX.setAccessible(true);
				this.entityLocZ = entityClass.getDeclaredField("locZ");
				this.entityLocZ.setAccessible(true);
			}
			if(!ver.isNewerOrEquals(Version.V1_14_R1))
				this.entityListField = worldClass.getDeclaredField("entityList");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ItemStack transferTag(LivingEntity horse, ItemStack saddle) {
		try {
	    	Class<?> craftAbsHorse = PacketUtils.getObcClass("entity.CraftAbstractHorse");
	    	
	        NBT nbt = new NBT(getItemTag(saddle));
        	nbt.setBoolean("phorse", true);
        	nbt.setString("entype", horse.getType().name());

			Object nmsHorse = craftAbsHorse.getMethod("getHandle").invoke(craftAbsHorse.cast(horse));
	        //EntityHorseAbstract nmsHorse = ((CraftAbstractHorse)horse).getHandle();
	        NBT horseTag = new NBT(null);
	        this.saveNbtData.invoke(nmsHorse, horseTag.getNmsNBT());
	        if(horse.getHealth() <= 0.5D) {
	            if(horseTag.getFloat("Health") <= 0.5F || horseTag.getFloat("HealF") <= 0.5F){
	                horseTag.setFloat("Health", 1F);
	                if(horseTag.hasKey("HealF"))
	                    horseTag.setFloat("HealF", 1F);
	            }
	        }
	        nbt.setTag("horsetag", horseTag);
	        nbt.setBoolean("phorse", true);
	        nbt.setBoolean("iscnameviz", horse.isCustomNameVisible());
	        if(horse.getCustomName() != null)
	        	nbt.setString("cname", horse.getCustomName());
	        return (ItemStack) this.itemAsCraftMirror.invoke(null, setItemTag(saddle, nbt));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

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
	    	return this.nbtGetTag.invoke(this.itemAsNMSCopy.invoke(null, item));
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
		}
    }
    
    protected Object setItemTag(ItemStack item, NBT tag) {
    	try {
    		Object nmsItem = this.itemAsNMSCopy.invoke(null, item);
	    	this.nbtSetTag.invoke(nmsItem, tag.getNmsNBT());
	    	return nmsItem;
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
	    	
	        int i = Maths.floor(getLocX(entity) / 16.0D);
	        int j = Maths.floor(getLocZ(entity) / 16.0D);
	        
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
	
	private double getLocX(Object entity) throws Exception {
		if(entityLocX == null) {
			return vecLocX.getDouble(entityLoc.get(entity));
		} else
			return entityLocX.getDouble(entity);
	}
	
	private double getLocZ(Object entity) throws Exception {
		if(entityLocZ == null) {
			return vecLocZ.getDouble(entityLoc.get(entity));
		} else
			return entityLocZ.getDouble(entity);
	}
}
