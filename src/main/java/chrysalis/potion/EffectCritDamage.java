package chrysalis.potion;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EffectCritDamage extends Effect {

	public EffectCritDamage(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onCrit(CriticalHitEvent event) {
		if((event.isVanillaCritical() || event.getResult() == Result.ALLOW) && event.getPlayer().isPotionActive(this)) {
			EffectInstance instance = event.getPlayer().getActivePotionEffect(this);
			float newModifier = 1.5f + (1 + instance.getAmplifier()) * 0.5f;
			if(event.getDamageModifier() < newModifier) {
				event.setDamageModifier(newModifier);
			}
		}
	}
}
