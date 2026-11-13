package net.zincstudios.scgextra.item.armor;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zincstudios.scgextra.SCGExtra;
import top.ribs.scguns.init.ModItems;

/**
 * 1.21.1 的 {@link ArmorMaterial} 是 record（不再是可以 implements 的接口），
 * 所以这里改成注册表 + 常量表；耐久倍率单独保存，因为 record 里没有耐久字段。
 */
public final class ModArmorMaterials {

    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, SCGExtra.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OPPRESSOR = register(
            "oppressor", 50, def(5, 8, 5, 3), 12, SoundEvents.ARMOR_EQUIP_IRON, 1.0F, 0F,
            () -> Ingredient.of(Items.COPPER_BLOCK, Items.EXPOSED_COPPER, Items.OXIDIZED_COPPER));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COMMISSAR = register(
            "commissar", 20, def(3, 7, 5, 3), 20, SoundEvents.ARMOR_EQUIP_LEATHER, 3.0F, 0.05F,
            () -> Ingredient.of(Items.IRON_INGOT));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> WRECKER = register(
            "wrecker", 10, def(4, 8, 6, 3), 10, SoundEvents.ARMOR_EQUIP_IRON, 3.0F, 0.2F,
            () -> Ingredient.of(ModItems.TREATED_IRON_INGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> LEVIATHAN = register(
            "leviathan", 16, def(10, 9, 5, 4), 16, SoundEvents.ARMOR_EQUIP_TURTLE, 3.0F, 0.1F,
            () -> Ingredient.of(Items.PRISMARINE, Items.PRISMARINE_BRICKS));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GOLDEN_IDOL = register(
            "golden_idol", 24, def(5, 9, 7, 5), 24, SoundEvents.ARMOR_EQUIP_GOLD, 3.0F, 0.1F,
            () -> Ingredient.of(Items.GOLD_INGOT));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ENLIGHTENED = register(
            "enlightened", 16, def(5, 6, 5, 4), 16, SoundEvents.ARMOR_EQUIP_LEATHER, 3.0F, 0.05F,
            () -> Ingredient.of(Items.IRON_INGOT));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> JUGGERNAUT = register(
            "juggernaut", 16, def(6, 11, 6, 5), 16, SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0F, 0.2F,
            () -> Ingredient.of(ModItems.TREATED_BRASS_INGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RITUAL = register(
            "ritual", 12, def(4, 8, 6, 4), 12, SoundEvents.ARMOR_EQUIP_DIAMOND, 4.0F, 0.1F,
            () -> Ingredient.of(ModItems.DIAMOND_STEEL_INGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PIONEER = register(
            "pioneer", 12, def(5, 10, 8, 5), 12, SoundEvents.ARMOR_EQUIP_LEATHER, 3F, 0.05F,
            () -> Ingredient.of(Items.SHULKER_SHELL, ModItems.DIAMOND_STEEL_INGOT.get()));

    private static final Map<String, Integer> DURABILITY_MULTIPLIERS = Map.of(
            "oppressor", 50, "commissar", 20, "wrecker", 10, "leviathan", 16,
            "golden_idol", 24, "enlightened", 16, "juggernaut", 16, "ritual", 12, "pioneer", 12);

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(
            String name, int durabilityMultiplier, Map<ArmorItem.Type, Integer> defense, int enchantmentValue,
            Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
            Supplier<Ingredient> repairIngredient) {
        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                defense,
                enchantmentValue,
                equipSound,
                repairIngredient,
                List.of(new ArmorMaterial.Layer(SCGExtra.asResource(name))),
                toughness,
                knockbackResistance));
    }

    private static Map<ArmorItem.Type, Integer> def(int helmet, int chest, int legs, int boots) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.CHESTPLATE, chest);
        map.put(ArmorItem.Type.LEGGINGS, legs);
        map.put(ArmorItem.Type.BOOTS, boots);
        return map;
    }

    /** 1.21 的护甲耐久不再属于 ArmorMaterial，构造物品时要自己写进 Properties。 */
    public static Item.Properties armorProperties(Holder<ArmorMaterial> material, ArmorItem.Type type) {
        String path = material.unwrapKey().map(key -> key.location().getPath()).orElse("unknown");
        return new Item.Properties().durability(type.getDurability(DURABILITY_MULTIPLIERS.getOrDefault(path, 1)));
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }

    private ModArmorMaterials() {
    }
}
