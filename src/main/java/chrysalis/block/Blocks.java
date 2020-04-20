package chrysalis.block;

import chrysalis.Chrysalis;
import chrysalis.block.assembly.AssemblyBlock;
import chrysalis.block.fan.FanBlock;
import chrysalis.block.hopper_duct.HopperDuctBlock;
import chrysalis.block.item_grate.ItemGrateBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class Blocks {
	
	@ObjectHolder(Chrysalis.MODID + ":iron_ore")
	public static Block IRON_ORE;
	
	@ObjectHolder(Chrysalis.MODID + ":gold_ore")
	public static Block GOLD_ORE;

	@ObjectHolder(Chrysalis.MODID + ":assembly")
	public static Block ASSEMBLY;

	@ObjectHolder(Chrysalis.MODID + ":hopper_duct")
	public static Block HOPPER_DUCT;

	@ObjectHolder(Chrysalis.MODID + ":item_grate")
	public static Block ITEM_GRATE;

	@ObjectHolder(Chrysalis.MODID + ":fan")
	public static Block FAN;

	public static class Tags {

		public static final Tag<Block> HOPPER_DUCTS = new BlockTags.Wrapper(
				new ResourceLocation(Chrysalis.MODID, "hopper_ducts"));
	}

	public static void register(IForgeRegistry<Block> registry) {
		register(registry, "gold_ore",
				new OreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
		register(registry, "iron_ore",
				new OreBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F)));
		register(registry, "assembly", new AssemblyBlock());
		register(registry, "hopper_duct", new HopperDuctBlock());
		register(registry, "item_grate", new ItemGrateBlock());
		register(registry, "fan", new FanBlock());
	}
	
	private static void register(IForgeRegistry<Block> registry, String regName, Block block) {
		registry.register(block.setRegistryName(regName));
	}
}
