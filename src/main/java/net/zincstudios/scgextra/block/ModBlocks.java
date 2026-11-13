package net.zincstudios.scgextra.block;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zincstudios.scgextra.SCGExtra;
import net.zincstudios.scgextra.block.barbed_wires.BarbedWireBlock;
import net.zincstudios.scgextra.block.electrical_wires.ElectricalWiresBlock;
import net.zincstudios.scgextra.block.wreckerturret.WreckerTurretBlock;
import net.zincstudios.scgextra.block.wreckerturret.WreckerTurretBlockModel;
import net.zincstudios.scgextra.block.wreckerturret.WreckerTurretBlockRenderer;
import net.zincstudios.scgextra.item.ModItems;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SCGExtra.MOD_ID);

    public static final DeferredHolder<Block, Block> BARBED_WIRES = registerBlock("barbed_wires",
            () -> new BarbedWireBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN).noCollission().strength(5.0F, 6.0F).requiresCorrectToolForDrops()));
    public static final DeferredHolder<Block, Block> ELECTRICAL_WIRES = registerBlock("electrical_wires",
            () -> new ElectricalWiresBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHAIN).noCollission().strength(5.0F, 6.0F).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, WreckerTurretBlock> WRECKER_TURRET = BLOCKS.register("wrecker_turret",
            () -> new WreckerTurretBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F, 8.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    public static final DeferredHolder<Item, Item> WRECKER_TURRET_ITEM = ModItems.ITEMS.register("wrecker_turret",
            () -> new BlockItem(WRECKER_TURRET.get(), new Item.Properties()));

    public static final DeferredHolder<Block, Block> WARZONE_SPRUCE_SAPLING = registerBlock("warzone_spruce_sapling",
            () -> new net.minecraft.world.level.block.SaplingBlock(
                    net.zincstudios.scgextra.worldgen.tree.WarzoneSpruceTreeGrower.WARZONE_SPRUCE,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> block) {
        DeferredHolder<Block, T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static DeferredHolder<Item, Item> registerBlockItem(String name, DeferredHolder<Block, ?> block) {
        // TODO: consider making a separate BLOCK_ITEM item registry for this class
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ModBlocks::registerRenderers);
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.WRECKER_TURRET.get(),
                ctx -> new WreckerTurretBlockRenderer<>(new WreckerTurretBlockModel<>(SCGExtra.asResource("wrecker_turret"))));
    }
}
