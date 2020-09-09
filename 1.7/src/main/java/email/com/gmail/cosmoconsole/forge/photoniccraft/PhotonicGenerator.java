package email.com.gmail.cosmoconsole.forge.photoniccraft;

import java.util.Random;

import cofh.lib.world.WorldGenMinableLargeVein;
import cpw.mods.fml.common.IWorldGenerator;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class PhotonicGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        switch(world.provider.dimensionId){
        case -1:
            generateNether(world, random, chunkX * 16, chunkZ * 16);
            break;
        case 0:
            generateSurface(world, random, chunkX * 16, chunkZ * 16);
            break;
        case 1:
            generateEnd(world, random, chunkX * 16, chunkZ * 16);
            break;
        }
	}

	private void generateEnd(World world, Random rand, int chunkX, int chunkZ) {}

	private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
		if (rand.nextFloat() < 0.8)
			(new WorldGenMinable(ModPhotonicCraft.anorthosite, 90)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(128)+96, chunkZ + rand.nextInt(16));
        for(int k = 0; k < 4; k++)
        	(new WorldGenMinable(ModPhotonicCraft.erbiumOre, 10)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(64), chunkZ + rand.nextInt(16));
        for(int k = 0; k < 6; k++)
	        (new WorldGenMinable(ModPhotonicCraft.tantalumOre, 8)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(24), chunkZ + rand.nextInt(16));
        for(int k = 0; k < 3; k++)
	        (new WorldGenMinable(ModPhotonicCraft.hahniumOre, 11)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(48), chunkZ + rand.nextInt(16));
        for(int k = 0; k < 3; k++)
	        (new WorldGenMinable(ModPhotonicCraft.bariumOre, 7)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(20), chunkZ + rand.nextInt(16));
        if (rand.nextFloat() < 0.7)
	        for(int k = 0; k < 9; k++)
	        	(new WorldGenMinable(ModPhotonicCraft.mercuryOre, 6)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(32), chunkZ + rand.nextInt(16));
        if (rand.nextFloat() < 0.3)
	        for(int k = 0; k < 9; k++)
	        	(new WorldGenMinable(ModPhotonicCraft.yttriumOre, 3)).generate(world, rand, chunkX + rand.nextInt(16), rand.nextInt(10), chunkZ + rand.nextInt(16));
	}

	private void generateNether(World world, Random rand, int chunkX, int chunkZ) {}
}
