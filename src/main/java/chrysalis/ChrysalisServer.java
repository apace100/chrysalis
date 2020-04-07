package chrysalis;

import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

public class ChrysalisServer implements ChrysalisProxy {
	
	@SubscribeEvent
	public void setup(FMLDedicatedServerSetupEvent event) {
		
	}

	public World getWorld() {
		return null;
	}
}
