package net.zincstudios.scgextra.entity.common.gun;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.Registries;
import top.ribs.scguns.common.Gun;
import top.ribs.scguns.common.ProjectileManager;
import top.ribs.scguns.item.GunItem;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

// Doesn't do reload logic
@ParametersAreNonnullByDefault
public class HeldScorchedSimGun extends ScorchedSimGun {

    private final ItemStack gunStack;

    public HeldScorchedSimGun(ItemStack gunStack) {
        this(gunStack, gunStack.copy());
    }

    private HeldScorchedSimGun(ItemStack gunStack, ItemStack snapshot) {
        super(
                gunFromStack(snapshot),
                false,
                (float)gunFromStack(snapshot).getIdealAttackRange(),
                (float)gunFromStack(snapshot).getIdealAttackRange()*1.5f,
                0,
                (level, entity, gunBase) -> ProjectileManager.getInstance()
                        .getFactory(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(Objects.requireNonNull(gunBase.getProjectile().getItem())))
                        .create(level, entity, snapshot, (GunItem) snapshot.getItem(), gunBase),
                vec -> vec
        );
        this.gunStack = gunStack;
    }

    @Override
    public boolean tickFire(LivingEntity shooter, net.minecraft.world.phys.Vec3 targetPos, float accuracyModifier, boolean firing) {
        // 枪可能在连发/换武器过程中被清空成空气；此时旧引用会变成 AirItem，
        // 继续走开火逻辑就会把 AirItem 强转 GunItem 导致服务端崩溃。
        if (!(this.gunStack.getItem() instanceof GunItem)) {
            return false;
        }
        return super.tickFire(shooter, targetPos, accuracyModifier, firing);
    }

    private static Gun gunFromStack(ItemStack gunStack) {
        if (!(gunStack.getItem() instanceof GunItem gunItem)) {
            throw new IllegalArgumentException("gunStack is not a GunItem");
        }
        return gunItem.getModifiedGun(gunStack);
    }

    @Override
    public boolean hasChanged(LivingEntity entity) {
        // NOTE: nbt tags not checked, mostly irrelevant and causes ItemStack.matches to be false.
        return !(this.gunStack.getItem() instanceof GunItem)
                || entity.getMainHandItem().getItem() != this.gunStack.getItem();
    }
}
