package chrysalis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Attributes {
	
	private static final Map<Class<? extends LivingEntity>, List<IAttribute>> attributeMap = new HashMap<>();
	
	public static final IAttribute CRIT_CHANCE = (new RangedAttribute((IAttribute)null, "generic.crit_chance", 0.0D, 0.0D, 1.0D)).setDescription("Critical Strike Chance (when not falling)").setShouldWatch(true);
	public static final IAttribute CRIT_DAMAGE = (new RangedAttribute((IAttribute)null, "generic.crit_damage", 1.5D, 0.0D, 1024.0D)).setDescription("Critical Strike Damage Multiplier").setShouldWatch(true);
	public static final IAttribute XP_GAIN = (new RangedAttribute((IAttribute)null, "generic.xp_gain", 1.0D, 0.0D, 1024.0D).setDescription("Gained XP Multiplier").setShouldWatch(true));
	
	public static void registerAll() {
		register(PlayerEntity.class, CRIT_CHANCE);
		register(PlayerEntity.class, CRIT_DAMAGE);
		register(PlayerEntity.class, XP_GAIN);
	}
	
	public static void register(Class<? extends LivingEntity> entityClass, IAttribute attribute) {
		List<IAttribute> list = null;
		if(!attributeMap.containsKey(entityClass)) {
			list = new LinkedList<IAttribute>();
			attributeMap.put(entityClass, list);
		} else {
			list = attributeMap.get(entityClass);
		}
		list.add(attribute);
	}
	
	@SubscribeEvent
	public static void onEntityConstruct(EntityEvent.EntityConstructing event) {
		if(event.getEntity() instanceof LivingEntity) {
			LivingEntity ent = (LivingEntity)event.getEntity();
			attributeMap.forEach((clz, atrList) -> {
				if(clz.isAssignableFrom(event.getEntity().getClass())) {
					atrList.forEach(atr -> ent.getAttributes().registerAttribute(atr));
				}
			});
		}
	}
}
