package chrysalis.block.xp_store;

import chrysalis.network.NetworkHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.play.server.SSetExperiencePacket;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class XPStoreBlock extends Block {

    private VoxelShape shape = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static IntegerProperty LEVEL = IntegerProperty.create("level", 0, 12);
	
	public XPStoreBlock() {
		super(Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(2f));
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.shape;
	}

	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.shape;
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
		return new XPStoreTileEntity();
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof XPStoreTileEntity) {
				int xpToStore = getXPPoints(player);
				if(xpToStore == 0) {
					xpToStore = xpBarCap(player.experienceLevel - 1);
				}
				if(xpToStore > 0) {
					int insert = ((XPStoreTileEntity)tileentity).insert(xpToStore, false);
					if(insert > 0) {
						changeXp(player, -insert);
						syncXp(player);
						updateFillLevel(worldIn, pos, state, (XPStoreTileEntity)tileentity);
					}
				}
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	private int getXPPoints(PlayerEntity player) {
		return MathHelper.floor(player.experience * (float)player.xpBarCap());
	}
	
	private void changeXp(PlayerEntity player, int amount) {
		player.experience += (float)amount / (float)player.xpBarCap();
		player.experienceTotal = MathHelper.clamp(player.experienceTotal + amount, 0, Integer.MAX_VALUE);

		while(player.experience < 0.0F) {
			float f = player.experience * (float)player.xpBarCap();
			if (player.experienceLevel > 0) {
				player.addExperienceLevel(-1);
				player.experience = 1.0F + f / (float)player.xpBarCap();
			} else {
				player.addExperienceLevel(-1);
				player.experience = 0.0F;
			}
		}

		while(player.experience >= 1.0F) {
			player.experience = (player.experience - 1.0F) * (float)player.xpBarCap();
			player.addExperienceLevel(1);
			player.experience /= (float)player.xpBarCap();
		}
	}
	
	public void syncXp(PlayerEntity player) {
		NetworkHelper.sendTo(new SSetExperiencePacket(player.experience, player.experienceTotal, player.experienceLevel), player);
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		if (!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof XPStoreTileEntity) {
				int desiredAmount = player.xpBarCap() - getXPPoints(player);
				int extract = ((XPStoreTileEntity)tileentity).extract(desiredAmount, false);
				if(extract > 0) {
					changeXp(player, extract);
					syncXp(player);
					updateFillLevel(worldIn, pos, state, (XPStoreTileEntity)tileentity);
				}
			}
		}
	}
	
	public void updateFillLevel(World worldIn, BlockPos pos, BlockState state, XPStoreTileEntity xpStore) {
		int now = state.get(LEVEL);
		int desired = (int)Math.ceil(xpStore.getFillPercentage() * 12);
		if(now != desired) {
			setFillLevel(worldIn, pos, state, desired);
		}
	}
	
	public void setFillLevel(World worldIn, BlockPos pos, BlockState state, int level) {
		worldIn.setBlockState(pos, state.with(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 12))), 2);
		worldIn.updateComparatorOutputLevel(pos, this);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(LEVEL, 0);
	}

	@SuppressWarnings("deprecation")
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof XPStoreTileEntity) {
				int xp = ((XPStoreTileEntity)tileentity).extract(XPStoreTileEntity.MAX_XP_STORED, false);
				while(xp > 0) {
					int j = ExperienceOrbEntity.getXPSplit(xp);
					xp -= j;
					worldIn.addEntity(new ExperienceOrbEntity(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, j));
				}
			}
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return blockState.get(LEVEL);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
	}

	public int xpBarCap(int lvl) {
		if(lvl < 0) {
			return 0;
		}
		if (lvl >= 30) {
			return 112 + (lvl - 30) * 9;
		} else {
			return lvl >= 15 ? 37 + (lvl - 15) * 5 : 7 + lvl * 2;
		}
	}
}
