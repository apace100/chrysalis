package chrysalis.block.hopper_duct;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.items.CapabilityItemHandler;

//import net.minecraft.util.BlockRenderLayer;

public class HopperDuctBlock extends SixWayBlock {
	
	public HopperDuctBlock() {
		super(0.125F, Block.Properties.create(Material.IRON).hardnessAndResistance(3F));
		this.setDefaultState(this.stateContainer.getBaseState().with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(UP, Boolean.valueOf(false)).with(DOWN, Boolean.valueOf(false)));
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.makeConnections(context.getWorld(), context.getPos());
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new HopperDuctTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	public BlockState makeConnections(IBlockReader reader, BlockPos pos) {
		return this.getDefaultState()
				.with(DOWN, Boolean.valueOf(connectsTo(reader, pos.down(), Direction.UP)))
				.with(UP, Boolean.valueOf(connectsTo(reader, pos.up(), Direction.DOWN)))
				.with(NORTH, Boolean.valueOf(connectsTo(reader, pos.north(), Direction.SOUTH)))
				.with(EAST, Boolean.valueOf(connectsTo(reader, pos.east(), Direction.WEST)))
				.with(SOUTH, Boolean.valueOf(connectsTo(reader, pos.south(), Direction.NORTH)))
				.with(WEST, Boolean.valueOf(connectsTo(reader, pos.west(), Direction.EAST)));
	}

	private boolean connectsTo(IBlockReader reader, BlockPos p, Direction d) {
		if(reader.getBlockState(p).getBlock() == this) {
			return true;
		}
		TileEntity te = reader.getTileEntity(p);
		if(te != null) {
			if(te.getTileEntity() instanceof HopperTileEntity) {
				return d == Direction.DOWN;
			}
			return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, d).isPresent();
		}
		return false;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		boolean shouldConnect = this.connectsTo(world, facingPos, facing);
		BooleanProperty prop = FACING_TO_PROPERTY_MAP.get(facing);
		if(stateIn.get(prop).booleanValue() != shouldConnect) {
			return stateIn.with(prop, shouldConnect);
		}
		return stateIn;
	}
	
	@Override
	public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
		super.onNeighborChange(state, world, pos, neighbor);
		this.setDirty(world, pos);
	}

	private void setDirty(IWorldReader world, BlockPos pos) {
		if(world.getBlockState(pos).getBlock() != this) {
			return;
		}
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof HopperDuctTileEntity) {
			HopperDuctTileEntity duct = (HopperDuctTileEntity)te;
			if(!duct.areConnectionsDirty) {
				duct.areConnectionsDirty = true;
				HopperDuctTileEntity.DIRECTIONS.forEach(d -> this.setDirty(world, pos.offset(d)));
			}
		}
	}

	/*public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}*/

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}
}
