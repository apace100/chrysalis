package chrysalis;

import chrysalis.block.Blocks;
import chrysalis.block.assembly.AssemblyContainer;
import chrysalis.block.assembly.AssemblyScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ChrysalisClient implements ChrysalisProxy {
	
	@SubscribeEvent
	public void setup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(AssemblyContainer.TYPE, AssemblyScreen::new);
		setupRenderLayers();
	}
	
	private void setupRenderLayers() {
		RenderTypeLookup.setRenderLayer(Blocks.ITEM_GRATE, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(Blocks.XP_STORE, RenderType.getCutout());
	}

	@Override
	public World getWorld() {
		return Minecraft.getInstance().world;
	}
}
