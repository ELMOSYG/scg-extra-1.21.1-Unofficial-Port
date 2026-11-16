package net.zincstudios.scgextra.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.zincstudios.scgextra.item.armor.ArmorSet;
import net.zincstudios.scgextra.item.armor.ArmorSets;

/**
 * 客户端专用：读取玩家当前穿着了几件某套装。
 *
 * <p>计数器属性是同步的（{@code scgextra:<set>}，每件 +0.5），所以客户端可以直接读出件数，
 * 不需要自己扫描四个装备槽。</p>
 */
@OnlyIn(Dist.CLIENT)
public final class ClientArmorSetInfo {

    private ClientArmorSetInfo() {
    }

    /** 当前玩家穿着的该套装件数（0-4）。没有玩家/属性时返回 0。 */
    public static int piecesWorn(ArmorSet set) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return 0;
        }
        Holder<Attribute> counter = ArmorSets.getCounter(set);
        if (counter == null) {
            return 0;
        }
        AttributeInstance instance = player.getAttribute(counter);
        if (instance == null) {
            return 0;
        }
        return Mth.clamp((int) Math.round(instance.getValue() / 0.5D), 0, 4);
    }
}
