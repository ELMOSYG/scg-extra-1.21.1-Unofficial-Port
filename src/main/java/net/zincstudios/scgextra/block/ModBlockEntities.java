package net.zincstudios.scgextra.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.block.electrical_wires.ElectricalWiresBlockEntity;
import net.zincstudios.scgextra.block.wreckerturret.WreckerTurretBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SCGExtra.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricalWiresBlockEntity>> ELECTRICAL_WIRES =
            BLOCK_ENTITIES.register("electrical_wires", () -> BlockEntityType.Builder
                    .of(ElectricalWiresBlockEntity::new, ModBlocks.ELECTRICAL_WIRES.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WreckerTurretBlockEntity>> WRECKER_TURRET =
            BLOCK_ENTITIES.register("wrecker_turret", () -> BlockEntityType.Builder
                    .of(WreckerTurretBlockEntity::new, ModBlocks.WRECKER_TURRET.get())
                    .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
        eventBus.addListener(ModBlockEntities::registerCapabilities);
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ELECTRICAL_WIRES.get(),
                ElectricalWiresBlockEntity::getEnergyStorage);
    }
}
