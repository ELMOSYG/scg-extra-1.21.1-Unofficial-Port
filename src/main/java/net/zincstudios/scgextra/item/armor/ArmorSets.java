package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.attributes.SCGEAttributes;
import top.ribs.scguns.attributes.SCAttributes;
import top.ribs.scguns.init.ModEffects;

import java.util.Map;

/**
 * 4.1.0 的九套护甲定义。数值均为小数：0.1 = 10%。
 */
public final class ArmorSets {

    public static final ArmorSet OPPRESSOR = new ArmorSet(SCGExtra.asResource("oppressor"),
            new ArmorSet.Traits().modifier(SCGEAttributes.UNDEAD_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:oppressor_full_undead_damage"), 0.1D, AttributeModifier.Operation.ADD_VALUE)),
            new ArmorSet.Traits().modifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ResourceLocation.parse("scgextra:oppressor_half_knockback_res"), 1.0D, AttributeModifier.Operation.ADD_VALUE)),
            Map.of(SCGEAttributes.UNDEAD_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:oppressor_part_undead_damage"), 0.05D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet COMMISSAR = new ArmorSet(SCGExtra.asResource("commissar"),
            new ArmorSet.Traits().modifier(SCAttributes.RELOAD_SPEED, new AttributeModifier(ResourceLocation.parse("scgextra:commissar_full_reload_speed"), 0.2D, AttributeModifier.Operation.ADD_VALUE)),
            new ArmorSet.Traits().modifier(SCGEAttributes.BULLET_DAMAGE_RESISTANCE, new AttributeModifier(ResourceLocation.parse("scgextra:commissar_half_bullet_resistance"), 0.1D, AttributeModifier.Operation.ADD_VALUE)),
            Map.of(SCAttributes.RELOAD_SPEED, new AttributeModifier(ResourceLocation.parse("scgextra:commissar_part_reload_speed"), 0.05D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet WRECKER = new ArmorSet(SCGExtra.asResource("wrecker"),
            new ArmorSet.Traits()
                    .modifier(SCGEAttributes.BULLET_DAMAGE_RESISTANCE, new AttributeModifier(ResourceLocation.parse("scgextra:wrecker_full_bullet_resistance"), -0.2D, AttributeModifier.Operation.ADD_VALUE))
                    .modifier(SCAttributes.BULLET_DAMAGE_MULTIPLIER, new AttributeModifier(ResourceLocation.parse("scgextra:wrecker_full_bullet_outgoing"), 0.2D, AttributeModifier.Operation.ADD_VALUE)),
            new ArmorSet.Traits(),
            Map.of(SCAttributes.BULLET_DAMAGE_MULTIPLIER, new AttributeModifier(ResourceLocation.parse("scgextra:wrecker_part_bullet_outgoing"), 0.05D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet LEVIATHAN = new LeviathanArmorSet(SCGExtra.asResource("leviathan"),
            new ArmorSet.Traits().effect(MobEffects.WATER_BREATHING, 0).effect(MobEffects.DOLPHINS_GRACE, 0),
            new ArmorSet.Traits(),
            Map.of(SCGEAttributes.MAGIC_DAMAGE_RESISTANCE, new AttributeModifier(ResourceLocation.parse("scgextra:leviathan_part_magic_resistance"), 0.1D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet GOLDEN_IDOL = new GoldenIdolArmorSet(SCGExtra.asResource("golden_idol"),
            new ArmorSet.Traits(),
            new ArmorSet.Traits().effect(MobEffects.FIRE_RESISTANCE, 0),
            Map.of(SCGEAttributes.MAGIC_DAMAGE_RESISTANCE, new AttributeModifier(ResourceLocation.parse("scgextra:golden_idol_part_magic_resistance"), 0.1D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet ENLIGHTENED = new EnlightenedArmorSet(SCGExtra.asResource("enlightened"),
            new ArmorSet.Traits(),
            new ArmorSet.Traits().effect(MobEffects.REGENERATION, 0),
            Map.of(Attributes.MAX_HEALTH, new AttributeModifier(ResourceLocation.parse("scgextra:enlightened_part_max_health"), 2.0D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet JUGGERNAUT = new ArmorSet(SCGExtra.asResource("juggernaut"),
            new ArmorSet.Traits()
                    .modifier(SCGEAttributes.RECOIL_REDUCTION, new AttributeModifier(ResourceLocation.parse("scgextra:juggernaut_full_recoil"), 0.1D, AttributeModifier.Operation.ADD_VALUE))
                    .modifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.parse("scgextra:juggernaut_full_speed"), -0.01D, AttributeModifier.Operation.ADD_VALUE)),
            new ArmorSet.Traits().resistance(ModEffects.LACERATED),
            Map.of(SCGEAttributes.RECOIL_REDUCTION, new AttributeModifier(ResourceLocation.parse("scgextra:juggernaut_part_recoil"), 0.05D, AttributeModifier.Operation.ADD_VALUE),
                    Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.parse("scgextra:juggernaut_part_speed"), -0.005D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet RITUAL = new RitualArmorSet(SCGExtra.asResource("ritual"),
            new ArmorSet.Traits(), new ArmorSet.Traits(),
            Map.of(SCAttributes.BULLET_DAMAGE_MULTIPLIER, new AttributeModifier(ResourceLocation.parse("scgextra:ritual_part_bullet_outgoing"), 0.025D, AttributeModifier.Operation.ADD_VALUE)));

    public static final ArmorSet PIONEER = new ArmorSet(SCGExtra.asResource("pioneer"),
            new ArmorSet.Traits()
                    .modifier(SCGEAttributes.BULLET_ADDITIONAL_CRIT_CHANCE, new AttributeModifier(ResourceLocation.parse("scgextra:pioneer_full_crit_chance"), 0.1D, AttributeModifier.Operation.ADD_VALUE))
                    .modifier(SCGEAttributes.BULLET_ADDITIONAL_CRIT_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:pioneer_full_crit_damage"), 0.1D, AttributeModifier.Operation.ADD_VALUE)),
            new ArmorSet.Traits().resistance(MobEffects.LEVITATION),
            Map.of(SCGEAttributes.BULLET_ADDITIONAL_CRIT_DAMAGE_MULT, new AttributeModifier(ResourceLocation.parse("scgextra:pioneer_part_crit_damage"), 0.05D, AttributeModifier.Operation.ADD_VALUE)));

    public static void onEntityHurt(LivingDamageEvent.Pre event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Map<ArmorSet, Integer> counts = ArmorSet.getArmorSetCounts(entity);

        if (counts.getOrDefault(WRECKER, 0) >= 2
                && event.getSource().getEntity() instanceof LivingEntity
                && !entity.getType().is(top.ribs.scguns.init.ModTags.Entities.CANNOT_BE_LACERATED)) {
            entity.addEffect(new MobEffectInstance(ModEffects.LACERATED, 120));
        }
        if (counts.getOrDefault(ENLIGHTENED, 0) >= 4
                && entity.level().getGameTime() > entity.getPersistentData().getLong("EnlightenedRegenCooldownEnd")) {
            entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1));
            CompoundTag tag = entity.getPersistentData();
            tag.putLong("EnlightenedRegenCooldownEnd", entity.level().getGameTime() + 60L);
        }
        if (counts.getOrDefault(RITUAL, 0) >= 4
                && entity.level().getGameTime() > entity.getPersistentData().getLong("RitualRegenCooldownEnd")) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60));
            CompoundTag tag = entity.getPersistentData();
            tag.putLong("RitualRegenCooldownEnd", entity.level().getGameTime() + 80L);
        }
    }

    private ArmorSets() {
    }
}
