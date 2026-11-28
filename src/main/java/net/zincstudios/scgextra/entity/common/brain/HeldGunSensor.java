package net.zincstudios.scgextra.entity.common.brain;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.zincstudios.scgextra.entity.ModBrainMemories;
import net.zincstudios.scgextra.entity.common.gun.HeldScorchedSimGun;
import net.zincstudios.scgextra.entity.common.gun.SimulatedGun;
import top.ribs.scguns.item.GunItem;

import java.util.Optional;
import java.util.Set;

public class HeldGunSensor extends Sensor<LivingEntity> {

    // I think we can afford a quicker scan rate for something this simple

    public HeldGunSensor() {
        this(10);
    }

    public HeldGunSensor(int scanRate) {
        super(scanRate);
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(
                ModBrainMemories.SIMULATED_GUN.get(),
                ModBrainMemories.WEAPON_IDEAL_RANGE.get(),
                ModBrainMemories.WEAPON_MAX_RANGE.get()
        );
    }

    /**
     * 外部模组（例如 Chromeware 的武器故障芯片）可能直接把主手物品清成空气。
     * 这里在主手变化时立即清掉“手持枪”的模拟枪记忆，不用等下一次 10 tick 扫描。
     * 自定义枪械（CustomScorchedSimGun）不依赖主手，不会被这里误清。
     */
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot() != EquipmentSlot.MAINHAND || !(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }
        if (event.getTo().getItem() instanceof GunItem) {
            return;
        }
        Brain<?> brain = entity.getBrain();
        if (brain.getMemory(ModBrainMemories.SIMULATED_GUN.get()).orElse(null) instanceof HeldScorchedSimGun) {
            brain.eraseMemory(ModBrainMemories.SIMULATED_GUN.get());
            brain.eraseMemory(ModBrainMemories.WEAPON_IDEAL_RANGE.get());
            brain.eraseMemory(ModBrainMemories.WEAPON_MAX_RANGE.get());
        }
    }

    @Override
    protected void doTick(ServerLevel level, LivingEntity entity) {
        Brain<?> brain = entity.getBrain();
        Optional<SimulatedGun> optional = brain.getMemory(ModBrainMemories.SIMULATED_GUN.get());

        if (!(entity.getMainHandItem().getItem() instanceof GunItem)) {
            // 枪被移除/替换成空手时，必须清掉旧的模拟枪记忆；否则 ShootTarget 会继续用
            // 已经失效的 HeldScorchedSimGun 开火，导致空指针或 AirItem 强转 GunItem。
            brain.eraseMemory(ModBrainMemories.SIMULATED_GUN.get());
            brain.eraseMemory(ModBrainMemories.WEAPON_IDEAL_RANGE.get());
            brain.eraseMemory(ModBrainMemories.WEAPON_MAX_RANGE.get());
            return;
        }

        if (optional.isEmpty() || optional.get().hasChanged(entity)) {
            SimulatedGun simGun = new HeldScorchedSimGun(entity.getMainHandItem());
            brain.setMemory(ModBrainMemories.SIMULATED_GUN.get(), simGun);
            brain.setMemory(ModBrainMemories.WEAPON_IDEAL_RANGE.get(), simGun.getIdealRange());
            brain.setMemory(ModBrainMemories.WEAPON_MAX_RANGE.get(), simGun.getMaxRange());
        }
    }
}
