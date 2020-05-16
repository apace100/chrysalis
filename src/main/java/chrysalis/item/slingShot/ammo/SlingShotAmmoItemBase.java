package chrysalis.item.slingShot.ammo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class SlingShotAmmoItemBase extends Item {

  public SlingShotAmmoItemBase(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    playSound(worldIn, playerIn.getPosition());
    if (!worldIn.isRemote) {
      SlingShotAmmoBase ammo = getShootableEntity(playerIn, worldIn);

      ammo.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, getPitchOffset(),
          getVelocity(), getInaccuracy());
      playerIn.world.addEntity(ammo);
    }
    playerIn.addStat(Stats.ITEM_USED.get(this));
    if (!playerIn.abilities.isCreativeMode) {
      itemstack.shrink(1);
    }
    playerIn.getCooldownTracker().setCooldown(this, getCoolDownInTicks());
    return ActionResult.resultSuccess(itemstack);
  }

  protected abstract int getCoolDownInTicks();

  protected void playSound(World world, BlockPos pos) {
    world.playSound((PlayerEntity) null, pos.getX(), pos.getY(), pos.getZ(),
        SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
        0.4F / (random.nextFloat() * 0.4F + 0.8F));
  }

  public abstract SlingShotAmmoBase getShootableEntity(PlayerEntity playerEntity, World world);

  protected abstract float getPitchOffset();

  protected abstract float getVelocity();

  protected abstract float getInaccuracy();
}
