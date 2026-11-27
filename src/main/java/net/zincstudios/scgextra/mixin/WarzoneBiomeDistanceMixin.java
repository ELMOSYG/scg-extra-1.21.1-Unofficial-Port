package net.zincstudios.scgextra.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.zincstudios.scgextra.CommonConfig;
import net.zincstudios.scgextra.worldgen.WorldSpawnHandler;
import net.zincstudios.scgextra.worldgen.biome.ModBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 让战区群系不在世界出生点附近生成。
 * <p>地形群系本身是气候驱动的，TerraBlender 没有“距离出生点”参数，所以这里直接拦截
 * {@link MultiNoiseBiomeSource#getNoiseBiome} 的结果：出生点半径内的战区群系统一替换成平原。</p>
 */
@Mixin(MultiNoiseBiomeSource.class)
public abstract class WarzoneBiomeDistanceMixin {

    @Inject(
            method = "getNoiseBiome(IIILnet/minecraft/world/level/biome/Climate$Sampler;)Lnet/minecraft/core/Holder;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void scgextra$excludeWarzoneNearSpawn(int x, int y, int z, Climate.Sampler sampler,
                                                  CallbackInfoReturnable<Holder<Biome>> cir) {
        int radius = CommonConfig.warzoneMinDistanceFromSpawn;
        if (radius <= 0) {
            return;
        }

        Holder<Biome> biome = cir.getReturnValue();
        if (biome == null || !biome.is(ModBiomes.WARZONE_BIOME)) {
            return;
        }

        // getNoiseBiome 使用的是 quart 坐标，1 quart = 4 个方块。
        int blockX = x << 2;
        int blockZ = z << 2;
        var center = WorldSpawnHandler.getExclusionCenter();
        long dx = (long) blockX - center.getX();
        long dz = (long) blockZ - center.getZ();
        if (dx * dx + dz * dz > (long) radius * radius) {
            return;
        }

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        cir.setReturnValue(server.registryAccess()
                .registryOrThrow(Registries.BIOME)
                .getHolderOrThrow(Biomes.PLAINS));
    }
}
