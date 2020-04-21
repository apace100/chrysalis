package chrysalis;

import chrysalis.block.Blocks;
import chrysalis.block.assembly.AssemblyContainer;
import chrysalis.block.assembly.AssemblyTileEntity;
import chrysalis.block.fan.FanTileEntity;
import chrysalis.block.hopper_duct.HopperDuctTileEntity;
import chrysalis.block.item_grate.ItemGrateTileEntity;
import chrysalis.block.xp_store.XPStoreTileEntity;
import chrysalis.item.Items;
import chrysalis.potion.Potions;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid=Chrysalis.MODID, bus=Mod.EventBusSubscriber.Bus.MOD)
public class ChrysalisRegistry {
	
	@SubscribeEvent
    public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();
        Blocks.register(reg);
    }
	
    @SubscribeEvent
    public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        Items.register(reg);
    }

    @SubscribeEvent
    public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event) {

    }
    
    @SubscribeEvent
    public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
    	IForgeRegistry<TileEntityType<?>> reg = event.getRegistry();
    	reg.register(TileEntityType.Builder.create(AssemblyTileEntity::new, Blocks.ASSEMBLY).build(null).setRegistryName("assembly"));
    	reg.register(TileEntityType.Builder.create(HopperDuctTileEntity::new, Blocks.HOPPER_DUCT).build(null).setRegistryName("hopper_duct"));
      reg.register(
          TileEntityType.Builder.create(ItemGrateTileEntity::new, Blocks.ITEM_GRATE).build(null)
              .setRegistryName("item_grate"));
      reg.register(TileEntityType.Builder.create(FanTileEntity::new, Blocks.FAN).build(null)
          .setRegistryName("fan"));
      reg.register(TileEntityType.Builder.create(XPStoreTileEntity::new, Blocks.XP_STORE).build(null).setRegistryName("xp_store"));
    }
    
    @SubscribeEvent
    public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
    	 IForgeRegistry<ContainerType<?>> reg = event.getRegistry();
    	 reg.register(IForgeContainerType.create((windowId, inv, data) -> {
         	return new AssemblyContainer(windowId, Chrysalis.proxy.getWorld(), data.readBlockPos(), inv);
         }).setRegistryName("assembly"));
    }

    @SubscribeEvent
    public static void onEnchantmentRegistry(final RegistryEvent.Register<Enchantment> event) {
    }

    @SubscribeEvent
    public static void onEffectRegistry(final RegistryEvent.Register<Effect> event) {
    	Potions.registerEffects(event.getRegistry());
    }
    
    @SubscribeEvent
    public static void onPotionRegistry(final RegistryEvent.Register<Potion> event) {
    	Potions.registerPotions(event.getRegistry());
    }
}
