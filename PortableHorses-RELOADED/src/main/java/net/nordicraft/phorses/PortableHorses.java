package net.nordicraft.phorses;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import net.nordicraft.phorses.api.NMSHandler;
import net.nordicraft.phorses.commands.PHCommand;
import net.nordicraft.phorses.implementations.v1_10_R1.Handler1_10_R1;
import net.nordicraft.phorses.implementations.v1_11_R1.Handler1_11_R1;
import net.nordicraft.phorses.implementations.v1_12_R1.Handler1_12_R1;
import net.nordicraft.phorses.implementations.v1_8_R1.Handler1_8_R1;
import net.nordicraft.phorses.implementations.v1_8_R2.Handler1_8_R2;
import net.nordicraft.phorses.implementations.v1_8_R3.Handler1_8_R3;
import net.nordicraft.phorses.implementations.v1_9_R1.Handler1_9_R1;
import net.nordicraft.phorses.implementations.v1_9_R2.Handler1_9_R2;
import net.nordicraft.phorses.listeners.PlayerDeathListener;
import net.nordicraft.phorses.listeners.RightClickListener;
import net.nordicraft.phorses.listeners.SpawnListener;
import net.nordicraft.phorses.listeners.TakeOffListener;
import net.nordicraft.phorses.listeners.packets.ItemUpdatePacketListener;
import net.nordicraft.phorses.utils.Config;
import net.nordicraft.phorses.utils.CustomConfig;
import net.nordicraft.phorses.utils.Storage;
import net.nordicraft.phorses.utils.Styler;

/**
 * Copyright (C) 2016 Vlad Ardelean - All Rights Reserved
 * You are not allowed to edit, modify or
 * decompile the contents of this file and/or
 * any other file found in the enclosing jar
 * unless explicitly permitted by me.
 * Written by Vlad Ardelean <LongLiveVladerius@gmail.com>
 */
public class PortableHorses extends JavaPlugin {
    private NMSHandler nmsHandler;
    private Storage storage;
    private CustomConfig handler;
    private Config config;
    private static Plugin instance;
    private Listener chosenListener;
    private Listener spawnListener;
    private Listener playerDeathListener;
    private ItemUpdatePacketListener itemUpdatePacketListener;
    private Styler styler;
    private static boolean post111;
    private PHCommand phcommand;
    private static ItemStack specialSaddle = null;


