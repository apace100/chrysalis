package chrysalis.ai;

import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AIExtender {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof AnimalEntity && !(event.getEntity() instanceof FoxEntity) && (((AnimalEntity) event.getEntity()).getNavigator() instanceof GroundPathNavigator)) {
			AnimalEntity animal = (AnimalEntity) event.getEntity();
			animal.goalSelector.addGoal(3, new TemptByItemGoal(animal, 1.25D));
		}
	}
}
