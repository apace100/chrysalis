package chrysalis.item.slingShot;

import chrysalis.item.slingShot.ammo.SlingShotAmmoBase;
import chrysalis.item.slingShot.ammo.SlingShotAmmoItemBase;
import chrysalis.utils.Utils;
import java.util.function.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SlingShot extends ShootableItem {

  public static final int RANGE = 5;
  public static final int Durability = 72000;
  public static final int MAX_DRAWING_DURATION = 30;
  public static final Predicate<ItemStack> SLING_SHOT_AMMO = itemStack -> {
    return itemStack.getItem() instanceof SlingShotAmmoItemBase;
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
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity playerentity = (PlayerEntity) entityLiving;
      if (!worldIn.isRemote) {
        Vec3d eyePositionOfPlayer = playerentity.getEyePosition(0F);
        Vec3d positionLookedAt = playerentity.getEyePosition(0F)
            .add(playerentity.getLookVec().scale(RANGE));
        RayTraceResult rayTraceResult = Utils
            .rayTrace(worldIn, playerentity, eyePositionOfPlayer,
                positionLookedAt);
        double velocity = getVelocity((double) Durability - timeLeft);
        switch (rayTraceResult.getType()) {
          case ENTITY:
            Entity entity = Utils
                .findEntiyOnPath(worldIn, playerentity, eyePositionOfPlayer, positionLookedAt)
                .getEntity();
            shootEntity(entity, playerentity.getLookVec(), velocity);
            break;
          case BLOCK:
            shootPlayer(playerentity, velocity);
            break;
          case MISS:
            shootProjectile(worldIn, playerentity, velocity);
            break;
        }
      }
    }

  }

  private void shootProjectile(World worldIn, PlayerEntity playerIn, double velocity) {
    System.out.println("shoot Projectile");
    ItemStack ammoStack = findAmmo(playerIn);
    if (!worldIn.isRemote) {
      if (ammoStack != null && ammoStack != ItemStack.EMPTY) {
        SlingShotAmmoItemBase ammoItem = (SlingShotAmmoItemBase) ammoStack.getItem();

        SlingShotAmmoBase slingShotAmmo = ammoItem.getShootableEntity(playerIn, worldIn);
        playShootSound(worldIn, playerIn);
        slingShotAmmo
            .shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F,
                (float) (2F * (velocity / 3)), 0.0F);
        playerIn.world.addEntity(slingShotAmmo);
      }

      playerIn.addStat(Stats.ITEM_USED.get(this));
      if (!playerIn.abilities.isCreativeMode) {
        ammoStack.shrink(1);
      }
    }
  }

  protected void playShootSound(World worldIn, PlayerEntity playerIn) {
    worldIn.playSound((PlayerEntity) null, playerIn.getPosX(), playerIn.getPosY(),
        playerIn.getPosZ(),
        SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F,
        0.4F / (random.nextFloat() * 0.4F + 0.8F));
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
      playerIn.setActiveHand(handIn);
      return ActionResult.resultConsume(itemstack);
  }

  /**
   * Get the predicate to match ammunition when searching the player's inventory, not their
   * main/offhand
   */
  public Predicate<ItemStack> getInventoryAmmoPredicate() {
    return SLING_SHOT_AMMO;
  }

  private ItemStack findAmmo(PlayerEntity player) {
    for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
      ItemStack itemstack = player.inventory.getStackInSlot(i);
      if (SLING_SHOT_AMMO.test(itemstack)) {
        return itemstack;
      }
    }
    return ItemStack.EMPTY;
  }
}
