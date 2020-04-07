package chrysalis.capability;

import net.minecraft.nbt.INBT;

public interface IChrysalisData {

	boolean hasKey(String key);
	
	INBT getTag(String key);
	void setTag(String key, INBT value);
	
	void setBoolean(String key, boolean value);
	boolean getBoolean(String key);
	
	void setInt(String key, int value);
	int getInt(String key);
	
	INBT serializeToNBT();
	void deserializeFromNBT(INBT nbt);
}
