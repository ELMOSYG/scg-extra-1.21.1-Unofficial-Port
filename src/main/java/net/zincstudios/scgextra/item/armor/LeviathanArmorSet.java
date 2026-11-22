package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import top.ribs.scguns.init.ModEffects;

import java.util.List;
import java.util.Map;

public class LeviathanArmorSet extends ArmorSet {

    public static final List<Holder<MobEffect>> DEBUFF_LIST = List.of(
            MobEffects.POISON, MobEffects.WITHER, MobEffects.BLINDNESS, MobEffects.CONFUSION,
            MobEffects.MOVEMENT_SLOWDOWN, ModEffects.LACERATED, ModEffects.SULFUR_POISONING);

    public LeviathanArmorSet(ResourceLocation resLoc, Traits fullSetTraits, Traits halfSetTraits,
                             Map<Holder<Attribute>, AttributeModifier> partModifiers) {
        super(resLoc, fullSetTraits, halfSetTraits, partModifiers);
    }

    @Override
    public void halfSetTick(LivingEntity entity) {
        if (entity.level().getGameTime() > entity.getPersistentData().getLong("CleanseCooldownEnd")) {
            for (Holder<MobEffect> effect : DEBUFF_LIST) {
                if (entity.hasEffect(effect)) {
                    entity.removeEffect(effect);
                    break;
                }
            }
            entity.getPersistentData().putLong("CleanseCooldownEnd", entity.level().getGameTime() + 200L);
        }
    }
}
