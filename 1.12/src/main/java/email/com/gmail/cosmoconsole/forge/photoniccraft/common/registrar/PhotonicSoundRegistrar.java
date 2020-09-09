package email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * PhotonicCraft's own sounds are registered here.
 */
public class PhotonicSoundRegistrar {

	private static SoundEvent goRegisterSound(IForgeRegistry<SoundEvent> registry, ResourceLocation resourceLocation) {
		SoundEvent se = new SoundEvent(resourceLocation);
		se.setRegistryName(resourceLocation);
		registry.register(se);
		return se;
	}
	
	public static void registerSounds(IForgeRegistry<SoundEvent> registry) {
		ModPhotonicCraft.microwave_ambient = goRegisterSound(registry, new ResourceLocation(ModPhotonicCraft.MODID, "microwave.ambient"));
		ModPhotonicCraft.microwave_beep = goRegisterSound(registry, new ResourceLocation(ModPhotonicCraft.MODID, "microwave.beep"));
		ModPhotonicCraft.microwave_start = goRegisterSound(registry, new ResourceLocation(ModPhotonicCraft.MODID, "microwave.start"));
		ModPhotonicCraft.deathray_fire = goRegisterSound(registry, new ResourceLocation(ModPhotonicCraft.MODID, "deathray.fire"));
	}

	private PhotonicSoundRegistrar() {
	}
}
