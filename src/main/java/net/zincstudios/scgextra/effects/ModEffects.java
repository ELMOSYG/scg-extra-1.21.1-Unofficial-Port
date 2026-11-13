package net.zincstudios.scgextra.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zincstudios.scgextra.SCGExtra;

public final class ModEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, SCGExtra.MOD_ID);

    public static final DeferredHolder<MobEffect, InkEffect> INK_EFFECT = MOB_EFFECTS.register("ink_effect", () -> new InkEffect(MobEffectCategory.HARMFUL, 0xFF0000));
    public static final DeferredHolder<MobEffect, GlowingInkEffect> GLOWING_INK_EFFECT = MOB_EFFECTS.register("glowing_ink_effect", () -> new GlowingInkEffect(MobEffectCategory.HARMFUL, 0xFF0000));

    public static final DeferredHolder<MobEffect, StunnedEffect> STUNNED_EFFECT = MOB_EFFECTS.register("stunned", () -> new StunnedEffect(MobEffectCategory.HARMFUL, 0x000000));

    public static final DeferredHolder<MobEffect, MobEffect> MORTAL = MOB_EFFECTS.register("mortal", () -> new MortalMobEffect(MobEffectCategory.NEUTRAL, 0x2C366D));

    public static final DeferredHolder<MobEffect, MobEffect> ENLIGHTENMENT = MOB_EFFECTS.register("enlightenment", () -> new EnlightenmentHealthBoostMobEffect(MobEffectCategory.BENEFICIAL, 0x000000)
            .addAttributeModifier(Attributes.MAX_HEALTH, EnlightenmentHealthBoostMobEffect.EFFECT_ID, 8.0D, AttributeModifier.Operation.ADD_VALUE));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
