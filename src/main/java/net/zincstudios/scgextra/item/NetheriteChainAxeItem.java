package net.zincstudios.scgextra.item;

import java.util.UUID;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.sounds.AsgharianSounds;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.NeoForgeMod;
import top.ribs.scguns.init.ModEffects;
import top.ribs.scguns.init.ModTags;

public class NetheriteChainAxeItem extends AxeItem implements HurtEffects, GeoItem{
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);


   private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("saw");

    public NetheriteChainAxeItem(Tier tier, float attackDamageModifier, float attackSpeedModifier,
            Properties properties) {
        super(tier, properties.attributes(AxeItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
        // TODO(阶段 4): 1.21 的额外触及距离改用 ItemAttributeModifiers + Attributes.ENTITY_INTERACTION_RANGE
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(!attacker.level().isClientSide()){
            attacker.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), AsgharianSounds.ASGHAR_WORKER_SAW.get(), SoundSource.NEUTRAL, 1F, 1F);
        }
        this.hurtEffect(stack, target, attacker);
        target.hurt(attacker.damageSources().mobAttack(attacker), 11);
        stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public void hurtEffect(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide() && !target.getType().is(ModTags.Entities.CANNOT_BE_LACERATED)) {
            target.addEffect(new MobEffectInstance(ModEffects.LACERATED, 200));
        }
        if (!target.level().isClientSide()) {
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200));
        }
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "saw", state->{
            state.setAnimation(IDLE);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new SimpleCustomRenderer(new GeoItemRenderer<>(
                new DefaultedItemGeoModel<>(SCGExtra.asResource("netherite_chain_axe"))
        )));
    }
    
}

