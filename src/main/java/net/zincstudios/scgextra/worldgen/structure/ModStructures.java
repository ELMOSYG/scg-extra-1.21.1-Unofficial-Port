package net.zincstudios.scgextra.worldgen.structure;

import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.worldgen.structure.structures.CampingSiteStructure;
import net.zincstudios.scgextra.worldgen.structure.structures.DestroyedMinesStructure;
import net.zincstudios.scgextra.worldgen.structure.structures.MineTrenchStructure;
import net.zincstudios.scgextra.worldgen.structure.structures.TrenchesAnchorStructure;
import net.zincstudios.scgextra.worldgen.structure.structures.WaterAvoidingStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister
            .create(Registries.STRUCTURE_TYPE, SCGExtra.MOD_ID);

    public static final DeferredHolder<StructureType<?>, StructureType<MineTrenchStructure>> MINE_TRENCH_STRUCTURE =
        STRUCTURE_TYPES.register(
            "mine_trench_structure",
            () -> () -> MineTrenchStructure.CODEC
        );

    public static final DeferredHolder<StructureType<?>, StructureType<DestroyedMinesStructure>> DESTROYED_MINE_STRUCTURE =
        STRUCTURE_TYPES.register(
            "destroyed_mine_structure",
            () -> () -> DestroyedMinesStructure.CODEC
        );

    public static final DeferredHolder<StructureType<?>, StructureType<TrenchesAnchorStructure>> TRENCHES_ANCHOR_STRUCTURE =
        STRUCTURE_TYPES.register(
            "trenches_anchor_structure",
            () -> () -> TrenchesAnchorStructure.CODEC
        );

    public static final DeferredHolder<StructureType<?>, StructureType<CampingSiteStructure>> CAMPING_SITE_STRUCTURE =
        STRUCTURE_TYPES.register(
            "camping_site_structure",
            () -> () -> CampingSiteStructure.CODEC
        );

    public static final DeferredHolder<StructureType<?>, StructureType<WaterAvoidingStructure>> WATER_AVOIDING_STRUCTURE =
        STRUCTURE_TYPES.register(
            "water_avoiding_structure",
            () -> () -> WaterAvoidingStructure.CODEC
        );

    public static void register(IEventBus modEventBus) {
        STRUCTURE_TYPES.register(modEventBus);
    }
}
