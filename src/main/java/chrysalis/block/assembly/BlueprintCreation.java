package chrysalis.block.assembly;

import chrysalis.item.Items;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlueprintCreation {
	
	@SubscribeEvent
	public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
		if(event.getPlayer().getHeldItemMainhand().getItem() == Items.BLUEPRINT) {
			if(event.getInventory() instanceof CraftingInventory) {
				World world = event.getPlayer().world;
				CraftingInventory craft = (CraftingInventory)event.getInventory();
				ICraftingRecipe recipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craft, world).orElse(null);
				if(recipe != null) {
					ItemStack output = recipe.getCraftingResult(craft);
					ItemStack blueprint = new ItemStack(Items.WRITTEN_BLUEPRINT);
					CompoundNBT nbt = blueprint.getOrCreateChildTag("Blueprint");
					nbt.putString("RecipeId", recipe.getId().toString());
					CompoundNBT outputNBT = new CompoundNBT();
					output.write(outputNBT);
					nbt.put("Output", outputNBT);
					NonNullList<ItemStack> input = NonNullList.create();
					NonNullList<ItemStack> matrix = NonNullList.create();
					for(int i = 0; i < craft.getSizeInventory(); i++) {
						ItemStack in = craft.getStackInSlot(i).copy();
						if(!in.isEmpty()) {
							in.setCount(1);
							boolean merged = false;
							for(ItemStack stack : input) {
								if(ItemHandlerHelper.canItemStacksStack(in, stack)) {
									stack.grow(in.getCount());
									merged = true;
									break;
								}
							}
							if(!merged) {
								input.add(in.copy());
							}
						}
						matrix.add(in);
					}
					CompoundNBT inputNBT = new CompoundNBT();
					ItemStackHelper.saveAllItems(inputNBT, input);
					nbt.putInt("InputCount", input.size());
					nbt.put("Input", inputNBT);
					CompoundNBT matrixNBT = new CompoundNBT();
					ItemStackHelper.saveAllItems(matrixNBT, matrix);
					nbt.putInt("CraftMatrixSize", matrix.size());
					nbt.put("CraftMatrix", matrixNBT);
					event.getPlayer().getHeldItemMainhand().shrink(1);
					ItemHandlerHelper.giveItemToPlayer(event.getPlayer(), blueprint, event.getPlayer().inventory.currentItem);
				}
			}
		}
	}
}
