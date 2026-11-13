package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.zincstudios.scgextra.attributes.SCGEAttributes;

import javax.annotation.Nullable;
import java.util.*;

public class ArmorSet {

    public static final int TICK_INTERVAL = 10;
    private static final Map<ResourceLocation, Holder<Attribute>> ALL_MODIFIER_ATTRIBUTES = new HashMap<>();

    private final ResourceLocation resLoc;
    private final Map<Holder<Attribute>, AttributeModifier> modifiers;
    private final Map<Holder<MobEffect>, Integer> effects;
    private final Set<Holder<MobEffect>> resistances;

    public ArmorSet(ResourceLocation resLoc, ArmorSet.Traits traits) {
        this.resLoc = resLoc;
        this.modifiers = Map.copyOf(traits.modifiers);
        this.effects = Map.copyOf(traits.effects);
        this.resistances = Set.copyOf(traits.resistances);
        for (Map.Entry<Holder<Attribute>, AttributeModifier> e : traits.modifiers.entrySet()) {
            ALL_MODIFIER_ATTRIBUTES.put(e.getValue().id(), e.getKey());
        }
    }

    // Only ticks every interval and on server side
    public void onTick(LivingEntity entity) {
        Map<ResourceLocation, AttributeModifier> desiredModifiers = new HashMap<>();

        ArmorSet armorSet = getArmorSet(entity);
        if (armorSet != null) {
            for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : armorSet.modifiers.entrySet()) {
                desiredModifiers.put(entry.getValue().id(), entry.getValue());
            }
            for (Map.Entry<Holder<MobEffect>, Integer> entry : armorSet.effects.entrySet()) {
                entity.addEffect(new MobEffectInstance(entry.getKey(), 115, entry.getValue(), true, true));
            }
            for (Holder<MobEffect> effect : armorSet.resistances) {
                entity.removeEffect(effect);
            }
        }

        for (Map.Entry<ResourceLocation, Holder<Attribute>> entry : ALL_MODIFIER_ATTRIBUTES.entrySet()) {
            ResourceLocation id = entry.getKey();
            AttributeInstance attr = entity.getAttribute(entry.getValue());
            if (attr == null) continue;

            boolean shouldHave = desiredModifiers.containsKey(id);
            boolean hasModifier = attr.getModifier(id) != null;

            if (shouldHave && !hasModifier) {
                attr.addTransientModifier(desiredModifiers.get(id));
            } else if (!shouldHave && hasModifier) {
                attr.removeModifier(id);
            }
        }
    }

    public ResourceLocation getResourceLocation() {
        return this.resLoc;
    }

    @Nullable
    public static ArmorSet getArmorSet(LivingEntity entity) {
        if (!(entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ArmorSetPart headPart)) return null;
        if (!(entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ArmorSetPart chestPart)) return null;
        if (!(entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof ArmorSetPart legPart)) return null;
        if (!(entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof ArmorSetPart feetPart)) return null;

        ArmorSet set = headPart.getArmorSet();
        if (set == chestPart.getArmorSet() && set == legPart.getArmorSet() && set == feetPart.getArmorSet()) return set;
        return null;
    }

    // TODO: no interval check, maybe cache result.
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();

        if (player.hasEffect(MobEffects.LEVITATION) && player.getAttribute(SCGEAttributes.PIONEER) != null
                && player.getAttribute(SCGEAttributes.PIONEER).getValue() >= 1.0) {
            player.removeEffect(MobEffects.LEVITATION);
        }

        if (player.tickCount % TICK_INTERVAL != 0) return;

        if (player.getAttribute(SCGEAttributes.GOLDEN_IDOL) != null && player.getAttribute(SCGEAttributes.GOLDEN_IDOL).getValue() >= 1.0) {
            List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5), e -> !(e instanceof Enemy));
            for (LivingEntity le : list) {
                le.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100));
                le.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100));
            }
        }
        if (player.getAttribute(SCGEAttributes.DEBUFF_ZONE) != null && player.getAttribute(SCGEAttributes.DEBUFF_ZONE).getValue() >= 1.0) {
            List<LivingEntity> list = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(10), e -> e instanceof Enemy);
            for (LivingEntity le : list) {
                le.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            }
        }

        ArmorSet armorSet = getArmorSet(player);
        if (armorSet == null) return;

        armorSet.onTick(player);
    }

    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;
        ArmorSet armorSet = getArmorSet(player);
        if (armorSet == null) return;

        if (armorSet.resistances.contains(event.getEffectInstance().getEffect())) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    // TODO: Consider combining with MedalItem.Traits to a separate class along with handling
    public static class Traits {
        private final Map<Holder<Attribute>, AttributeModifier> modifiers = new HashMap<>();
        private final Map<Holder<MobEffect>, Integer> effects = new HashMap<>();
        private final Set<Holder<MobEffect>> resistances = new HashSet<>();

        public ArmorSet.Traits modifier(Holder<Attribute> attribute, AttributeModifier modifier) {
            this.modifiers.put(attribute, modifier);
            return this;
        }

        public ArmorSet.Traits effect(Holder<MobEffect> effect, int amplifier) {
            this.effects.put(effect, amplifier);
            return this;
        }

        public ArmorSet.Traits resistance(Holder<MobEffect> effect) {
            this.resistances.add(effect);
            return this;
        }
    }
}
