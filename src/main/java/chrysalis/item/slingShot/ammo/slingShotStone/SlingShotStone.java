package chrysalis.item.slingShot.ammo.slingShotStone;

import chrysalis.Chrysalis;
import chrysalis.item.Items;
import chrysalis.item.slingShot.ammo.SlingShotAmmoBase;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
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

  @ObjectHolder(Chrysalis.MODID + ":sling_shot_stone")
  public static EntityType<SlingShotStone> TYPE;

  private static final int DAMAGE_BASE = 1;

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
    float damage = (float) (DAMAGE_BASE * this.shootingType.getDamageMultiplier());
    entity
        .attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
  }

  @Override
  public void hitBlock(BlockRayTraceResult result) {
    if (!world.isRemote()) {
      BlockPos pos = result.getPos();
      BlockState state = world.getBlockState(pos);
      if (state.getBlock() instanceof AbstractButtonBlock || state
          .getBlock() instanceof LeverBlock) {
        PlayerEntity player = (PlayerEntity) this.getThrower();
        Hand hand = Hand.MAIN_HAND;
        state.onBlockActivated(world, player, hand, result);
      }
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
