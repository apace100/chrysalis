package chrysalis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockReader;

public class ItemGrateBlock extends Block {
	
	public ItemGrateBlock() {
		super(Block.Properties.create(Material.IRON).notSolid().hardnessAndResistance(5F));
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ItemGrateTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	/*public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}*/
}