package chrysalis.events;

import chrysalis.Attributes;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class AttributeEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onCrit(CriticalHitEvent event) {
		if(!event.isVanillaCritical()) {
			double chance = event.getPlayer().getAttribute(Attributes.CRIT_CHANCE).getValue();
			if(event.getPlayer().getRNG().nextFloat() < chance) {
				event.setResult(Result.ALLOW);
			}
		}
		if((event.isVanillaCritical() || event.getResult() == Result.ALLOW)) {
			double modifier = event.getPlayer().getAttribute(Attributes.CRIT_DAMAGE).getValue();
			if(event.getDamageModifier() < modifier) {
				event.setDamageModifier((float)modifier);
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onXPGained(PlayerXpEvent.XpChange event) {
		double multi = event.getPlayer().getAttribute(Attributes.XP_GAIN).getValue();
		int newXp = (int)Math.floor((double)event.getAmount() * multi);
		event.setAmount(newXp);
	}
}
