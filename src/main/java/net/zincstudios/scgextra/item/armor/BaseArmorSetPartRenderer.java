package net.zincstudios.scgextra.item.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.zincstudios.scgextra.item.ModItems;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class BaseArmorSetPartRenderer extends GeoArmorRenderer<GeoArmorSetPartItem> {

    public BaseArmorSetPartRenderer(GeoModel<GeoArmorSetPartItem> model) {
        super(model);
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            public void render(PoseStack poseStack, GeoArmorSetPartItem animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
                RenderType emissiveRenderType = software.bernie.geckolib.cache.texture.AutoGlowingTexture.getRenderType(getRenderer().getTextureLocation(animatable));
                getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, emissiveRenderType, bufferSource.getBuffer(emissiveRenderType),
                        partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF
                );
            }
        });
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void hideHeadWithCommissarHelmet(RenderPlayerEvent.Pre event) {
        PlayerRenderer renderer = event.getRenderer();
        PlayerModel<AbstractClientPlayer> model = renderer.getModel();
        if (event.getEntity().getItemBySlot(EquipmentSlot.HEAD).getItem() == ModItems.COMMISSAR_HELMET.get()) {
            // 同时隐藏内层头部和 1.8+ 双层皮肤的外层 hat，否则外层皮肤会从政委头盔里穿出来。
            model.head.visible = false;
            model.hat.visible = false;
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void restoreHeadAfterCommissarHelmet(RenderPlayerEvent.Post event) {
        PlayerRenderer renderer = event.getRenderer();
        PlayerModel<AbstractClientPlayer> model = renderer.getModel();
        // Pre 里改的是共享的 PlayerModel，必须在 Post 恢复，否则别的玩家/后续帧会一直少头。
        model.head.visible = true;
        model.hat.visible = true;
    }
}
