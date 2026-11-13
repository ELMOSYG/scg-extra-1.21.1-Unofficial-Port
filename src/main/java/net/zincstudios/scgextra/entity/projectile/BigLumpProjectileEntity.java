package net.zincstudios.scgextra.entity.projectile;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BigLumpProjectileEntity extends ArmoredWhaleProjectileEntity{

    public BigLumpProjectileEntity(net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.projectile.AbstractArrow> type, Level world) {
        super(type, world);
    }

    public BigLumpProjectileEntity(Level world, LivingEntity shooter) {
        super(world, shooter);
    }

    public static BigLumpProjectileEntity create(net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.Entity> type, Level world) {
        return new BigLumpProjectileEntity((net.minecraft.world.entity.EntityType<? extends net.minecraft.world.entity.projectile.AbstractArrow>) type, world);
    }
    @Override
    public double getBaseDamage() {
        return 4;
    }
}
