package net.nordicraft.phorses.utils;

import org.bukkit.Bukkit;

public enum Version {

	V1_8_R1("1.8.1", 81, "v1_8_R1.Handler1_8_R1"),
	V1_8_R2("1.8.3", 82, "v1_8_R2.Handler1_8_R2"),
	V1_8_R3("1.8.8", 83, "v1_8_R3.Handler1_8_R3"),
	V1_9_R1("1.9.1", 91, "v1_9_R1.Handler1_9_R1"),
	V1_9_R2("1.9.4", 90, "v1_9_R2.Handler1_9_R2"),
	V1_10_R1("1.10", 101, "v1_10_R1.Handler1_10_R1"),
	V1_11_R1("1.11", 111, "v1_11_R1.Handler1_11_R1"),
	V1_12_R1("1.12", 121, "v1_12_R1.Handler1_12_R1"),
	V1_13_R1("1.13.1", 131, "v1_13_R1.Handler1_13_R1"),
	V1_13_R2("1.13.2", 132, "v1_13_R2.Handler1_13_R2"),
	V1_14_R1("1.14", 141, "v1_14_R1.Handler1_14_R1"),
	V1_15_R1("1.15", 151, "v1_15_R1.Handler1_15_R1"),
	V1_16_R1("1.16.1", 161, "v1_16_R1.Handler1_16_R1"),
	V1_16_R2("1.16.2", 162, "v1_16_R2.Handler1_16_R2"),
	V1_16_R3("1.16.4", 163, "v1_16_R3.Handler1_16_R3"),
	V1_17_R1("1.17", 171, null),
	HIGHER("higher", Integer.MAX_VALUE, null);

	private static final Version VERSION;
	
	static {
		VERSION = getVersionByName(Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]);
	}
	
	private final int power;
	private final String name;
	private final String handlerName;
	
	Version(String name, int power, String handlerName) {
		this.name = name;
		this.power = power;
		this.handlerName = handlerName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHandlerName() {
		return handlerName;
	}
	
	public boolean isSupported() {
		return handlerName != null;
	}

	public boolean isNewerThan(Version other) {
		return power > other.getPower();
	}
	
	public boolean isNewerOrEquals(Version other) {
		return power >= other.getPower();
	}

	public int getPower() {
		return power;
	}
	
	public boolean isNewHorseSystem() {
		return power > V1_10_R1.power; // until 1.10 include it was old system
	}
	
	public static Version getVersionByName(String name) {
		for (Version v : Version.values())
			if (name.equalsIgnoreCase(v.name()))
				return v;
		return HIGHER;
	}
	
	public static boolean isNewerOrEquals(Version v1, Version v2) {
		return v1.isNewerOrEquals(v2);
	}
	
	public static Version getVersion() {
		return VERSION;
	}
}
