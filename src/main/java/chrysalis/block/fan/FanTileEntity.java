package chrysalis.block.fan;

import chrysalis.Chrysalis;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

public class FanTileEntity extends TileEntity implements ITickableTileEntity {

  @ObjectHolder(Chrysalis.MODID + ":fan")
  public static TileEntityType<FanTileEntity> TYPE;

  private double elevationDistance = 0;
  private Direction fanDirection;

  public FanTileEntity() {
    super(TYPE);
  }

  @Override
  public void tick() {
    if (world != null) {
      fanDirection = world.getBlockState(pos).get(BlockStateProperties.FACING);
      elevationDistance = getActuallElevationHeigth();
      moveEntities();
    }
  }

  private void moveEntities() {
    AxisAlignedBB scan = new AxisAlignedBB(pos,
        pos.offset(fanDirection, (int) elevationDistance - 1).add(1.0, 1.0, 1.0));
    List<Entity> list = world.getEntitiesWithinAABB(Entity.class, scan);
    for (Entity entity : list) {
      addMotion(entity);
      entity.fallDistance = 0;
    }
  }

  private void addMotion(Entity entity) {
    double factor = 0.35;
    if (entity.isShiftKeyDown()) {
      fanDirection = fanDirection.getOpposite();
    }
    switch (fanDirection) {
      case DOWN:
        entity.setMotion(entity.getMotion().x, factor * -1, entity.getMotion().z);
        break;
      case UP:
        entity.setMotion(entity.getMotion().x, factor, entity.getMotion().z);
        break;
      case NORTH:
        entity.setMotion(entity.getMotion().x, entity.getMotion().y, factor * -1);
        break;
      case SOUTH:
        entity.setMotion(entity.getMotion().x, entity.getMotion().y, factor);
        break;
      case WEST:
        entity.setMotion(factor * -1, entity.getMotion().y, entity.getMotion().z);
        break;
      case EAST:
        entity.setMotion(factor, entity.getMotion().y, entity.getMotion().z);
        break;
      default:
        break;
    }
  }

  private double getActuallElevationHeigth() {
    int redstonePower = world.getRedstonePowerFromNeighbors(pos);
    for (int i = 1; i <= redstonePower; i++) {
      BlockPos posToCheck = pos.offset(fanDirection, i);
      if (world.getBlockState(posToCheck)
          .isSolidSide(world, posToCheck, fanDirection.getOpposite())) {
        return i;
      }
    }
    return redstonePower;
  }
}
