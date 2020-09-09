package email.com.gmail.cosmoconsole.forge.photoniccraft.common.registrar;

import static email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft.MODID;

import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityColossalCreeper;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityGammaEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserBeam;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserEffect;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserPointer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * PhotonicCraft's own entities are registered here.
 */
public class PhotonicEntityRegistrar {
	public static void registerEntities(IForgeRegistry<EntityEntry> registry) {
		registry
				.register(EntityEntryBuilder.create().entity(EntityColossalCreeper.class)
						.id(new ResourceLocation(MODID, "colossalcreeper"), 20).name(MODID + "_colossalcreeper")
						.egg(0x00ff00, 0x000000).tracker(80, 5, true).build());
		registry
				.register(EntityEntryBuilder.create().entity(EntityLaserBeam.class)
						.id(new ResourceLocation(MODID, "laserbeam"), 30).name(MODID + "_laserbeam")
						.tracker(128, 10, false).build());
		registry
				.register(EntityEntryBuilder.create().entity(EntityLaserEffect.class)
						.id(new ResourceLocation(MODID, "lasereffect"), 31).name(MODID + "_lasereffect")
						.tracker(64, 100, false).build());
		registry
				.register(EntityEntryBuilder.create().entity(EntityLaserPointer.class)
						.id(new ResourceLocation(MODID, "laserpointer"), 32).name(MODID + "_laserpointer")
						.tracker(64, 20, false).build());
		registry
				.register(EntityEntryBuilder.create().entity(EntityGammaEffect.class)
						.id(new ResourceLocation(MODID, "gammaeffect"), 33).name(MODID + "_gammaeffect")
						.tracker(64, 10, false).build());
	}
}
