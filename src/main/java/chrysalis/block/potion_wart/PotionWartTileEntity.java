package chrysalis.block.potion_wart;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import chrysalis.Chrysalis;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
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
	
	private UUID ownerUUID;
	private List<WartEffect> effects;
	
	private PotionModifier potionModifier;
	private Set<BlockModifier> blockModifiers;
	
	public PotionWartTileEntity() {
		this(TYPE);
	}
	
	public PotionWartTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
		this.effects = new LinkedList<WartEffect>();
		this.potionModifier = PotionModifier.NONE;
		this.blockModifiers = new HashSet<>();
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		ownerUUID = compound.getUniqueId("Owner");
		ListNBT list = (ListNBT)compound.get("Effects");
		effects.clear();
		int count = list.size();
		for(int i = 0; i < count; i++) {
			effects.add(this.readEffect(list.getCompound(i)));
		}
		potionModifier = PotionModifier.values()[compound.getInt("PotionModifier")];
		blockModifiers.clear();
		IntArrayNBT bms = (IntArrayNBT)compound.get("BlockModifiers");
		count = bms.size();
		for(int i = 0; i < count; i++) {
			blockModifiers.add(BlockModifier.values()[bms.get(i).getInt()]);
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT comp = super.write(compound);
		comp.putUniqueId("Owner", ownerUUID);
		ListNBT list = new ListNBT();
		for(WartEffect we : effects) {
			list.add(this.writeEffect(we));
		}
		comp.put("Effects", list);
		comp.putInt("PotionModifier", potionModifier.ordinal());
		int[] bmIds = new int[blockModifiers.size()];
		int index = 0;
		for(BlockModifier bm : blockModifiers) {
			bmIds[index++] = bm.ordinal();
		}
		comp.put("BlockModifiers", new IntArrayNBT(bmIds));
		return comp;
	}
	
	public boolean isOwner(PlayerEntity player) {
		System.out.println("Owner: " + ownerUUID.toString());
		System.out.println("Player: " + player.getUniqueID().toString());
		return player.getUniqueID().equals(ownerUUID);
	}
	
	public void setOwner(PlayerEntity player) {
		this.ownerUUID = player.getUniqueID();
		markDirty();
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
	
	public void clearEffects() {
		this.effects.clear();
		this.markDirty();
	}
	
	public boolean hasPotionModifier() {
		return potionModifier != PotionModifier.NONE;
	}
	
	public boolean hasBlockModifier(BlockModifier blockMod) {
		return blockModifiers.contains(blockMod);
	}
	
	public boolean isValidModifier(PlayerEntity player, Item item) {
		if(item == Items.GLOWSTONE || item == Items.REDSTONE_BLOCK) {
			return !hasPotionModifier();
		}
		if(item == Items.OBSIDIAN) {
			return !hasBlockModifier(BlockModifier.BLAST_RESISTANT);
		}
		return item == Items.MILK_BUCKET && isOwner(player) && hasEffects();
	}
	
	public void addModifier(Item item) {
		if(!hasPotionModifier()) {
			if(item == Items.GLOWSTONE) {
				potionModifier = PotionModifier.AMPLIFIER;
			} else
			if(item == Items.REDSTONE_BLOCK) {
				potionModifier = PotionModifier.DURATION;
			}
		}
		if(item == Items.OBSIDIAN) {
			blockModifiers.add(BlockModifier.BLAST_RESISTANT);
		}
		if(item == Items.MILK_BUCKET) {
			this.clearEffects();
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
		int duration = potionModifier == PotionModifier.DURATION ? (int)(we.duration * 1.5F) : we.duration;
		int amplifier = potionModifier == PotionModifier.AMPLIFIER ? we.amplifier + 1 : we.amplifier;
		return new EffectInstance(we.effect, duration, amplifier);
	}
	
	private class WartEffect {
		public Effect effect;
		public int amplifier;
		public int duration;
	}
	
	private enum PotionModifier {
		NONE, DURATION, AMPLIFIER
	}
	
	public enum BlockModifier {
		BLAST_RESISTANT
	}
}
