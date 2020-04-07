package chrysalis.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AABBUtils {

	public static AxisAlignedBB getCenteredCube(BlockPos center, int xSpan, int ySpan, int zSpan) {
		Vec3i halfVector = new Vec3i(xSpan / 2, ySpan / 2, zSpan / 2);
		return new AxisAlignedBB(center.subtract(halfVector), center.add(halfVector));
	}
	
	public static AxisAlignedBB getCenteredCubeUpwards(BlockPos center, int xSpan, int ySpan, int zSpan) {
		Vec3i halfVector = new Vec3i(xSpan / 2, 0, zSpan / 2);
		return new AxisAlignedBB(center.subtract(halfVector), center.add(halfVector).up(ySpan));
	}
	
	public static AxisAlignedBB getCenteredCube(Entity center, double xSpan, double ySpan, double zSpan) {
		return new AxisAlignedBB(center.getPosX() - xSpan / 2, center.getPosY() - ySpan / 2, center.getPosZ() - zSpan / 2, center.getPosX() + xSpan / 2, center.getPosY() + ySpan / 2, center.getPosZ() + zSpan / 2);
	}
	
	public static AxisAlignedBB getCenteredCube(Entity center, double span) {
		return getCenteredCube(center, span, span, span);
	}
	
	public static AxisAlignedBB getCenteredHorizontalCube(BlockPos center, double spanHorizontal) {
		Vec3d halfVector = new Vec3d(spanHorizontal / 2, 0.5, spanHorizontal / 2);
		Vec3d centerVec = new Vec3d(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);
		return new AxisAlignedBB(centerVec.subtract(halfVector), centerVec.add(halfVector));
	}
}
