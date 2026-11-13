package net.zincstudios.scgextra.item;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.WeakHashMap;

// TODO: redo to only pierce armor and not through resistance effect
public class ArmorPiercingHandler {

    private static final WeakHashMap<LivingEntity, Float> PENDING_DAMAGE = new WeakHashMap<>();

    public static void onLivingHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker
                && attacker.getMainHandItem().getItem() instanceof ArmorPiercing) {
            PENDING_DAMAGE.put(event.getEntity(), event.getAmount());
        }
    }

    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }
        Float fullDamage = PENDING_DAMAGE.remove(event.getEntity());
        if (fullDamage != null && fullDamage > event.getNewDamage()) {
            event.setNewDamage(fullDamage);
        }
    }
}
