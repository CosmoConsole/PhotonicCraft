package email.com.gmail.cosmoconsole.forge.photoniccraft.client.renderer.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad.Builder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PrismBakedModel implements IBakedModel {
	public static TextureAtlasSprite glassSprite = null;
	public static TextureAtlasSprite prismSprite = null;
	private final static double SQRT3 = Math.sqrt(3);
	private final static double C0 = 0.5 - (0.25 * SQRT3);
	private final static double C1 = 0.5 + (0.25 * SQRT3);
	private final static Integer[][] uvRotateTable = new Integer[][] { { 0, 0, 0, 16, 16, 16, 16, 0 },
			{ 0, 16, 16, 16, 16, 0, 0, 0 }, { 16, 16, 0, 16, 0, 0, 16, 0 }, { 16, 0, 16, 16, 0, 16, 0, 0 } };
	private final static Vec3d[][] faces = new Vec3d[][] {
			// TOP
			{ new Vec3d(0., 1., 0.), new Vec3d(0., 1., 1.), new Vec3d(1., 1., 1.), new Vec3d(1., 1., 0.) },
			// BOTTOM
			{ new Vec3d(1., 0., 1.), new Vec3d(0., 0., 1.), new Vec3d(0., 0., 0.), new Vec3d(1., 0., 0.) },
			// SOUTH
			{ new Vec3d(0., 1., C1), new Vec3d(0., 0., C1), new Vec3d(1., 0., C1), new Vec3d(1., 1., C1) },
			// NORTHWEST
			{ new Vec3d(.5, 1., C0), new Vec3d(.5, 0., C0), new Vec3d(0., 0., C1), new Vec3d(0., 1., C1) },
			// NORTHEAST
			{ new Vec3d(1., 1., C1), new Vec3d(1., 0., C1), new Vec3d(.5, 0., C0), new Vec3d(.5, 1., C0) } };
	private final static Vec3d[] normals = new Vec3d[] {
			// TOP
			new Vec3d(0., 1., 0.),
			// BOTTOM
			new Vec3d(0., -1., 0.),
			// SOUTH
			null, // compute in real-time
			// NORTHWEST
			null, // compute in real-time
			// NORTHEAST
			null // compute in real-time
	};
	private final static EnumFacing[] orient = new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, null, null, null };
	private static TextureAtlasSprite[] textures = null;
	private final static Integer[] uvrots = new Integer[] { 3, 2, 0, 0, 0 };
	private IBakedModel base;
	private int rotation;

	public static void initTextures() {
		textures = new TextureAtlasSprite[] { prismSprite, prismSprite, glassSprite, glassSprite, glassSprite };
	}

	public PrismBakedModel(IBakedModel base) {
		this(base, 0);
	}

	public PrismBakedModel(IBakedModel base, int rotation) {
		this.base = base;
		this.rotation = rotation;
	}

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		List<BakedQuad> quads = new ArrayList<>();
		for (int i = 0; i < faces.length; ++i) {
			UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.BLOCK);
			writeQuad(builder, performRotations(faces[i]), normals[i], textures[i], uvrots[i]);
			if (orient[i] != null)
				builder.setQuadOrientation(orient[i]);
			builder.setApplyDiffuseLighting(false);
			quads.add(builder.build());
		}
		return quads;
	}

	private Vec3d[] performRotations(Vec3d[] vec3ds) {
		Vec3d[] res = new Vec3d[vec3ds.length];
		for (int i = 0; i < res.length; ++i) {
			res[i] = rotateVector(vec3ds[i], rotation);
		}
		return res;
	}

	private Vec3d rotateVector(Vec3d vec3d, int rot) {
		double x = vec3d.x - 0.5;
		double z = vec3d.z - 0.5;
		double ox = 0;
		while (rot > 0) {
			ox = x;
			x = z;
			z = -ox;
			--rot;
		}
		return new Vec3d(x + 0.5, vec3d.y, z + 0.5);
	}

	private void writeQuad(Builder builder, Vec3d[] vertex, Vec3d normal, TextureAtlasSprite sprite, int uvRotate) {
		if (normal == null) {
			normal = vertex[0].subtract(vertex[1]).crossProduct(vertex[2].subtract(vertex[1])).normalize().scale(-1);
		}
		builder.setTexture(sprite);
		putVertex(builder, vertex[0], normal, uvRotateTable[uvRotate][0], uvRotateTable[uvRotate][1], sprite);
		putVertex(builder, vertex[1], normal, uvRotateTable[uvRotate][2], uvRotateTable[uvRotate][3], sprite);
		putVertex(builder, vertex[2], normal, uvRotateTable[uvRotate][4], uvRotateTable[uvRotate][5], sprite);
		putVertex(builder, vertex[3], normal, uvRotateTable[uvRotate][6], uvRotateTable[uvRotate][7], sprite);
	}

	private void putVertex(Builder builder, Vec3d face, Vec3d normal, float u, float v, TextureAtlasSprite sprite) {
		VertexFormat fmt = builder.getVertexFormat();
		for (int e = 0; e < fmt.getElementCount(); ++e) {
			switch (fmt.getElement(e).getUsage()) {
			case POSITION:
				builder.put(e, (float) face.x, (float) face.y, (float) face.z, 1.0f);
				break;
			case NORMAL:
				builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 1.0f);
				break;
			case COLOR:
				builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
				break;
			case UV:
				if (fmt.getElement(e).getIndex() == 0) {
					u = sprite.getInterpolatedU(u);
					v = sprite.getInterpolatedV(v);
					builder.put(e, u, v, 0f, 1f);
					break;
				}
			default:
				builder.put(e);
			}
		}
	}

	@Override
	public boolean isAmbientOcclusion() {
		return base.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return base.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return base.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return glassSprite;
	}

	@Override
	public ItemOverrideList getOverrides() {
		return base.getOverrides();
	}
}
