package net.zincstudios.scgextra.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import top.ribs.scguns.blockentity.MineUnitBlockEntity;

@Mixin(value = MineUnitBlockEntity.class)
public class MineUnitBlockEntityMixin {

    @WrapOperation(
            method = "triggerGrenade",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/ItemEntity;getControllingPassenger()Lnet/minecraft/world/entity/LivingEntity;"
            )
    )
    private static LivingEntity tickHas(ItemEntity instance, Operation<LivingEntity> original) {
        // 1.21 的 ItemEntity 不再有 getControllingPassenger() 的“投掷者”语义，
        // 原版 scguns 这里会拿到 null，后续 explosion 的 source 需要非空实体，
        // 所以构造一个临时 ArmorStand 作为伤害来源。
        //
        // 关键：必须立刻 discard。否则这个“幽灵盔甲架”会被当作有效攻击者，
        // 被炸到的生物会把 ATTACK_TARGET 设为它，然后对着已经消失的地雷原地挥空。
        ArmorStand stand = new ArmorStand(instance.level(), instance.getX(), instance.getY(), instance.getZ());
        stand.setInvisible(true);
        stand.discard();
        return stand;
    }
}
