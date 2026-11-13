package net.zincstudios.scgextra.attributes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zincstudios.scgextra.SCGExtra;

public final class SCGEAttributes {

    private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister
            .create(Registries.ATTRIBUTE, SCGExtra.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> RECOIL_MULT = ATTRIBUTES.register("recoil_multiplier",
            () -> (new RangedAttribute("attribute.scgextra.recoil_multiplier", 0.0F, -100.0, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_DAMAGE_TAKEN_MULT = ATTRIBUTES.register("bullet_damage_taken_multiplier",
            () -> (new RangedAttribute("attribute.scgextra.bullet_damage_taken_multiplier", 0.0F, 0.0, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_ADDITIONAL_CRIT_CHANCE = ATTRIBUTES.register("bullet_additional_crit_chance",
            () -> (new RangedAttribute("attribute.scgextra.bullet_additional_crit_chance", 0.0F, -100, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_ADDITIONAL_CRIT_DAMAGE_MULT = ATTRIBUTES.register("bullet_additional_crit_damage_multiplier",
            () -> (new RangedAttribute("attribute.scgextra.bullet_additional_crit_damage_multiplier", 0.0F, -100, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> BULLET_GRAVITY_MULT = ATTRIBUTES.register("bullet_gravity_multiplier",
            () -> (new RangedAttribute("attribute.scgextra.bullet_gravity_multiplier", 0.0F, 0.0, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> UNDEAD_DAMAGE_MULT = ATTRIBUTES.register("undead_damage_multiplier",
            () -> (new RangedAttribute("attribute.scgextra.undead_damage_multiplier", 0.0F, -100, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE_REDUCTION = ATTRIBUTES.register("magic_damage_reduction",
            () -> (new RangedAttribute("attribute.scgextra.magic_damage_reduction", 0.0F, -100, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> DEBUFF_ZONE = ATTRIBUTES.register("debuff_zone",
            () -> (new RangedAttribute("attribute.scgextra.debuff_zone", 0F, 0, 100.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> GENERAL_DAMAGE_MULT = ATTRIBUTES.register("general_damage_mult",
            () -> (new RangedAttribute("attribute.scgextra.general_damage_mult", 0F, -100F, 100.0F)).setSyncable(true));
    //probably not the best way of doing it
    public static final DeferredHolder<Attribute, Attribute> OPPRESSOR = ATTRIBUTES.register("oppressor",
        () -> (new RangedAttribute("attribute.scgextra.oppressor", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> COMMISSAR = ATTRIBUTES.register("commissar",
        () -> (new RangedAttribute("attribute.scgextra.commissar", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> WRECEKR = ATTRIBUTES.register("wrecker",
        () -> (new RangedAttribute("attribute.scgextra.wrecker", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> LEVIATHAN = ATTRIBUTES.register("leviathan",
        () -> (new RangedAttribute("attribute.scgextra.leviathan", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> GOLDEN_IDOL = ATTRIBUTES.register("golden_idol",
        () -> (new RangedAttribute("attribute.scgextra.golden_idol", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> ENLIGHTENED = ATTRIBUTES.register("enlightened",
        () -> (new RangedAttribute("attribute.scgextra.enlightened", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> JUGGERNAUT = ATTRIBUTES.register("juggernaut",
        () -> (new RangedAttribute("attribute.scgextra.juggernaut", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> RITUAL = ATTRIBUTES.register("ritual",
        () -> (new RangedAttribute("attribute.scgextra.ritual", 0F, 0, 2.0F)).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> PIONEER = ATTRIBUTES.register("pioneer",
        () -> (new RangedAttribute("attribute.scgextra.pioneer", 0F, 0, 2.0F)).setSyncable(true));

    public static void register(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(SCGEAttributes::onEntityAttributeModification);
    }

    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, RECOIL_MULT);
        event.add(EntityType.PLAYER, BULLET_DAMAGE_TAKEN_MULT);
        event.add(EntityType.PLAYER, BULLET_ADDITIONAL_CRIT_CHANCE);
        event.add(EntityType.PLAYER, BULLET_ADDITIONAL_CRIT_DAMAGE_MULT);
        event.add(EntityType.PLAYER, BULLET_GRAVITY_MULT);
        event.add(EntityType.PLAYER, UNDEAD_DAMAGE_MULT);
        event.add(EntityType.PLAYER, MAGIC_DAMAGE_REDUCTION);
        event.add(EntityType.PLAYER, DEBUFF_ZONE);
        event.add(EntityType.PLAYER, GENERAL_DAMAGE_MULT);

        event.add(EntityType.PLAYER, OPPRESSOR);
        event.add(EntityType.PLAYER, COMMISSAR);
        event.add(EntityType.PLAYER, WRECEKR);
        event.add(EntityType.PLAYER, LEVIATHAN);
        event.add(EntityType.PLAYER, GOLDEN_IDOL);
        event.add(EntityType.PLAYER, ENLIGHTENED);
        event.add(EntityType.PLAYER, JUGGERNAUT);
        event.add(EntityType.PLAYER, RITUAL);
        event.add(EntityType.PLAYER, PIONEER);
    }
}
