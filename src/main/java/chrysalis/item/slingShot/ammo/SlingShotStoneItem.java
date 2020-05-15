package chrysalis.item.slingShot.ammo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SlingShotStoneItem extends Item {

  public SlingShotStoneItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);
    worldIn
        .playSound((PlayerEntity) null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
            SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
            0.4F / (random.nextFloat() * 0.4F + 0.8F));
    if (!worldIn.isRemote) {
      System.out.println("throw");
      SlingShotStone slingShotStone = new SlingShotStone(playerIn.getPosX(), playerIn.getPosY(),
          playerIn.getPosZ(), worldIn);
      slingShotStone.setItem(itemstack);
      slingShotStone.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 15F, 1.0F);
      playerIn.world.addEntity(slingShotStone);
    }

    playerIn.addStat(Stats.ITEM_USED.get(this));
    if (!playerIn.abilities.isCreativeMode) {
      itemstack.shrink(1);
    }

    return ActionResult.resultSuccess(itemstack);
  }
}

