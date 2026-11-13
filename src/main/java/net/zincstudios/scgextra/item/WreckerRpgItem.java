package net.zincstudios.scgextra.item;

import net.minecraft.world.item.Item;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.zincstudios.scgextra.SCGExtra;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.item.GunItem;

import java.util.List;
import java.util.function.Consumer;

// TODO: proper gun implementation
@MethodsReturnNonnullByDefault
public class WreckerRpgItem extends GunItem implements GeoItem {

    private static final RawAnimation NO_AMMO = RawAnimation.begin().thenPlayAndHold("no_ammo");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public WreckerRpgItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable("item." + SCGExtra.MOD_ID + ".single_use_desc").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        CompoundTag tag = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
        if (!tag.contains("SingleUseFired")) {
            tag.putBoolean("SingleUseFired", true);
            tag.putInt("AmmoCount", 1);
            stack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.of(tag));
            stack.setDamageValue(0);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 0,
                state -> {
                    ItemStack stack = state.getData(DataTickets.ITEMSTACK);
                    if (Gun.hasAmmo(stack)) return PlayState.STOP;
                    return state.setAndContinue(NO_AMMO);
                })
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new SimpleCustomRenderer(new GeoItemRenderer<>(
                new DefaultedItemGeoModel<>(SCGExtra.asResource("wrecker_rpg"))
        )));
    }
}
