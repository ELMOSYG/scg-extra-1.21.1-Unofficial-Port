package net.zincstudios.scgextra.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.NeoForgeMod;

import java.util.UUID;

public class WreckerSledgeHammerItem extends SwordItem implements ArmorPiercing {

    private static final UUID REACH_MODIFIER_UUID = UUID.fromString("4f3a58fe-2d90-4e83-9d71-1e2c9d3ef04a");


    public WreckerSledgeHammerItem(Tier tier, int attackDamage, float attackSpeed, double reachModifier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamage, attackSpeed)));
    }

    // TODO(阶段 4): 1.21 的额外触及距离改用 ItemAttributeModifiers + Attributes.ENTITY_INTERACTION_RANGE
}
