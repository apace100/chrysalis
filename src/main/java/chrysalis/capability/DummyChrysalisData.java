package chrysalis.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class DummyChrysalisData implements IChrysalisData {

	@Override
	public boolean hasKey(String key) {
		return false;
	}

	@Override
	public INBT getTag(String key) {
		return new CompoundNBT();
	}

	@Override
	public void setTag(String key, INBT value) {
		// no op
	}

	@Override
	public void setBoolean(String key, boolean value) {
		// no op
	}

	@Override
	public boolean getBoolean(String key) {
		return false;
	}

	@Override
	public void setInt(String key, int value) {
		// no op
	}

	@Override
	public int getInt(String key) {
		return 0;
	}

	@Override
	public INBT serializeToNBT() {
		return new CompoundNBT();
	}

	@Override
	public void deserializeFromNBT(INBT nbt) {
		// no op
	}

}
