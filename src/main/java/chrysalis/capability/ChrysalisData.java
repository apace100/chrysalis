package chrysalis.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class ChrysalisData implements IChrysalisData {

	private CompoundNBT data;
	
	public ChrysalisData() {
		data = new CompoundNBT();
	}
	
	@Override
	public boolean hasKey(String key) {
		return data.contains(key);
	}

	@Override
	public INBT getTag(String key) {
		if(!hasKey(key)) {
			throw new IllegalArgumentException("Error: tag with key '" + key + "' was not defined on " + ChrysalisData.class.getSimpleName() + ".");
		}
		return data.get(key);
	}

	@Override
	public void setTag(String key, INBT base) {
		data.put(key, base);
	}

	@Override
	public INBT serializeToNBT() {
		return data.copy();
	}

	@Override
	public void deserializeFromNBT(INBT nbt) {
		data = (CompoundNBT)nbt.copy();
	}

	@Override
	public void setBoolean(String key, boolean value) {
		data.putBoolean(key, value);
	}

	@Override
	public boolean getBoolean(String key) {
		return data.getBoolean(key);
	}

	@Override
	public void setInt(String key, int value) {
		data.putInt(key, value);
	}

	@Override
	public int getInt(String key) {
		return data.getInt(key);
	}

	
}
