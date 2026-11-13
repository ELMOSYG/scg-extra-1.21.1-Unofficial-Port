package net.zincstudios.scgextra.worldgen.tree;

import java.util.Optional;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.zincstudios.scgextra.worldgen.ModConfiguredFeatures;

/** 1.21 的 TreeGrower 是 final 类，不能再继承，改用静态实例。 */
public final class WarzoneSpruceTreeGrower {

    public static final TreeGrower WARZONE_SPRUCE = new TreeGrower(
            "warzone_spruce",
            Optional.empty(),
            Optional.of(ModConfiguredFeatures.WARZONE_SPRUCE_KEY),
            Optional.empty());

    private WarzoneSpruceTreeGrower() {
    }
}
