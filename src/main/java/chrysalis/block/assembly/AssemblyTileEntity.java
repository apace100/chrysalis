package chrysalis.block.assembly;

import chrysalis.Chrysalis;
import chrysalis.block.Blocks;
import chrysalis.item.Items;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

public class AssemblyTileEntity extends TileEntity implements INamedContainerProvider {

	@ObjectHolder(Chrysalis.MODID + ":assembly")
	public static TileEntityType<AssemblyTileEntity> TYPE;

	protected LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(this::createInputItemHandler);
	protected LazyOptional<IItemHandler> blueprintItemHandler = LazyOptional.of(this::createBlueprintItemHandler);
	protected LazyOptional<IItemHandler> resultItemHandler = LazyOptional.of(this::createResultItemHandler);
	
	private ICraftingRecipe recipe = null;
	private ItemStack craftingResult = ItemStack.EMPTY;
	private NonNullList<ItemStack> craftingInput = NonNullList.create();
	private NonNullList<ItemStack> craftingMatrix = NonNullList.create();
	
	protected ItemStack overflow = ItemStack.EMPTY;

	protected static final int ENERGY_BUFFER = 800;
	protected static final int ENERGY_USE = 4;
	protected static final int MAX_ENERGY_RECEIVED = 20;

	public AssemblyTileEntity() {
		super(TYPE);
	}
	
