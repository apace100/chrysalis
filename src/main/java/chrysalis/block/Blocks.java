package chrysalis.block;

import chrysalis.Chrysalis;
import chrysalis.block.assembly.AssemblyBlock;
import chrysalis.block.fan.FanBlock;
import chrysalis.block.hopper_duct.HopperDuctBlock;
import chrysalis.block.item_grate.ItemGrateBlock;
import chrysalis.block.potion_wart.PotionWartBlock;
import chrysalis.block.potion_wart.PotionWartDoorBlock;
import chrysalis.block.xp_store.XPStoreBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
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
	
	@ObjectHolder(Chrysalis.MODID + ":press")
	public static Block PRESS;
	
	@ObjectHolder(Chrysalis.MODID + ":xp_store")
	public static Block XP_STORE;

	@ObjectHolder(Chrysalis.MODID + ":potion_wart")
	public static Block POTION_WART;
	
	@ObjectHolder(Chrysalis.MODID + ":potion_wart_wooden_door")
	public static Block POTION_WART_WOODEN_DOOR;

	@ObjectHolder(Chrysalis.MODID + ":potion_wart_iron_door")
	public static Block POTION_WART_IRON_DOOR;
	
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
		register(registry, "press", new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F)));
		register(registry, "xp_store", new XPStoreBlock());
		register(registry, "potion_wart", new PotionWartBlock());
		register(registry, "potion_wart_wooden_door", new PotionWartDoorBlock(Block.Properties.create(Material.ORGANIC).hardnessAndResistance(1F).sound(SoundType.WOOD).notSolid()));
		register(registry, "potion_wart_iron_door", new PotionWartDoorBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(5F).sound(SoundType.METAL).notSolid()));
	}
	
	private static void register(IForgeRegistry<Block> registry, String regName, Block block) {
		registry.register(block.setRegistryName(regName));
	}
}
