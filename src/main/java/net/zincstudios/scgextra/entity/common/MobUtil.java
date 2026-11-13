package net.zincstudios.scgextra.entity.common;

import com.mrcrayfish.framework.api.network.LevelLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import top.ribs.scguns.Config;
import top.ribs.scguns.item.GunItem;
import top.ribs.scguns.item.attachment.IAttachment;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class MobUtil {

    // Common Constants
    public static final int DEFAULT_STUN_DURATION = 60;

    public static void addGunAttachment(ItemStack gunStack, ItemStack attachmentStack) {
        if (gunStack.getItem() instanceof GunItem gunItem
                && attachmentStack.getItem() instanceof IAttachment<?> attachment
                && gunItem.getGun().canAttachType(attachment.getType())
                && attachment.canAttachTo(gunStack)) {

            CompoundTag tag = gunStack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
            CompoundTag attachments;
            if (tag.contains("Attachments")) {
                attachments = tag.getCompound("Attachments");
            } else {
                attachments = new CompoundTag();
            }

            attachments.put(attachment.getType().getTagKey(), attachmentStack.save(net.minecraft.core.RegistryAccess.EMPTY, new CompoundTag()));

            tag.put("Attachments", attachments);
            gunStack.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                    net.minecraft.world.item.component.CustomData.of(tag));
        }
    }

    public static Vec3 lerpVec(Vec3 a, Vec3 b, double t) {
        return new Vec3(
                Mth.lerp(t, a.x, b.x),
                Mth.lerp(t, a.y, b.y),
                Mth.lerp(t, a.z, b.z)
        );
    }

    /**
     * GeckoLib 的 {@code AnimationState#isMoving()} 直接读水平位移，但它在客户端的采样时机
     * 并不可靠（移动时偶尔返回 false），会让控制器在行走时播放站立/瞄准动画，表现为滑步。
     * 这里用上一 tick 的坐标差做同样但更稳定的判断。
     */
    public static boolean isMoving(net.minecraft.world.entity.Entity entity) {
        double dx = entity.getX() - entity.xOld;
        double dz = entity.getZ() - entity.zOld;
        return dx * dx + dz * dz > 2.0E-4D;
    }

    public static LevelLocation levelLocationFromEntity(LivingEntity entity) {
        return LevelLocation.create(
                (net.minecraft.server.level.ServerLevel) entity.level(),
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                Config.COMMON.network.projectileTrackingRange.get()
        );
    }

    /**
     * Used to gradually turn an entity to a direction. Intended to be invoked every tick when being used.
     */
    public static void turnEntityToYaw(LivingEntity entity, float yaw, float turnSpeed) {
        entity.setYRot(Mth.approachDegrees(entity.getYRot(), yaw, turnSpeed));
        entity.setYHeadRot(entity.getYRot());
        entity.setYBodyRot(entity.getYRot());
    }

    public static void putBlockPosToTag(BlockPos blockPos, String prefix, CompoundTag tag) {
        tag.putInt(prefix + "X", blockPos.getX());
        tag.putInt(prefix + "Y", blockPos.getY());
        tag.putInt(prefix + "Z", blockPos.getZ());
    }

    public static @Nullable BlockPos getBlocKPosFromTag(String prefix, CompoundTag tag) {
        if (tag.contains(prefix+"X")) {
            return new BlockPos(tag.getInt(prefix+"X"), tag.getInt(prefix+"Y"), tag.getInt(prefix+"Z"));
        }
        return null;
    }

    public static Vec3 vecFromRot(float yRot) {
        float yaw = (float) Math.toRadians(yRot);
        float x = -Mth.sin(yaw);
        float z = Mth.cos(yaw);
        return new Vec3(x, 0, z);
    }

    public static float rotFromVec(Vec3 vec) {
        return (float) Mth.atan2(-vec.x, vec.z) * Mth.RAD_TO_DEG;
    }

    public static Vec3 toVec(float yaw, float pitch) {
        double yawRad   = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);
        double x = -Math.sin(yawRad) * Math.cos(pitchRad);
        double y = -Math.sin(pitchRad);
        double z =  Math.cos(yawRad) * Math.cos(pitchRad);

        return new Vec3(x, y, z);
    }

    public static void tickDeath(LivingEntity entity, int tick) {
        // Override to only extend death time
        ++entity.deathTime;
        if (entity.deathTime >= tick && !entity.level().isClientSide() && !entity.isRemoved()) {
            entity.level().broadcastEntityEvent(entity, (byte)60);
            entity.remove(RemovalReason.KILLED);
        }
    }
}
