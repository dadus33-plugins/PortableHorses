package net.nordicraft.phorses.listeners;

import net.nordicraft.phorses.PortableHorses;
import net.nordicraft.phorses.api.NMSHandler;
import net.nordicraft.phorses.utils.Storage;
import net.nordicraft.phorses.utils.Styler;
import org.bukkit.Material;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (C) 2016 Vlad Ardelean - All Rights Reserved
 * You are not allowed to edit, modify or
 * decompile the contents of this file and/or
 * any other file found in the enclosing jar
 * unless explicitly permitted by me.
 * Written by Vlad Ardelean <LongLiveVladerius@gmail.com>
 */
public class TakeOffListener implements Listener{

    final NMSHandler handler;
    final Storage c;
    final Styler styler;

    public TakeOffListener(NMSHandler handler, Storage storage, Styler styler){
        this.handler = handler;
        this.c = storage;
        this.styler = styler;
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e){
        if(!PortableHorses.newHorseSystem()) {
            if (!(e.getClickedInventory() instanceof HorseInventory) || e.getSlot() != 0) {
                return;
            }
            Player p = (Player) e.getWhoClicked();

            HorseInventory horseinv = (HorseInventory) e.getClickedInventory();
            Horse h = (Horse) e.getClickedInventory().getHolder();

            if (h.getOwner() != null)
                if (!h.getOwner().getUniqueId().equals(p.getUniqueId()) && !c.TAKEOFF_ALLOW_STRANGER_TAKE_OFF && !p.hasPermission("portablehorses.use.ignore-ownership")) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    if(c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty()){
                        return;
                    }
                    p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    return;
                }

            if (e.getCurrentItem().getType().equals(Material.SADDLE)) {

                ItemStack currentSaddle = e.getCurrentItem();
                if (c.USE_SPECIAL_SADDLE) {
                    if (!handler.isSpecialSaddle(currentSaddle)) {
                        return;
                    }
                }
                e.setCancelled(true);
                e.setCursor(null);

                if (p.getInventory().firstEmpty() == -1) {
                    switch (c.TAKE_OFF_FULL_INV_MODE) {
                        case "CANCEL":
                            if (!c.TAKE_OFF_OPERATION_CANCELLED.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_OPERATION_CANCELLED);
                            e.setCancelled(true);
                            return;
                        case "GROUND":
                            if (!c.STORE_INVENTORY && h.isCarryingChest()) {
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                                }
                                horseinv.clear();
                            }
                            if (!c.ALLOW_RECURSIVE_PHORSE && h.isCarryingChest())
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        if (content.getType().equals(Material.SADDLE)) {
                                            if (handler.isPHorse(content)) {
                                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                                e.setCancelled(true);
                                                return;
                                            }
                                        }
                                }
                            if (!c.STORE_ARMOR && horseinv.getArmor() != null) {
                                h.getWorld().dropItem(h.getEyeLocation(), horseinv.getArmor());
                                horseinv.setArmor(null);
                            }
                            horseinv.setSaddle(new ItemStack(Material.SADDLE));
                            currentSaddle = handler.transferTag(h, currentSaddle);
                            currentSaddle = styler.styleSaddle(h, currentSaddle);
                            p.getWorld().dropItem(p.getEyeLocation(), currentSaddle);
                            SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
                            h.remove();
                            if (!c.TAKE_OFF_SADDLE_DROPPED.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_SADDLE_DROPPED);
                            return;
                        default:


                            if (!c.STORE_INVENTORY && h.isCarryingChest()) {
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                                }
                                horseinv.clear();
                            }
                            if (!c.ALLOW_RECURSIVE_PHORSE && h.isCarryingChest())
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        if (content.getType().equals(Material.SADDLE)) {
                                            if (handler.isPHorse(content)) {
                                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                                e.setCancelled(true);
                                                return;
                                            }
                                        }
                                }
                            if (!c.STORE_ARMOR && horseinv.getArmor() != null) {
                                h.getWorld().dropItem(h.getEyeLocation(), horseinv.getArmor());
                                horseinv.setArmor(null);
                            }

