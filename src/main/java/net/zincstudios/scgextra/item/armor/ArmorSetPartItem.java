package net.zincstudios.scgextra.item.armor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ArmorSetPartItem extends ArmorItem implements ArmorSetPart {

    private final ArmorSet armorSet;

    public ArmorSetPartItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet) {
        this(material, type, armorSet, ModArmorMaterials.armorProperties(material, type));
    }

    public ArmorSetPartItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet, Properties properties) {
        super(material, type, properties);
        this.armorSet = armorSet;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (ItemAttributeModifiers.Entry entry : super.getDefaultAttributeModifiers().modifiers()) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }

        EquipmentSlotGroup slot = EquipmentSlotGroup.bySlot(this.type.getSlot());
        for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : this.armorSet.getPartModifiers().entrySet()) {
            AttributeModifier original = entry.getValue();
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                    original.id().getNamespace(),
                    original.id().getPath() + "_" + this.type.name().toLowerCase(Locale.ROOT));
            builder.add(entry.getKey(), new AttributeModifier(id, original.amount(), original.operation()), slot);
        }
        return builder.build();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        ResourceLocation loc = this.armorSet.getResourceLocation();

        if (Screen.hasShiftDown()) {
            int pieces = 0;
            if (FMLEnvironment.dist == Dist.CLIENT) {
                pieces = net.zincstudios.scgextra.client.ClientArmorSetInfo.piecesWorn(this.armorSet);
            }
            boolean halfActive = pieces >= 2;
            boolean fullActive = pieces >= 4;
            ChatFormatting halfColor = halfActive ? ChatFormatting.GREEN : ChatFormatting.GRAY;
            ChatFormatting fullColor = fullActive ? ChatFormatting.GREEN : ChatFormatting.GRAY;

            tooltipComponents.add(Component.translatable("armorset.progress", pieces)
                    .withStyle(pieces >= 2 ? ChatFormatting.WHITE : ChatFormatting.GRAY));

            tooltipComponents.add(Component.translatable("armorset.half_set_bonus.description")
                    .withStyle(halfColor));
            tooltipComponents.add(Component.translatable(
                            "armorset." + loc.getNamespace() + "." + loc.getPath() + ".half_set.description")
                    .withStyle(halfColor));

            tooltipComponents.add(Component.translatable("armorset.full_set_bonus.description")
                    .withStyle(fullColor));
            tooltipComponents.add(Component.translatable(
                            "armorset." + loc.getNamespace() + "." + loc.getPath() + ".description")
                    .withStyle(fullColor));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.scguns.hold_shift")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    @Override
    public @NotNull ArmorSet getArmorSet() {
        return this.armorSet;
    }
}
