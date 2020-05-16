package chrysalis.item.slingShot.ammo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class SlingShotAmmoBase extends ProjectileItemEntity {

  public SlingShotAmmoBase(
      EntityType<? extends ProjectileItemEntity> type,
      World worldIn) {
    super(type, worldIn);
  }

  public SlingShotAmmoBase(
      EntityType<? extends ProjectileItemEntity> type,
      LivingEntity livingEntityIn, World worldIn) {
    super(type, livingEntityIn, worldIn);
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    System.out.println("impact: " + result.getType());
    switch (result.getType()) {
      case ENTITY:
        Entity entity = ((EntityRayTraceResult) result).getEntity();
        hitEntity(entity);
        break;
      case BLOCK:
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
        hitBlock(blockRayTraceResult);
        break;
      case MISS:
        hitNothing(result);
        break;
    }
//    this.setVelocity(0,this.getGravityVelocity(),0);
    this.remove();
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  protected abstract void hitEntity(Entity entity);

  protected abstract void hitBlock(BlockRayTraceResult result);

  protected abstract void hitNothing(RayTraceResult result);

  protected abstract void afterImpact();
}
