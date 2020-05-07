package chrysalis.item.slingShot.ammo;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;

public class SlingShotStone extends Entity {

  public UUID shootingEntity;

  public SlingShotStone(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
  }


  @Override
  protected void registerData() {

  }

  @Override
  protected void readAdditional(CompoundNBT compound) {

  }

  @Override
  protected void writeAdditional(CompoundNBT compound) {

  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return null;
  }

//  public SlingShotStone(EntityType<?> entityTypeIn,
//      World worldIn) {
//    super(entityTypeIn, worldIn);
//  }

//  @Override
//  protected void registerData() {
//
//  }
//
//  @Override
//  protected void readAdditional(CompoundNBT compound) {
//
//  }
//
//  @Override
//  protected void writeAdditional(CompoundNBT compound) {
//
//  }
//
//  @Nullable
//  public Entity getShooter() {
//    return this.shootingEntity != null && this.world instanceof ServerWorld ? ((ServerWorld)this.world).getEntityByUuid(this.shootingEntity) : null;
//  }
//
//
//  public IPacket<?> createSpawnPacket() {
//    Entity entity = this.getShooter();
//    return new SSpawnObjectPacket(this, entity == null ? 0 : entity.getEntityId());
//  }

}
