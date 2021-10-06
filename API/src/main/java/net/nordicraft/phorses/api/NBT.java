package net.nordicraft.phorses.api;

import net.nordicraft.phorses.utils.PacketUtils;

@SuppressWarnings("unchecked")
public class NBT {

	private final Object nmsNBT;
	
	public NBT(Object nmsNBT) throws Exception {
		if(nmsNBT == null)
			this.nmsNBT = PacketUtils.getNmsClass("NBTTagCompound").getConstructor().newInstance();
		else
			this.nmsNBT = nmsNBT;
	}

	public Object getNmsNBT() {
		return nmsNBT;
	}
	
	public boolean hasKey(String s) throws Exception {
		return (boolean) nmsNBT.getClass().getMethod("hasKey", String.class).invoke(getNmsNBT(), s);
	}
	
	public boolean getBoolean(String s) throws Exception {
		return get("getBoolean", s);
	}
	
	public void setBoolean(String s, boolean val) throws Exception {
		set("setBoolean", s, boolean.class, val);
	}
	
	public float getFloat(String s) throws Exception {
		return get("getFloat", s);
	}
	
	public void setFloat(String s, float val) throws Exception {
		set("setFloat", s, float.class, val);
	}
	
	public String getString(String s) throws Exception {
		return get("getString", s);
	}
	
	public void setString(String s, String val) throws Exception {
		set("setString", s, String.class, val);
	}
	
	public NBT getTag(String s) throws Exception {
		return new NBT(get("getCompound", s));
	}
	
	public void setTag(String s, NBT val) throws Exception {
		set("set", s, (Class<Object>) PacketUtils.getNmsClass("NBTBase"), val.getNmsNBT());
	}
	
	public <T> T get(String methodName, String key) throws Exception {
		return (T) nmsNBT.getClass().getMethod(methodName, String.class).invoke(getNmsNBT(), key);
	}
	
	public <T> void set(String methodName, String key, Class<T> clazz, T val) throws Exception {
		nmsNBT.getClass().getMethod(methodName, String.class, clazz).invoke(getNmsNBT(), key, val);
	}
}
