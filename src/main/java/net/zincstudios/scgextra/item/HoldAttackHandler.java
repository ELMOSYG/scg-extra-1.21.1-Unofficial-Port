package net.zincstudios.scgextra.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;

/**
 * 长按攻击的服务器端状态。
 *
 * <p>TODO(阶段 6): 客户端 {@code onClientTick} 与 {@code HoldAttackMessage} 的网络同步依赖 Framework 网络重写。
 */
public class HoldAttackHandler {

    private static final HashMap<ServerPlayer, Boolean> SERVER_HELD_ATTACK = new HashMap<>();

    /** 客户端是否按住攻击键（阶段 6 由网络包更新）。 */
    private static boolean sentHeld = false;

    public static void setServerHeld(ServerPlayer player, boolean held) {
        SERVER_HELD_ATTACK.put(player, held);
    }

    public static void setClientHeld(boolean held) {
        sentHeld = held;
    }

    public static boolean isHeldAttack(Player player) {
        if (player.level().isClientSide) {
            return sentHeld;
        }
        return player instanceof ServerPlayer serverPlayer
                && SERVER_HELD_ATTACK.getOrDefault(serverPlayer, false);
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        ItemStack heldStack = player.getMainHandItem();
        if (!(heldStack.getItem() instanceof HoldAttack holdAttackItem)) {
            return;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            if (SERVER_HELD_ATTACK.getOrDefault(serverPlayer, false)) {
                holdAttackItem.onPlayerAttackTick(heldStack, player.level(), player);
            }
        } else if (sentHeld) {
            holdAttackItem.onPlayerAttackTick(heldStack, player.level(), player);
        }
    }

    public static void onClientTick(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        if (mc.player == null || mc.getConnection() == null) {
            sentHeld = false;
            return;
        }
        boolean held = mc.screen == null && mc.options.keyAttack.isDown();
        if (held != sentHeld) {
            sentHeld = held;
            net.zincstudios.scgextra.network.SCGEPacketHandler.sendToServer(
                    new net.zincstudios.scgextra.network.HoldAttackMessage(held));
        }
    }
}
