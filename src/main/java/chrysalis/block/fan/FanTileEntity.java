package chrysalis.block.fan;

import chrysalis.Chrysalis;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.registries.ObjectHolder;

public class FanTileEntity extends TileEntity implements ITickableTileEntity {

  @ObjectHolder(Chrysalis.MODID + ":fan")
  public static TileEntityType<FanTileEntity> TYPE;

  public FanTileEntity() {
    super(TYPE);
    System.out.println("craete empty");
  }


  @Override
  public void tick() {
    if (world != null) {
      double x = pos.getX();
      double y = pos.getY() + 1.0d;
      double z = pos.getZ();
      double elevationHeigth = world.getStrongPower(pos);
      for (int i = 1; i <= elevationHeigth; i++) {
        if (!world.isAirBlock(pos.add(0, i, 0))) {
          elevationHeigth = i;
          break;
        }
      }
      AxisAlignedBB scanAbove = new AxisAlignedBB(x, y, z, x + 1.0, y + elevationHeigth, z + 1.0);
      List<Entity> list = world.getEntitiesWithinAABB(Entity.class, scanAbove);

      for (Entity entity : list) {

        if (!entity.isShiftKeyDown()) {

          entity.setMotion(entity.getMotion().x, 0.35, entity.getMotion().z);
        }
        entity.fallDistance = 0;
      }
    }
  }
}
