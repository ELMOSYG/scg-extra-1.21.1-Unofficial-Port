package net.zincstudios.scgextra.network;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import com.mrcrayfish.framework.api.network.LevelLocation;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.zincstudios.scgextra.SCGExtra;

import java.util.function.Supplier;

/**
 * Framework 0.13 网络：每个消息用 {@code StreamCodec} + handler 注册。
 */
public final class SCGEPacketHandler {

    private static FrameworkNetwork playChannel;

    private SCGEPacketHandler() {
    }

    public static void init() {
        playChannel = FrameworkAPI.createNetworkBuilder(SCGExtra.asResource("play"), 1)
                .registerPlayMessage("gun_flash", GunFlashMessage.class,
                        GunFlashMessage.STREAM_CODEC, GunFlashMessage::handle, PacketFlow.CLIENTBOUND)
                .registerPlayMessage("hold_attack", HoldAttackMessage.class,
                        HoldAttackMessage.STREAM_CODEC, HoldAttackMessage::handle, PacketFlow.SERVERBOUND)
                .registerPlayMessage("turret_trigger", TurretTriggerMessage.class,
                        TurretTriggerMessage.STREAM_CODEC, TurretTriggerMessage::handle, PacketFlow.SERVERBOUND)
                .build();
    }

    public static FrameworkNetwork getPlayChannel() {
        return playChannel;
    }

    public static void sendToNearbyPlayers(Supplier<LevelLocation> levelLocation, Object message) {
        playChannel.sendToNearbyPlayers(levelLocation, message);
    }

    public static void sendToServer(Object message) {
        playChannel.sendToServer(message);
    }

    public static void sendToPlayer(ServerPlayer player, Object message) {
        playChannel.sendToPlayer(() -> player, message);
    }
}
