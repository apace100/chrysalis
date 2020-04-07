package chrysalis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import chrysalis.ai.AIExtender;
import chrysalis.block.assembly.BlueprintCreation;
import chrysalis.item.Items;
import chrysalis.potion.Potions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Chrysalis.MODID)
public class Chrysalis {
    
	public static final String MODID = "chrysalis";
	
	public static final ItemGroup ITEM_GROUP = new ItemGroup(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Items.CHRYSALIS);
		}
    };
	
    @SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();
    
    public static ChrysalisProxy proxy = DistExecutor.runForDist(() -> () -> new ChrysalisClient(), () -> () -> new ChrysalisServer());

    public Chrysalis() {
    	final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    	bus.addListener(this::setup);
    	bus.register(proxy);
    	//DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> bus.register(new ChrysalisClient()));
    }

    private void setup(final FMLCommonSetupEvent event) {
        Potions.registerRecipes();
        Attributes.registerAll();
        MinecraftForge.EVENT_BUS.register(Attributes.class);
        MinecraftForge.EVENT_BUS.register(AttributeEventHandler.class);
        MinecraftForge.EVENT_BUS.register(new BlueprintCreation());
        MinecraftForge.EVENT_BUS.register(new AIExtender());
    }
}
