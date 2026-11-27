package net.zincstudios.scgextra.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.worldgen.biome.ModBiomes;

/**
 * 保证新世界的世界出生点不在战区群系里。
 * <p>战区群系在部分地形 mod 下会覆盖出生点，前期体验很差；这里在服务端启动时检查，
 * 如果出生点落在战区，就沿螺旋向最近的非战区地表位置迁移世界出生点。</p>
 */
public final class WorldSpawnHandler {

    private static final int SEARCH_STEP = 16;
    private static final int MAX_RADIUS = 2048;
    private static volatile BlockPos exclusionCenter = BlockPos.ZERO;

    private WorldSpawnHandler() {
    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        ensureSpawnOutsideWarzone(event.getServer());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ensureSpawnOutsideWarzone(event.getServer());
    }

    private static void ensureSpawnOutsideWarzone(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) {
            return;
        }

        BlockPos spawn = overworld.getSharedSpawnPos();
        exclusionCenter = spawn;
        if (!overworld.getBiome(spawn).is(ModBiomes.WARZONE_BIOME)) {
            return;
        }

        BlockPos replacement = findNearestNonWarzoneSpawn(overworld, spawn);
        if (replacement == null) {
            SCGExtra.LOGGER.warn("World spawn is inside the warzone biome, but no replacement spawn was found within {} blocks.", MAX_RADIUS);
            return;
        }

        overworld.setDefaultSpawnPos(replacement, 0.0F);
        exclusionCenter = replacement;
        SCGExtra.LOGGER.info("Moved world spawn out of the warzone biome: {} -> {}", spawn, replacement);
    }

    public static BlockPos getExclusionCenter() {
        return exclusionCenter;
    }

    private static BlockPos findNearestNonWarzoneSpawn(ServerLevel level, BlockPos origin) {
        for (int radius = 0; radius <= MAX_RADIUS; radius += SEARCH_STEP) {
            for (int dx = -radius; dx <= radius; dx += SEARCH_STEP) {
                for (int dz = -radius; dz <= radius; dz += SEARCH_STEP) {
                    if (radius > 0 && Math.max(Math.abs(dx), Math.abs(dz)) != radius) {
                        continue;
                    }

                    int x = origin.getX() + dx;
                    int z = origin.getZ() + dz;
                    if (!level.isInWorldBounds(new BlockPos(x, level.getMinBuildHeight(), z))) {
                        continue;
                    }

                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    BlockPos candidate = new BlockPos(x, y, z);
                    if (!level.getBiome(candidate).is(ModBiomes.WARZONE_BIOME)) {
                        return candidate;
                    }
                }
            }
        }
        return null;
    }
}
