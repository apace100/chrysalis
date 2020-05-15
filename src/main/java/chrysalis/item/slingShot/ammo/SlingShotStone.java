package chrysalis.item.slingShot.ammo;

import chrysalis.Chrysalis;
import chrysalis.item.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class SlingShotStone extends ProjectileItemEntity {

  private static final int DAMAGE = 1;
  @ObjectHolder(Chrysalis.MODID + ":sling_shot_stone_item")
  public static EntityType<SlingShotStone> TYPE;

  public SlingShotStone(double x, double y, double z, World world) {
    super(TYPE, x, y, z, world);
  }

  public SlingShotStone(EntityType<? extends ProjectileItemEntity> type, World worldIn) {
    super(type, worldIn);
    System.out.println("spawn SlingShotStone");
  }

  @Override
  protected Item getDefaultItem() {
    return Items.SLING_SHOT_STONE_ITEM;
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
        break;
    }
    this.remove();
  }

  private void hitBlock(BlockRayTraceResult result) {
//    System.out.println("hitBlock");
//    if (!world.isRemote()) {
//      BlockPos pos = result.getPos();
//      BlockState state = world.getBlockState(pos);
//      PlayerEntity player = (PlayerEntity) getThrower();
//      Hand hand = player.getActiveHand();
//      state.onBlockActivated(world, player, hand, result);
//    }
  }

  private void hitEntity(Entity entity) {
    System.out.println("hitEntity");
    entity
        .attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) DAMAGE);
  }

  @Override
  public void tick() {
    super.tick();
    System.out.println("tick");
  }
}
