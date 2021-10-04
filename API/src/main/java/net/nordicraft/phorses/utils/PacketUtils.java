package net.nordicraft.phorses.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class PacketUtils {

	public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",")
			.split(",")[3];
	public static final String NMS_PREFIX;
	public static final String OBC_PREFIX;

	public static final Class<?> CRAFT_PLAYER_CLASS, CRAFT_SERVER_CLASS, CRAFT_ENTITY_CLASS;
	public static Class<?> ENUM_PLAYER_INFO;
	
	/**
	 * This Map is to reduce Reflection action which take more ressources than just RAM action
	 */
	private static final HashMap<String, Class<?>> ALL_CLASS;
	
	static {
		ALL_CLASS = new HashMap<>();
		NMS_PREFIX = Version.getVersion().isNewerOrEquals(Version.V1_17_R1) ? "net.minecraft." : "net.minecraft.server." + VERSION + ".";
		OBC_PREFIX = "org.bukkit.craftbukkit." + VERSION + ".";
		CRAFT_PLAYER_CLASS = getObcClass("entity.CraftPlayer");
		CRAFT_SERVER_CLASS = getObcClass("CraftServer");
		CRAFT_ENTITY_CLASS = getObcClass("entity.CraftEntity");
	}
	
	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @return clazz the searched class
	 */
	public static Class<?> getNmsClass(String name){
		return getNmsClass(name, null);
	}
	
	/**
	 * Get the Class in NMS, with a processing reducer
	 * 
	 * @param name of the NMS class (in net.minecraft.server package ONLY, because it's NMS)
	 * @return clazz the searched class
	 */
	public static Class<?> getNmsClass(String name, String packagePrefix){
		return ALL_CLASS.computeIfAbsent(name, (a) -> {
			try {
				return Class.forName(NMS_PREFIX + (Version.getVersion().isNewerOrEquals(Version.V1_17_R1) ? packagePrefix : "") + name);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}
	
	/**
	 * Get the Class in OBC, with a processing reducer
	 * 
	 * @param name of the OBC class (in org.bukkit.craftbukkit package ONLY, because it's NMS)
	 * @return clazz the searched class
	 */
	public static Class<?> getObcClass(String name){
		return ALL_CLASS.computeIfAbsent(name, (a) -> {
			try {
				return Class.forName(OBC_PREFIX + name);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}
	
	/**
	 * Create a new instance of a packet (without any parameters)
	 * 
	 * @param packetName the name of the packet which is in NMS
	 * @return the created packet
	 */
	public static Object createPacket(String packetName) {
		try {
			return getNmsClass(packetName, "network.protocol.game.").getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param p the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getCraftServer() {
		try {
			Object craftServer = CRAFT_SERVER_CLASS.cast(Bukkit.getServer());
			return craftServer.getClass().getMethod("getHandle").invoke(craftServer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get NMS entity player of specified one
	 * 
	 * @param p the player that we want the NMS entity player
	 * @return the entity player
	 */
	public static Object getNMSEntity(Entity et) {
		try {
			Object craftEntity = CRAFT_ENTITY_CLASS.cast(et);
			return craftEntity.getClass().getMethod("getHandle").invoke(craftEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Get the NMS world server
	 * 
	 * @param world the world of which we want to get the NMS world server
	 * @return the world server of location's world
	 */
	public static Object getWorldServer(World world) {
		try {
			Object object = getObcClass("CraftWorld").cast(world);
			return object.getClass().getMethod("getHandle").invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getNmsEntityName(Object nmsEntity) {
		try {
			if(Version.getVersion().isNewerOrEquals(Version.V1_13_R1)) {
				Object chatBaseComponent = getNmsClass("Entity", "world.entity.").getDeclaredMethod("getDisplayName").invoke(nmsEntity);
				return (String) getNmsClass("IChatBaseComponent", "network.chat.").getDeclaredMethod("getString").invoke(chatBaseComponent);
			} else {
				return (String) getNmsClass("Entity", "world.entity.").getDeclaredMethod("getName").invoke(nmsEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
