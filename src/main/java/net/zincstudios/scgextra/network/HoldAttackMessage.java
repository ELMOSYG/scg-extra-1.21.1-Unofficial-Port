package net.zincstudios.scgextra.network;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.zincstudios.scgextra.item.HoldAttackHandler;

public final class HoldAttackMessage {

    public static final StreamCodec<RegistryFriendlyByteBuf, HoldAttackMessage> STREAM_CODEC = StreamCodec.of(
            (buf, message) -> buf.writeBoolean(message.held),
            buf -> new HoldAttackMessage(buf.readBoolean()));

    private final boolean held;

    public HoldAttackMessage(boolean held) {
        this.held = held;
    }

    public static void handle(HoldAttackMessage message, MessageContext context) {
        context.execute(() -> context.getPlayer()
                .filter(player -> player instanceof ServerPlayer)
                .map(player -> (ServerPlayer) player)
                .ifPresent(player -> HoldAttackHandler.setServerHeld(player, message.held)));
        context.setHandled(true);
    }
}
