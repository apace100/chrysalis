package chrysalis.block;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import chrysalis.Chrysalis;
import chrysalis.block.assembly.AssemblyTileEntity;
import chrysalis.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ObjectHolder;

// TODO: 	Prevent items from being inserted into inventory they were pushed out of.
//			Right now this is prevented by just not inserting to hoppers at all, but
//			this could cause problems if mod blocks from other mods push into the duct.
public class HopperDuctTileEntity extends TileEntity {
	
	protected static final List<Direction> DIRECTIONS = ImmutableList.copyOf(Direction.values());
	
	@ObjectHolder(Chrysalis.MODID + ":hopper_duct")
	public static TileEntityType<AssemblyTileEntity> TYPE;
	
	protected LazyOptional<IItemHandler> inputItemHandler = LazyOptional.of(this::createInputItemHandler);
	
	protected boolean areConnectionsDirty = true;
	
	private List<Handler> connections;

	public HopperDuctTileEntity() {
		this(TYPE);
	}
	
	public HopperDuctTileEntity(TileEntityType<?> type) {
		super(type);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inputItemHandler.cast();
		}
		return super.getCapability(cap, side);
	}

	private void updateConnections() {
		HashSet<BlockPos> visited = new HashSet<>();
		LinkedList<Handler> handlers = new LinkedList<>();
		DIRECTIONS.forEach(d -> traceDucts(world, pos.offset(d), pos, visited, handlers, 0));
		handlers.sort((h1, h2) -> h1.distance - h2.distance);
		this.connections = handlers;
		this.areConnectionsDirty = false;
	}
	
	private IItemHandler createInputItemHandler() {
		return new InputItemHandler();
	}
	
	class InputItemHandler implements IItemHandler {
		
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
			if(areConnectionsDirty) {
				updateConnections();
			}
			ItemStack remaining = stack.copy();
			for(Handler h : connections) {
				if(remaining.isEmpty()) {
					break;
				}
				remaining = ItemHandlerHelper.insertItemStacked(h.itemHandler, remaining, simulate);
			}
			return remaining;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
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
	}
	
	private boolean isHopperDuct(World world, BlockPos pos) {
		return world.getBlockState(pos).getBlock().isIn(Blocks.Tags.HOPPER_DUCTS);
	}
	
	protected void traceDucts(World world, BlockPos pos, BlockPos from, HashSet<BlockPos> visited, LinkedList<Handler> handlers, int distance) {
		if(visited.contains(pos)) {
			if(!isHopperDuct(world, pos)) {
				TileEntity te = world.getTileEntity(pos);
				Direction direction = Utils.getFacingFromNeighboringPos(from, pos).getOpposite();
				if(isValidTileEntity(te)) {
					te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).ifPresent(ih -> {
						Handler handler = new Handler(ih, distance);
						if(!handlers.contains(handler)) {
							handlers.add(handler);
						}
					});
				}
			}
			return;
		}
		visited.add(pos);
		TileEntity te = world.getTileEntity(pos);
		Direction direction = Utils.getFacingFromNeighboringPos(from, pos).getOpposite();
		if(isHopperDuct(world, pos)) {
			DIRECTIONS.forEach(d -> {
				BlockPos nextPos = pos.offset(d);
				if(!nextPos.equals(from)) {
					traceDucts(world, nextPos, pos, visited, handlers, distance + 1);
				}
			});
		} else
		if(isValidTileEntity(te)) {
			te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).ifPresent(ih -> handlers.add(new Handler(ih, distance)));
		}
	}
	
	private boolean isValidTileEntity(TileEntity te) {
		return te != null && !(te instanceof HopperTileEntity);
	}

	private class Handler {
		public IItemHandler itemHandler;
		public int distance;
		
		public Handler(IItemHandler ih, int d) {
			this.itemHandler = ih;
			this.distance = d;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Handler) {
				return itemHandler.equals(((Handler)obj).itemHandler);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return itemHandler.hashCode();
		}
		
		
	}
}
