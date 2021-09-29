package net.nordicraft.phorses.api;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (C) 2016 Vlad Ardelean - All Rights Reserved
 * You are not allowed to edit, modify or
 * decompile the contents of this file and/or
 * any other file found in the enclosing jar
 * unless explicitly permitted by me.
 * Written by Vlad Ardelean <LongLiveVladerius@gmail.com>
 */

public interface NMSHandler {

    ItemStack transferTag(LivingEntity horse, ItemStack saddle);

    void spawnFromSaddle(ItemStack saddle, LivingEntity h);

    boolean isPHorse(ItemStack saddle);

    boolean isSpecialSaddle(ItemStack saddle);

    ItemStack setSpecialSaddle(ItemStack saddle);

    double getSpeedOfHorse(LivingEntity h);

    LivingEntity forceSpawn(EntityType type, Location spawnLoc);

    LivingEntity spawn(EntityType type, Location spawnLocation);

    boolean isFakeSaddle(ItemStack saddle);

}
