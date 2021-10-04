package net.nordicraft.phorses.api;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.nordicraft.phorses.utils.PacketUtils;

public abstract class NMSHandler {

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

	public abstract double getSpeedOfHorse(LivingEntity h);

	public abstract LivingEntity forceSpawn(EntityType type, Location spawnLoc);

	public abstract LivingEntity spawn(EntityType type, Location spawnLocation);

	public abstract boolean isFakeSaddle(ItemStack saddle);

	public EntityType getEntityType(ItemStack saddle) {
		return EntityType.HORSE;
	}
}
