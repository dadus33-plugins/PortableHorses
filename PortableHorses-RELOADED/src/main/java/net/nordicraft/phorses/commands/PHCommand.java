package net.nordicraft.phorses.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.nordicraft.phorses.PortableHorses;
import net.nordicraft.phorses.utils.Storage;

public class PHCommand implements CommandExecutor, TabCompleter {

	private final List<String> tabs = new ArrayList<>();
	private final String cm;
	private final PortableHorses plugin;
	private Storage c;

	public PHCommand(Storage c, PortableHorses p) {
		this.c = c;
		this.plugin = p;
		
		load();
		
		if (plugin.getCommand("ph").getPermissionMessage() != null)
			cm = ChatColor.translateAlternateColorCodes('&', plugin.getCommand("ph").getPermissionMessage());
		else
			cm = ChatColor.RED
					+ "I\\'m sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.";
	}
	
	public void load() {
		if (c.ENABLE_HELP_COMMAND)
			tabs.addAll(Arrays.asList("help", "givesaddle", "reload"));
		else
			tabs.addAll(Arrays.asList("givesaddle", "reload"));
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command cmd, String label, String[] args) {
		if (args.length < 1 || args.length > 2) {
			return false;
		}

		if (!tabs.contains(args[0])) {
			return false;
		}

		switch (args[0].toLowerCase()) {
		case "help":
			if (!commandSender.hasPermission("portablehorses.command.help")) {
				commandSender.sendMessage(cm);
				return true;
			}
			String[] msgs = new String[c.MESSAGES_HELP_COMMAND.size()];
			commandSender.sendMessage(c.MESSAGES_HELP_COMMAND.toArray(msgs));
			return true;
		case "reload":
			if (!commandSender.hasPermission("portablehorses.command.reload")) {
				commandSender.sendMessage(cm);
				return true;
			}
			plugin.reload();
			if (!c.MESSAGES_RELOAD.isEmpty()) {
				commandSender.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_RELOAD);
			}
			return true;
		default:
			Player toGive;
			if (args.length == 1) {
				if (!(commandSender instanceof Player)) {
					if (!c.MESSAGES_CANT_SEND_FROM_CONSOLE.isEmpty()) {
						commandSender.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_CANT_SEND_FROM_CONSOLE);
					}
					return true;
				}
				toGive = (Player) commandSender;
			} else {
				if ((toGive = Bukkit.getPlayer(args[1])) == null) {
					if (!c.MESSAGES_PLAYER_NOT_FOUND.isEmpty()) {
						commandSender.sendMessage(c.MESSAGES_PLAYER_NOT_FOUND);
					}
					return true;
				}
			}
			if (!c.USE_SPECIAL_SADDLE) {
				if (!c.MESSAGES_SPECIAL_SADDLE_NOT_ENABLED.isEmpty())
					commandSender.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_SPECIAL_SADDLE_NOT_ENABLED);
				return true;
			}
			if (!commandSender.hasPermission("portablehorses.command.givesaddle")) {
				commandSender.sendMessage(cm);
				return true;
			}

			if (toGive.getInventory().firstEmpty() == -1) {
				if (!c.MESSAGES_NO_SPACE_FOR_SPECIAL_SADDLE.isEmpty())
					toGive.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_NO_SPACE_FOR_SPECIAL_SADDLE);
				return true;
			}
			if (!c.MESSAGES_SPECIAL_SADDLE_GIVEN.isEmpty())
				toGive.sendMessage(c.MESSAGES_PREFIX + c.MESSAGES_SPECIAL_SADDLE_GIVEN);
			toGive.getInventory().addItem(net.nordicraft.phorses.PortableHorses.getSpSaddle());
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command cmd, String label, String[] args) {
		if (args.length == 1)
			return tabs;
		return Collections.emptyList();
	}
}
