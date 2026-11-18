package net.zincstudios.scgextra.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 禁止本模组的生物被船、矿车或机械动力（Create）的坐垫“抓走”。
 *
 * <p>Create 的 {@code SeatBlock#canBePickedUp} 对敌对生物是否生效取决于服务端
 * {@code seatHostileMobs} 配置；即使配置正确，原版船/矿车也允许任意实体作为乘客。
 * 生物一旦被塞进坐垫或船里，Brain/Goal AI 会卡在“骑乘”状态，原地不动。</p>
 *
 * <p>这里在 {@code Entity#startRiding} 入口拦截：只对 {@code scgextra} 命名空间的
 * 生物生效，拒绝船、矿车和 {@code create} 命名空间里名字带 seat 的载具。
 * Create 的实体标签（{@code create:ignore_seat}）也同步配置了一份，双保险。</p>
 */
@Mixin(value = Entity.class)
public abstract class EntityRideGuardMixin {

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z", at = @At("HEAD"), cancellable = true)
    private void scgextra$denySeatAndBoat(Entity vehicle, boolean force,
                                          CallbackInfoReturnable<Boolean> cir) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof LivingEntity)) {
            return;
        }
        ResourceLocation selfId = self.getType().builtInRegistryHolder().key().location();
        if (!"scgextra".equals(selfId.getNamespace())) {
            return;
        }

        boolean vanillaVehicle = vehicle instanceof Boat || vehicle instanceof AbstractMinecart;
        ResourceLocation vehicleId = vehicle.getType().builtInRegistryHolder().key().location();
        boolean createSeat = "create".equals(vehicleId.getNamespace())
                && vehicleId.getPath().contains("seat");

        if (vanillaVehicle || createSeat) {
            cir.setReturnValue(false);
        }
    }
}
