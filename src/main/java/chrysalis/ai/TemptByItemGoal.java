package chrysalis.ai;

import java.util.EnumSet;
import java.util.List;

import chrysalis.utils.AABBUtils;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.GroundPathNavigator;

public class TemptByItemGoal extends Goal {
	protected final AnimalEntity animal;
	private final double speed;
	protected ItemEntity targetItem;
	private int delayTemptCounter;
	private boolean isRunning;

	public TemptByItemGoal(AnimalEntity animal, double speedIn) {
		this.animal = animal;
		this.speed = speedIn;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(animal.getNavigator() instanceof GroundPathNavigator)) {
			throw new IllegalArgumentException("Unsupported mob type for TemptByItemGoal");
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			List<ItemEntity> list = this.animal.world.getEntitiesWithinAABB(ItemEntity.class, AABBUtils.getCenteredCube(this.animal, 15), ie -> this.animal.isBreedingItem(ie.getItem()));
			if (list.isEmpty()) {
				return false;//this.animal.canBreed();
			} else {
				this.targetItem = list.get(0);
				return true;
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		if(this.targetItem != null && this.targetItem.isAddedToWorld()) {
			if (this.animal.getDistanceSq(this.targetItem) < 36.0D) {
				return this.animal.canBreed();
			}
		}

		return this.shouldExecute();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		this.isRunning = true;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	public void resetTask() {
		this.targetItem = null;
		this.animal.getNavigator().clearPath();
		this.delayTemptCounter = 40;
		this.isRunning = false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		if(this.targetItem == null || this.animal == null || this.animal.getLookController() == null) {
			return;
		}
		this.animal.getLookController().setLookPositionWithEntity(this.targetItem, (float)(this.animal.getHorizontalFaceSpeed() + 20), (float)this.animal.getVerticalFaceSpeed());
		if (this.animal.getDistanceSq(this.targetItem) < 0.8D) {
			this.animal.getNavigator().clearPath();
			this.eat(this.targetItem);
			this.resetTask();
			this.delayTemptCounter = 600;
		} else {
			this.animal.getNavigator().tryMoveToEntityLiving(this.targetItem, this.speed);
		}

	}

	protected void eat(ItemEntity itemEntity) {
		ItemStack stack = itemEntity.getItem();
		if (this.animal.isBreedingItem(stack)) {
			if (this.animal.canBreed()) {
				stack.shrink(1);
				if(stack.isEmpty()) {
					itemEntity.remove();
				}
				this.animal.setInLove(null);
				return;
			}

			if (this.animal.isChild()) {
				stack.shrink(1);
				if(stack.isEmpty()) {
					itemEntity.remove();
				}
				this.animal.ageUp((int)((float)(-this.animal.getGrowingAge() / 20) * 0.1F), true);
				return;
			}
		}
	}

	/**
	 * @see #isRunning
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}
