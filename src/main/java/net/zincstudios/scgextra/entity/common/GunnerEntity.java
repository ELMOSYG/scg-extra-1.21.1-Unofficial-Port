package net.zincstudios.scgextra.entity.common;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import net.zincstudios.scgextra.entity.asgharian.SimpleGunAttackGoal;
import top.ribs.scguns.entity.ai.GunAttackGoal;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import top.ribs.scguns.config.GunnerMobSpawner;

// TODO: consider synced data for target entity too.
// If so, consider updating client-side rotation to look at the target to mitigate minecraft's shitty ai look controls.

// TODO: consider manipulating goal to hold fire.

// TODO: consider extensions for mobs with non-item guns.

/**
 * Base class for entities that uses guns and the GunAttackGoal goal.
 * Used for handling and fetching states from the aforementioned goal.
 * @see GunAttackGoal
 *
 * @deprecated Extend EquippedEntity and use brain instead
 */
@SuppressWarnings("unused")
@Deprecated
public abstract class GunnerEntity extends EquippedEntity {

    private static final EntityDataAccessor<Boolean> AIMING =
            SynchedEntityData.defineId(GunnerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final int CACHE_UPDATE_INTERVAL = 100;

    @SuppressWarnings("FieldMayBeFinal")
    @Nullable private GunAttackGoal<?> gunAttackGoalCache = null;  // server-side only

    protected GunnerEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    protected GunnerEntity(EntityType<? extends Monster> entity, Level level, ResourceLocation equipmentResLoc) {
        super(entity, level, equipmentResLoc);
    }

    public @Nullable GunAttackGoal<?> getGunAttackGoal() {
        if (this.level().isClientSide()) return null;

        if (this.gunAttackGoalCache != null && this.tickCount%CACHE_UPDATE_INTERVAL != 0) {
            return this.gunAttackGoalCache;
        } else {
            for(WrappedGoal goal : this.goalSelector.getAvailableGoals()){
                if(goal.getGoal() instanceof GunAttackGoal<?> gunAttackGoal){
                    return gunAttackGoal;
                }
            }
            return null;
        }
    }

    private void updateAimStates() {
        // scg-extra 自己的简化枪 AI：用它的状态机驱动瞄准动画
        for (WrappedGoal wrapped : this.goalSelector.getAvailableGoals()) {
            if (wrapped.getGoal() instanceof SimpleGunAttackGoal<?> simple) {
                var state = simple.getGoalState();
                this.entityData.set(AIMING, state == SimpleGunAttackGoal.AIMING_STATE
                        || state == SimpleGunAttackGoal.FIRING_STATE);
                return;
            }
        }

        GunAttackGoal<?> gunAttackGoal = getGunAttackGoal();
        if (gunAttackGoal == null) return;
        if (gunAttackGoal instanceof net.zincstudios.scgextra.mixin.GunAttackGoalAccessor accessor) {
            this.entityData.set(AIMING, accessor.getAimingStabilityTimer() > 0);
        }
    }

    // Can be discarded if we control setAggressive on GunAttackGoalProperly
    public boolean isAiming() {
        return this.entityData.get(AIMING);
    }

    // Called on server when firing a gun, primarily used for animation triggers
    public void onGunAttack(LivingEntity target, ItemStack itemStack) {
    }

    public Vec3 getProjectileSpawnOffset() {
        return Vec3.ZERO;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                        SpawnGroupData spawnData) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData);
        // 兜底：RRC 等 GunnerEntity 子类自身不注册枪战 goal，依赖 scguns 在出生后自动注入。
        // 这里在装备已经穿好之后立即再评估一次，避免出生 40 tick 内被打断/时序问题导致永远没有枪战 AI。
        if (!this.level().isClientSide()) {
            GunnerMobSpawner.reassessWeaponGoal(this);
        }
        return data;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AIMING, false);
    }

    @Override
    public void tick() {
        super.tick();
        updateAimStates();
    }
}
