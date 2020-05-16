package chrysalis.item.slingShot.ammo.slingShotStone;

import chrysalis.Chrysalis;
import chrysalis.item.Items;
import chrysalis.item.slingShot.ammo.SlingShotAmmoBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class SlingShotStone extends SlingShotAmmoBase {

  private static final int DAMAGE = 1;
  @ObjectHolder(Chrysalis.MODID + ":sling_shot_stone")
  public static EntityType<SlingShotStone> TYPE;

  public SlingShotStone(LivingEntity livingEntity, World world) {
    super(TYPE, livingEntity, world);
  }

  public SlingShotStone(EntityType<SlingShotStone> type, World worldIn) {
    super(type, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return Items.SLING_SHOT_STONE_ITEM;
  }

  @Override
  public void hitEntity(Entity entity) {
    System.out.println("hitEntity");
    entity
        .attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float) DAMAGE);
  }

  @Override
  public void hitBlock(BlockRayTraceResult result) {
    System.out.println("hitBlock");
    if (!world.isRemote()) {
      BlockPos pos = result.getPos();
      BlockState state = world.getBlockState(pos);
      PlayerEntity player = (PlayerEntity) this.getThrower();
      Hand hand = Hand.MAIN_HAND;
      state.onBlockActivated(world, player, hand, result);
      state.onProjectileCollision(world, state, result, this);
    }
  }

  @Override
  protected void hitNothing(RayTraceResult result) {

  }

  @Override
  protected void afterImpact() {

  }
}
