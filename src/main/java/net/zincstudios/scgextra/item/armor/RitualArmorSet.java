package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.zincstudios.scgextra.effects.ModEffects;

import java.util.Map;

public class RitualArmorSet extends ArmorSet {

    public static final int COOLDOWN_DURATION = 24000;

    public RitualArmorSet(ResourceLocation resLoc, Traits fullSetTraits, Traits halfSetTraits,
                          Map<Holder<Attribute>, AttributeModifier> partModifiers) {
        super(resLoc, fullSetTraits, halfSetTraits, partModifiers);
    }

    @Override
    public void fullSetTick(LivingEntity entity) {
        int cooldownTicks = (int) (getReviveCooldownEnd(entity) - entity.level().getGameTime());
        MobEffectInstance effectInstance = entity.getEffect(ModEffects.MORTAL);

        if (cooldownTicks > 0) {
            if (effectInstance == null || effectInstance.getDuration() != cooldownTicks) {
                entity.addEffect(new MobEffectInstance(ModEffects.MORTAL, cooldownTicks, 0, true, true));
            }
        } else if (effectInstance != null) {
            entity.removeEffect(ModEffects.MORTAL);
        }
    }

    public static void setReviveCooldown(LivingEntity entity, long cooldownDuration) {
        CompoundTag tag = entity.getPersistentData();
        tag.putLong("ReviveCooldownEnd", entity.level().getGameTime() + cooldownDuration);
    }

    public static long getReviveCooldownEnd(LivingEntity entity) {
        CompoundTag tag = entity.getPersistentData();
        if (!tag.contains("ReviveCooldownEnd")) {
            return 0L;
        }
        return tag.getLong("ReviveCooldownEnd");
    }
}
