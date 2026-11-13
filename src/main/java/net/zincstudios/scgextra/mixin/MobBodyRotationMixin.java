package net.zincstudios.scgextra.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.zincstudios.scgextra.entity.common.GunnerBodyRotationControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 把所有 scg-extra 生物的身体转向控制换成 {@link GunnerBodyRotationControl}，
 * 不再局限于 GunnerEntity 子类（FAC 的普通枪手等继承 EquippedEntity 的单位也要覆盖）。
 */
@Mixin(Mob.class)
public class MobBodyRotationMixin {

    @Inject(method = "createBodyControl", at = @At("RETURN"), cancellable = true)
    private void scgextra$replaceBodyControl(CallbackInfoReturnable<BodyRotationControl> cir) {
        Mob self = (Mob) (Object) this;
        if (self.getType() == null) {
            return;
        }
        var key = BuiltInRegistries.ENTITY_TYPE.getKey(self.getType());
        if (key != null && "scgextra".equals(key.getNamespace())) {
            cir.setReturnValue(new GunnerBodyRotationControl(self));
        }
    }
}
