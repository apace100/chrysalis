package chrysalis.block.fan;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class FanBlock extends Block {

  public FanBlock() {
    super(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(4f));
  }

  @Override
  public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
    entityIn.fallDistance = 0;
  }

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos,
      @Nullable Direction side) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new FanTileEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }


}
