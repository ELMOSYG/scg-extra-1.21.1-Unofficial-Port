package net.zincstudios.scgextra.entity.cog.devastator;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zincstudios.scgextra.SCGExtra;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

/**
 * 毁灭者的“炮塔”其实挂在 {@code chest} 骨骼上（腿部才是履带，{@code head} 只是顶部小部件）。
 *
 * <p>{@link DefaultedEntityGeoModel} 的第二个构造参数是 <b>headBone 名字</b>，不是 subtype；
 * 所以原来的 {@code new DefaultedEntityGeoModel<>(..., "chest")} 实际上已经让 GeckoLib 把
 * {@code chest} 当作头骨、按实体头部朝向旋转了——只是方向与目标相反，看起来像“不锁定”。</p>
 *
 * <p>这里在父类按 {@code EntityModelData} 旋转完 chest 之后，把 chest 的 Y/X 旋转取反，
 * 让它真正指向目标；{@code head} 不再单独旋转，避免顶部小部件和炮塔脱节。</p>
 */
@OnlyIn(Dist.CLIENT)
public class CogDevastatorModel extends DefaultedEntityGeoModel<CogDevastatorEntity> {

    public CogDevastatorModel() {
        // 第二个参数是 headBone：炮塔在 chest 上
        super(SCGExtra.asResource("cog/cog_devastator"), "chest");
    }

    @Override
    public void setCustomAnimations(CogDevastatorEntity animatable, long instanceId,
                                    AnimationState<CogDevastatorEntity> animationState) {
        // 父类会按 EntityModelData 的 netHeadYaw/headPitch 旋转 chest
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone chest = getAnimationProcessor().getBone("chest");
        if (chest == null) {
            return;
        }

        // 模型 chest 的 Y 轴与 MC yaw 方向相反；只翻转水平旋转，俯仰保持 GeckoLib 的 headPitch
        chest.setRotY(-chest.getRotY());
    }
}
