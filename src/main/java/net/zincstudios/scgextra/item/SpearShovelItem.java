package net.zincstudios.scgextra.item;

import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.zincstudios.scgextra.SCGExtra;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
public class SpearShovelItem extends ShovelItem implements GeoItem {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public SpearShovelItem(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, properties.attributes(ShovelItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new SimpleCustomRenderer(new GeoItemRenderer<>(
                new DefaultedItemGeoModel<>(SCGExtra.asResource("spear_shovel"))
        )));
    }
}
