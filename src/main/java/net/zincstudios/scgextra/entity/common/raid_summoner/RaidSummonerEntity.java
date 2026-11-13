package net.zincstudios.scgextra.entity.common.raid_summoner;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 夜袭召唤者：阶段 2 只保留生成/存活逻辑。
 *
 * <p>TODO(阶段 4): 恢复 Whaler / RRC 的刷怪池（依赖各派系实体注册表）。
 */
public class RaidSummonerEntity extends Mob implements GeoEntity {

    private static long lastRaidAttempt = 0;
    private final AnimatableInstanceCache geocache = GeckoLibUtil.createInstanceCache(this);

    public RaidSummonerEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) return;
        if (this.isInWater()) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        if (this.level().getNearestPlayer(this, 200) == null) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        // TODO(阶段 4): 按 50/50 生成 Whaler / RRC 小队
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        long dayTime = level.dayTime() % 24000;
        long currentDay = level.dayTime() / 24000L;

        if (!(dayTime >= 13000 && dayTime < 23000)) return false;
        if (lastRaidAttempt == 0) {
            lastRaidAttempt = currentDay;
            return false;
        } else if (lastRaidAttempt == currentDay) {
            return false;
        } else {
            lastRaidAttempt = currentDay;
            return true;
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geocache;
    }
}
