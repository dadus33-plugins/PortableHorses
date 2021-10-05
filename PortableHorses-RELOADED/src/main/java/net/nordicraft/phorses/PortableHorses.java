package net.nordicraft.phorses;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import net.nordicraft.phorses.listeners.PlayerDeathListener;
import net.nordicraft.phorses.listeners.RightClickListener;
import net.nordicraft.phorses.listeners.SpawnListener;
import net.nordicraft.phorses.listeners.TakeOffListener;
import net.nordicraft.phorses.listeners.packets.ItemUpdatePacketListener;
import net.nordicraft.phorses.utils.Config;
import net.nordicraft.phorses.utils.CustomConfig;
import net.nordicraft.phorses.utils.Storage;
import net.nordicraft.phorses.utils.Styler;
import net.nordicraft.phorses.utils.Version;

@SuppressWarnings("deprecation")
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
    private PHCommand phcommand;
    private static ItemStack specialSaddle = null;

    public void onEnable(){
        instance = this;
        Version version = Version.getVersion();
		if(version.isSupported())
			PortableHorses.instance().getLogger().info("Loading support for " + version.name());
		else
			PortableHorses.instance().getLogger().info("Version " + version.name() + " isn't supported yet.");
		try {
			nmsHandler = (NMSHandler) Class.forName("net.nordicraft.phorses.implementations." + version.getHandlerName())
					.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
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
        return Version.getVersion().isNewHorseSystem();
    }

}
