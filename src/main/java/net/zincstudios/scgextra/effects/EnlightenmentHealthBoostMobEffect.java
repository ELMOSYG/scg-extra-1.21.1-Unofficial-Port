package net.zincstudios.scgextra.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.zincstudios.scgextra.SCGExtra;

// Because reapplying HealthBoostMobEffect resets current hp
public class EnlightenmentHealthBoostMobEffect extends MobEffect {

    public static final ResourceLocation EFFECT_ID = SCGExtra.asResource("enlightenment_health_boost");

    public EnlightenmentHealthBoostMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
        if (instance != null) {
            instance.removeModifier(EFFECT_ID);
            if (entity.getHealth() > entity.getMaxHealth()) {
                entity.setHealth(entity.getMaxHealth());
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration == 1;
    }
}
