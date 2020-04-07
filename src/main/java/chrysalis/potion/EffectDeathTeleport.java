package chrysalis.potion;

import java.util.Random;

import chrysalis.capability.CapabilityChrysalisData;
import chrysalis.capability.IChrysalisData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EffectDeathTeleport extends Effect {

	public EffectDeathTeleport() {
		super(EffectType.BENEFICIAL, 0x3C485C);
		MinecraftForge.EVENT_BUS.register(new Object() {
			@SubscribeEvent
			public void onLivingDie(LivingDeathEvent event) {
				if(event.getEntityLiving() instanceof PlayerEntity) {
					LivingEntity living = event.getEntityLiving();
					IChrysalisData data = CapabilityChrysalisData.get(living);
					CompoundNBT lastDeath = new CompoundNBT();
					lastDeath.putDouble("X", living.getPosX());
					lastDeath.putDouble("Y", living.getPosY());
					lastDeath.putDouble("Z", living.getPosZ());
					lastDeath.putString("Dimension", living.dimension.getRegistryName().toString());
					data.setTag("LastDeathPosition", lastDeath);
				}
				
			}
		});
	}

	@Override
	public void performEffect(LivingEntity living, int amplifier) {
		IChrysalisData data = CapabilityChrysalisData.get(living);
		if(data.hasKey("LastDeathPosition")) {
			CompoundNBT lastDeath = (CompoundNBT)data.getTag("LastDeathPosition");
			double x = lastDeath.getDouble("X");
			double y = lastDeath.getDouble("Y");
			double z = lastDeath.getDouble("Z");
			String deathDimension = lastDeath.getString("Dimension");
			
			if(living.dimension.getRegistryName().toString().equals(deathDimension)) {
				Random rand = living.getRNG();
				
				living.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1F, 1F);
				for(int i = 0; i < 32; ++i) {
					living.world.addParticle(ParticleTypes.PORTAL, living.getPosX(), living.getPosY() + rand.nextDouble() * 2.0D, living.getPosZ(), rand.nextGaussian(), 0.0D, rand.nextGaussian());
				}
				
				living.setPositionAndUpdate(x, y, z);
				living.fallDistance = 0f;
				
				for(int i = 0; i < 32; ++i) {
					living.world.addParticle(ParticleTypes.PORTAL, living.getPosX(), living.getPosY() + rand.nextDouble() * 2.0D, living.getPosZ(), rand.nextGaussian(), 0.0D, rand.nextGaussian());
				}
			}
		} else {
			living.playSound(SoundEvents.ENTITY_ENDER_EYE_DEATH, 1F, 1F);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean isInstant() {
		return true;
	}
}
