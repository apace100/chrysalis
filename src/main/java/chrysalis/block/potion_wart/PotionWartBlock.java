package chrysalis.block.potion_wart;

import java.util.List;

import chrysalis.block.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class PotionWartBlock extends Block {
	
	public PotionWartBlock() {
		super(Block.Properties.create(Material.ORGANIC, MaterialColor.RED).hardnessAndResistance(1.0F).sound(SoundType.WOOD));
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
		if(event.getWorld().getBlockState(event.getPos()).getBlock() instanceof PotionWartBlock) {
			TileEntity te = event.getWorld().getTileEntity(event.getPos());
			if(te != null && te instanceof PotionWartTileEntity) {
				PotionWartTileEntity wart = (PotionWartTileEntity)te;
				wart.applyEffects(event.getPlayer());
			}
		}
	}
	
	@SubscribeEvent
	public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
		if(event.getWorld().getBlockState(event.getPos()).getBlock() == net.minecraft.block.Blocks.NETHER_WART_BLOCK) {
			ItemStack item = event.getItemStack();
			if(item != null && !item.isEmpty() && item.getItem() instanceof PotionItem) {
				event.getWorld().setBlockState(event.getPos(), Blocks.POTION_WART.getDefaultState());
				TileEntity te = event.getWorld().getTileEntity(event.getPos());
				if(te != null && te instanceof PotionWartTileEntity) {
					PotionWartTileEntity wart = (PotionWartTileEntity)te;
					if(item != null && !item.isEmpty() && item.getItem() instanceof PotionItem && !wart.hasEffects()) {
						List<EffectInstance> effects = PotionUtils.getEffectsFromStack(item);
						wart.setEffects(effects);
						item.shrink(1);
						ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), new ItemStack(Items.GLASS_BOTTLE), event.getPlayer().inventory.currentItem);
						event.setCancellationResult(ActionResultType.CONSUME);
						event.setCanceled(true);
						return;
					}
				}
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
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			if(item != null && !item.isEmpty() && item.getItem() instanceof PotionItem && !wart.hasEffects()) {
				List<EffectInstance> effects = PotionUtils.getEffectsFromStack(item);
				wart.setEffects(effects);
				item.shrink(1);
				ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_BOTTLE), player.inventory.currentItem);
				return ActionResultType.CONSUME;
			}
			wart.applyEffects(player);
		}
		return ActionResultType.PASS;
	}

	@Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			wart.applyEffects(entityIn);
		}
	}

	@Override
	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			wart.applyEffects(player);
		}
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			wart.applyEffects(entityIn);
		}
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te != null && te instanceof PotionWartTileEntity) {
			PotionWartTileEntity wart = (PotionWartTileEntity)te;
			wart.applyEffects(entityIn);
		}
	}
	
	
}
