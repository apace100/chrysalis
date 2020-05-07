package chrysalis.item.slingShot;

import chrysalis.utils.Utils;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SlingShot extends ShootableItem {

  public static final int RANGE = 5;
  public static final int Durability = 72000;
  public static final int MAX_DRAWING_DURATION = 30;
  public static final Predicate<ItemStack> SLING_SHOT_AMMO = itemStack -> {
//    return itemStack.getItem() instanceof SlingShotAmmoBase;
    return true;
  };

  public SlingShot(Properties properties) {
    super(properties);
  }

  public static double getVelocity(Double charge) {
    System.out.println("charge= " + charge);

    double f = charge / 20.0F;
    f = (f * f + f * 2.0F) / 3.0F;
    f *= 3f;

    if (f > 4.33d) {
      f = 4.33d;
    }
    return f;
  }

  /**
   * Called when the player stops using an Item (stops holding the right mouse button).
   */
  public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving,
      int timeLeft) {
    System.out.println("deactivated SlingShot");
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity playerentity = (PlayerEntity) entityLiving;
      if (!worldIn.isRemote) {
        Vec3d eyePositionOfPlayer = playerentity.getEyePosition(0F);
        Vec3d positionLookedAt = playerentity.getEyePosition(0F)
            .add(playerentity.getLookVec().scale(RANGE));
        RayTraceResult rayTraceResult = Utils
            .rayTrace(worldIn, playerentity, eyePositionOfPlayer,
                positionLookedAt);
        System.out.println(rayTraceResult.getType());
        double velocity = getVelocity((double) Durability - timeLeft);
        System.out.println(velocity);
        switch (rayTraceResult.getType()) {
          case ENTITY:
            Entity entity = Utils
                .findEntiyOnPath(worldIn, playerentity, eyePositionOfPlayer, positionLookedAt)
                .getEntity();
            shootEntity(entity, entity.getLookVec(), velocity);
            break;
          case BLOCK:
            shootPlayer(playerentity, velocity);
            break;
          case MISS:
            shootProjektile();
            break;
        }
      }
    }
//      boolean isCreativeMode = playerentity.abilities.isCreativeMode;
//      ItemStack itemstack = playerentity.findAmmo(stack);
//
//      int i = this.getUseDuration(stack) - timeLeft;
//      i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || isCreativeMode);
//      if (i < 0) return;
//
//      if (!itemstack.isEmpty() || isCreativeMode) {
//        if (itemstack.isEmpty()) {
//          itemstack = new ItemStack(chrysalis.item.Items.SLING_SHOT_STONE);
//        }
//
//        float f = getArrowVelocity(i);
//        if (((double)f > 0.1D)) {
////          boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof SlingShotAmmoBase
////              && ((SlingShotAmmoBase)itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
//          if (!worldIn.isRemote) {
////            SlingShotAmmoBase SlingShotAmmo = (SlingShotAmmoBase) (itemstack.getItem() instanceof SlingShotAmmoBase ? itemstack.getItem() : chrysalis.item.Items.SLING_SHOT_STONE);
////            AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
////            abstractarrowentity = customeArrow(abstractarrowentity);
////            abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, f * 3.0F, 1.0F);
////            if (f == 1.0F) {
////              abstractarrowentity.setIsCritical(true);
////            }
//
////            int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
////            if (j > 0) {
////              abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double)j * 0.5D + 0.5D);
////            }
////
////            int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
////            if (k > 0) {
////              abstractarrowentity.setKnockbackStrength(k);
////            }
////
////            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
////              abstractarrowentity.setFire(100);
////            }
//
//            stack.damageItem(1, playerentity,playerEntity -> {
//             playerEntity.sendBreakAnimation(playerentity.getActiveHand());
//            });
////            if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == net.minecraft.item.Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
////              abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
////            }
//
//            worldIn.addEntity(abstractarrowentity);
//          }
//
//          worldIn.playSound((PlayerEntity)null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
//          if (!flag1 && !playerentity.abilities.isCreativeMode) {
//            itemstack.shrink(1);
//            if (itemstack.isEmpty()) {
//              playerentity.inventory.deleteStack(itemstack);
//            }
//          }
//
//          playerentity.addStat(Stats.ITEM_USED.get(this));
//        }
//      }
//    }

  }

  private void shootProjektile() {
    System.out.println("shoot Projectile");

  }

  private void shootEntity(Entity entity, Vec3d direction, double velocity) {
    System.out.println("shoot Entity");
    direction = direction.normalize();
    entity.addVelocity(direction.getX() * velocity, direction.getY() * velocity / 3,
        direction.getZ() * velocity);
    entity.velocityChanged = true;
  }

  private void shootPlayer(PlayerEntity playerentity, double velocity) {
    System.out.println("shoot Player");
    shootEntity(playerentity, playerentity.getLookVec().inverse(), velocity);
  }

  /**
   * How long it takes to use or consume an item
   */
  public int getUseDuration(ItemStack stack) {
    return Durability;
  }

  /**
   * returns the action that specifies what animation to play when the items is being used
   */
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.BOW;
  }

  /**
   * Called to trigger the item's "innate" right click behavior. To handle when this item is used on
   * a Block, see {@link #onItemUse}.
   */
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    System.out.println("activated SlingShot");
    ItemStack itemstack = playerIn.getHeldItem(handIn);
//    boolean flag = !playerIn.findAmmo(itemstack).isEmpty();
//    playerIn.addStat(Stats.ITEM_USED.get(this));
    if (!playerIn.abilities.isCreativeMode) {
      return ActionResult.resultFail(itemstack);
    } else {
      playerIn.setActiveHand(handIn);
      return ActionResult.resultConsume(itemstack);
    }
  }

  /**
   * Get the predicate to match ammunition when searching the player's inventory, not their
   * main/offhand
   */
  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return SLING_SHOT_AMMO;
  }

  public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
    return arrow;
  }
}
