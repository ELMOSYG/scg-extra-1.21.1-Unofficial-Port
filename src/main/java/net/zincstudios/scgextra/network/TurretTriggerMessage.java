package net.zincstudios.scgextra.network;

import com.mrcrayfish.framework.api.network.MessageContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.zincstudios.scgextra.block.wreckerturret.WreckerTurretBlockEntity;
import net.zincstudios.scgextra.entity.turret.TurretSeatEntity;

public final class TurretTriggerMessage {

    public static final StreamCodec<RegistryFriendlyByteBuf, TurretTriggerMessage> STREAM_CODEC = StreamCodec.of(
            (buf, message) -> buf.writeBoolean(message.held),
            buf -> new TurretTriggerMessage(buf.readBoolean()));

    private final boolean held;

    public TurretTriggerMessage(boolean held) {
        this.held = held;
    }

    public static void handle(TurretTriggerMessage message, MessageContext context) {
        context.execute(() -> {
            ServerPlayer player = context.getPlayer()
                    .filter(p -> p instanceof ServerPlayer)
                    .map(p -> (ServerPlayer) p)
                    .orElse(null);
            if (player == null || !(player.getVehicle() instanceof TurretSeatEntity seat)) {
                return;
            }
            if (player.level().getBlockEntity(seat.getTurretPos()) instanceof WreckerTurretBlockEntity turret) {
                turret.setTriggerHeld(player, message.held);
            }
        });
        context.setHandled(true);
    }
}
