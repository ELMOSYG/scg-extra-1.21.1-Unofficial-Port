package net.zincstudios.scgextra;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

import net.zincstudios.scgextra.attributes.SCGEAttributes;
import net.zincstudios.scgextra.block.ModBlockEntities;
import net.zincstudios.scgextra.block.ModBlocks;
import net.zincstudios.scgextra.effects.ModEffects;
import net.zincstudios.scgextra.item.ModItems;
import net.zincstudios.scgextra.particle.ModParticleTypes;
import net.zincstudios.scgextra.sounds.ModSounds;

import org.slf4j.Logger;

/**
 * SCG Extra — 1.21.1 NeoForge port.
 *
 * <p>阶段 1（注册层）只注册物品/方块/声音/效果/粒子/属性。
 * 实体、袭击、网络、世界生成、Mixin 与 datagen 在后续阶段接回（见 PORTING-CHECKLIST.md）。
 */
@SuppressWarnings("unused")
@Mod(SCGExtra.MOD_ID)
public class SCGExtra
{
    public static final String MOD_ID = "scgextra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SCGExtra(IEventBus modEventBus, ModContainer modContainer)
    {
        ModEffects.register(modEventBus);
        // 护甲材料必须在物品之前注册：物品构造时会读取 ArmorMaterial 的 id 决定耐久。
        net.zincstudios.scgextra.item.armor.ModArmorMaterials.register(modEventBus);
        ModItems.register(modEventBus);
        ModSounds.register(modEventBus);
        ModParticleTypes.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        SCGEAttributes.register(modEventBus);
        net.zincstudios.scgextra.entity.ModEntities.register(modEventBus);
        net.zincstudios.scgextra.worldgen.structure.ModStructures.register(modEventBus);
        net.zincstudios.scgextra.worldgen.structure.ModStructureProcessors.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        modEventBus.addListener(CommonConfig::onLoad);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(CommonConfig::onReload);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            NeoForge.EVENT_BUS.addListener(
                    net.zincstudios.scgextra.entity.common.client.GunFlashHandler::onTick);
        }

        // 袭击系统 / 数据 / 命令
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.raid.WaveRaidManager::onLevelTick);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.raid.WaveRaidManager::onLevelLoad);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.entity.Faction::onTagsUpdated);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.data.RaidDataLoader::onAddReloadListeners);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.debug.DevTestCommands::registerCommands);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.SpawnEventHandler::onCheckSpawn);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.SpawnEventHandler::onFinalizeSpawn);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.SpawnEventHandler::onExplosionDetonate);

        // TODO(阶段 9): DataGenerators。
    }

    private void commonSetup(final net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent event) {
        event.enqueueWork(net.zincstudios.scgextra.network.SCGEPacketHandler::init);
        event.enqueueWork(() -> {
            net.zincstudios.scgextra.worldgen.biome.ModTerrablender.registerBiomes();
            terrablender.api.SurfaceRuleManager.addSurfaceRules(
                    terrablender.api.SurfaceRuleManager.RuleCategory.OVERWORLD,
                    MOD_ID,
                    net.zincstudios.scgextra.worldgen.biome.surface.ModSurfaceRules.makeRules());
        });
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
