package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;

import java.util.Map;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.zincstudios.scgextra.attributes.SCGEAttributes;

public class PioneerChestplateItem extends ArmorSetPartWithExtrasItem {

    public PioneerChestplateItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet, Map<? extends Holder<Attribute>, AttributeModifier> modifiers, String armorTypeName) {
        super(material, type, armorSet, modifiers, armorTypeName);
    }

    public PioneerChestplateItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet, Properties properties, Map<? extends Holder<Attribute>, AttributeModifier> modifiers, String armorTypeName) {
        super(material, type, armorSet, properties, modifiers, armorTypeName);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return (entity.getAttribute(SCGEAttributes.PIONEER)!=null) ? (entity.getAttribute(SCGEAttributes.PIONEER).getValue() >= 1.0) : false;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide) {
            int nextFlightTick = flightTicks + 1;
            if (nextFlightTick % 10 == 0) {
                entity.gameEvent(net.minecraft.world.level.gameevent.GameEvent.ELYTRA_GLIDE);
            }
        }
        return true;
    }
}
