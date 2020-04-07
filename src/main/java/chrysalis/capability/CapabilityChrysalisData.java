package chrysalis.capability;

import chrysalis.Chrysalis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilityChrysalisData {

	@CapabilityInject(IChrysalisData.class)
	public static Capability<IChrysalisData> CAPABILITY = null;
	
	public static final ResourceLocation LOCATION = new ResourceLocation(Chrysalis.MODID, "data");

	private static final IChrysalisData DUMMY = new DummyChrysalisData();
	
	public static IChrysalisData get(LivingEntity entity) {
		return entity.getCapability(CAPABILITY).orElse(DUMMY);
	}
	
	public static void register() {
		CapabilityManager.INSTANCE.register(IChrysalisData.class, new IStorage<IChrysalisData>()
        {

			@Override
			public INBT writeNBT(Capability<IChrysalisData> capability, IChrysalisData instance, Direction side) {
				return instance.serializeToNBT();
			}

			@Override
			public void readNBT(Capability<IChrysalisData> capability, IChrysalisData instance, Direction side, INBT nbt) {
				instance.deserializeFromNBT(nbt);
			}
        },
        () -> new ChrysalisData());
	}
	
	@SubscribeEvent
	public static void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof PlayerEntity) {
			event.addCapability(LOCATION, new ICapabilitySerializable<INBT>() {
				private IChrysalisData instance = new ChrysalisData();

				@SuppressWarnings("unchecked")
				@Override
				public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
					if(cap == CAPABILITY) {
						return (LazyOptional<T>)LazyOptional.of(() -> instance);
					}
					return LazyOptional.empty();
				}

				@Override
				public INBT serializeNBT() {
					return instance.serializeToNBT();
				}

				@Override
				public void deserializeNBT(INBT nbt) {
					instance.deserializeFromNBT(nbt);
				}
				
			});
		}
	}
	
	@SubscribeEvent
	public static void onPlayerClone(PlayerEvent.Clone event) {
		event.getOriginal().getCapability(CAPABILITY).ifPresent(originalCap -> {
			event.getPlayer().getCapability(CAPABILITY).ifPresent(newCap -> {
				newCap.deserializeFromNBT(originalCap.serializeToNBT());
			});
		});
	}
}
