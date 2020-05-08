package chrysalis.potion;

import chrysalis.Attributes;
import chrysalis.Chrysalis;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
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
	
	@ObjectHolder(Chrysalis.MODID + ":haste")
	public static Potion HASTE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_haste")
	public static Potion LONG_HASTE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_haste")
	public static Potion STRONG_HASTE_POTION;

	@ObjectHolder(Chrysalis.MODID + ":mining_fatigue")
	public static Potion MINING_FATIGUE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_mining_fatigue")
	public static Potion LONG_MINING_FATIGUE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_mining_fatigue")
	public static Potion STRONG_MINING_FATIGUE_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":nausea")
	public static Potion NAUSEA_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_nausea")
	public static Potion LONG_NAUSEA_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":blindness")
	public static Potion BLINDNESS_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_blindness")
	public static Potion LONG_BLINDNESS_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_blindness")
	public static Potion STRONG_BLINDNESS_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":hunger")
	public static Potion HUNGER_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_hunger")
	public static Potion LONG_HUNGER_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_hunger")
	public static Potion STRONG_HUNGER_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":wither")
	public static Potion WITHER_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_wither")
	public static Potion LONG_WITHER_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_wither")
	public static Potion STRONG_WITHER_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":absorption")
	public static Potion ABSORPTION_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_absorption")
	public static Potion LONG_ABSORPTION_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_absorption")
	public static Potion STRONG_ABSORPTION_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":glowing")
	public static Potion GLOWING_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_glowing")
	public static Potion LONG_GLOWING_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":levitation")
	public static Potion LEVITATION_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":long_levitation")
	public static Potion LONG_LEVITATION_POTION;
	
	@ObjectHolder(Chrysalis.MODID + ":strong_levitation")
	public static Potion STRONG_LEVITATION_POTION;

	public static void registerEffects(IForgeRegistry<Effect> registry) {
		registry.register(new EffectDeathTeleport().setRegistryName("death_teleport"));
		registry.register(new EffectAttribute(EffectType.BENEFICIAL, 0xB58D1F).addAttributesModifier(Attributes.CRIT_CHANCE, "5BDD6A9B-9107-447B-9AD0-7538D22CB44C", 0.4, Operation.ADDITION).setRegistryName("crit_chance"));
		registry.register(new EffectAttribute(EffectType.BENEFICIAL, 0x1F8FB8).addAttributesModifier(Attributes.CRIT_DAMAGE, "DC4D3C87-6EF3-489A-82E7-2D1EC04F5176", 0.5, Operation.ADDITION).setRegistryName("crit_damage"));
	}
	
	public static void registerPotions(IForgeRegistry<Potion> registry) {
		registry.register(new Potion(new EffectInstance(DEATH_TELEPORT_EFFECT)).setRegistryName("death_teleport"));
		
		registry.register(new Potion(new EffectInstance(CRIT_CHANCE_EFFECT, 3600)).setRegistryName("crit_chance"));
		registry.register(new Potion(new EffectInstance(CRIT_CHANCE_EFFECT, 7200)).setRegistryName("long_crit_chance"));
		registry.register(new Potion(new EffectInstance(CRIT_CHANCE_EFFECT, 1800, 1)).setRegistryName("strong_crit_chance"));
		
		registry.register(new Potion(new EffectInstance(CRIT_DAMAGE_EFFECT, 3600)).setRegistryName("crit_damage"));
		registry.register(new Potion(new EffectInstance(CRIT_DAMAGE_EFFECT, 7200)).setRegistryName("long_crit_damage"));
		registry.register(new Potion(new EffectInstance(CRIT_DAMAGE_EFFECT, 1800, 1)).setRegistryName("strong_crit_damage"));
		
		registry.register(new Potion(new EffectInstance(Effects.HASTE, 3600)).setRegistryName("haste"));
		registry.register(new Potion(new EffectInstance(Effects.HASTE, 7200)).setRegistryName("long_haste"));
		registry.register(new Potion(new EffectInstance(Effects.HASTE, 1800, 1)).setRegistryName("strong_haste"));
		
		registry.register(new Potion(new EffectInstance(Effects.MINING_FATIGUE, 2400)).setRegistryName("mining_fatigue"));
		registry.register(new Potion(new EffectInstance(Effects.MINING_FATIGUE, 4800)).setRegistryName("long_mining_fatigue"));
		registry.register(new Potion(new EffectInstance(Effects.MINING_FATIGUE, 1600, 1)).setRegistryName("strong_mining_fatigue"));
		
		registry.register(new Potion(new EffectInstance(Effects.NAUSEA, 2400)).setRegistryName("nausea"));
		registry.register(new Potion(new EffectInstance(Effects.NAUSEA, 4800)).setRegistryName("long_nausea"));
		
		registry.register(new Potion(new EffectInstance(Effects.BLINDNESS, 2400)).setRegistryName("blindness"));
		registry.register(new Potion(new EffectInstance(Effects.BLINDNESS, 4800)).setRegistryName("long_blindness"));
		registry.register(new Potion(new EffectInstance(Effects.BLINDNESS, 1600, 1)).setRegistryName("strong_blindness"));
		
		registry.register(new Potion(new EffectInstance(Effects.HUNGER, 2400)).setRegistryName("hunger"));
		registry.register(new Potion(new EffectInstance(Effects.HUNGER, 4800)).setRegistryName("long_hunger"));
		registry.register(new Potion(new EffectInstance(Effects.HUNGER, 1600, 1)).setRegistryName("strong_hunger"));
		
		registry.register(new Potion(new EffectInstance(Effects.WITHER, 1600)).setRegistryName("wither"));
		registry.register(new Potion(new EffectInstance(Effects.WITHER, 3200)).setRegistryName("long_wither"));
		registry.register(new Potion(new EffectInstance(Effects.WITHER, 800, 1)).setRegistryName("strong_wither"));
		
		registry.register(new Potion(new EffectInstance(Effects.ABSORPTION, 3600)).setRegistryName("absorption"));
		registry.register(new Potion(new EffectInstance(Effects.ABSORPTION, 7200)).setRegistryName("long_absorption"));
		registry.register(new Potion(new EffectInstance(Effects.ABSORPTION, 1800, 1)).setRegistryName("strong_absorption"));

		registry.register(new Potion(new EffectInstance(Effects.GLOWING, 3600)).setRegistryName("glowing"));
		registry.register(new Potion(new EffectInstance(Effects.GLOWING, 7200)).setRegistryName("long_glowing"));
		
		registry.register(new Potion(new EffectInstance(Effects.LEVITATION, 2400)).setRegistryName("levitation"));
		registry.register(new Potion(new EffectInstance(Effects.LEVITATION, 4800)).setRegistryName("long_levitation"));
		registry.register(new Potion(new EffectInstance(Effects.LEVITATION, 1600, 1)).setRegistryName("strong_levitation"));
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
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.SWEET_BERRIES, HASTE_POTION);
		PotionBrewing.addMix(HASTE_POTION, Items.REDSTONE, LONG_HASTE_POTION);
		PotionBrewing.addMix(HASTE_POTION, Items.GLOWSTONE_DUST, STRONG_HASTE_POTION);

		PotionBrewing.addMix(HASTE_POTION, Items.FERMENTED_SPIDER_EYE, MINING_FATIGUE_POTION);
		PotionBrewing.addMix(LONG_HASTE_POTION, Items.FERMENTED_SPIDER_EYE, LONG_MINING_FATIGUE_POTION);
		PotionBrewing.addMix(STRONG_HASTE_POTION, Items.FERMENTED_SPIDER_EYE, STRONG_MINING_FATIGUE_POTION);
		PotionBrewing.addMix(MINING_FATIGUE_POTION, Items.REDSTONE, LONG_MINING_FATIGUE_POTION);
		PotionBrewing.addMix(MINING_FATIGUE_POTION, Items.GLOWSTONE_DUST, STRONG_MINING_FATIGUE_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.CHORUS_FRUIT, NAUSEA_POTION);
		PotionBrewing.addMix(NAUSEA_POTION, Items.REDSTONE, LONG_NAUSEA_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.POISONOUS_POTATO, BLINDNESS_POTION);
		PotionBrewing.addMix(BLINDNESS_POTION, Items.REDSTONE, LONG_BLINDNESS_POTION);
		PotionBrewing.addMix(BLINDNESS_POTION, Items.GLOWSTONE_DUST, STRONG_BLINDNESS_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.ROTTEN_FLESH, HUNGER_POTION);
		PotionBrewing.addMix(HUNGER_POTION, Items.REDSTONE, LONG_HUNGER_POTION);
		PotionBrewing.addMix(HUNGER_POTION, Items.GLOWSTONE_DUST, STRONG_HUNGER_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.WITHER_ROSE, WITHER_POTION);
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.WITHER_SKELETON_SKULL, WITHER_POTION);
		PotionBrewing.addMix(WITHER_POTION, Items.REDSTONE, LONG_WITHER_POTION);
		PotionBrewing.addMix(WITHER_POTION, Items.GLOWSTONE_DUST, STRONG_WITHER_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.SPONGE, ABSORPTION_POTION);
		PotionBrewing.addMix(ABSORPTION_POTION, Items.REDSTONE, LONG_ABSORPTION_POTION);
		PotionBrewing.addMix(ABSORPTION_POTION, Items.GLOWSTONE_DUST, STRONG_ABSORPTION_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.GLOWSTONE, GLOWING_POTION);
		PotionBrewing.addMix(GLOWING_POTION, Items.REDSTONE, LONG_GLOWING_POTION);
		
		PotionBrewing.addMix(net.minecraft.potion.Potions.AWKWARD, Items.SHULKER_SHELL, LEVITATION_POTION);
		PotionBrewing.addMix(LEVITATION_POTION, Items.REDSTONE, LONG_LEVITATION_POTION);
		PotionBrewing.addMix(LEVITATION_POTION, Items.GLOWSTONE_DUST, STRONG_LEVITATION_POTION);
	}
}
