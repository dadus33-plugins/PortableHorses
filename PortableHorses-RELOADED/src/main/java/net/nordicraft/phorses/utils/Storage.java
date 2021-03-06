package net.nordicraft.phorses.utils;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Copyright (C) 2016 Vlad Ardelean - All Rights Reserved You are not allowed to
 * edit, modify or decompile the contents of this file and/or any other file
 * found in the enclosing jar unless explicitly permitted by me. Written by Vlad
 * Ardelean <LongLiveVladerius@gmail.com>
 */

public class Storage {

	private FileConfiguration conf;

	public String MODE;
	public boolean ALLOW_RECURSIVE_PHORSE;
	public boolean DROP_NORMAL_SADDLE;
	public boolean AUTO_SADDLE;
	public boolean GIVE_SADDLE;
	public String AUTO_SADDLE_FALLBACK_OPTION;
	public boolean STORE_ARMOR;
	public boolean STORE_INVENTORY;
	public boolean USE_SPECIAL_SADDLE;
	public boolean ENABLE_HELP_COMMAND;
	public boolean AUTO_MOUNT;
	public boolean AUTO_TAKE_ON_DISMOUNT;
	public boolean COMBATLOG_INTEGRATION;
	public boolean ENABLE_FAKE_DEATH_EVENT;

	public String DISPLAY_NAME;

	public boolean PHORSE_ENCHANTED;
	public boolean SHOW_COLOR;
	public boolean SHOW_STYLE;
	public boolean SHOW_VARIANT;
	public boolean SHOW_JUMP_STRENGTH;
	public boolean SHOW_HAS_CHEST;
	public boolean SHOW_HEALTH;
	public boolean SHOW_SPEED;

	public String COLOR_BLACK;
	public String COLOR_BROWN;
	public String COLOR_CHESTNUT;
	public String COLOR_CREAMY;
	public String COLOR_DARK_BROWN;
	public String COLOR_GRAY;
	public String COLOR_WHITE;

	public String STYLE_BLACK_DOTS;
	public String STYLE_WHITE_DOTS;
	public String STYLE_NONE;
	public String STYLE_WHITE;
	public String STYLE_WHITEFIELD;

	public String VARIANT_DONKEY;
	public String VARIANT_HORSE;
	public String VARIANT_MULE;
	public String VARIANT_SKELETON_HORSE;
	public String VARIANT_UNDEAD_HORSE;

	public String HAS_CHEST;

	public List<String> DATA;
	public List<String> COLOR;
	public List<String> STYLE;
	public List<String> VARIANT;
	public List<String> JUMP;
	public List<String> HEALTH;
	public List<String> SPEED;

	public boolean TAKEOFF_ALLOW_STRANGER_TAKE_OFF;

	public boolean RIGHTCLICK_ALLOW_STRANGER_TAKE;
	public boolean RIGHT_CLICK_ALLOW_PHORSE_OVERRIDE;

	public boolean SPECIAL_CRAFTABLE;

	public Material SPECIAL_RECEIPE_SLOT1;
	public Material SPECIAL_RECEIPE_SLOT2;
	public Material SPECIAL_RECEIPE_SLOT3;
	public Material SPECIAL_RECEIPE_SLOT4;
	public Material SPECIAL_RECEIPE_SLOT5;
	public Material SPECIAL_RECEIPE_SLOT6;
	public Material SPECIAL_RECEIPE_SLOT7;
	public Material SPECIAL_RECEIPE_SLOT8;
	public Material SPECIAL_RECEIPE_SLOT9;
	public boolean SPECIAL_RECEIPE_IGNORE_ORDER;

	public String SPECIAL_NAME;
	public List<String> SPECIAL_LORE;
	public boolean SPECIAL_ENCHANTED;

	public List<String> MESSAGES_HELP_COMMAND;
	public String MESSAGES_PREFIX;
	public String MESSAGES_HORSE_TAKEN;
	public String MESSAGES_HORSE_SPAWNED;
	public String MESSAGES_CANT_ADD_ITEM;
	public String TAKE_OFF_FULL_INV_MODE;
	public String TAKE_OFF_SADDLE_DROPPED;
	public String TAKE_OFF_OPERATION_CANCELLED;
	public String TAKE_OFF_FIRST_ITEM_REPLACED;
	public String TAKE_OFF_DENIED_NOT_OWNER;

