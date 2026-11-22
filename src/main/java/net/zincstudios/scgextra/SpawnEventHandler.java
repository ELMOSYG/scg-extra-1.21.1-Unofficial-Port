package net.zincstudios.scgextra;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.zincstudios.scgextra.entity.asgharian.surgeon.AsgharSurgeonEntity;
import net.zincstudios.scgextra.entity.asgharian.flamer.AsgharFlamerEntity;
import net.zincstudios.scgextra.entity.cog.bombardier.CogBombardierEntity;
import net.zincstudios.scgextra.entity.cog.venator.CogVenatorEntity;
import net.zincstudios.scgextra.entity.fac.bluecoat.FacBluecoatEntity;
import net.zincstudios.scgextra.entity.fac.commissar.FacCommissarEntity;
import net.zincstudios.scgextra.entity.fac.trench_goblin.FacTrenchGoblinEntity;
import net.zincstudios.scgextra.entity.fac.trench_sniper.FacTrenchSniperEntity;
import net.zincstudios.scgextra.entity.rrc.oppressor.OppressorEntity;
import net.zincstudios.scgextra.entity.rrc.tallman.TallmanEntity;
import net.zincstudios.scgextra.entity.wreckers.wrecker_green.WreckerGreenEntity;
import net.zincstudios.scgextra.entity.wreckers.wrecker_jumbo.WreckerJumboEntity;
import net.zincstudios.scgextra.entity.wreckers.wrecker_turret_gunner.WreckerTurretGunnerEntity;
import net.zincstudios.scgextra.item.ModItems;
import net.zincstudios.scgextra.worldgen.biome.ModBiomes;
import top.ribs.scguns.entity.projectile.RocketEntity;

/**
 * 对应 4.1.0 的 {@code SCGExtra.SpawnEvent}：
 * <ul>
 *   <li>战区里禁止动物自然生成；</li>
 *   <li>结构刷出的特定枪手/单位设为 persist，防止被自然清除或反复补刷；</li>
 *   <li>破坏者 RPG / 破坏者·绿 的爆炸不炸掉掉落物。</li>
 * </ul>
 */
public final class SpawnEventHandler {

    private SpawnEventHandler() {
    }

    @SubscribeEvent
    public static void onCheckSpawn(MobSpawnEvent.PositionCheck event) {
        LevelAccessor level = event.getLevel();
        if (event.getEntity() instanceof Animal
                && level.getBiome(event.getEntity().blockPosition()).is(ModBiomes.WARZONE_BIOME)) {
            event.setResult(MobSpawnEvent.PositionCheck.Result.FAIL);
        }
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (event.getSpawnType() != MobSpawnType.STRUCTURE) {
            return;
        }
        var mob = event.getEntity();
        if (mob instanceof FacTrenchSniperEntity
                || mob instanceof FacTrenchGoblinEntity
                || mob instanceof FacBluecoatEntity
                || mob instanceof FacCommissarEntity
                || mob instanceof TallmanEntity
                || mob instanceof OppressorEntity
                || mob instanceof CogVenatorEntity
                || mob instanceof CogBombardierEntity
                || mob instanceof WreckerTurretGunnerEntity
                || mob instanceof WreckerJumboEntity
                || mob instanceof AsgharFlamerEntity
                || mob instanceof AsgharSurgeonEntity) {
            mob.setPersistenceRequired();
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        var source = event.getExplosion().getDirectSourceEntity();
        boolean fromWreckerRpg = source instanceof RocketEntity rocket
                && rocket.getShooter() instanceof LivingEntity shooter
                && shooter.getMainHandItem().is(ModItems.WRECKER_RPG.get());
        boolean fromWreckerGreen = source instanceof WreckerGreenEntity;

        if (fromWreckerRpg || fromWreckerGreen) {
            event.getAffectedEntities().removeIf(entity -> entity instanceof ItemEntity);
        }
    }
}
