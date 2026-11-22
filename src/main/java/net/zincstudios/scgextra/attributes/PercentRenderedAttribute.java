package net.zincstudios.scgextra.attributes;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.fml.ModList;

/**
 * 以百分比显示数值的属性（0.1 = 10%）。
 * 装有 AttributeSlib 时使用它的百分比文本，否则退化为普通“xx%”文本。
 */
public class PercentRenderedAttribute extends RangedAttribute {

    public PercentRenderedAttribute(String descriptionId, double defaultValue, double min, double max) {
        super(descriptionId, defaultValue, min, max);
    }

    @Override
    public MutableComponent toValueComponent(AttributeModifier.Operation operation, double value, TooltipFlag flag) {
        String formatted = ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(value * 100.0D);
        if (ModList.get() != null && ModList.get().isLoaded("attributeslib")) {
            return Component.translatable("attributeslib.value.percent", formatted);
        }
        return Component.literal(formatted + "%");
    }
}
