package net.zincstudios.scgextra.attributes;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import top.ribs.scguns.init.ModDamageTypes;

/**
 * 4.1.0 的伤害属性处理：子弹抗性、魔法抗性、对亡灵增伤。
 */
public final class AttributeHandler {

    private AttributeHandler() {
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player) {
            if (source.is(ModDamageTypes.BULLET)) {
                double mult = 1.0D - player.getAttributeValue(SCGEAttributes.BULLET_DAMAGE_RESISTANCE);
                event.setNewDamage((float) (mult * event.getNewDamage()));
            }
            if (source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC)) {
                double mult = 1.0D - player.getAttributeValue(SCGEAttributes.MAGIC_DAMAGE_RESISTANCE);
                event.setNewDamage((float) (mult * event.getNewDamage()));
            }
        }

        if (source.getEntity() instanceof Player player && entity.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
            double mult = player.getAttributeValue(SCGEAttributes.UNDEAD_DAMAGE_MULT);
            event.setNewDamage((float) (mult * event.getNewDamage()));
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        // 旧存档可能残留旧属性的 base value；登录时重置到默认值
        for (var holder : SCGEAttributes.ALL) {
            AttributeInstance instance = event.getEntity().getAttribute(holder);
            if (instance != null) {
                instance.setBaseValue(holder.value().getDefaultValue());
            }
        }
    }
}
