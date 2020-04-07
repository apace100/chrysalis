package chrysalis.block;

import java.util.List;

import chrysalis.Chrysalis;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.registries.ObjectHolder;

public class ItemGrateTileEntity extends TileEntity {

	@ObjectHolder(Chrysalis.MODID + ":item_grate")
	public static TileEntityType<ItemGrateTileEntity> TYPE;
	
	private IInventory inventory;
	private LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(this::createInputItemHandler);
	private LazyOptional<IItemHandler> outputItemHandler = LazyOptional.of(this::createOutputItemHandler);
	
	public ItemGrateTileEntity() {
		this(TYPE);
	}
	
	public ItemGrateTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		this.initializeInventory();
	}
	
	private void initializeInventory() {
		if(this.inventory == null) {
			this.inventory = new Inventory(9);
		}
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.initializeInventory();
		if(compound.contains("Inventory")) {
			ListNBT list = (ListNBT) compound.get("Inventory");
			for(int i = 0; i < inventory.getSizeInventory(); i++) {
				inventory.setInventorySlotContents(i, ItemStack.read(list.getCompound(i)));
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT comp = super.write(compound);
		ListNBT list = new ListNBT();
		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			CompoundNBT nbt = new CompoundNBT();
			nbt = inventory.getStackInSlot(i).write(nbt);
			list.add(nbt);
		}
		comp.put("Inventory", list);
		return comp;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == Direction.UP) {
				return inputItemHandler.cast();
			} else
			if(side == Direction.DOWN) {
				return outputItemHandler.cast();
			}
		}
		return super.getCapability(cap, side);
	}

	private Ingredient getIngredient() {
		ItemStack[] stacks = new ItemStack[4];
		int count = 0;
		for(int i = 0; i < 4; i++) {
			Direction dir = Direction.byHorizontalIndex(i);
			AxisAlignedBB aabb = new AxisAlignedBB(pos.offset(dir), pos.offset(dir).add(1, 1, 1));
			List<ItemFrameEntity> frames = world.getEntitiesWithinAABB(ItemFrameEntity.class, aabb);
			for(ItemFrameEntity frame : frames) {
				if(frame.getHorizontalFacing() == dir) {
					stacks[i] = frame.getDisplayedItem();
					count++;
				}
			}
		}
		if(count > 0) {
			ItemStack[] ings = new ItemStack[count];
			int c = 0;
			for(int i = 0; i < 4; i++) {
				if(stacks[i] != null) {
					ings[c++] = stacks[i];
				}
			}
			return Ingredient.fromStacks(ings);
		}
		return Ingredient.EMPTY;
	}
	
	private IItemHandler createInputItemHandler() {
		return new InvWrapper(this.inventory) {

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				Ingredient ingredient = getIngredient();
				if(ingredient == Ingredient.EMPTY || ingredient.test(stack)) {
					if(!simulate) {
						markDirty();
					}
					return super.insertItem(slot, stack, simulate);
				}
				return stack;
			}
			
		};
	}
	
	private IItemHandler createOutputItemHandler() {
		return new InvWrapper(this.inventory) {

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				if(!simulate) {
					markDirty();
				}
				return super.extractItem(slot, amount, simulate);
			}
			
		};
	}
}