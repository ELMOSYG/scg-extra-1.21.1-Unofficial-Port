package net.zincstudios.scgextra.entity.cog.devastator;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zincstudios.scgextra.SCGExtra;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/**
 * 毁灭者的头部是坦克炮塔造型：有目标时锁定目标方向，而不是跟着身体转。
 *
 * <p>服务端已经通过 {@code getLookControl().setLookAt(target, 90, 90)} 设置了
 * {@code yHeadRot}/{@code xRot}，但 GeckoLib 不会自动把头部骨骼按实体头部朝向旋转，
 * 所以这里在 {@code setCustomAnimations} 里手动旋转 {@code head} 骨骼。</p>
 *
 * <p>客户端通过同步的 {@code TARGET_ID} 拿到目标实体，直接按双方位置计算世界朝向，
 * 再减去身体当前朝向，得到头部骨骼的局部旋转；没有目标时交给动画本身。</p>
 */
@OnlyIn(Dist.CLIENT)
public class CogDevastatorModel extends DefaultedEntityGeoModel<CogDevastatorEntity> {

    /** head 骨骼 pivot 的 y 坐标约 43 像素，换算成世界高度。 */
    private static final double HEAD_HEIGHT = 43.0D / 16.0D;

    public CogDevastatorModel() {
        super(SCGExtra.asResource("cog/cog_devastator"), "chest");
    }

    @Override
    public void setCustomAnimations(CogDevastatorEntity animatable, long instanceId,
                                    AnimationState<CogDevastatorEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = getAnimationProcessor().getBone("head");
        if (head == null) {
            return;
        }

        LivingEntity target = animatable.getTarget();
        if (target == null || !target.isAlive()) {
            return; // 没目标时保留动画本身的头部动作
        }

        double dx = target.getX() - animatable.getX();
        double dz = target.getZ() - animatable.getZ();
        double dy = target.getEyeY() - (animatable.getY() + HEAD_HEIGHT);

        float targetYaw = (float) (Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
        float yawOffset = Mth.wrapDegrees(targetYaw - animatable.getYRot());
        float pitch = (float) (-(Mth.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * Mth.RAD_TO_DEG));

        head.setRotY(yawOffset * Mth.DEG_TO_RAD);
        head.setRotX(Mth.clamp(pitch, -60.0F, 60.0F) * Mth.DEG_TO_RAD);
    }
}
