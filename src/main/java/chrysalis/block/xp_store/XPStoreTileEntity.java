package chrysalis.block.xp_store;

import chrysalis.Chrysalis;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class XPStoreTileEntity extends TileEntity {

	@ObjectHolder(Chrysalis.MODID + ":xp_store")
	public static TileEntityType<XPStoreTileEntity> TYPE;
	
	protected static final int MAX_XP_STORED = 4800;
	private int storedXp = 0;
	
	public XPStoreTileEntity() {
		super(TYPE);
	}
	
	/**
	 * Tries to extract XP from the store
	 * @param maxAmount The maximum amount of XP that should be extracted
	 * @param simulate whether the result should only be simulated (XP will not be extracted)
	 * @return the amount of XP that was extracted
	 */
	public int extract(int maxAmount, boolean simulate) {
		int amount = Math.min(maxAmount, storedXp);
		if(!simulate) {
			storedXp -= amount;
		}
		return amount;
	}
	
	/**
	 * Tries to insert XP into the store.
	 * @param maxAmount The maximum amount of XP that should be stored
	 * @param simulate whether the result should only be simulated (Xp will not be stored)
	 * @return the amount of XP that was inserted
	 */
	public int insert(int maxAmount, boolean simulate) {
		int amount = Math.min(MAX_XP_STORED - storedXp, maxAmount);
		if(!simulate) {
			storedXp += amount;
		}
		return amount;
	}
	
	public float getFillPercentage() {
		return (float)storedXp / MAX_XP_STORED;
	}

	@Override
	public void read(CompoundNBT compound) {
		storedXp = compound.getInt("XP");
		super.read(compound);
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.putInt("XP", storedXp);
		return super.write(compound);
	}
}
