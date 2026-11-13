package net.zincstudios.scgextra.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.ribs.scguns.client.handler.HeadshotBoxDebugRenderer;

/**
 * scguns 的调试准星框（F3+B）会对任意 MONSTER/CREATURE 实体生成动态头部碰撞箱。
 * Sable 的物理结构/子维度实体可能带着巨大的 AABB，绘制这些框会拖垮帧率。
 * 这里直接跳过异常大的实体。
 */
@Mixin(value = HeadshotBoxDebugRenderer.class, remap = false)
public class HeadshotBoxDebugRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private static void scgextra$skipHugeBoxes(PoseStack poseStack, VertexConsumer consumer, Entity entity, AABB box, CallbackInfo ci) {
        if (box == null) {
            ci.cancel();
            return;
        }
        if (box.getXsize() > 16.0D || box.getYsize() > 16.0D || box.getZsize() > 16.0D) {
            ci.cancel();
        }
    }
}
