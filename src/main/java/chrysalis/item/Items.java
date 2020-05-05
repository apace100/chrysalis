package chrysalis.item;

import chrysalis.Chrysalis;
import chrysalis.block.Blocks;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class Items {

	@ObjectHolder(Chrysalis.MODID + ":chrysalis")
	public static Item CHRYSALIS;

	@ObjectHolder(Chrysalis.MODID + ":blueprint")
	public static Item BLUEPRINT;

	@ObjectHolder(Chrysalis.MODID + ":written_blueprint")
	public static Item WRITTEN_BLUEPRINT;

	@ObjectHolder(Chrysalis.MODID + ":acid_bottle")
	public static Item ACID_BOTTLE;

	private static void addBlockItem(IForgeRegistry<Item> registry, Block block, ItemGroup group) {
		Item item = null;
		if (group != null) {
			item = new BlockItem(block, new Item.Properties().group(group))
					.setRegistryName(block.getRegistryName());
		} else {
			item = new BlockItem(block, new Item.Properties()).setRegistryName(block.getRegistryName());
		}
		registry.register(item);
	}
	
	private static void addItem(IForgeRegistry<Item> registry, String name, Item item) {
		registry.register(item.setRegistryName(name));
	}
	
	public static void register(IForgeRegistry<Item> registry) {
		addBlockItem(registry, Blocks.IRON_ORE, null);
		addBlockItem(registry, Blocks.GOLD_ORE, null);
		addBlockItem(registry, Blocks.ASSEMBLY, Chrysalis.ITEM_GROUP);
		addBlockItem(registry, Blocks.HOPPER_DUCT, Chrysalis.ITEM_GROUP);
		addBlockItem(registry, Blocks.ITEM_GRATE, Chrysalis.ITEM_GROUP);
		addBlockItem(registry, Blocks.FAN, Chrysalis.ITEM_GROUP);
		addBlockItem(registry, Blocks.PRESS, Chrysalis.ITEM_GROUP);
		addBlockItem(registry, Blocks.XP_STORE, Chrysalis.ITEM_GROUP);

		addItem(registry, "xp_seed",
				new XPSeedItem(new Item.Properties().group(Chrysalis.ITEM_GROUP).food(XPSeedItem.FOOD)));
		addItem(registry, "acid_bottle",
				new AcidBottle(new Item.Properties().group(Chrysalis.ITEM_GROUP).maxStackSize(1)));
		addItem(registry, "chrysalis", new Item(new Item.Properties()));
		addItem(registry, "blueprint", new Item(new Item.Properties().group(Chrysalis.ITEM_GROUP)));
		addItem(registry, "written_blueprint", new Item(new Item.Properties().maxStackSize(1)) {

			@Override
			public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip,
					ITooltipFlag flagIn) {
				CompoundNBT blueprintNBT = stack.getChildTag("Blueprint");
				if (blueprintNBT != null) {
					ItemStack output = ItemStack.read(blueprintNBT.getCompound("Output"));
					StringTextComponent outputTooltip = new StringTextComponent(" (" + output.getCount() + "x ");
					outputTooltip.appendSibling(output.getDisplayName()).appendText(")");
					tooltip.get(0).appendSibling(outputTooltip);

					NonNullList<ItemStack> inputs = NonNullList.withSize(blueprintNBT.getInt("InputCount"), ItemStack.EMPTY);
					ItemStackHelper.loadAllItems(blueprintNBT.getCompound("Input"), inputs);
					for(ItemStack input : inputs) {
						StringTextComponent inputTooltip = new StringTextComponent(input.getCount() + "x ");
						inputTooltip.appendSibling(input.getDisplayName());
						inputTooltip.applyTextStyle(TextFormatting.GRAY);
						tooltip.add(inputTooltip);
					}
				}
				super.addInformation(stack, worldIn, tooltip, flagIn);
			}
		});
	}
}
