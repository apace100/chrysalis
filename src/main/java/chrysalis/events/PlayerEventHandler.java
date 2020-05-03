package chrysalis.events;

import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(bus = Bus.FORGE)
public class PlayerEventHandler {

  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
    System.out.println("EVENT TRIGGERED");
    PlayerEntity player = event.getPlayer();
    ItemStack heldItemStack = player.getHeldItem(event.getHand());
    if (heldItemStack.getItem() == Items.GLASS_BOTTLE) {
      if (event.getTarget() instanceof CaveSpiderEntity) {
        heldItemStack.setCount(heldItemStack.getCount() - 1);
        ItemStack poisonStack = new ItemStack(Items.POTION);
        PotionUtils.addPotionToItemStack(poisonStack, Potions.POISON);
        System.out.println(poisonStack);
        System.out.println(poisonStack.getItem());
        player.inventory.addItemStackToInventory(poisonStack);
      }
    }
  }
}
