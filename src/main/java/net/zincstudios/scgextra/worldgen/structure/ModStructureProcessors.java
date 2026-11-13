package net.zincstudios.scgextra.worldgen.structure;

import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.worldgen.structure.processors.CogFactoryProcessor;
import net.zincstudios.scgextra.worldgen.structure.processors.WarshipProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModStructureProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> STRUCTURE_PROCESSOR_TYPES = DeferredRegister
            .create(Registries.STRUCTURE_PROCESSOR, SCGExtra.MOD_ID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<CogFactoryProcessor>> COG_FACTORY_PROCESSOR = STRUCTURE_PROCESSOR_TYPES.register("cog_factory_processor", ()->()->CogFactoryProcessor.CODEC);
    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<WarshipProcessor>> WARSHIP_PROCESSOR = STRUCTURE_PROCESSOR_TYPES.register("warship_processor", ()->()->WarshipProcessor.CODEC);

    public static void register(IEventBus modEventBus) {
        STRUCTURE_PROCESSOR_TYPES.register(modEventBus);
    }
}
