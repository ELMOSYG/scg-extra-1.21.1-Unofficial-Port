package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.Enemy;

import java.util.List;
import java.util.Map;

public class GoldenIdolArmorSet extends ArmorSet {

    public static final float REGEN_RADIUS = 5.0F;
    public static final float DEBUFF_RADIUS = 10.0F;

    public GoldenIdolArmorSet(ResourceLocation resLoc, Traits fullSetTraits, Traits halfSetTraits,
                              Map<Holder<Attribute>, AttributeModifier> partModifiers) {
        super(resLoc, fullSetTraits, halfSetTraits, partModifiers);
    }

    @Override
    public void halfSetTick(LivingEntity entity) {
        List<LivingEntity> allies = entity.level().getEntitiesOfClass(LivingEntity.class,
                entity.getBoundingBox().inflate(REGEN_RADIUS, REGEN_RADIUS / 2.0, REGEN_RADIUS),
                other -> entity.closerThan(other, REGEN_RADIUS) && !(other instanceof Enemy));
        for (LivingEntity ally : allies) {
            ally.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 115, 0, true, true));
        }
    }

    @Override
    public void fullSetTick(LivingEntity entity) {
        List<LivingEntity> enemies = entity.level().getEntitiesOfClass(LivingEntity.class,
                entity.getBoundingBox().inflate(DEBUFF_RADIUS, DEBUFF_RADIUS / 2.0, DEBUFF_RADIUS),
                other -> entity.closerThan(other, DEBUFF_RADIUS) && other instanceof Enemy);
        for (LivingEntity enemy : enemies) {
            enemy.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 115, 1, true, true));
            enemy.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 115, 1, true, true));
        }
    }
}
