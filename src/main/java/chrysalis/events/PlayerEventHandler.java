package chrysalis.events;

import chrysalis.capability.CapabilityChrysalisData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(bus = Bus.FORGE)
public class PlayerEventHandler {

  private static int milkingCoolDown = 3 * 20 * 60;
  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
    if (!event.getWorld().isRemote) {
      System.out.println("EVENT TRIGGERED");
      String milked = LivingUpdateEventHandler.milked;
      PlayerEntity player = event.getPlayer();
      Entity target = event.getTarget();
      ItemStack heldItemStack = player.getHeldItem(event.getHand());
      if (heldItemStack.getItem() == Items.GLASS_BOTTLE) {
        if (target instanceof CaveSpiderEntity) {
          target.getCapability(CapabilityChrysalisData.CAPABILITY).ifPresent(iChrysalisData -> {
            if (iChrysalisData.hasKey(milked) && iChrysalisData.getInt(milked) <= 0) {
              milkSpider(player, heldItemStack);
              iChrysalisData.setInt(milked, milkingCoolDown);
            } else if (!iChrysalisData.hasKey(milked)) {
              milkSpider(player, heldItemStack);
              iChrysalisData.setInt(milked, milkingCoolDown);
            }
          });

        }
      }
    }
  }

  private static void milkSpider(PlayerEntity player, ItemStack heldItemStack) {
    heldItemStack.setCount(heldItemStack.getCount() - 1);
    ItemStack acidStack = new ItemStack(chrysalis.item.Items.ACID_BOTTLE);
    player.inventory.addItemStackToInventory(acidStack);
  }
}
