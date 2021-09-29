package net.nordicraft.phorses.api;


import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface ModernNMSHandler extends NMSHandler {
    EntityType getEntityType(ItemStack saddle);
}
