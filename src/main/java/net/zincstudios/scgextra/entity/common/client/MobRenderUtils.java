package net.zincstudios.scgextra.entity.common.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix4f;
import top.ribs.scguns.client.GunRenderType;

public final class MobRenderUtils {

    public static void renderMuzzleFlash(PoseStack poseStack, RenderType renderType, MultiBufferSource buffer, ResourceLocation flashTexture,
                                         boolean enchanted, RandomSource rand, float scale) {
        poseStack.mulPose(Axis.ZP.rotationDegrees(360 * rand.nextFloat()));
        poseStack.scale(scale, scale, scale);
        poseStack.translate((rand.nextFloat()-0.5)*0.07, (rand.nextFloat()-0.5)*0.07, 0);

        poseStack.pushPose();
        poseStack.translate(-0.5, -0.5, 0);
        MobRenderUtils.drawMuzzleFlash(poseStack, renderType, buffer, flashTexture, enchanted);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(360 * rand.nextFloat()));
        poseStack.translate(-0.5, -0.5, 0);
        MobRenderUtils.drawMuzzleFlash(poseStack, renderType, buffer, flashTexture, enchanted);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(360 * rand.nextFloat()));
        poseStack.translate(-0.5, -0.5, 0);
        MobRenderUtils.drawMuzzleFlash(poseStack, renderType, buffer, flashTexture, enchanted);
        poseStack.popPose();
    }

    public static void drawMuzzleFlash(PoseStack poseStack, RenderType renderType, MultiBufferSource buffer, ResourceLocation flashTexture, boolean enchanted) {
        float minU = enchanted ? 0.5F : 0.0F;
        float maxU = enchanted ? 1.0F : 0.5F;

        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer builder = buffer.getBuffer(GunRenderType.getMuzzleFlash(flashTexture));

        builder.addVertex(matrix, 0.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(maxU, 1.0F).setLight(15728880);
        builder.addVertex(matrix, 1.0F, 0.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(minU, 1.0F).setLight(15728880);
        builder.addVertex(matrix, 1.0F, 1.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(minU, 0.0F).setLight(15728880);
        builder.addVertex(matrix, 0.0F, 1.0F, 0.0F).setColor(1.0F, 1.0F, 1.0F, 1.0F).setUv(maxU, 0.0F).setLight(15728880);

        // needed to reset to previous buffer because it leaks to the rest of the model
        buffer.getBuffer(renderType);
    }

    public static boolean resourceExists(ResourceLocation resLoc) {
        return Minecraft.getInstance().getResourceManager().getResource(resLoc).isPresent();
    }
}
