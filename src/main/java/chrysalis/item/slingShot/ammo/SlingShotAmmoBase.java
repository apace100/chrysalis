package chrysalis.item.slingShot.ammo;

import net.minecraft.entity.IProjectile;
import net.minecraft.item.Item;

public abstract class SlingShotAmmoBase extends Item implements IProjectile {

  public SlingShotAmmoBase(Properties properties) {
    super(properties);
  }

//  public SlingShotAmmoBase(EntityType<?> entityTypeIn,
//      World worldIn) {
//    super(entityTypeIn, worldIn);
//  }

//  public void shoot(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
//    float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
//    float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180F));
//    float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
//    this.shoot((double)f, (double)f1, (double)f2, velocity, inaccuracy);
//    this.setMotion(this.getMotion().add(shooter.getMotion().x, shooter.onGround ? 0.0D : shooter.getMotion().y, shooter.getMotion().z));
//  }
//
//  /**
//   * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
//   */
//  public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
//    Vec3d vec3d = (new Vec3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
//    this.setMotion(vec3d);
//    float f = MathHelper.sqrt(horizontalMag(vec3d));
//    this.rotationYaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * (double)(180F / (float)Math.PI));
//    this.rotationPitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * (double)(180F / (float)Math.PI));
//    this.prevRotationYaw = this.rotationYaw;
//    this.prevRotationPitch = this.rotationPitch;
////    this.ticksInGround = 0;
//  }

//  public SlingShotAmmoBase createAmmo(SlingShotAmmoBase ammoType){
//    return
//  }
}
