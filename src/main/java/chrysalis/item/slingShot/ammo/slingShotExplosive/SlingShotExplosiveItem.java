package chrysalis.item.slingShot.ammo.slingShotExplosive;

import chrysalis.item.slingShot.ammo.SlingShotAmmoBase;
import chrysalis.item.slingShot.ammo.SlingShotAmmoItemBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SlingShotExplosiveItem extends SlingShotAmmoItemBase {

  private static final double ITEM_COOLDOWN_IN_SECS = 2.5;

  public SlingShotExplosiveItem(Properties properties) {
    super(properties);
  }

  @Override
  protected int getCoolDownInTicks() {
    return (int) Math.round(ITEM_COOLDOWN_IN_SECS * 20);
  }

  @Override
  public SlingShotAmmoBase getShootableEntity(PlayerEntity playerEntity, World world) {
    return new SlingShotExplosive(playerEntity, world);
  }

  @Override
  protected float getPitchOffset() {
    return 0;
  }

  @Override
  protected float getVelocity() {
    return 1F;
  }

  @Override
  protected float getInaccuracy() {
    return 2;
  }
}
