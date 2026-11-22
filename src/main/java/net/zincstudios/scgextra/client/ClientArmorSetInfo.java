package net.zincstudios.scgextra.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.zincstudios.scgextra.item.armor.ArmorSet;

@net.neoforged.api.distmarker.OnlyIn(net.neoforged.api.distmarker.Dist.CLIENT)
public final class ClientArmorSetInfo {

    private ClientArmorSetInfo() {
    }

    public static int piecesWorn(ArmorSet set) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return 0;
        }
        return ArmorSet.getArmorSetCounts(player).getOrDefault(set, 0);
    }
}
