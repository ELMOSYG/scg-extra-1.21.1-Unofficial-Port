package net.zincstudios.scgextra.entity;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.entity.common.client.BaseEntityRenderer;
import net.zincstudios.scgextra.entity.common.raid_summoner.RaidSummonerEntity;
import net.zincstudios.scgextra.entity.fac.FACEntities;
import net.zincstudios.scgextra.entity.neutral.NeutralEntities;
import net.zincstudios.scgextra.entity.projectile.ArmoredWhaleProjectileEntity;
import net.zincstudios.scgextra.entity.projectile.BigLumpProjectileEntity;
import net.zincstudios.scgextra.entity.projectile.FireProjectile;
import net.zincstudios.scgextra.entity.projectile.SoulFireBallRenderer;
import net.zincstudios.scgextra.entity.projectile.SoulFireball;
import net.zincstudios.scgextra.entity.rrc.RRCEntities;
import net.zincstudios.scgextra.entity.whaler.WhalerEntities;
import net.zincstudios.scgextra.item.ModItems;
import net.zincstudios.scgextra.item.curios.MedKitRenderer;
import net.zincstudios.scgextra.item.curios.MediPackRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.ribs.scguns.entity.client.EnemyProjectileRenderer;

/**
 * 实体注册表。
 *
 * <p>阶段 4 按派系分批接回：目前 RRC / FAC / neutral / whaler 已完成。
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, SCGExtra.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<RaidSummonerEntity>> RAID_SUMMONER = ENTITY_TYPES
            .register("raid_summoner", () -> EntityType.Builder.of(RaidSummonerEntity::new, MobCategory.MONSTER)
                    .updateInterval(1)
                    .sized(0.5F, 0.5F)
                    .build("raid_summoner"));

    public static final DeferredHolder<EntityType<?>, EntityType<ArmoredWhaleProjectileEntity>> WHALE_PROJECTILE = ENTITY_TYPES
            .register("whale_tank_projectile", () -> EntityType.Builder.of(ArmoredWhaleProjectileEntity::create, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("whale_tank_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<BigLumpProjectileEntity>> BIG_LUMP_PROJECTILE = ENTITY_TYPES
            .register("big_lump_projectile", () -> EntityType.Builder.of(BigLumpProjectileEntity::create, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("big_lump_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<FireProjectile>> FIRE_PROJECTILE = ENTITY_TYPES
            .register("fire_projectile", () -> EntityType.Builder.of(FireProjectile::create, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .setShouldReceiveVelocityUpdates(true)
                    .build("fire_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<SoulFireball>> LARGE_SOUL_FIREBALL = ENTITY_TYPES
            .register("soul_fireball", () -> EntityType.Builder.<SoulFireball>of(
                            SoulFireball::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(3)
                    .build("soul_fireball"));

    // TODO(阶段 4): TURRET_SEAT（依赖 entity/turret 包）

    public static final DeferredHolder<EntityType<?>, EntityType<net.zincstudios.scgextra.entity.turret.TurretSeatEntity>> TURRET_SEAT =
            ENTITY_TYPES.register("turret_seat", () -> EntityType.Builder
                    .<net.zincstudios.scgextra.entity.turret.TurretSeatEntity>of(
                            net.zincstudios.scgextra.entity.turret.TurretSeatEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .noSummon()
                    .fireImmune()
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .build("turret_seat"));

    public static void register(IEventBus modEventBus) {
        ModBrainMemories.register(modEventBus);
        ModBrainSensors.register(modEventBus);
        ModBrainActivities.register(modEventBus);

        ENTITY_TYPES.register(modEventBus);

        RRCEntities.register(modEventBus);
        FACEntities.register(modEventBus);
        NeutralEntities.register(modEventBus);
        WhalerEntities.register(modEventBus);
        net.zincstudios.scgextra.entity.piglin.PiglinEntities.register(modEventBus);
        net.zincstudios.scgextra.entity.cog.COGEntities.register(modEventBus);
        net.zincstudios.scgextra.entity.asgharian.AsgharianEntities.register(modEventBus);
        net.zincstudios.scgextra.entity.wreckers.WreckersEntities.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.entity.turret.TurretManningHandler::onLeftClickBlock);
        NeoForge.EVENT_BUS.addListener(net.zincstudios.scgextra.entity.turret.TurretManningHandler::onAttackEntity);

        modEventBus.addListener(ModEntities::registerAttributes);
        NeoForge.EVENT_BUS.addListener(EntityAdjustments::onEntityJoin);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ModEntities::onClientSetup);
        }
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(RAID_SUMMONER.get(), RaidSummonerEntity.createAttributes().build());
    }

    @OnlyIn(value = Dist.CLIENT)
    private static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(RAID_SUMMONER.get(),
                ctx -> new BaseEntityRenderer<>(ctx, SCGExtra.asResource("whaler/net")));
        EntityRenderers.register(WHALE_PROJECTILE.get(), EnemyProjectileRenderer::new);
        EntityRenderers.register(BIG_LUMP_PROJECTILE.get(), EnemyProjectileRenderer::new);
        EntityRenderers.register(FIRE_PROJECTILE.get(), EnemyProjectileRenderer::new);
        EntityRenderers.register(LARGE_SOUL_FIREBALL.get(), SoulFireBallRenderer::new);
        EntityRenderers.register(TURRET_SEAT.get(), NoopRenderer::new);
        CuriosRendererRegistry.register(ModItems.MEDKIT.get(), MedKitRenderer::new);
        CuriosRendererRegistry.register(ModItems.MEDIPACK.get(), MediPackRenderer::new);
    }
}
