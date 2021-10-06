package net.nordicraft.phorses;

import java.util.Arrays;
import java.util.List;

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
	private Listener playerDeathListener;
	private ItemUpdatePacketListener itemUpdatePacketListener;
	private Styler styler;
	private PHCommand phcommand;
	private static ItemStack specialSaddle = null;

	@Override
	public void onEnable() {
		instance = this;
		Version version = Version.getVersion();
		PortableHorses.instance().getLogger().info(version.isSupported() ? "Loading support for " + version.name()
				: "Version " + version.name() + " isn't supported yet.");
		try {
			nmsHandler = (NMSHandler) Class
					.forName("net.nordicraft.phorses.implementations." + version.getHandlerName()).getConstructor()
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		handler = new CustomConfig(this);
		config = new Config("config");
		if (config.file == null || config.fileConfig == null) {
			handler.saveDefaultConfig(config);
		}
		storage = new Storage(config, handler);
		styler = new Styler(storage, this);
		if (storage.MODE.equalsIgnoreCase("TAKEOFF")) {
			chosenListener = new TakeOffListener(nmsHandler, storage, styler);
		} else {
			chosenListener = new RightClickListener(nmsHandler, storage, styler);
		}

		getServer().getPluginManager().registerEvents(chosenListener, this);
		getServer().getPluginManager().registerEvents(new SpawnListener(nmsHandler, storage, styler), this);
		load();

		if (storage.ENABLE_FAKE_DEATH_EVENT) {
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			this.itemUpdatePacketListener = new ItemUpdatePacketListener(this, nmsHandler);
			protocolManager.addPacketListener(itemUpdatePacketListener);
		}
		phcommand = new PHCommand(storage, this);
		getCommand("ph").setExecutor(phcommand);
		getCommand("ph").setTabCompleter(phcommand);
	}

	@Override
	public void onDisable() {
		nmsHandler = null;
		storage = null;
		handler = null;
		config = null;
		instance = null;
		chosenListener = null;
	}

	public void load() {
		if (storage.USE_SPECIAL_SADDLE) {
			specialSaddle = nmsHandler.setSpecialSaddle(new ItemStack(Material.SADDLE));
			ItemMeta resultMeta = specialSaddle.getItemMeta();
			resultMeta.setDisplayName(storage.SPECIAL_NAME);
			resultMeta.setLore(storage.SPECIAL_LORE);
			if (storage.SPECIAL_ENCHANTED) {
				resultMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
				resultMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			specialSaddle.setItemMeta(resultMeta);
			if(storage.SPECIAL_CRAFTABLE) {
				Recipe recipe;
				if (storage.SPECIAL_RECEIPE_IGNORE_ORDER) {
					ShapelessRecipe sr = new ShapelessRecipe(specialSaddle);
					addIngredientIfNotAir(sr, Arrays.asList(storage.SPECIAL_RECEIPE_SLOT1, storage.SPECIAL_RECEIPE_SLOT2,
							storage.SPECIAL_RECEIPE_SLOT3, storage.SPECIAL_RECEIPE_SLOT4, storage.SPECIAL_RECEIPE_SLOT5,
							storage.SPECIAL_RECEIPE_SLOT6, storage.SPECIAL_RECEIPE_SLOT7, storage.SPECIAL_RECEIPE_SLOT8,
							storage.SPECIAL_RECEIPE_SLOT9));
					recipe = sr;
				} else {
					ShapedRecipe shr = new ShapedRecipe(specialSaddle);
					shr.shape("!@#", "$%^", "&*(");
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT1, '!');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT2, '@');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT3, '#');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT4, '$');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT5, '%');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT6, '^');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT7, '&');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT8, '*');
					setIngredientIfNotAir(shr, storage.SPECIAL_RECEIPE_SLOT9, '(');
					recipe = shr;
				}
				if (!getServer().addRecipe(recipe)) {
					getLogger().warning(ChatColor.RED
							+ "For some reason the plugin couldn't register the recipe for the special saddle!");
				}
			}
		}
	}

	public void reload() {
		handler.reloadCustomConfig(config);
		if (config.file == null || config.fileConfig == null) {
			handler.saveDefaultConfig(config);
		}
		storage.load(config, handler);
		phcommand.load();

		HandlerList.unregisterAll(chosenListener);
		if (playerDeathListener != null)
			HandlerList.unregisterAll(playerDeathListener);

		if (storage.MODE.equalsIgnoreCase("TAKEOFF")) {
			chosenListener = new TakeOffListener(nmsHandler, storage, styler);
		} else {
			chosenListener = new RightClickListener(nmsHandler, storage, styler);
		}
		getServer().getPluginManager().registerEvents(chosenListener, this);

		if (storage.ENABLE_FAKE_DEATH_EVENT) {
			playerDeathListener = new PlayerDeathListener(nmsHandler, this);
			getServer().getPluginManager().registerEvents(playerDeathListener, this);
		}
		getCommand("ph").setTabCompleter(phcommand);
		getCommand("ph").setExecutor(phcommand);

		load();
	}

	private void addIngredientIfNotAir(ShapelessRecipe shr, List<Material> list) {
		list.forEach((type) -> {
			if (type.equals(Material.AIR))
				shr.addIngredient(type);
		});
	}

	private void setIngredientIfNotAir(ShapedRecipe shr, Material type, char c) {
		if (type != Material.AIR)
			shr.setIngredient(c, type);
	}

	public NMSHandler getHandler() {
		return this.nmsHandler;
	}

	public ItemUpdatePacketListener getDeathItemPacketListener() {
		return this.itemUpdatePacketListener;
	}

	public static ItemStack getSpSaddle() {
		return specialSaddle;
	}

	public static PortableHorses instance() {
		return (PortableHorses) instance;
	}

	public static boolean newHorseSystem() {
		return Version.getVersion().isNewHorseSystem();
	}

}
