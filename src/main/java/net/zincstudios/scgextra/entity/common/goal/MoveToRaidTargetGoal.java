package net.zincstudios.scgextra.entity.common.goal;

import java.lang.ref.WeakReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

/**
 * 原版猪灵/猪灵蛮兵不参与 scg-extra 的波次袭击 AI；这里给它们一个持续走向
 * 袭击目标玩家的 goal（对应 4.1.0 的 {@code MoveToRaidTargetGoal}）。
 */
public class MoveToRaidTargetGoal extends Goal {

    private final Mob mob;
    private final WeakReference<LivingEntity> targetRef;

    public MoveToRaidTargetGoal(Mob mob, LivingEntity target) {
        this.mob = mob;
        this.targetRef = new WeakReference<>(target);
    }

    @Override
    public boolean canUse() {
        if (this.mob.isRemoved() || this.mob.isDeadOrDying()) {
            return false;
        }
        LivingEntity target = this.targetRef.get();
        if (target == null || target.isRemoved() || target.isDeadOrDying()) {
            return false;
        }
        return !this.mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }

    @Override
    public void start() {
        LivingEntity target = this.targetRef.get();
        if (target == null) {
            return;
        }
        this.mob.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, 1.0F, 4));
    }

    @Override
    public boolean canContinueToUse() {
        return true;
    }
}