    public void onEnable(){
        instance = this;
        post111 = isPost111();
        switch(getVersion(Bukkit.getServer())){
            case "v1_8_R1": nmsHandler = new Handler1_8_R1(this); break;
            case "v1_8_R2": nmsHandler = new Handler1_8_R2(this); break;
            case "v1_8_R3": nmsHandler = new Handler1_8_R3(this); break;
            case "v1_9_R1": nmsHandler = new Handler1_9_R1(this); break;
            case "v1_9_R2": nmsHandler = new Handler1_9_R2(this); break;
            case "v1_10_R1": nmsHandler = new Handler1_10_R1(this); break;
            case "v1_11_R1": nmsHandler = new Handler1_11_R1(this); break;
            case "v1_12_R1": nmsHandler = new Handler1_12_R1(this); break;
        }

        handler = new CustomConfig(this);
        config = new Config("config");
        if (config.file == null || config.fileConfig == null){
            handler.saveDefaultConfig(config);
        }
        storage = new Storage(config, handler);
        styler = new Styler(storage, this);
        if(storage.MODE.equalsIgnoreCase("TAKEOFF")){
            chosenListener = new TakeOffListener(nmsHandler, storage, styler);
        }else{
            chosenListener = new RightClickListener(nmsHandler, storage, styler);
        }

        getServer().getPluginManager().registerEvents(chosenListener, this);
        spawnListener = new SpawnListener(nmsHandler, storage, styler);
        getServer().getPluginManager().registerEvents(spawnListener, this);

        if(storage.USE_SPECIAL_SADDLE && storage.SPECIAL_CRAFTABLE){
            Recipe recipe;
            ItemStack result = new ItemStack(Material.SADDLE);
            result = nmsHandler.setSpecialSaddle(result);
            ItemMeta resultMeta = result.getItemMeta();
            resultMeta.setDisplayName(storage.SPECIAL_NAME);
            resultMeta.setLore(storage.SPECIAL_LORE);
            if(storage.SPECIAL_ENCHANTED){
                resultMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                resultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            result.setItemMeta(resultMeta);
            if(storage.SPECIAL_RECEIPE_IGNORE_ORDER){
                ShapelessRecipe sr = new ShapelessRecipe(result);
                if(storage.SPECIAL_RECEIPE_SLOT1 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT1);
                if(storage.SPECIAL_RECEIPE_SLOT2 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT2);
                if(storage.SPECIAL_RECEIPE_SLOT3 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT3);
                if(storage.SPECIAL_RECEIPE_SLOT4 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT4);
                if(storage.SPECIAL_RECEIPE_SLOT5 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT5);
                if(storage.SPECIAL_RECEIPE_SLOT6 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT6);
                if(storage.SPECIAL_RECEIPE_SLOT7 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT7);
                if(storage.SPECIAL_RECEIPE_SLOT8 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT8);
                if(storage.SPECIAL_RECEIPE_SLOT9 != Material.AIR)
                sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT9);
                recipe = sr;
            }else{
                ShapedRecipe shr = new ShapedRecipe(result);
                shr.shape("!@#",
                          "$%^",
                          "&*(");
                if(storage.SPECIAL_RECEIPE_SLOT1 != Material.AIR)
                shr.setIngredient('!', storage.SPECIAL_RECEIPE_SLOT1);
                if(storage.SPECIAL_RECEIPE_SLOT2 != Material.AIR)
                shr.setIngredient('@', storage.SPECIAL_RECEIPE_SLOT2);
                if(storage.SPECIAL_RECEIPE_SLOT3 != Material.AIR)
                shr.setIngredient('#', storage.SPECIAL_RECEIPE_SLOT3);
                if(storage.SPECIAL_RECEIPE_SLOT4 != Material.AIR)
                shr.setIngredient('$', storage.SPECIAL_RECEIPE_SLOT4);
                if(storage.SPECIAL_RECEIPE_SLOT5 != Material.AIR)
                shr.setIngredient('%', storage.SPECIAL_RECEIPE_SLOT5);
                if(storage.SPECIAL_RECEIPE_SLOT6 != Material.AIR)
                shr.setIngredient('^', storage.SPECIAL_RECEIPE_SLOT6);
                if(storage.SPECIAL_RECEIPE_SLOT7 != Material.AIR)
                shr.setIngredient('&', storage.SPECIAL_RECEIPE_SLOT7);
                if(storage.SPECIAL_RECEIPE_SLOT8 != Material.AIR)
                shr.setIngredient('*', storage.SPECIAL_RECEIPE_SLOT8);
                if(storage.SPECIAL_RECEIPE_SLOT9 != Material.AIR)
                shr.setIngredient('(', storage.SPECIAL_RECEIPE_SLOT9);
                recipe = shr;
            }
            if(!Bukkit.getServer().addRecipe(recipe)){
                Bukkit.getLogger().log(Level.WARNING, ChatColor.RED+"For some reason the plugin couldn't register the recipe for the special saddle!");
            }
        }

        if(storage.ENABLE_FAKE_DEATH_EVENT){
            ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
            this.itemUpdatePacketListener = new ItemUpdatePacketListener(this, nmsHandler);
            protocolManager.addPacketListener(itemUpdatePacketListener);
        }
        phcommand = new PHCommand(storage, this, nmsHandler);
        Bukkit.getPluginCommand("ph").setExecutor(phcommand);
        Bukkit.getPluginCommand("ph").setTabCompleter(phcommand);
        if(storage.USE_SPECIAL_SADDLE){
            specialSaddle = styler.makeSpecialSaddle();
        }

    }

    public void onDisable(){
        nmsHandler = null;
        storage = null;
        handler = null;
        config = null;
        instance = null;
        chosenListener = null;
    }


