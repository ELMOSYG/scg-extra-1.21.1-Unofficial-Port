package net.zincstudios.scgextra.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.entity.turret.TurretSeatEntity;
import net.zincstudios.scgextra.network.SCGEPacketHandler;
import net.zincstudios.scgextra.network.TurretTriggerMessage;

@EventBusSubscriber(modid = SCGExtra.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class TurretInputHandler {

    private static boolean sentHeld;

    private TurretInputHandler() {}

    public static boolean isManningTurret() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && mc.player.getVehicle() instanceof TurretSeatEntity;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.getConnection() == null) {
            sentHeld = false;
            return;
        }
        boolean held = mc.screen == null
                && mc.player.getVehicle() instanceof TurretSeatEntity
                && mc.options.keyAttack.isDown();
        if (held != sentHeld) {
            sentHeld = held;
            SCGEPacketHandler.sendToServer(new TurretTriggerMessage(held));
        }
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        if (event.isAttack() && isManningTurret()) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }
}
