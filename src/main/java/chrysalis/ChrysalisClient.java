package chrysalis;

import chrysalis.block.Blocks;
import chrysalis.block.assembly.AssemblyContainer;
import chrysalis.block.assembly.AssemblyScreen;
import chrysalis.item.slingShot.ammo.SlingShotStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ChrysalisClient implements ChrysalisProxy {
	
	@SubscribeEvent
	public void setup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(AssemblyContainer.TYPE, AssemblyScreen::new);
		RenderingRegistry.registerEntityRenderingHandler(
				SlingShotStone.TYPE,
				erm -> new SpriteRenderer<>(erm, Minecraft.getInstance().getItemRenderer()));
		setupRenderLayers();
	}
	
	private void setupRenderLayers() {
		RenderTypeLookup.setRenderLayer(Blocks.ITEM_GRATE, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(Blocks.XP_STORE, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(Blocks.POTION_WART_WOODEN_DOOR, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(Blocks.POTION_WART_IRON_DOOR, RenderType.getCutout());
	}

	@Override
	public World getWorld() {
		return Minecraft.getInstance().world;
	}
}
