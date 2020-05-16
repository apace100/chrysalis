package chrysalis.item.slingShot.ammo.slingShotExplosive;

import chrysalis.Chrysalis;
import chrysalis.item.Items;
import chrysalis.item.slingShot.ammo.SlingShotAmmoBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class SlingShotExplosive extends SlingShotAmmoBase {

  @ObjectHolder(Chrysalis.MODID + ":sling_shot_explosive")
  public static EntityType<SlingShotExplosive> TYPE;

  private static float EXPLOSION_RADIUS = 2F;

  public SlingShotExplosive(LivingEntity livingEntity, World world) {
    super(TYPE, livingEntity, world);
  }

  public SlingShotExplosive(EntityType<SlingShotExplosive> type, World worldIn) {
    super(type, worldIn);
  }

  @Override
  protected void hitEntity(Entity entity) {
    explode();
  }

  @Override
  protected void hitBlock(BlockRayTraceResult result) {
    explode();
  }

  @Override
  protected void hitNothing(RayTraceResult result) {

  }

  protected void explode() {
    this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(),
        EXPLOSION_RADIUS, Explosion.Mode.BREAK);
  }

  @Override
  protected void afterImpact() {

  }

  @Override
  protected Item getDefaultItem() {
    return Items.SLING_SHOT_EXPLOSIVE_ITEM;
  }
}