                            horseinv.setSaddle(new ItemStack(Material.SADDLE));
                            currentSaddle = handler.transferTag(h, currentSaddle);
                            currentSaddle = styler.styleSaddle(h, currentSaddle);
                            p.getInventory().setItem(35, currentSaddle);
                            SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
                            h.remove();
                            if (!c.TAKE_OFF_FIRST_ITEM_REPLACED.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_FIRST_ITEM_REPLACED);
                            return;
                    }

                }

                if (!c.STORE_INVENTORY && h.isCarryingChest()) {
                    for (int i = 2; i <= 16; ++i) {
                        ItemStack content = horseinv.getItem(i);
                        if (content != null)
                            h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                    }
                    horseinv.clear();
                }
                if (!c.ALLOW_RECURSIVE_PHORSE && h.isCarryingChest())
                    for (int i = 2; i <= 16; ++i) {
                        ItemStack content = horseinv.getItem(i);
                        if (content != null)
                            if (content.getType().equals(Material.SADDLE)) {
                                if (handler.isPHorse(content)) {
                                    if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                        p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                    }
                if (!c.STORE_ARMOR && horseinv.getArmor() != null) {
                    h.getWorld().dropItem(h.getEyeLocation(), horseinv.getArmor());
                    horseinv.setArmor(null);
                }

                if (!c.MESSAGES_HORSE_TAKEN.isEmpty())
                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);

                horseinv.setSaddle(new ItemStack(Material.SADDLE));
                currentSaddle = handler.transferTag(h, currentSaddle);
                currentSaddle = styler.styleSaddle(h, currentSaddle);
                p.getInventory().addItem(currentSaddle);
                SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
                h.remove();
            }
        }else{

            if(e.getClickedInventory()==null){
                return;
            }

            if (!(e.getClickedInventory().getHolder() instanceof AbstractHorse) || e.getSlot() != 0) {
                return;
            }

            if(e.getClickedInventory().getHolder() instanceof Llama){
                return;
            }

            Player p = (Player) e.getWhoClicked();

            Inventory horseinv = e.getClickedInventory();
            AbstractHorse h = (AbstractHorse) e.getClickedInventory().getHolder();

            if (h.getOwner() != null)
                if (!h.getOwner().getUniqueId().equals(p.getUniqueId()) && !c.TAKEOFF_ALLOW_STRANGER_TAKE_OFF && !p.hasPermission("portablehorses.use.ignore-ownership")) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    if(c.TAKE_OFF_DENIED_NOT_OWNER.isEmpty()){
                        return;
                    }
                    p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_DENIED_NOT_OWNER);
                    return;
                }

            if (e.getCurrentItem().getType().equals(Material.SADDLE)) {

                ItemStack currentSaddle = e.getCurrentItem();
                if (c.USE_SPECIAL_SADDLE) {
                    if (!handler.isSpecialSaddle(currentSaddle)) {
                        return;
                    }
                }
                e.setCancelled(true);
                e.setCursor(null);

                if (p.getInventory().firstEmpty() == -1) {
                    switch (c.TAKE_OFF_FULL_INV_MODE) {
                        case "CANCEL":
                            if (!c.TAKE_OFF_OPERATION_CANCELLED.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_OPERATION_CANCELLED);
                            e.setCancelled(true);
                            return;
                        case "GROUND":
                            if (!c.STORE_INVENTORY && h.getInventory().getSize()>=16) {
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                                }
                                horseinv.clear();
                            }
                            if (!c.ALLOW_RECURSIVE_PHORSE && h.getInventory().getSize()>=16)
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        if (content.getType().equals(Material.SADDLE)) {
                                            if (handler.isPHorse(content)) {
                                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                                e.setCancelled(true);
                                                return;
                                            }
                                        }
                                }
                            if (!c.STORE_ARMOR && horseinv.getItem(1) != null) {
                                h.getWorld().dropItem(h.getEyeLocation(), horseinv.getItem(1));
                                horseinv.setItem(1, null);
                            }
                            horseinv.setItem(0, new ItemStack(Material.SADDLE));
                            currentSaddle = handler.transferTag(h, currentSaddle);
                            currentSaddle = styler.styleSaddleNew(h, currentSaddle);
                            p.getWorld().dropItem(p.getEyeLocation(), currentSaddle);
                            SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
                            h.remove();
                            if (!c.TAKE_OFF_SADDLE_DROPPED.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_SADDLE_DROPPED);
                            return;
                        default:


                            if (!c.STORE_INVENTORY && h.getInventory().getSize()>=16) {
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                                }
                                horseinv.clear();
                            }
                            if (!c.ALLOW_RECURSIVE_PHORSE && h.getInventory().getSize()>=16)
                                for (int i = 2; i <= 16; ++i) {
                                    ItemStack content = horseinv.getItem(i);
                                    if (content != null)
                                        if (content.getType().equals(Material.SADDLE)) {
                                            if (handler.isPHorse(content)) {
                                                if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                                e.setCancelled(true);
                                                return;
                                            }
                                        }
                                }
                            if (!c.STORE_ARMOR && horseinv.getItem(1) != null) {
                                h.getWorld().dropItem(h.getEyeLocation(), horseinv.getItem(1));
                                horseinv.setItem(1, null);
                            }

                            horseinv.setItem(1, new ItemStack(Material.SADDLE));
                            currentSaddle = handler.transferTag(h, currentSaddle);
                            currentSaddle = styler.styleSaddleNew(h, currentSaddle);
                            p.getInventory().setItem(35, currentSaddle);
                            SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
                            h.remove();
                            if (!c.TAKE_OFF_FIRST_ITEM_REPLACED.isEmpty())
                                p.sendMessage(c.MESSAGES_PREFIX + c.TAKE_OFF_FIRST_ITEM_REPLACED);
                            return;
                    }

                }

                if (!c.STORE_INVENTORY && h.getInventory().getSize()>=16) {
                    for (int i = 2; i <= 16; ++i) {
                        ItemStack content = horseinv.getItem(i);
                        if (content != null)
                            h.getWorld().dropItemNaturally(h.getEyeLocation(), content);
                    }
                    horseinv.clear();
                }
                if (!c.ALLOW_RECURSIVE_PHORSE && h.getInventory().getSize()>=16)
                    for (int i = 2; i <= 16; ++i) {
                        ItemStack content = horseinv.getItem(i);
                        if (content != null)
                            if (content.getType().equals(Material.SADDLE)) {
                                if (handler.isPHorse(content)) {
                                    if (!c.MESSAGES_CANT_ADD_ITEM.isEmpty())
                                        p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_ADD_ITEM);
                                    e.setCancelled(true);
                                    return;
                                }
                            }
                    }
                if (!c.STORE_ARMOR && horseinv.getItem(1) != null) {
                    h.getWorld().dropItem(h.getEyeLocation(), horseinv.getItem(1));
                    horseinv.setItem(1, null);
                }

                if (!c.MESSAGES_HORSE_TAKEN.isEmpty())
                    p.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_HORSE_TAKEN);

                horseinv.setItem(0, new ItemStack(Material.SADDLE));
                currentSaddle = handler.transferTag(h, currentSaddle);
                currentSaddle = styler.styleSaddleNew(h, currentSaddle);
                p.getInventory().addItem(currentSaddle);
                SpawnListener.TAKERS.put(p.getName(), h.getUniqueId());
                h.remove();
            }
        }
    }



}
