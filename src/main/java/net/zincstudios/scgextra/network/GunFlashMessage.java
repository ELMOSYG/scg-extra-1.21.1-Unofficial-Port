package net.zincstudios.scgextra.network;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.zincstudios.scgextra.entity.common.client.GunFlashHandler;

/**
 * 枪口闪光同步。
 *
 * <p>{@code gunIndex == 0} 表示手持枪的闪光；附魔变体由客户端自行判断。
 */
public final class GunFlashMessage {

    public static final StreamCodec<RegistryFriendlyByteBuf, GunFlashMessage> STREAM_CODEC = StreamCodec.of(
            (buf, message) -> {
                buf.writeInt(message.entityId);
                buf.writeInt(message.gunIndex);
                buf.writeResourceLocation(message.flashLoc);
                buf.writeBoolean(message.enchanted);
                buf.writeFloat(message.scale);
            },
            buf -> new GunFlashMessage(buf.readInt(), buf.readInt(), buf.readResourceLocation(),
                    buf.readBoolean(), buf.readFloat()));

    private final int entityId;
    private final int gunIndex;
    private final ResourceLocation flashLoc;
    private final boolean enchanted;
    private final float scale;

    public GunFlashMessage(int entityId, int gunIndex, ResourceLocation flashLoc, boolean enchanted, float scale) {
        this.entityId = entityId;
        this.gunIndex = gunIndex;
        this.flashLoc = flashLoc;
        this.enchanted = enchanted;
        this.scale = scale;
    }

    public GunFlashMessage(int entityId, int gunIndex, ResourceLocation flashLoc) {
        this(entityId, gunIndex, flashLoc, false, 1F);
    }

    public static void handle(GunFlashMessage message, MessageContext context) {
        context.execute(() -> GunFlashHandler.addToFlash(message.entityId,
                new GunFlashHandler.FlashData(message.gunIndex, message.flashLoc, message.enchanted, message.scale)));
        context.setHandled(true);
    }
}
