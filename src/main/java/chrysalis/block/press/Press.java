package chrysalis.block.press;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import chrysalis.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.PistonEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Press {
	
	private static final HashMap<Item, Item> SIMPLE_PRESS_RECIPES = new HashMap<Item, Item>();
	private static final HashMap<Item, AdvancedPressRecipe> ADVANCED_PRESS_RECIPES = new HashMap<>();
	
	static {
		SIMPLE_PRESS_RECIPES.put(Items.COBBLESTONE, Items.GRAVEL);
		SIMPLE_PRESS_RECIPES.put(Items.GRAVEL, Items.SAND);

		ADVANCED_PRESS_RECIPES.put(Items.IRON_PICKAXE, new AdvancedPressRecipe(Items.IRON_INGOT, 3, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_AXE, new AdvancedPressRecipe(Items.IRON_INGOT, 3, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_SHOVEL, new AdvancedPressRecipe(Items.IRON_INGOT, 1, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_HOE, new AdvancedPressRecipe(Items.IRON_INGOT, 8, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_HELMET, new AdvancedPressRecipe(Items.IRON_INGOT, 5, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_CHESTPLATE, new AdvancedPressRecipe(Items.IRON_INGOT, 8, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_LEGGINGS, new AdvancedPressRecipe(Items.IRON_INGOT, 7, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.IRON_BOOTS, new AdvancedPressRecipe(Items.IRON_INGOT, 4, Items.IRON_NUGGET, 9));
		
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_PICKAXE, new AdvancedPressRecipe(Items.GOLD_INGOT, 3, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_AXE, new AdvancedPressRecipe(Items.GOLD_INGOT, 3, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_SHOVEL, new AdvancedPressRecipe(Items.GOLD_INGOT, 1, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_HOE, new AdvancedPressRecipe(Items.GOLD_INGOT, 8, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_HELMET, new AdvancedPressRecipe(Items.GOLD_INGOT, 5, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_CHESTPLATE, new AdvancedPressRecipe(Items.GOLD_INGOT, 8, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_LEGGINGS, new AdvancedPressRecipe(Items.GOLD_INGOT, 7, Items.GOLD_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.GOLDEN_BOOTS, new AdvancedPressRecipe(Items.GOLD_INGOT, 4, Items.GOLD_NUGGET, 9));
		
		ADVANCED_PRESS_RECIPES.put(Items.COMPASS, new AdvancedPressRecipe(Items.IRON_INGOT, 4, Items.IRON_NUGGET, 9));
		ADVANCED_PRESS_RECIPES.put(Items.CLOCK, new AdvancedPressRecipe(Items.GOLD_INGOT, 4, Items.IRON_NUGGET, 9));
		
	}
	
	@SubscribeEvent
	public void onPistonPush(PistonEvent.Post event) {
		if(event.getPistonMoveType().isExtend) {
			BlockPos pos = event.getFaceOffsetPos().offset(event.getDirection());
			if(event.getWorld().getBlockState(pos).getBlock() == Blocks.PRESS) {
				List<ItemEntity> entities = event.getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.offset(event.getDirection().getOpposite())));
				for(ItemEntity ie : entities) {
					ItemStack stack = ie.getItem();
					if(SIMPLE_PRESS_RECIPES.containsKey(stack.getItem())) {
						ItemStack newStack = new ItemStack(SIMPLE_PRESS_RECIPES.get(stack.getItem()), stack.getCount());
						ie.setItem(newStack);
					} else
					if(ADVANCED_PRESS_RECIPES.containsKey(stack.getItem())) {
						AdvancedPressRecipe recipe = ADVANCED_PRESS_RECIPES.get(stack.getItem());
						List<ItemStack> results = recipe.getResults(stack);
						if(results.size() == 0) {
							ie.remove();
						} else {
							ie.setItem(results.get(0));
							if(results.size() > 1) {
								ItemEntity newIe = new ItemEntity(ie.world, ie.getPosX(), ie.getPosY(), ie.getPosZ(), results.get(1));
								event.getWorld().addEntity(newIe);
							}
						}
					}
				}
			}
		}
	}
	
	private static class AdvancedPressRecipe {
		private Item mainMaterial;
		private int mainCount;
		private Item secondMaterial;
		private int conversionCount;
		
		public AdvancedPressRecipe(Item main, int mainCount, Item second, int conversionCount) {
			this.mainMaterial = main;
			this.mainCount = mainCount;
			this.secondMaterial = second;
			this.conversionCount = conversionCount;
		}
		
		public List<ItemStack> getResults(ItemStack input) {
			double fullValue = mainCount * conversionCount;
			int maxDamage = input.getItem().getMaxDamage(input);
			double percent = (double)(maxDamage - input.getDamage()) / (double)maxDamage;
			double itemValue = percent * fullValue;
			int mainResult = (int)(itemValue / conversionCount);
			int secondResult = (int)(itemValue % conversionCount);
			List<ItemStack> resultStacks = new LinkedList<ItemStack>();
			if(mainResult > 0) {
				resultStacks.add(new ItemStack(mainMaterial, mainResult));
			}
			if(secondResult > 0) {
				resultStacks.add(new ItemStack(secondMaterial, secondResult));
			}
			return resultStacks;
		}
	}
}
