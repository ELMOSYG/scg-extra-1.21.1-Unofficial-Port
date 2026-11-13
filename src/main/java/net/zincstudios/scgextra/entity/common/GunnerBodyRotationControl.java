package net.zincstudios.scgextra.entity.common;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

/**
 * 更自然的身体转向控制，替代原版 {@code BodyRotationControl} 在站立时
 * “每 tick 只转 1/10”的迟缓逻辑，也避免我们之前直接锁定 yBodyRot = yHeadRot 的机械感。
 *
 * <p>规则：
 * <ul>
 *   <li>移动中：身体以 45°/tick 的速度追向移动方向（{@code getYRot()}），几乎即时但仍有过渡；</li>
 *   <li>站立/瞄准：身体用指数平滑（每 tick 完成剩余角度的 35%）转向头部朝向，
 *       180° 约 6~8 tick 完成，转向曲线是缓入缓出而不是匀速硬转；</li>
 *   <li>始终把头部限制在身体 ±{@code getMaxHeadYRot()} 范围内，避免头身脱节。</li>
 * </ul>
 */
public class GunnerBodyRotationControl extends BodyRotationControl {

    private final Mob mob;

    public GunnerBodyRotationControl(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void clientTick() {
        double dx = this.mob.getX() - this.mob.xOld;
        double dz = this.mob.getZ() - this.mob.zOld;
        boolean moving = dx * dx + dz * dz > 2.5E-7D;

        if (moving) {
            this.mob.yBodyRot = Mth.approachDegrees(this.mob.yBodyRot, this.mob.getYRot(), 45.0F);
        } else {
            float delta = Mth.wrapDegrees(this.mob.getYHeadRot() - this.mob.yBodyRot);
            // 指数平滑：越接近目标转得越慢，观感自然
            this.mob.yBodyRot += delta * 0.35F;
        }

        float maxHeadYaw = this.mob.getMaxHeadYRot();
        float headDelta = Mth.wrapDegrees(this.mob.getYHeadRot() - this.mob.yBodyRot);
        if (headDelta > maxHeadYaw || headDelta < -maxHeadYaw) {
            this.mob.setYHeadRot(this.mob.yBodyRot + Mth.clamp(headDelta, -maxHeadYaw, maxHeadYaw));
        }
    }
}
