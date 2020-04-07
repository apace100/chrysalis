package chrysalis.potion;

import java.util.Random;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EffectCritChance extends Effect {

	public EffectCritChance(EffectType typeIn, int liquidColorIn) {
		super(typeIn, liquidColorIn);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private Random random = new Random();

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onCrit(CriticalHitEvent event) {
		if(!event.isVanillaCritical() && event.getPlayer().isPotionActive(this)) {
			EffectInstance instance = event.getPlayer().getActivePotionEffect(this);
			float chance = 0.3f + instance.getAmplifier() * 0.35f;
			if(random.nextFloat() < chance) {
				event.setResult(Result.ALLOW);
			}
		}
	}
}