	public String MESSAGES_AUTO_SADDLE_FIRST_ITEM_REPLACED;
	public String MESSAGES_AUTO_SADDLE_SADDLE_DROPPED;
	public String MESSAGES_RELOAD;
	public String MESSAGES_PLAYER_NOT_FOUND;
	public String MESSAGES_SPECIAL_SADDLE_NOT_ENABLED;
	public String MESSAGES_CANT_SEND_FROM_CONSOLE;
	public String MESSAGES_NO_SPACE_FOR_SPECIAL_SADDLE;
	public String MESSAGES_SPECIAL_SADDLE_GIVEN;
	public String MESSAGES_CANT_OVERRIDE_PHORSE;
	public String MESSAGES_CANT_PLACE;
	public String MESSAGES_CANT_SPAWN_COMBATLOGGED;
	public String MESSAGES_CANT_USE_NO_PERM;

	public Storage(Config cfg, CustomConfig handler) {
		load(cfg, handler);
	}
	
	public void load(Config cfg, CustomConfig handler) {
		this.conf = handler.getCustomConfig(cfg);

		switch (conf.getString("General.mode")) {
		case "RIGHTCLICK":
			this.MODE = "RIGHTCLICK";
			break;
		case "TAKEOFF":
			this.MODE = "TAKEOFF";
			break;
		default:
			handler.plugin.getLogger().warning(ChatColor.RED + "Invalid value found in config.yml on field 'General.mode'. Found '"
					+ conf.getString("General.mode") + "'. Type available: 'TAKEOFF' or 'RIGHTCLICK'. Option resetted as default value: 'TAKEOFF'");
			conf.set("General.mode", "TAKEOFF");
			this.MODE = "TAKEOFF";
		}
		this.ALLOW_RECURSIVE_PHORSE = conf.getBoolean("General.allow-recursive-phorse");
		this.DROP_NORMAL_SADDLE = conf.getBoolean("General.drop-normal-saddle");
		this.AUTO_SADDLE = conf.getBoolean("General.auto-saddle");
		this.AUTO_MOUNT = conf.getBoolean("General.auto-mount");
		this.COMBATLOG_INTEGRATION = conf.getBoolean("General.combatlog-integration");
		this.ENABLE_FAKE_DEATH_EVENT = conf.getBoolean("General.enable-fake-death-event");
		this.AUTO_TAKE_ON_DISMOUNT = conf.getBoolean("General.auto-take-on-dismount");
		this.GIVE_SADDLE = conf.getBoolean("General.give-saddle");
		switch (conf.getString("General.auto-saddle-fallback-method").toUpperCase()) {
		case "HORSE":
			this.AUTO_SADDLE_FALLBACK_OPTION = "HORSE";
			break;
		case "FIRSTITEM":
			this.AUTO_SADDLE_FALLBACK_OPTION = "FIRSTITEM";
			break;
		case "GROUND":
			this.AUTO_SADDLE_FALLBACK_OPTION = "GROUND";
			break;
		default:
			Bukkit.getLogger().log(Level.WARNING, ChatColor.translateAlternateColorCodes('&',
					"&cInvalid value found in plugins/PortableHorses/config.yml on field "
							+ "\"General.auto-saddle-fallback-option\". Found \""
							+ conf.getString("General.auto-saddle-fallback-option") + "\" while"
							+ " only possible values are \"HORSE\", \"FIRSTITEM\" or \"GROUND\". The option was reset to"
							+ " default value: \"HORSE\""));
			conf.set("General.auto-saddle=fallback-option", "HORSE");
			this.AUTO_SADDLE_FALLBACK_OPTION = "HORSE";
		}
		this.STORE_ARMOR = conf.getBoolean("General.store-armor");
		this.STORE_INVENTORY = conf.getBoolean("General.store-inventory");
		this.USE_SPECIAL_SADDLE = conf.getBoolean("General.use-special-saddle");
		this.ENABLE_HELP_COMMAND = conf.getBoolean("General.enable-help-command");
		this.SHOW_COLOR = conf.getBoolean("PortableHorse.show-color");
		this.SHOW_STYLE = conf.getBoolean("PortableHorse.show-style");
		this.SHOW_VARIANT = conf.getBoolean("PortableHorse.show-variant");
		this.SHOW_JUMP_STRENGTH = conf.getBoolean("PortableHorse.show-jump-strength");
		this.SHOW_HAS_CHEST = conf.getBoolean("portableHorse.show-has-chest");
		this.SHOW_HEALTH = conf.getBoolean("PortableHorse.show-health");
		this.SHOW_SPEED = conf.getBoolean("PortableHorse.show-speed");
		this.COLOR_BLACK = color(conf.getString("PortableHorse.Translations.Colors.BLACK"));
		this.COLOR_BROWN = color(conf.getString("PortableHorse.Translations.Colors.BROWN"));
		this.COLOR_CHESTNUT = color(conf.getString("PortableHorse.Translations.Colors.CHESTNUT"));
		this.COLOR_CREAMY = color(conf.getString("PortableHorse.Translations.Colors.CREAMY"));
		this.COLOR_DARK_BROWN = color(conf.getString("PortableHorse.Translations.Colors.DARK_BROWN"));
		this.COLOR_GRAY = color(conf.getString("PortableHorse.Translations.Colors.GRAY"));
		this.COLOR_WHITE = color(conf.getString("PortableHorse.Translations.Colors.WHITE"));
		this.STYLE_BLACK_DOTS = color(conf.getString("PortableHorse.Translations.Styles.BLACK_DOTS"));
		this.STYLE_NONE = color(conf.getString("PortableHorse.Translations.Styles.NONE"));
		this.STYLE_WHITE = color(conf.getString("PortableHorse.Translations.Styles.WHITE"));
		this.STYLE_WHITEFIELD = color(conf.getString("PortableHorse.Translations.Styles.WHITEFIELD"));
		this.STYLE_WHITE_DOTS = color(conf.getString("PortableHorse.Translations.Styles.WHITE_DOTS"));
		this.VARIANT_DONKEY = color(conf.getString("PortableHorse.Translations.Variants.DONKEY"));
		this.VARIANT_HORSE = color(conf.getString("PortableHorse.Translations.Variants.HORSE"));
		this.VARIANT_MULE = color(conf.getString("PortableHorse.Translations.Variants.MULE"));
		this.VARIANT_SKELETON_HORSE = color(conf.getString("PortableHorse.Translations.Variants.SKELETON_HORSE"));
		this.VARIANT_UNDEAD_HORSE = color(conf.getString("PortableHorse.Translations.Variants.UNDEAD_HORSE"));
		this.HAS_CHEST = color(conf.getString("PortableHorse.Translations.has-chest"));
		this.DATA = colorList(conf.getStringList("PortableHorse.Translations.data"));
		this.COLOR = colorList(conf.getStringList("PortableHorse.Translations.color"));
		this.STYLE = colorList(conf.getStringList("PortableHorse.Translations.style"));
		this.VARIANT = colorList(conf.getStringList("PortableHorse.Translations.variant"));
		this.JUMP = colorList(conf.getStringList("PortableHorse.Translations.jump"));
		this.HEALTH = colorList(conf.getStringList("PortableHorse.Translations.health"));
		this.SPEED = colorList(conf.getStringList("PortableHorse.Translations.speed"));
		this.TAKEOFF_ALLOW_STRANGER_TAKE_OFF = conf.getBoolean("Mode-settings.takeoff.allow-stranger-take-off");
		this.RIGHTCLICK_ALLOW_STRANGER_TAKE = conf.getBoolean("Mode-settings.rightclick.allow-stranger-take");
		this.RIGHT_CLICK_ALLOW_PHORSE_OVERRIDE = conf.getBoolean("Mode-settings.rightclick.allow-override");
		this.SPECIAL_CRAFTABLE = conf.getBoolean("Special-saddle.craftable");
		this.SPECIAL_ENCHANTED = conf.getBoolean("Special-saddle.enchanted");
		this.SPECIAL_LORE = colorList(conf.getStringList("Special-saddle.lore"));
		this.SPECIAL_NAME = color(conf.getString("Special-saddle.name"));
		this.SPECIAL_RECEIPE_IGNORE_ORDER = conf.getBoolean("Special-saddle.recipe.ignore-order");
		this.SPECIAL_RECEIPE_SLOT1 = Material.valueOf(conf.getString("Special-saddle.recipe.slot1"));
		this.SPECIAL_RECEIPE_SLOT2 = Material.valueOf(conf.getString("Special-saddle.recipe.slot2"));
		this.SPECIAL_RECEIPE_SLOT3 = Material.valueOf(conf.getString("Special-saddle.recipe.slot3"));
		this.SPECIAL_RECEIPE_SLOT4 = Material.valueOf(conf.getString("Special-saddle.recipe.slot4"));
		this.SPECIAL_RECEIPE_SLOT5 = Material.valueOf(conf.getString("Special-saddle.recipe.slot5"));
		this.SPECIAL_RECEIPE_SLOT6 = Material.valueOf(conf.getString("Special-saddle.recipe.slot6"));
		this.SPECIAL_RECEIPE_SLOT7 = Material.valueOf(conf.getString("Special-saddle.recipe.slot7"));
		this.SPECIAL_RECEIPE_SLOT8 = Material.valueOf(conf.getString("Special-saddle.recipe.slot8"));
		this.SPECIAL_RECEIPE_SLOT9 = Material.valueOf(conf.getString("Special-saddle.recipe.slot9"));
		this.MESSAGES_HELP_COMMAND = colorList(conf.getStringList("Messages.help-command"));
		this.MESSAGES_PREFIX = color(conf.getString("Messages.prefix"));
		this.MESSAGES_HORSE_SPAWNED = color(conf.getString("Messages.horse-spawned"));
		this.MESSAGES_HORSE_TAKEN = color(conf.getString("Messages.horse-taken"));
		this.MESSAGES_CANT_ADD_ITEM = color(conf.getString("Messages.cant-use-nested-saddle"));
		String take_off_full_inv_mode = conf.getString("Mode-settings.takeoff.take-off-full-inv-mode");
		switch (take_off_full_inv_mode.toUpperCase()) {
		case "GROUND":
			this.TAKE_OFF_FULL_INV_MODE = "GROUND";
			break;
		case "CANCEL":
			this.TAKE_OFF_FULL_INV_MODE = "CANCEL";
			break;
		case "FIRSTITEM":
			this.TAKE_OFF_FULL_INV_MODE = "FIRSTITEM";
			break;
		default:
			Bukkit.getLogger().log(Level.WARNING, ChatColor.translateAlternateColorCodes('&',
					"&cInvalid value found in plugins/PortableHorses/config.yml on field "
							+ "\"Mode-settings.take-off-full-inv-mode\". Found \"" + take_off_full_inv_mode + "\" while"
							+ " the only possible values are \"GROUND\", \"CANCEL\" or \"FIRSTITEM\". The option was reset to"
							+ " default value: \"GROUND\""));
			conf.set("Mode-settings.takeoff.take-off-full-inv-mode", "GROUND");
			this.TAKE_OFF_FULL_INV_MODE = "TAKEOFF";
		}
		this.TAKE_OFF_FIRST_ITEM_REPLACED = color(conf.getString("Messages.take-off-first-item-replaced"));
		this.TAKE_OFF_OPERATION_CANCELLED = color(conf.getString("Messages.take-off-operation-cancelled"));
		this.TAKE_OFF_SADDLE_DROPPED = color(conf.getString("Messages.take-off-saddle-dropped"));
		this.TAKE_OFF_DENIED_NOT_OWNER = color(conf.getString("Messages.take-off-denied-not-owner"));
		this.DISPLAY_NAME = color(conf.getString("PortableHorse.Translations.Display-name"));
		this.MESSAGES_AUTO_SADDLE_FIRST_ITEM_REPLACED = color(
				conf.getString("Messages.auto-saddle-first-item-replaced"));
		this.MESSAGES_AUTO_SADDLE_SADDLE_DROPPED = color(conf.getString("Messages.auto-saddle-saddle-dropped"));
		this.MESSAGES_RELOAD = color(conf.getString("Messages.reload"));
		this.MESSAGES_PLAYER_NOT_FOUND = color(conf.getString("Messages.player-not-found"));
		this.MESSAGES_SPECIAL_SADDLE_NOT_ENABLED = color(conf.getString("Messages.special-saddle-not-enabled"));
		this.MESSAGES_CANT_SEND_FROM_CONSOLE = color(conf.getString("Messages.cant-send-from-console"));
		this.MESSAGES_NO_SPACE_FOR_SPECIAL_SADDLE = color(conf.getString("Messages.no-space-for-special-saddle"));
		this.MESSAGES_SPECIAL_SADDLE_GIVEN = color(conf.getString("Messages.special-saddle-given"));
		this.MESSAGES_CANT_OVERRIDE_PHORSE = color(conf.getString("Messages.cant-override-phorse"));
		this.MESSAGES_CANT_PLACE = color(conf.getString("Messages.cant-place"));
		this.MESSAGES_CANT_SPAWN_COMBATLOGGED = color(conf.getString("Messages.combatlog-cant-spawn"));
		this.MESSAGES_CANT_USE_NO_PERM = color(conf.getString("Messages.cant-use-no-permission"));
		this.PHORSE_ENCHANTED = conf.getBoolean("PortableHorse.enchanted");
	}

	private static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	private static List<String> colorList(List<String> ls) {
		List<String> ret = new ArrayList<String>();
		for (String s : ls) {
			ret.add(color(s));
		}
		return ret;
	}

}
