package chrysalis.utils;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

public class Utils {

	public static Direction getFacingFromNeighboringPos(BlockPos from, BlockPos to) {
		return Direction.getFacingFromVector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}
	
	public static int getSunlight(World world, BlockPos pos) {
		int i = world.getLightFor(LightType.SKY, pos) - world.getSkylightSubtracted();
    float f = world.getCelestialAngleRadians(1.0F);
    if (i > 0) {
      float f1 = f < (float) Math.PI ? 0.0F : ((float) Math.PI * 2F);
      f = f + (f1 - f) * 0.2F;
      i = Math.round((float) i * MathHelper.cos(f));
    }

    i = MathHelper.clamp(i, 0, 15);
    return i;
  }

  public static RayTraceResult rayTrace(World world, Entity excluding, Vec3d from, Vec3d to) {
    EntityRayTraceResult entityRayTraceResult = findEntiyOnPath(world, excluding, from, to);
    BlockRayTraceResult blockRayTraceResult = findBlockOnPath(world, excluding, from, to);

    if (entityRayTraceResult != null) {
      return entityRayTraceResult;
    } else {
      return blockRayTraceResult;
    }
  }

  public static EntityRayTraceResult findEntiyOnPath(World world, Entity excluding, Vec3d from,
      Vec3d to) {
    double d0 = to.subtract(from).lengthSquared();
    Entity entity = null;

    AxisAlignedBB aabb = new AxisAlignedBB(from, to);
    System.out.println("In AABB:");
    for (Entity e : world.getEntitiesWithinAABBExcludingEntity(excluding, aabb)) {
      System.out.println("    " + e.toString());
      Optional<Vec3d> vec = e.getBoundingBox().rayTrace(from, to);
      if (vec.isPresent()) {
        double dist = from.squareDistanceTo(vec.get());
        if (dist <= d0) {
          d0 = dist;
          entity = e;
        }
      }
    }

    if (entity != null) {
      return new EntityRayTraceResult(entity);
    } else {
      return null;
    }
  }

  public static BlockRayTraceResult findBlockOnPath(World world, Entity excluding, Vec3d from,
      Vec3d to) {
    if (!world.isRemote()) {
      return world.rayTraceBlocks(
          new RayTraceContext(from, to, BlockMode.COLLIDER, FluidMode.NONE, excluding));
    } else {
      return null;
    }
  }

}
