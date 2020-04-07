package chrysalis.potion;

import chrysalis.Attributes;
import chrysalis.Chrysalis;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class Potions {
	
	@ObjectHolder(Chrysalis.MODID + ":death_teleport")
	public static Effect DEATH_TELEPORT_EFFECT;
	
	@ObjectHolder(Chrysalis.MODID + ":death_teleport")
	public static Potion DEATH_TELEPORT_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":crit_chance")
	public static Effect CRIT_CHANCE_EFFECT;
	
	@ObjectHolder(Chrysalis.MODID + ":crit_damage")
	public static Effect CRIT_DAMAGE_EFFECT;
	
	@ObjectHolder(Chrysalis.MODID + ":crit_chance")
	public static Potion CRIT_CHANCE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_crit_chance")
	public static Potion LONG_CRIT_CHANCE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_crit_chance")
	public static Potion STRONG_CRIT_CHANCE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":crit_damage")
	public static Potion CRIT_DAMAGE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_crit_damage")
	public static Potion LONG_CRIT_DAMAGE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_crit_damage")
	public static Potion STRONG_CRIT_DAMAGE_POTION;

	public static void registerEffects(IForgeRegistry<Effect> registry) {
		registry.register(new EffectDeathTeleport().setRegistryName("death_teleport"));
		registry.register(new EffectAttribute(EffectType.BENEFICIAL, 0xB58D1F).addAttributesModifier(Attributes.CRIT_CHANCE, "5BDD6A9B-9107-447B-9AD0-7538D22CB44C", 0.4, Operation.ADDITION).setRegistryName("crit_chance"));
//		registry.register(new EffectCritChance(EffectType.BENEFICIAL, 0xB58D1F).setRegistryName("crit_chance"));
		registry.register(new EffectAttribute(EffectType.BENEFICIAL, 0x1F8FB8).addAttributesModifier(Attributes.CRIT_DAMAGE, "DC4D3C87-6EF3-489A-82E7-2D1EC04F5176", 0.5, Operation.ADDITION).setRegistryName("crit_damage"));
//		registry.register(new EffectCritDamage(EffectType.BENEFICIAL, 0x1F8FB8).setRegistryName("crit_damage"));
	}
	
	public static void registerPotions(IForgeRegistry<Potion> registry) {
		registry.register(new Potion(new EffectInstance(DEATH_TELEPORT_EFFECT)).setRegistryName("death_teleport"));
		registry.register(new Potion(new EffectInstance(CRIT_CHANCE_EFFECT, 3600)).setRegistryName("crit_chance"));
		registry.register(new Potion(new EffectInstance(CRIT_CHANCE_EFFECT, 7200)).setRegistryName("long_crit_chance"));
		registry.register(new Potion(new EffectInstance(CRIT_CHANCE_EFFECT, 1800, 1)).setRegistryName("strong_crit_chance"));
		registry.register(new Potion(new EffectInstance(CRIT_DAMAGE_EFFECT, 3600)).setRegistryName("crit_damage"));
		registry.register(new Potion(new EffectInstance(CRIT_DAMAGE_EFFECT, 7200)).setRegistryName("long_crit_damage"));
		registry.register(new Potion(new EffectInstance(CRIT_DAMAGE_EFFECT, 1800, 1)).setRegistryName("strong_crit_damage"));
	}
	
	public static void registerRecipes() {
		PotionBrewing.addMix(net.minecraft.potion.Potions.SLOW_FALLING, Items.FERMENTED_SPIDER_EYE, DEATH_TELEPORT_POTION);
		PotionBrewing.addMix(net.minecraft.potion.Potions.LONG_SLOW_FALLING, Items.FERMENTED_SPIDER_EYE, DEATH_TELEPORT_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.BONE, CRIT_CHANCE_POTION);
		PotionBrewing.addMix(CRIT_CHANCE_POTION, Items.REDSTONE, LONG_CRIT_CHANCE_POTION);
		PotionBrewing.addMix(CRIT_CHANCE_POTION, Items.GLOWSTONE_DUST, STRONG_CRIT_CHANCE_POTION);
		
		PotionBrewing.addMix(CRIT_CHANCE_POTION, Items.FERMENTED_SPIDER_EYE, CRIT_DAMAGE_POTION);
		PotionBrewing.addMix(LONG_CRIT_CHANCE_POTION, Items.FERMENTED_SPIDER_EYE, LONG_CRIT_DAMAGE_POTION);
		PotionBrewing.addMix(STRONG_CRIT_CHANCE_POTION, Items.FERMENTED_SPIDER_EYE, STRONG_CRIT_DAMAGE_POTION);
		PotionBrewing.addMix(CRIT_DAMAGE_POTION, Items.REDSTONE, LONG_CRIT_DAMAGE_POTION);
		PotionBrewing.addMix(CRIT_DAMAGE_POTION, Items.GLOWSTONE_DUST, STRONG_CRIT_DAMAGE_POTION);
	}
}
