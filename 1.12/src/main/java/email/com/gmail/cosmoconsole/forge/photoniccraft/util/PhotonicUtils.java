package email.com.gmail.cosmoconsole.forge.photoniccraft.util;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.apache.commons.io.output.ByteArrayOutputStream;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import email.com.gmail.cosmoconsole.forge.photoniccraft.client.network.PhotonicRadioPacket;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.PhotonicItems;
import email.com.gmail.cosmoconsole.forge.photoniccraft.common.entity.EntityLaserPointer;
import io.netty.buffer.ByteBuf;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PhotonicUtils {
	public static final String TAG_ITEM_RF_ENERGY = "Energy";

	public static Random rand = new Random();

	static Deflater deflater;

	static Inflater inflater;

	static byte[] compressionBuffer = new byte[PhotonicRadioPacket.PACKET_SIZE * 4];

	private static Method registerCriterion;

	public static double angleDifference(double x, double y) {
		double P = (float) (Math.abs(x - y) % (2 * Math.PI));
		return (double) (P > Math.PI ? (2 * Math.PI) - P : P);
	}

	public static float angleDifference(float x, float y) {
		float P = (float) (Math.abs(x - y) % (2 * Math.PI));
		return (float) (P > Math.PI ? (2 * Math.PI) - P : P);
	}

	public static byte[] cloneByteArray(byte[] d) {
		return Arrays.copyOf(d, d.length);
	}

	public static byte[] compressByteArray(byte[] audio) {
		deflater = new Deflater();
		deflater.setInput(audio);
		deflater.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(audio.length);
		byte[] buf = new byte[1024];
		while (!deflater.finished())
			bos.write(buf, 0, deflater.deflate(buf));
		try {
			bos.close();
		} catch (IOException e) {
			return audio;
		}
		return bos.toByteArray();
	}

	public synchronized static void compressByteArrayToByteBuf(ByteBuf buf, byte[] audio, int start, int length) {
		Deflater deflater = new Deflater();
		deflater.setInput(audio, start, length);
		deflater.finish();
		int sz = deflater.deflate(compressionBuffer);
		buf.writeInt(sz);
		buf.writeBytes(compressionBuffer, 0, sz);
	}

	public static int convertEnumFacingToInt(EnumFacing facing) {
		switch (facing) {
		case DOWN:
			return 0;
		case UP:
			return 1;
		case NORTH:
			return 2;
		case SOUTH:
			return 3;
		case WEST:
			return 4;
		case EAST:
			return 5;
		}
		return 2;
	}

	public static World convertIntegerToWorld(int world) {
		return DimensionManager.getWorld(world);
	}

	public static EnumFacing convertIntToEnumFacing(int face) {
		switch (face) {
		case 0:
			return EnumFacing.DOWN;
		case 1:
			return EnumFacing.UP;
		case 2:
			return EnumFacing.NORTH;
		case 3:
			return EnumFacing.SOUTH;
		case 4:
			return EnumFacing.WEST;
		case 5:
			return EnumFacing.EAST;
		}
		return EnumFacing.NORTH;
	}

	public static EnumFacing convertIntToEnumFacingForceHorizontal(int face) {
		switch (face) {
		case 2:
			return EnumFacing.NORTH;
		case 3:
			return EnumFacing.SOUTH;
		case 4:
			return EnumFacing.WEST;
		case 5:
			return EnumFacing.EAST;
		}
		return EnumFacing.NORTH;
	}

	public static int convertWorldToInteger(World world) {
		return world.provider.getDimension();
	}

	public static void debugMessage(String string) {
		if (ModPhotonicCraft.DEBUG_VERSION) {
			System.out.println(string);
		}
	}

	public synchronized static byte[] decompressByteArray(byte[] audio) throws DataFormatException {
		inflater = new Inflater();
		inflater.setInput(audio);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(audio.length * 2);
		try {
			while (!inflater.finished())
				bos.write(compressionBuffer, 0, inflater.inflate(compressionBuffer));
			bos.close();
		} catch (IOException e2) {
			return audio;
		}
		return bos.toByteArray();
	}

	public static boolean doesPathHaveAncestor(Path path, Path ancestor) {
		return path.startsWith(ancestor);
	}

	public static void dumpHex(String msg, byte[] data, int start, int length) {
		StringBuilder b = new StringBuilder(String.format("%s <%d> ", msg, length));
		for (int i = start; i < start + length; ++i) {
			b.append(String.format("%02x", data[i] & 0xFF));
			b.append(' ');
		}
		PhotonicUtils.debugMessage(b.toString().trim());
	}

	public static float frac(float z) {
		return (float) (z - Math.floor(z));
	}

	public static double frac(double z) {
		return (double) (z - Math.floor(z));
	}

	@Deprecated
	public static int getBlockMetadata(World world, BlockPos pos) {
		IBlockState bs = world.getBlockState(pos);
		return bs.getBlock().getMetaFromState(bs);
	}

	@Deprecated
	public static int getBlockMetadata_XYZ(World worldObj, int nx, int ny, int nz) {
		BlockPos bp = new BlockPos(nx, ny, nz);
		return getBlockMetadata(worldObj, bp);
	}

	public static AxisAlignedBB getCollisionBoundingBoxSafe(Entity entity2) {
		AxisAlignedBB aabb = entity2.getCollisionBoundingBox();
		if (aabb == null) {
			aabb = entity2.getRenderBoundingBox();
			if (aabb == null) {
				return entity2.getEntityBoundingBox();
			}
		}
		return aabb;
	}

	public static EntityPlayer getPlayerFromUUID(MinecraftServer srv, UUID u) {
		return srv.getPlayerList().getPlayerByUUID(u);
	}

	public static List<EnumFacing> getShuffledFaces() {
		List<EnumFacing> ml = new ArrayList<EnumFacing>();
		for (EnumFacing face : EnumFacing.VALUES)
			ml.add(face);
		Collections.shuffle(ml);
		return ml;
	}

	public static boolean hasNoPoweredOnRadio(EntityPlayer player) {
		for (ItemStack i : PhotonicUtils.multipleLists(player.inventory.mainInventory,
				player.inventory.offHandInventory, player.inventory.armorInventory)) {
			if (!i.isEmpty() && i.getItem() == PhotonicItems.pocketRadio && i.hasTagCompound()
					&& i.getTagCompound().getBoolean("powered")) {
				return false;
			}
		}
		return true;
	}

	public static boolean isInside(int a, int i, int j) {
		return a >= i && a <= j;
	}

	public static boolean itemTypeEquals(Item a, Item b) {
		return (a == b) || (a.equals(b)) || (a.getRegistryName().compareTo(b.getRegistryName()) == 0);
	}

	public static <T> ListConcatenator<T> multipleLists(List<T>... args) {
		return new ListConcatenator<T>(args);
	}

	public static <T> boolean nullSafeEquals(T a, T b) {
		if (a == null || b == null)
			return a == null && b == null;
		return a.equals(b);
	}

	public static boolean readBoolProperty(IBlockState blockState, PropertyBool pb) {
		return blockState.getValue(pb);
	}

	public static EnumFacing readDirectionProperty(IBlockState blockState, PropertyDirection pface) {
		return blockState.getValue(pface);
	}

	public static int readDirectionPropertyAsInteger(IBlockState blockState, PropertyDirection pface) {
		return convertEnumFacingToInt(readDirectionProperty(blockState, pface));
	}

	public static int readIntegerProperty(IBlockState blockState, PropertyInteger pint) {
		return blockState.getValue(pint);
	}

	public static <T extends ICriterionInstance> ICriterionTrigger<T> registerAdvancementTrigger(
			ICriterionTrigger<T> trigger) {
		if (registerCriterion == null) {
			registerCriterion = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a",
					ICriterionTrigger.class);
			registerCriterion.setAccessible(true);
		}
		try {
			trigger = (ICriterionTrigger<T>) registerCriterion.invoke(null, trigger);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.err.println(
					"Cannot register advancement criterion " + trigger.getId() + ". I probably need some fixes!");
			e.printStackTrace();
		}
		return trigger;
	}

	public static double sigsub(double i, double n) {
		return i - Math.signum(i) * n;
	}

	public static void silentNoiseGenerate(byte[] noise) {
		for (int i = 0; i < noise.length; ++i)
			noise[i] = (byte) (rand.nextInt(32) + 112);
	}

	public static int unsign(byte b) {
		int i = b;
		if (i < 0)
			return i + 256;
		return i;
	}

	public static boolean worldEquals(World a, World b) {
		if (a == null || b == null) return a == b;
		return a.getWorldInfo().getWorldName().equals(b.getWorldInfo().getWorldName())
				&& a.provider.getDimension() == b.provider.getDimension()
				&& a.getTotalWorldTime() == b.getTotalWorldTime();
	}

	@SideOnly(Side.CLIENT)
	public static void applyInfraredEffect() {
		try {
			Minecraft.getMinecraft().entityRenderer
					.loadShader(new ResourceLocation(ModPhotonicCraft.MODID, "shaders/post/infrared.json"));
			Minecraft.getMinecraft().entityRenderer.disableLightmap();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void removeInfraredEffect() {
		Minecraft.getMinecraft().entityRenderer.enableLightmap();
		Minecraft.getMinecraft().entityRenderer.stopUseShader();
	}

	public static int getStainedGlassColor(IBlockState blockState) {
		return blockState.getBlock().getMetaFromState(blockState);
	}

	public static NonNullList<ItemStack> getHotbarInventory(EntityPlayer p) {
		NonNullList<ItemStack> n = NonNullList.<ItemStack>create();
		List<ItemStack> hotbar = p.inventory.mainInventory.subList(0, p.inventory.getHotbarSize());
		for (ItemStack i: hotbar) {
			if (i == null) 
				i = ItemStack.EMPTY;
			n.add(i);
		}
		return n;
	}

	public static int mainSlotToSlotIndex(int i) {
		return i;
	}

	public static int offHandSlotToSlotIndex(int i) {
		return -106 + i;
	}

	public static int armorSlotToSlotIndex(int i) {
		return 100 + i;
	}

	public static void sendPacketToPlayer(SimpleNetworkWrapper network, EntityPlayer player,
			IMessage message) {
		if (player instanceof EntityPlayerMP) {
			network.sendTo(message, (EntityPlayerMP) player);
		} else { // singleplayer, send to "all"
			network.sendToAll(message);
		}
	}

	public static Object simpleItemToTable(ItemStack item) {
		HashMap<Object, Object> table = new HashMap<>();
		table.put("name", item.getItem().getRegistryName().toString());
		table.put("damage", item.getItemDamage());
		table.put("count", item.getCount());
		return table;
	}

	public static double clamp(double x, double a, double b) {
		if (x < a) {
			return a;
		}
		if (x > b) {
			return b;
		}
		return x;
	}

	public static final double DEG_45 = 0.25 * Math.PI;
	public static final int MAX_LIGHT_LEVEL = 15;
	
	public static EntityLaserPointer spawnLaserPointer(World w, double x, double y, double z, Entity hitEntity) {
		if (hitEntity != null) {
			if (hitEntity instanceof EntityLivingBase) {
				EntityLivingBase el = (EntityLivingBase) hitEntity;
				if (el instanceof EntityPlayer && (((EntityPlayer) el).isSpectator()))
					return null;
				if (Math.abs(y - (el.getEyeHeight() + el.posY)) < 0.3) {
					double ay = Math.toRadians(MathHelper.wrapDegrees(el.rotationYawHead + 90f));
					double ly = Math.atan2(z - el.posZ, x - el.posX);
					if (PhotonicUtils.angleDifference(ly, ay) < DEG_45) {
						if (el instanceof EntityPlayer) {
							EntityPlayer ep = (EntityPlayer) el;
							if (ep.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null
									&& ep.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
											.getItem() == PhotonicItems.safetyglasses) {
							} else {
								el.addPotionEffect(
										new PotionEffect(MobEffects.BLINDNESS, 40, 4, true, false));
								el.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 40, 1, true, false));
							}
						}
						el.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 150, 4, true, false));
						el.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 150, 1, true, false));
					}
				}
			}
		}
		EntityLaserPointer e2 = new EntityLaserPointer(w);
		e2.setPositionAndRotation(x, y, z, 0.0f, 0.0f);
		e2.setTicks(5);
		w.spawnEntity(e2);
		for (Object e : w.getEntitiesWithinAABB(EntityOcelot.class,
				new AxisAlignedBB(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10))) {
			EntityOcelot eo = (EntityOcelot) e;
			if (eo.isSitting()) {
				double r = Math.random();
				if (r < 0.05)
					eo.setSitting(false);
				else
					continue;
			}
			eo.setSprinting(true);
			eo.getNavigator().tryMoveToXYZ(x, y, z, 1.2);
		}
		return e2;
	}
	
	private PhotonicUtils() {
	}
}
