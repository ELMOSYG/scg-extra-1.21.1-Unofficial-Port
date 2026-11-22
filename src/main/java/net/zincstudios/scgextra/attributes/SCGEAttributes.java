package net.zincstudios.scgextra.attributes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 4.1.0 的百分比属性体系。
 * <p>数值约定：0.1 = 10%；抗性/减伤属性是“减少比例”，默认 0。</p>
 */
public final class SCGEAttributes {

    private static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(net.minecraft.core.registries.Registries.ATTRIBUTE, "scgextra");

    public static final DeferredHolder<Attribute, Attribute> RECOIL_REDUCTION = ATTRIBUTES.register("recoil_reduction",
            () -> new PercentRenderedAttribute("attribute.scgextra.recoil_reduction", 0.0D, -10.0D, 1.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_DAMAGE_RESISTANCE = ATTRIBUTES.register("bullet_damage_resistance",
            () -> new PercentRenderedAttribute("attribute.scgextra.bullet_damage_resistance", 0.0D, -10.0D, 1.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_ADDITIONAL_CRIT_CHANCE = ATTRIBUTES.register("bullet_additional_crit_chance",
            () -> new PercentRenderedAttribute("attribute.scgextra.bullet_additional_crit_chance", 0.0D, -1.0D, 1.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_ADDITIONAL_CRIT_DAMAGE_MULT = ATTRIBUTES.register("bullet_additional_crit_damage_multiplier",
            () -> new PercentRenderedAttribute("attribute.scgextra.bullet_additional_crit_damage_multiplier", 0.0D, -1.0D, 1.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_GRAVITY = ATTRIBUTES.register("bullet_gravity_multiplier",
            () -> new PercentRenderedAttribute("attribute.scgextra.bullet_gravity", 1.0D, 0.0D, 10.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> UNDEAD_DAMAGE_MULT = ATTRIBUTES.register("undead_damage_multiplier",
            () -> new PercentRenderedAttribute("attribute.scgextra.undead_damage_multiplier", 1.0D, 0.0D, 10.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE_RESISTANCE = ATTRIBUTES.register("magic_damage_resistance",
            () -> new PercentRenderedAttribute("attribute.scgextra.magic_damage_resistance", 0.0D, -10.0D, 1.0D).setSyncable(true));

    public static final java.util.List<DeferredHolder<Attribute, Attribute>> ALL = java.util.List.of(
            RECOIL_REDUCTION, BULLET_DAMAGE_RESISTANCE, BULLET_ADDITIONAL_CRIT_CHANCE,
            BULLET_ADDITIONAL_CRIT_DAMAGE_MULT, BULLET_GRAVITY, UNDEAD_DAMAGE_MULT, MAGIC_DAMAGE_RESISTANCE);

    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(SCGEAttributes::onEntityAttributeModification);
        NeoForge.EVENT_BUS.addListener(AttributeHandler::onLivingHurt);
        NeoForge.EVENT_BUS.addListener(AttributeHandler::onPlayerLogin);
    }

    private static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, RECOIL_REDUCTION);
        event.add(EntityType.PLAYER, BULLET_DAMAGE_RESISTANCE);
        event.add(EntityType.PLAYER, BULLET_ADDITIONAL_CRIT_CHANCE);
        event.add(EntityType.PLAYER, BULLET_ADDITIONAL_CRIT_DAMAGE_MULT);
        event.add(EntityType.PLAYER, BULLET_GRAVITY);
        event.add(EntityType.PLAYER, UNDEAD_DAMAGE_MULT);
        event.add(EntityType.PLAYER, MAGIC_DAMAGE_RESISTANCE);
    }

    private SCGEAttributes() {
    }
}