    public void reload(){
        handler.reloadCustomConfig(config);
        chosenListener = null;
        handler = new CustomConfig(instance);
        config = new Config("config");
        if (config.file == null || config.fileConfig == null){
            handler.saveDefaultConfig(config);
        }
        storage = new Storage(config, handler);
        styler = new Styler(storage, this);
        phcommand = new PHCommand(storage, this, nmsHandler);

        HandlerList.unregisterAll(this);

        if(storage.MODE.equalsIgnoreCase("TAKEOFF")){
            chosenListener = new TakeOffListener(nmsHandler, storage, styler);
        }else{
            chosenListener = new RightClickListener(nmsHandler, storage, styler);
        }
        getServer().getPluginManager().registerEvents(chosenListener, this);
        spawnListener = new SpawnListener(nmsHandler, storage, styler);
        getServer().getPluginManager().registerEvents(spawnListener, this);
        if(storage.ENABLE_FAKE_DEATH_EVENT){
            playerDeathListener = new PlayerDeathListener(nmsHandler, this);
            getServer().getPluginManager().registerEvents(playerDeathListener, this);
        }
        getCommand("ph").setTabCompleter(phcommand);
        getCommand("ph").setExecutor(phcommand);
        if(storage.USE_SPECIAL_SADDLE){
            specialSaddle = styler.makeSpecialSaddle();
        }
        if(storage.USE_SPECIAL_SADDLE && storage.SPECIAL_CRAFTABLE){
            Recipe recipe;

            if(storage.SPECIAL_RECEIPE_IGNORE_ORDER){
                ShapelessRecipe sr = new ShapelessRecipe(specialSaddle);
                if(storage.SPECIAL_RECEIPE_SLOT1 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT1);
                if(storage.SPECIAL_RECEIPE_SLOT2 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT2);
                if(storage.SPECIAL_RECEIPE_SLOT3 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT3);
                if(storage.SPECIAL_RECEIPE_SLOT4 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT4);
                if(storage.SPECIAL_RECEIPE_SLOT5 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT5);
                if(storage.SPECIAL_RECEIPE_SLOT6 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT6);
                if(storage.SPECIAL_RECEIPE_SLOT7 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT7);
                if(storage.SPECIAL_RECEIPE_SLOT8 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT8);
                if(storage.SPECIAL_RECEIPE_SLOT9 != Material.AIR)
                    sr.addIngredient(storage.SPECIAL_RECEIPE_SLOT9);
                recipe = sr;
            }else{
                ShapedRecipe shr = new ShapedRecipe(specialSaddle);
                shr.shape("!@#",
                        "$%^",
                        "&*(");
                if(storage.SPECIAL_RECEIPE_SLOT1 != Material.AIR)
                    shr.setIngredient('!', storage.SPECIAL_RECEIPE_SLOT1);
                if(storage.SPECIAL_RECEIPE_SLOT2 != Material.AIR)
                    shr.setIngredient('@', storage.SPECIAL_RECEIPE_SLOT2);
                if(storage.SPECIAL_RECEIPE_SLOT3 != Material.AIR)
                    shr.setIngredient('#', storage.SPECIAL_RECEIPE_SLOT3);
                if(storage.SPECIAL_RECEIPE_SLOT4 != Material.AIR)
                    shr.setIngredient('$', storage.SPECIAL_RECEIPE_SLOT4);
                if(storage.SPECIAL_RECEIPE_SLOT5 != Material.AIR)
                    shr.setIngredient('%', storage.SPECIAL_RECEIPE_SLOT5);
                if(storage.SPECIAL_RECEIPE_SLOT6 != Material.AIR)
                    shr.setIngredient('^', storage.SPECIAL_RECEIPE_SLOT6);
                if(storage.SPECIAL_RECEIPE_SLOT7 != Material.AIR)
                    shr.setIngredient('&', storage.SPECIAL_RECEIPE_SLOT7);
                if(storage.SPECIAL_RECEIPE_SLOT8 != Material.AIR)
                    shr.setIngredient('*', storage.SPECIAL_RECEIPE_SLOT8);
                if(storage.SPECIAL_RECEIPE_SLOT9 != Material.AIR)
                    shr.setIngredient('(', storage.SPECIAL_RECEIPE_SLOT9);
                recipe = shr;
            }
            if(!Bukkit.getServer().addRecipe(recipe)){
                Bukkit.getLogger().log(Level.WARNING, ChatColor.RED+"For some reason the plugin couldn't register the recipe for the special saddle!");
            }
        }

    }



    private static String getVersion(Server server) {
        final String packageName = server.getClass().getPackage().getName();

        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    private boolean isPost111(){
        String version = getVersion(Bukkit.getServer());
        switch(version){
            case "v1_8_R1": return false;
            case "v1_8_R2": return false;
            case "v1_8_R3": return false;
            case "v1_9_R1": return false;
            case "v1_9_R2": return false;
            case "v1_10_R1": return false;
            default: return true;
        }
    }

    public NMSHandler getHandler(){
        return this.nmsHandler;
    }

    public ItemUpdatePacketListener getDeathItemPacketListener(){
        return this.itemUpdatePacketListener;
    }

    public static ItemStack getSpSaddle(){
        return specialSaddle;
    }


    public static PortableHorses instance(){
        return (PortableHorses)instance;
    }


    public static boolean newHorseSystem(){
        return post111;
    }

}
