package chrysalis.block.potion_wart;

import java.util.List;

import chrysalis.block.potion_wart.PotionWartTileEntity.BlockModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class PotionWartDoorBlock extends DoorBlock {

	public PotionWartDoorBlock(Properties builder) {
		super(builder);
		MinecraftForge.EVENT_BUS.register(this);
	}
		
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if(placer instanceof PlayerEntity) {
			TileEntity te = worldIn.getTileEntity(pos);
			if(te != null && te instanceof PotionWartTileEntity) {
				PotionWartTileEntity wart = (PotionWartTileEntity)te;
				wart.setOwner((PlayerEntity)placer);
			}
		}
	}

	@SubscribeEvent
	public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		BlockState state = event.getWorld().getBlockState(event.getPos());
		if(state.getBlock() instanceof PotionWartDoorBlock) {
			BlockPos pos = event.getPos();
			if(state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
				pos = pos.down();
			}
			TileEntity te = event.getWorld().getTileEntity(pos);
			if(te != null && te instanceof PotionWartTileEntity) {
				PotionWartTileEntity wart = (PotionWartTileEntity)te;
				wart.applyEffects(event.getPlayer());
			}
		}
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PotionWartTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player,
			Hand handIn, BlockRayTraceResult hit) {
		ItemStack item = player.getHeldItem(handIn);
		BlockPos tePos = pos;
		if(state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
			tePos = pos.down();
		}
		TileEntity te = worldIn.getTileEntity(tePos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			if(item != null && !item.isEmpty() && item.getItem() instanceof PotionItem && !wart.hasEffects()) {
				List<EffectInstance> effects = PotionUtils.getEffectsFromStack(item);
				wart.setEffects(effects);
				item.shrink(1);
				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_BOTTLE), player.inventory.currentItem);
				return ActionResultType.CONSUME;
			} else
				if(item != null && !item.isEmpty() && wart.isValidModifier(player, item.getItem())) {
					wart.addModifier(item.getItem());
					ItemStack container = null;
					if(item.hasContainerItem()) {
						container = item.getContainerItem();
					}
					item.shrink(1);
					if(container != null) {
						ItemHandlerHelper.giveItemToPlayer(player, container, player.inventory.currentItem);
					}
					return ActionResultType.CONSUME;
				}
			wart.applyEffects(player);
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.down();
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			wart.applyEffects(entityIn);
		}
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if(worldIn.getBlockState(pos).get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.down();
		}
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			wart.applyEffects(entityIn);
		}
	}

	@Override
	public float getExplosionResistance(BlockState state, IWorldReader world, BlockPos pos, Entity exploder,
			Explosion explosion) {
		if(state.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
			pos = pos.down();
		}
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			if(wart.hasBlockModifier(BlockModifier.BLAST_RESISTANT)) {
				return 1200F;
			}
		}
		return 1F;
	}
}
