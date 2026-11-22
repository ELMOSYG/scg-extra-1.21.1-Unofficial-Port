package net.zincstudios.scgextra.worldgen.structure.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.zincstudios.scgextra.worldgen.structure.ModStructureProcessors;

/**
 * 给结构模板里的实体写入 {@code PersistenceRequired}，避免刷出来的结构怪被自然清除/反复刷新。
 * 对应 4.1.0 的 {@code persist_mobs_processor}。
 */
public class MobPersistProcessor extends StructureProcessor {

    public static final MapCodec<MobPersistProcessor> CODEC = MapCodec.unit(MobPersistProcessor::new);

    @Override
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader level, BlockPos pos,
                                                               StructureTemplate.StructureEntityInfo rawEntityInfo,
                                                               StructureTemplate.StructureEntityInfo entityInfo,
                                                               StructurePlaceSettings settings,
                                                               StructureTemplate template) {
        if (rawEntityInfo != null) {
            rawEntityInfo.nbt.putBoolean("PersistenceRequired", true);
        }
        if (entityInfo != null) {
            entityInfo.nbt.putBoolean("PersistenceRequired", true);
        }
        return super.processEntity(level, pos, rawEntityInfo, entityInfo, settings, template);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructureProcessors.MOB_PERSISTENCE_PROCESSOR.get();
    }
}
