package net.zincstudios.scgextra.block.electrical_wires;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.zincstudios.scgextra.block.ModBlockEntities;

public class ElectricalWiresBlockEntity extends BlockEntity {

    private final EnergyStorage energyStorage = new EnergyStorage(2000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (!simulate && received > 0) {
                setChanged();
                sync();
            }
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (!simulate && extracted > 0) {
                setChanged();
                sync();
            }
            return extracted;
        }

        @Override
        public boolean canReceive() {
            return this.getEnergyStored() < 2000;
        }
    };

    public ElectricalWiresBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ELECTRICAL_WIRES.get(), pos, blockState);
    }

    public IEnergyStorage getEnergyStorage(@SuppressWarnings("unused") Direction side) {
        return energyStorage;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Energy", energyStorage.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(registries, tag.get("Energy"));
        }
    }

    private void sync() {
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ElectricalWiresBlockEntity blockEntity) {
        boolean wasLit = state.getValue(ElectricalWiresBlock.LIT);
        boolean isLit = false;
        if (!level.isClientSide) {
            if (blockEntity.hasEnoughEnergy(200)) {
                isLit = true;
            }
            if (wasLit != isLit) {
                level.setBlock(pos, state.setValue(ElectricalWiresBlock.LIT, isLit), 3);
            }
            for (Direction direction : Direction.values()) {
                BlockEntity adjacentEntity = level.getBlockEntity(pos.relative(direction));
                if (!(adjacentEntity instanceof ElectricalWiresBlockEntity ewbe)) {
                    continue;
                }
                IEnergyStorage handler = level.getCapability(Capabilities.EnergyStorage.BLOCK,
                        pos.relative(direction), direction.getOpposite());
                if (handler == null) {
                    continue;
                }
                if (ewbe.getEnergy() + 200 <= blockEntity.getEnergy()) {
                    if (handler.canReceive()) {
                        int extracted = blockEntity.energyStorage.extractEnergy(200, true);
                        int accepted = handler.receiveEnergy(extracted, false);
                        blockEntity.energyStorage.extractEnergy(accepted, false);
                        blockEntity.setChanged();
                        blockEntity.sync();
                    }
                }
            }
        }
    }

    private boolean hasEnoughEnergy(int amount) {
        return energyStorage.getEnergyStored() >= amount;
    }

    public void consumeEnergy(int amount) {
        energyStorage.extractEnergy(amount, false);
        setChanged();
        sync();
    }

    public void addEnergy(int amount) {
        energyStorage.receiveEnergy(amount, false);
        setChanged();
        sync();
    }

    public int getEnergy() {
        return energyStorage.getEnergyStored();
    }
}
