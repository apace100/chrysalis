package chrysalis.item.slingShot.ammo.slingShotStone;

import chrysalis.item.slingShot.ammo.SlingShotAmmoBase;
import chrysalis.item.slingShot.ammo.SlingShotAmmoItemBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class SlingShotStoneItem extends SlingShotAmmoItemBase {

  private static final Double ITEM_COOLDOWN_IN_SECS = 2.0;

  public SlingShotStoneItem(Properties properties) {
    super(properties);
  }

  @Override
  protected int getCoolDownInTicks() {
    return (int) Math.round(ITEM_COOLDOWN_IN_SECS * 20);
  }

  @Override
  public SlingShotAmmoBase getShootableEntity(PlayerEntity playerEntity, World world) {
    return new SlingShotStone(playerEntity, world);
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

