package net.nordicraft.phorses.utils;

import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;

@SuppressWarnings("deprecation")
public class EntityUtils {

	public static double getMaxHealth(Entity entity) {
		if(Version.getVersion().isNewerOrEquals(Version.V1_9_R1))
			return ((Attributable) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		else
			return ((Damageable) entity).getMaxHealth();
	}
}
