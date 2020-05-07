package chrysalis.events;

import chrysalis.capability.CapabilityChrysalisData;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(bus = Bus.FORGE)
public class LivingUpdateEventHandler {

  public static String milked = "isMilked";

  @SubscribeEvent
  public static void livingUpdate(LivingUpdateEvent event) {
    event.getEntity().getCapability(CapabilityChrysalisData.CAPABILITY)
        .ifPresent(iChrysalisData -> {
          iChrysalisData.setInt(milked, iChrysalisData.getInt(milked) - 1);
        });

  }
}
