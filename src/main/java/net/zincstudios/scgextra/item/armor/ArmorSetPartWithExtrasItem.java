package net.zincstudios.scgextra.item.armor;

import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ArmorSetPartWithExtrasItem extends GeoArmorSetPartItem {
    ListMultimap<Holder<Attribute>, AttributeModifier> multimap;

    public final String armorTypeName;

    public ArmorSetPartWithExtrasItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet,
                                      Map<? extends Holder<Attribute>, AttributeModifier> modifiers, String armorTypeName) {
        super(material, type, armorSet);
        multimap = ArrayListMultimap.create();
        modifiers.forEach(multimap::put);
        this.armorTypeName = armorTypeName;
    }

    public ArmorSetPartWithExtrasItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet, Properties properties,
                                      Map<? extends Holder<Attribute>, AttributeModifier> modifiers, String armorTypeName) {
        super(material, type, armorSet, properties);
        multimap = ArrayListMultimap.create();
        modifiers.forEach(multimap::put);
        this.armorTypeName = armorTypeName;
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        super.getDefaultAttributeModifiers().modifiers().forEach(entry ->
                builder.add(entry.attribute(), entry.modifier(), entry.slot()));
        multimap.forEach((attribute, modifier) ->
                builder.add(attribute, modifier, EquipmentSlotGroup.bySlot(this.type.getSlot())));
        return builder.build();
    }
}

