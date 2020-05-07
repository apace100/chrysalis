package chrysalis.block.potion_wart;

import java.util.LinkedList;
import java.util.List;

import chrysalis.Chrysalis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

public class PotionWartTileEntity extends TileEntity {
	
	@ObjectHolder(Chrysalis.MODID + ":nether_wart")
	public static TileEntityType<PotionWartTileEntity> TYPE;
	
	private static final float DURATION_MULTIPLIER = 0.05F;
	private List<WartEffect> effects;
	
	private Modifier modifier;
	
	public PotionWartTileEntity() {
		this(TYPE);
	}
	
	public PotionWartTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		this.effects = new LinkedList<WartEffect>();
		this.modifier = Modifier.NONE;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		ListNBT list = (ListNBT)compound.get("Effects");
		effects.clear();
		int count = list.size();
		for(int i = 0; i < count; i++) {
			effects.add(this.readEffect(list.getCompound(i)));
		}
		modifier = Modifier.values()[compound.getInt("Modifier")];
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT comp = super.write(compound);
		ListNBT list = new ListNBT();
		for(WartEffect we : effects) {
			list.add(this.writeEffect(we));
		}
		comp.put("Effects", list);
		comp.putInt("Modifier", modifier.ordinal());
		return comp;
	}

	public boolean hasEffects() {
		return effects != null && effects.size() > 0;
	}
	
	public void applyEffects(Entity entity) {
		if(entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity)entity;
			for(WartEffect e : effects) {
				EffectInstance ei = createInstanceFromEffect(e);
				living.addPotionEffect(ei);
			}
		}
	}
	
	public void setEffects(List<EffectInstance> effects) {
		this.effects.clear();
		for(EffectInstance ei : effects) {
			if(!ei.getPotion().isInstant()) {
				this.effects.add(createEffectFromInstance(ei));
			}
		}
		this.markDirty();
	}
	
	public boolean hasModifier() {
		return modifier != Modifier.NONE;
	}
	
	public void setModifier(Item item) {
		if(item == Items.GLOWSTONE) {
			modifier = Modifier.AMPLIFIER;
		} else
		if(item == Items.REDSTONE_BLOCK) {
			modifier = Modifier.DURATION;
		}
		this.markDirty();
	}
	
	public boolean isModifier(Item item) {
		return item == Items.GLOWSTONE || item == Items.REDSTONE_BLOCK;
	}
	
	private CompoundNBT writeEffect(WartEffect we) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("Effect", we.effect.getRegistryName().toString());
		nbt.putInt("Amplifier", we.amplifier);
		nbt.putInt("Duration", we.duration);
		return nbt;
	}
	
	private WartEffect readEffect(CompoundNBT nbt) {
		WartEffect we = new WartEffect();
		we.effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(nbt.getString("Effect")));
		we.amplifier = nbt.getInt("Amplifier");
		we.duration = nbt.getInt("Duration");
		return we;
	}
	
	private WartEffect createEffectFromInstance(EffectInstance instance) {
		WartEffect we = new WartEffect();
		we.effect = instance.getPotion();
		we.amplifier = instance.getAmplifier();
		we.duration = (int)Math.ceil(instance.getDuration() * DURATION_MULTIPLIER);
		return we;
	}
	
	private EffectInstance createInstanceFromEffect(WartEffect we) {
		int duration = modifier == Modifier.DURATION ? (int)(we.duration * 1.5F) : we.duration;
		int amplifier = modifier == Modifier.AMPLIFIER ? we.amplifier + 1 : we.amplifier;
		return new EffectInstance(we.effect, duration, amplifier);
	}
	
	private class WartEffect {
		public Effect effect;
		public int amplifier;
		public int duration;
	}
	
	private enum Modifier {
		NONE, DURATION, AMPLIFIER
	}
}
