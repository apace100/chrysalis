package chrysalis.block.fan;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
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
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state,
      @Nullable LivingEntity placer, ItemStack stack) {
    if (placer != null) {
      Direction facing = getFacingFromEntity(pos, placer);
      System.out.println(facing);
      worldIn.setBlockState(pos, state.with(
          BlockStateProperties.FACING, facing), 2);
    }
  }

  private Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction
        .getFacingFromVector((float) (entity.getPosition().getX() - clickedBlock.getX()),
            (float) (entity.getPosition().getY() - clickedBlock.getY()),
            (float) (entity.getPosition().getZ() - clickedBlock.getZ()));
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING);
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
    System.out.println(state.getProperties());
    return new FanTileEntity();
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }


}
