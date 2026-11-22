package net.zincstudios.scgextra.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zincstudios.scgextra.item.armor.ArmorSet;
import net.zincstudios.scgextra.item.armor.ArmorSets;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Entity.class)
public class EntityMixin {

    @WrapMethod(method = "dampensVibrations")
    private boolean atDampensVibrations(Operation<Boolean> original) {
        if ((Object)this instanceof Player player) {
            return ArmorSet.isHalfSet(player, ArmorSets.ENLIGHTENED);
        }
        return original.call();
    }
}
