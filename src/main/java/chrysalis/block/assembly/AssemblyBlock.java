package chrysalis.block.assembly;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;

public class AssemblyBlock extends Block {

	public AssemblyBlock() {
		super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(4f));
		
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new AssemblyTileEntity();
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof AssemblyTileEntity) {
				NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider)tileentity, pos);
			}
		}

		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			AssemblyTileEntity te = (AssemblyTileEntity)worldIn.getTileEntity(pos);
			if(te != null) {
				NonNullList<ItemStack> stacks = NonNullList.create();
				IItemHandler input = te.getInputItemHandler();
				if(input != null) {
					for(int i = 0; i < input.getSlots(); i++) {
						stacks.add(input.getStackInSlot(i));
					}
				}
				stacks.add(te.getBlueprintItemHandler().getStackInSlot(0));
				stacks.add(te.overflow);
				InventoryHelper.dropItems(worldIn, pos, stacks);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
}
