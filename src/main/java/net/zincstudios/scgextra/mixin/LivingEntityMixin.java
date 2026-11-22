package net.zincstudios.scgextra.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.zincstudios.scgextra.item.armor.ArmorSet;
import net.zincstudios.scgextra.item.armor.ArmorSets;
import net.zincstudios.scgextra.item.armor.RitualArmorSet;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LivingEntity.class)
public class LivingEntityMixin {

    @WrapMethod(method = "checkTotemDeathProtection")
    private boolean atCheckTotemDeathProtection(DamageSource damageSource, Operation<Boolean> original) {
        LivingEntity self = (LivingEntity) (Object) this;
        Level level = self.level();

        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                && ArmorSet.isFullSet(self, ArmorSets.RITUAL)) {
            int cooldownTicks = (int) (RitualArmorSet.getReviveCooldownEnd(self) - level.getGameTime());
            if (cooldownTicks <= 0) {
                self.setHealth(1.0F);
                self.removeAllEffects();
                self.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                self.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                self.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                level.broadcastEntityEvent(self, (byte) 35);

                RitualArmorSet.setReviveCooldown(self, RitualArmorSet.COOLDOWN_DURATION);
                return true;
            }
        }

        return original.call(damageSource);
    }
}
