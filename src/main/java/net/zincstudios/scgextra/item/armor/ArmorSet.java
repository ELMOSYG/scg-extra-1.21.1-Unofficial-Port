package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nullable;
import java.util.*;

/**
 * 4.1.0 的护甲套装系统：直接扫描已穿件数，按 full/half traits 应用属性与效果；
 * 每件护甲自己的 partModifiers 由 {@link ArmorSetPartItem} 负责。
 */
public class ArmorSet {

    public static final int TICK_INTERVAL = 10;
    private static final Map<ResourceLocation, Holder<Attribute>> ALL_MODIFIER_ATTRIBUTES = new HashMap<>();

    private final ResourceLocation resLoc;
    private final Traits fullSetTraits;
    private final Traits halfSetTraits;
    private final Map<Holder<Attribute>, AttributeModifier> partModifiers;

    public ArmorSet(ResourceLocation resLoc, Traits fullSetTraits) {
        this(resLoc, fullSetTraits, new Traits(), Map.of());
    }

    public ArmorSet(ResourceLocation resLoc, Traits fullSetTraits, Traits halfSetTraits,
                    Map<Holder<Attribute>, AttributeModifier> partModifiers) {
        this.resLoc = resLoc;
        this.fullSetTraits = fullSetTraits;
        this.halfSetTraits = halfSetTraits;
        this.partModifiers = partModifiers;

        registerModifierHandler(fullSetTraits);
        registerModifierHandler(halfSetTraits);
        for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : partModifiers.entrySet()) {
            ALL_MODIFIER_ATTRIBUTES.put(entry.getValue().id(), entry.getKey());
        }
    }

    private static void registerModifierHandler(Traits traits) {
        for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : traits.modifiers.entrySet()) {
            ALL_MODIFIER_ATTRIBUTES.put(entry.getValue().id(), entry.getKey());
        }
    }

    public void halfSetTick(LivingEntity entity) {
    }

    public void fullSetTick(LivingEntity entity) {
    }

    public ResourceLocation getResourceLocation() {
        return this.resLoc;
    }

    public Traits getFullSetTraits() {
        return this.fullSetTraits;
    }

    public Traits getHalfSetTraits() {
        return this.halfSetTraits;
    }

    public Map<Holder<Attribute>, AttributeModifier> getPartModifiers() {
        return this.partModifiers;
    }

    public static boolean isFullSet(LivingEntity entity, ArmorSet armorSet) {
        return getArmorSetCounts(entity).getOrDefault(armorSet, 0) >= 4;
    }

    public static boolean isHalfSet(LivingEntity entity, ArmorSet armorSet) {
        return getArmorSetCounts(entity).getOrDefault(armorSet, 0) >= 2;
    }

    public static Map<ArmorSet, Integer> getArmorSetCounts(LivingEntity entity) {
        Map<ArmorSet, Integer> counts = new IdentityHashMap<>();
        for (ItemStack stack : entity.getArmorSlots()) {
            if (stack.getItem() instanceof ArmorSetPart part) {
                counts.merge(part.getArmorSet(), 1, Integer::sum);
            }
        }
        return counts;
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        Player player = event.getEntity();
        if (player.tickCount % TICK_INTERVAL != 0) {
            return;
        }
        applyArmorTraits(player);
    }

    public static void applyArmorTraits(LivingEntity entity) {
        Map<ResourceLocation, AttributeModifier> desiredModifiers = new HashMap<>();

        for (Map.Entry<ArmorSet, Integer> entry : getArmorSetCounts(entity).entrySet()) {
            ArmorSet armorSet = entry.getKey();
            int pieces = entry.getValue();

            if (pieces >= 4) {
                for (Map.Entry<Holder<Attribute>, AttributeModifier> modifier : armorSet.fullSetTraits.modifiers.entrySet()) {
                    desiredModifiers.put(modifier.getValue().id(), modifier.getValue());
                }
                for (Map.Entry<Holder<MobEffect>, Integer> effect : armorSet.fullSetTraits.effects.entrySet()) {
                    entity.addEffect(new MobEffectInstance(effect.getKey(), 115, effect.getValue(), true, true));
                }
                for (Holder<MobEffect> resistance : armorSet.fullSetTraits.resistances) {
                    entity.removeEffect(resistance);
                }
                armorSet.fullSetTick(entity);
            }

            if (pieces >= 2) {
                for (Map.Entry<Holder<Attribute>, AttributeModifier> modifier : armorSet.halfSetTraits.modifiers.entrySet()) {
                    desiredModifiers.put(modifier.getValue().id(), modifier.getValue());
                }
                for (Map.Entry<Holder<MobEffect>, Integer> effect : armorSet.halfSetTraits.effects.entrySet()) {
                    entity.addEffect(new MobEffectInstance(effect.getKey(), 115, effect.getValue(), true, true));
                }
                for (Holder<MobEffect> resistance : armorSet.halfSetTraits.resistances) {
                    entity.removeEffect(resistance);
                }
                armorSet.halfSetTick(entity);
            }
        }

        for (Map.Entry<ResourceLocation, Holder<Attribute>> entry : ALL_MODIFIER_ATTRIBUTES.entrySet()) {
            ResourceLocation id = entry.getKey();
            AttributeInstance attribute = entity.getAttribute(entry.getValue());
            if (attribute == null) {
                continue;
            }
            boolean shouldHave = desiredModifiers.containsKey(id);
            boolean hasModifier = attribute.getModifier(id) != null;
            if (shouldHave && !hasModifier) {
                attribute.addTransientModifier(desiredModifiers.get(id));
            } else if (!shouldHave && hasModifier) {
                attribute.removeModifier(id);
            }
        }
    }

    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }
        Holder<MobEffect> effect = event.getEffectInstance().getEffect();
        for (Map.Entry<ArmorSet, Integer> entry : getArmorSetCounts(player).entrySet()) {
            ArmorSet armorSet = entry.getKey();
            int pieces = entry.getValue();
            if (pieces >= 4 && armorSet.fullSetTraits.resistances.contains(effect)) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
            if (pieces >= 2 && armorSet.halfSetTraits.resistances.contains(effect)) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    public static class Traits {
        private final Map<Holder<Attribute>, AttributeModifier> modifiers = new HashMap<>();
        private final Map<Holder<MobEffect>, Integer> effects = new HashMap<>();
        private final Set<Holder<MobEffect>> resistances = new HashSet<>();

        public Traits modifier(Holder<Attribute> attribute, AttributeModifier modifier) {
            this.modifiers.put(attribute, modifier);
            return this;
        }

        public Traits effect(Holder<MobEffect> effect, int amplifier) {
            this.effects.put(effect, amplifier);
            return this;
        }

        public Traits resistance(Holder<MobEffect> effect) {
            this.resistances.add(effect);
            return this;
        }
    }
}
