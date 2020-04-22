package chrysalis.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class XPSeedItem extends Item {
	
	public static final Food FOOD = (new Food.Builder()).setAlwaysEdible().build();
	
	public XPSeedItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if(!worldIn.isRemote && entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity)entityLiving;
			player.xpSeed = worldIn.rand.nextInt();
		}
		System.out.println("done eating");
		stack.shrink(1);
		return stack;
	}
	
}
