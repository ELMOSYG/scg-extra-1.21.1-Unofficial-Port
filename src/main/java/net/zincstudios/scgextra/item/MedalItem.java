package net.zincstudios.scgextra.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.*;

public class MedalItem extends Item {

    private static final int TICK_INTERVAL = 10;
    private static final Map<ResourceLocation, Holder<Attribute>> ALL_MODIFIER_ATTRIBUTES = new HashMap<>();

    private final Map<Holder<Attribute>, AttributeModifier> modifiers;
    private final Map<Holder<MobEffect>, Integer> effects;
    private final Set<Holder<MobEffect>> resistances;

    public MedalItem(MedalItem.Traits traits) {
        this(new Item.Properties().stacksTo(1), traits);
    }

    public MedalItem(Item.Properties properties, MedalItem.Traits traits) {
        super(properties);
        this.modifiers = Map.copyOf(traits.modifiers);
        this.effects = Map.copyOf(traits.effects);
        this.resistances = Set.copyOf(traits.resistances);
        for (Map.Entry<Holder<Attribute>, AttributeModifier> e : traits.modifiers.entrySet()) {
            ALL_MODIFIER_ATTRIBUTES.put(e.getValue().id(), e.getKey());
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context,
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        ResourceLocation loc = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(this);
        if (loc == null) return;

        if (Screen.hasShiftDown()) {
            tooltipComponents.add(Component.translatable("item." + loc.getNamespace() + "." + loc.getPath() + ".description").withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable("tooltip.scguns.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();

        if (player.tickCount % TICK_INTERVAL != 0) return;

        Map<ResourceLocation, AttributeModifier> desiredModifiers = new HashMap<>();

        List<ItemStack> stacks = new ArrayList<>();

        CuriosApi.getCuriosInventory(player).ifPresent(itemHandler -> {
            IDynamicStackHandler charms = itemHandler.getCurios().get("charm").getStacks();
            for (int i = 0; i < charms.getSlots(); i++) {
                ItemStack stack = charms.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    stacks.add(stack);
                }
            }
        });

        for (ItemStack stack : stacks) {
            if (!(stack.getItem() instanceof MedalItem medalItem)) continue;

            for (Map.Entry<Holder<Attribute>, AttributeModifier> entry : medalItem.modifiers.entrySet()) {
                desiredModifiers.put(entry.getValue().id(), entry.getValue());
            }
            for (Map.Entry<Holder<MobEffect>, Integer> entry : medalItem.effects.entrySet()) {
                player.addEffect(new MobEffectInstance(entry.getKey(), 115, entry.getValue(), true, true));
            }
            for (Holder<MobEffect> effect : medalItem.resistances) {
                player.removeEffect(effect);
            }
        }

        for (Map.Entry<ResourceLocation, Holder<Attribute>> entry : ALL_MODIFIER_ATTRIBUTES.entrySet()) {
            ResourceLocation id = entry.getKey();
            AttributeInstance attr = player.getAttribute(entry.getValue());
            if (attr == null) continue;

            boolean shouldHave = desiredModifiers.containsKey(id);
            boolean hasModifier = attr.getModifier(id) != null;

            if (shouldHave && !hasModifier) {
                attr.addTransientModifier(desiredModifiers.get(id));
            } else if (!shouldHave && hasModifier) {
                attr.removeModifier(id);
            }
        }
    }

    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;

        for (ItemStack stack : player.getInventory().items) {
            if (!(stack.getItem() instanceof MedalItem medalItem)) continue;
            if (medalItem.resistances.contains(event.getEffectInstance().getEffect())) {
                event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
            }
        }
    }

    // for cleaner registration
    public static class Traits {
        private final Map<Holder<Attribute>, AttributeModifier> modifiers = new HashMap<>();
        private final Map<Holder<MobEffect>, Integer> effects = new HashMap<>();
        private final Set<Holder<MobEffect>> resistances = new HashSet<>();

        public MedalItem.Traits modifier(Holder<Attribute> attribute, AttributeModifier modifier) {
            this.modifiers.put(attribute, modifier);
            return this;
        }

        public MedalItem.Traits effect(Holder<MobEffect> effect, int amplifier) {
            this.effects.put(effect, amplifier);
            return this;
        }

        public MedalItem.Traits resistance(Holder<MobEffect> effect) {
            this.resistances.add(effect);
            return this;
        }
    }
}
