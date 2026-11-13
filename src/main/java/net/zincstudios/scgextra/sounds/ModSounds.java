package net.zincstudios.scgextra.sounds;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.zincstudios.scgextra.SCGExtra;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, SCGExtra.MOD_ID);

    public static void register(IEventBus eventbus) {
        WhalerSounds.init();
        RRCSounds.init();
        FACSounds.init();
        WreckersSounds.init();
        AsgharianSounds.init();
        COGSounds.init();
        NeutralSounds.init();
        PiglinSounds.init();

        SOUND_EVENTS.register(eventbus);
    }
}
