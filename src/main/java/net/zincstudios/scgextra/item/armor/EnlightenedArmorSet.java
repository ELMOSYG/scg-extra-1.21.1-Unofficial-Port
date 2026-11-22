package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Map;

public class EnlightenedArmorSet extends ArmorSet {

    public EnlightenedArmorSet(ResourceLocation resLoc, Traits fullSetTraits, Traits halfSetTraits,
                               Map<Holder<Attribute>, AttributeModifier> partModifiers) {
        super(resLoc, fullSetTraits, halfSetTraits, partModifiers);
    }

    @Override
    public void halfSetTick(LivingEntity entity) {
        if (entity.level().getGameTime() > entity.getPersistentData().getLong("EnlightenedRegenCooldownEnd")) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                entity.heal(1.0F);
            }
            entity.getPersistentData().putLong("EnlightenedRegenCooldownEnd", entity.level().getGameTime() + 50L);
        }
    }
}