	private void updateRecipe() {
		ItemStack blueprint = getBlueprintItemHandler().getStackInSlot(0);
		if(blueprint != null && !blueprint.isEmpty()) {
			CompoundNBT bpNBT = blueprint.getChildTag("Blueprint");

			recipe = ((ICraftingRecipe)world.getRecipeManager().getRecipe(new ResourceLocation(bpNBT.getString("RecipeId"))).orElse(null));
			craftingResult = ItemStack.read(bpNBT.getCompound("Output"));
			craftingInput = NonNullList.withSize(bpNBT.getInt("InputCount"), ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(bpNBT.getCompound("Input"), craftingInput);
			craftingMatrix = NonNullList.withSize(bpNBT.getInt("CraftMatrixSize"), ItemStack.EMPTY);
			ItemStackHelper.loadAllItems(bpNBT.getCompound("CraftMatrix"), craftingMatrix);
		} else {
			craftingResult = ItemStack.EMPTY;
			craftingInput.clear();
			craftingMatrix.clear();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound) {
		inputItemHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(compound.getCompound("Input")));
		blueprintItemHandler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(compound.getCompound("BlueprintStack")));
		overflow = ItemStack.read(compound.getCompound("Overflow"));
		super.read(compound);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		inputItemHandler.ifPresent(h -> {
			CompoundNBT inputCompound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
			compound.put("Input", inputCompound);
		});
		blueprintItemHandler.ifPresent(h -> {
			CompoundNBT outputCompound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
			compound.put("BlueprintStack", outputCompound);
		});
		CompoundNBT overflowNBT = new CompoundNBT();
		overflow.write(overflowNBT);
		compound.put("Overflow", overflowNBT);
		return super.write(compound);
	}


	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side != Direction.DOWN) {
				return inputItemHandler.cast();
			} else {
				return resultItemHandler.cast();
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity entity) {
		return new AssemblyContainer(id, world, pos, inv);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TranslationTextComponent("container." + Blocks.ASSEMBLY.getRegistryName().getPath());
	}

	private IItemHandler createInputItemHandler() {
		return new ItemStackHandler(9) {

			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				markDirty();
			}
			
		};
	}

	protected IItemHandler getInputItemHandler() {
		return inputItemHandler.orElse(null);
	}

	private IItemHandler createBlueprintItemHandler() {
		return new BlueprintItemHandler(1);
	}

	protected BlueprintItemHandler getBlueprintItemHandler() {
		return (BlueprintItemHandler)blueprintItemHandler.orElse(null);
	}
	
	private boolean canCraft() {
		if(recipe == null) {
			if(!getBlueprintItemHandler().getStackInSlot(0).isEmpty()) {
				updateRecipe();
				if(recipe == null) {
					return false;
				}
			} else {
				return false;
			}
		}
		if(!craftingResult.isEmpty()) {
			if(craftingInput.size() > 0) {
				IItemHandler input = getInputItemHandler();
				for(ItemStack in : craftingInput) {
					int remaining = in.getCount();
					for(int i = 0; i < input.getSlots() && remaining > 0; i++) {
						ItemStack inputStack = input.getStackInSlot(i);
						if(ItemHandlerHelper.canItemStacksStack(in, inputStack)) {
							ItemStack extractStack = input.extractItem(i, remaining, true);
							remaining -= extractStack.getCount();
						}
					}
					if(remaining > 0) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	private void shrinkCraftingInputs() {
		IItemHandler input = getInputItemHandler();
		for(ItemStack in : craftingInput) {
			int remaining = in.getCount();
			if(in.hasContainerItem()) {
				for(int i = 0; i < input.getSlots() && remaining > 0; i++) {
					ItemStack inputStack = input.getStackInSlot(i);
					if(ItemHandlerHelper.canItemStacksStack(in, inputStack)) {
						ItemStack extractStack = input.extractItem(i, remaining, false);
						remaining -= extractStack.getCount();
						ItemStack container = extractStack.getContainerItem();
						for(int di = 0; di < 4; di++) {
							Direction d = Direction.byHorizontalIndex(di);
							TileEntity te = world.getTileEntity(pos.offset(d));
							if(te != null) {
								IItemHandler ih = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d.getOpposite()).orElse(null);
								if(ih != null) {
									ItemStack remainder = ItemHandlerHelper.insertItemStacked(ih, container, false);
									container = remainder;
								}
							}
							if(container == null || container.isEmpty()) {
								break;
							}
						}
						if(container != null && !container.isEmpty()) {
							ItemEntity ie = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.1D, pos.getZ() + 0.5D, container);
							world.addEntity(ie);
						}
					}
				}
			} else {
				for(int i = 0; i < input.getSlots() && remaining > 0; i++) {
					ItemStack inputStack = input.getStackInSlot(i);
					if(ItemHandlerHelper.canItemStacksStack(in, inputStack)) {
						ItemStack extractStack = input.extractItem(i, remaining, false);
						remaining -= extractStack.getCount();
					}
				}
			}
		}
	}

	protected class BlueprintItemHandler extends ItemStackHandler {

		public BlueprintItemHandler(int i) {
			super(i);
		}

		@Override
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			updateRecipe();
			markDirty();
		}
		
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.getItem() == Items.WRITTEN_BLUEPRINT && stack.getChildTag("Blueprint") != null;
		}
	}

	private IItemHandler createResultItemHandler() {
		return new IItemHandler() {

			@Override
			public int getSlots() {
				return 1;
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				return ItemStack.EMPTY;
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return stack;
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				if(!overflow.isEmpty()) {
					ItemStack result = overflow.copy();
					int extractCount = Math.min(amount, overflow.getCount());
					result.setCount(extractCount);
					if(!simulate) {
						overflow.shrink(extractCount);
						markDirty();
					}
					return result;
				}
				if(canCraft()) {
					CraftingInventory craftinginventory = new CraftingInventory(new Container((ContainerType<?>)null, -1) {
						public boolean canInteractWith(PlayerEntity playerIn) {
							return false;
						}
					}, 3, 3);
					for(int i = 0; i < craftingMatrix.size(); i++) {
						craftinginventory.setInventorySlotContents(i, craftingMatrix.get(i));
					}
					ItemStack crafted = recipe.getCraftingResult(craftinginventory);
					int extractCount = Math.min(amount, crafted.getCount());
					ItemStack result = crafted.copy();
					result.setCount(extractCount);
					if(!simulate) {
						shrinkCraftingInputs();
						crafted.shrink(extractCount);
						overflow = crafted;
						markDirty();
					}
					return result;
				}
				return ItemStack.EMPTY;
			}

			@Override
			public int getSlotLimit(int slot) {
				return 0;
			}

			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return false;
			}
		};
	}
	
	protected IItemHandler getResultItemHandler() {
		return resultItemHandler.orElse(null);
	}
}
