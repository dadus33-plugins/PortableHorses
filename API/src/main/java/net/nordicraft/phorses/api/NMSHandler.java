package net.nordicraft.phorses.api;

import java.lang.reflect.Method;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.nordicraft.phorses.utils.PacketUtils;
import net.nordicraft.phorses.utils.ReflectionUtils;
import net.nordicraft.phorses.utils.Version;

public abstract class NMSHandler {

	private Object speedAttribute;
	private Method getterAttribute;
	
	public NMSHandler() {
		try {
			Class<?> attributesClass = PacketUtils.getNmsClass("GenericAttributes");
			speedAttribute = attributesClass.getDeclaredField(Version.getVersion().isNewerOrEquals(Version.V1_8_R3) ? "MOVEMENT_SPEED" : "d").get(attributesClass);
			getterAttribute = PacketUtils.getNmsClass("EntityLiving").getDeclaredMethod("getAttributeInstance", PacketUtils.getNmsClass("IAttribute"));
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

	public abstract LivingEntity forceSpawn(EntityType type, Location spawnLoc);

	public abstract LivingEntity spawn(EntityType type, Location spawnLocation);

	public abstract boolean isFakeSaddle(ItemStack saddle);

	public EntityType getEntityType(ItemStack saddle) {
		return EntityType.HORSE;
	}
}
