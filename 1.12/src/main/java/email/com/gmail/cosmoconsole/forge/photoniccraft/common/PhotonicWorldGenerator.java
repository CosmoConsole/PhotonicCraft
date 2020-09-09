package email.com.gmail.cosmoconsole.forge.photoniccraft.common;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

/**
 * The world generator that is responsible for generating PhotonicCraft ores.
 */
public class PhotonicWorldGenerator implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
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

	private void generateEnd(World world, Random rand, int chunkX, int chunkZ) {
	}

	private void generateNether(World world, Random rand, int chunkX, int chunkZ) {
	}

	private void generateSafe(WorldGenMinable worldGenMinable, World world, Random rand, BlockPos blockPos) {
		worldGenMinable.generate(world, rand, blockPos);
	}

	private void generateSurface(World world, Random rand, int chunkX, int chunkZ) {
		if (rand.nextFloat() < 0.9)
			generateSafe((new WorldGenMinable(PhotonicBlocks.anorthosite.getDefaultState(), 50)), world, rand,
					new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(128) + 80, chunkZ + rand.nextInt(16)));
		for (int k = 0; k < 4; k++)
			generateSafe((new WorldGenMinable(PhotonicBlocks.erbiumOre.getDefaultState(), 8)), world, rand,
					new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(64), chunkZ + rand.nextInt(16)));
		for (int k = 0; k < 4; k++)
			generateSafe((new WorldGenMinable(PhotonicBlocks.tantalumOre.getDefaultState(), 7)), world, rand,
					new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(24), chunkZ + rand.nextInt(16)));
		for (int k = 0; k < 3; k++)
			generateSafe((new WorldGenMinable(PhotonicBlocks.hahniumOre.getDefaultState(), 6)), world, rand,
					new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(48), chunkZ + rand.nextInt(16)));
		for (int k = 0; k < 2; k++)
			generateSafe((new WorldGenMinable(PhotonicBlocks.rheniumOre.getDefaultState(), 6)), world, rand,
					new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(20), chunkZ + rand.nextInt(16)));
		if (rand.nextFloat() < 0.9)
			for (int k = 0; k < 4; k++)
				generateSafe((new WorldGenMinable(PhotonicBlocks.mercuryOre.getDefaultState(), 5)), world, rand,
						new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(32), chunkZ + rand.nextInt(16)));
		if (rand.nextFloat() < 0.4)
			for (int k = 0; k < 4; k++)
				generateSafe((new WorldGenMinable(PhotonicBlocks.yttriumOre.getDefaultState(), 2)), world, rand,
						new BlockPos(chunkX + rand.nextInt(16), rand.nextInt(10), chunkZ + rand.nextInt(16)));
	}
}
