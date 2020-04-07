package chrysalis.block.assembly;

import chrysalis.Chrysalis;
import chrysalis.block.Blocks;
import chrysalis.item.Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;

public class AssemblyContainer extends Container {

	@ObjectHolder(Chrysalis.MODID + ":assembly")
	public static ContainerType<AssemblyContainer> TYPE;
	
	private AssemblyTileEntity tileEntity;
	
	public AssemblyContainer(int windowId, World world, BlockPos pos, PlayerInventory inventory) {
		super(TYPE, windowId);
		tileEntity = (AssemblyTileEntity)world.getTileEntity(pos);
		tileEntity.inputItemHandler.ifPresent(handler -> {
			for(int i = 0; i < 3; ++i) {
		         for(int j = 0; j < 3; ++j) {
		            this.addSlot(new SlotItemHandler(handler, j + i * 3, 30 + j * 18, 17 + i * 18));
		         }
		      }
		});
		tileEntity.blueprintItemHandler.ifPresent(handler -> 
			this.addSlot(new SlotItemHandler(handler, 0, 124, 35)));
		
		this.addPlayerInventorySlots(inventory);
		
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerIn, Blocks.ASSEMBLY);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack copyStack = null;
		Slot slot = this.getSlot(index);
		if(slot != null && slot.getHasStack() && !slot.getStack().isEmpty()) {
			ItemStack stack = slot.getStack();
			copyStack = stack.copy();
			if(index < 10) {
				if(!this.mergeItemStack(stack, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack, copyStack);
			} else {
				if(stack.getItem() == Items.WRITTEN_BLUEPRINT) {
					if(!this.mergeItemStack(stack, 9, 10, false)) {
						return ItemStack.EMPTY;
					}
				} else
				if(!this.mergeItemStack(stack, 0, 9, false)) {
					return ItemStack.EMPTY;
				}
				if(index < 37) {
					if(!this.mergeItemStack(stack, 37, 46, false)) {
						return ItemStack.EMPTY;
					}
				} else
				if(index < 46) {
					if(!this.mergeItemStack(stack, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			}
			
			if(stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
			
			if(stack.getCount() == copyStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, stack);
		}
		
		return copyStack;
	}

	private void addPlayerInventorySlots(PlayerInventory inv) {
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(inv, k, 8 + k * 18, 142));
		}
	}
}
