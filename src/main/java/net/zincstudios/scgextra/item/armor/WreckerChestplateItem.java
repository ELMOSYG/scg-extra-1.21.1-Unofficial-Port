package net.zincstudios.scgextra.item.armor;

import net.minecraft.core.Holder;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.animation.PlayState;

public class WreckerChestplateItem extends GeoArmorSetPartItem{

    public WreckerChestplateItem(Holder<ArmorMaterial> material, Type type, ArmorSet armorSet) {
        super(material, type, armorSet);
    }
    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        Player player = (Player) entity;
        if (!level.isClientSide || player.isDeadOrDying() || stack != player.getItemBySlot(EquipmentSlot.CHEST)) return;

        ClientLevel clientLevel = (ClientLevel) level;  // Dedicated Server doesn't like doing instanceof ClientLevel
        Vec3 pos1;
        Vec3 pos2;
        if (player.tickCount%10 == 0) {
            pos1 = new Vec3(3.6043/16f, 27.5/16f, -5.6913/16f);
            pos2 = new Vec3(-4.5543/16f, 27.5/16f, -5.6913/16f);
        } else {
            return;
        }

        if (player.isCrouching()) {
            pos1 = pos1.subtract(0, 0.35, 0);
            pos2 = pos2.subtract(0, 0.35, 0);
        }

        pos1 = pos1.yRot(-player.yBodyRot * Mth.DEG_TO_RAD)
            .add(player.position());
        pos2 = pos2.yRot(-player.yBodyRot * Mth.DEG_TO_RAD)
            .add(player.position());

        clientLevel.addParticle(
            ParticleTypes.SMOKE,
            pos1.x, pos1.y, pos1.z,
            0, 0, 0
        );

        clientLevel.addParticle(
            ParticleTypes.SMOKE,
            pos2.x, pos2.y, pos2.z,
            0, 0, 0
        );
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 0, state->{
            state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
            return PlayState.CONTINUE;
        }));
    }
    
}
