package email.com.gmail.cosmoconsole.forge.photoniccraft;

import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.entity.EntityLaserPointer;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityRemoteReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

@Sharable
public class PhotonicPacketHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {
    double DEG_45 = 0.78539816339744830962;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket packet) throws Exception {
        if (packet.channel().equals("PhotonicCraft")) {
            ByteBuf payload = packet.payload();
            if (payload.readableBytes() >= 4) {
                int type = payload.readInt();
				System.out.println("packet: type=" + type + ", size=" + payload.readableBytes());
                if (type == 1 && payload.readableBytes() == 32) {
                	int did = payload.readInt();
                	World par2World = DimensionManager.getWorld(did);
                	double x = payload.readDouble();
                	double y = payload.readDouble();
                	double z = payload.readDouble();
                	int ent = payload.readInt();
                	if (ent != -1) {
                		Entity e = par2World.getEntityByID(ent);
                		if (e instanceof EntityLivingBase) {
                			EntityLivingBase el = (EntityLivingBase)e;
                			if (Math.abs(y - (el.getEyeHeight() + el.posY)) < 0.3) {
                				double ay = Math.toRadians(MathHelper.wrapAngleTo180_float(el.rotationYawHead + 90f));
                				double ly = Math.atan2(z - el.posZ, x - el.posX);
                				if (Math.abs(ly - ay) < DEG_45) {
                					if (el instanceof EntityPlayer) {
                						EntityPlayer ep = (EntityPlayer)el; 
                						if (ep.inventory.armorInventory[3] != null && ep.inventory.armorInventory[3].getItem() == ModPhotonicCraft.safetyglasses) {}
                						else {
        	                				el.addPotionEffect(new PotionEffect(Potion.blindness.id, 40, 1, true));
        	                				el.addPotionEffect(new PotionEffect(Potion.confusion.id, 40, 1, true));
        	                				el.addPotionEffect(new PotionEffect(Potion.nightVision.id, 40, 1, true));
                						}
                					}
	                				el.addPotionEffect(new PotionEffect(Potion.blindness.id, 150, 1, true));
	                				el.addPotionEffect(new PotionEffect(Potion.confusion.id, 150, 1, true));
	                				el.addPotionEffect(new PotionEffect(Potion.nightVision.id, 150, 1, true));
                				}
                			}
                		}
                	}
    		        EntityLaserPointer e2 = new EntityLaserPointer(par2World);
    				e2.setPositionAndRotation(x, y, z, 0.0f, 0.0f);
    				e2.setTicks(5);
    				par2World.spawnEntityInWorld(e2);
    				for (Object e: par2World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox(x-10,y-10,z-10,x+10,y+10,z+10))) {
    					EntityOcelot eo = (EntityOcelot) e;
    					if (eo.isSitting()) {
    						double r = Math.random();
    						if (r < 0.05)
    							eo.setSitting(false);
    						else
    							continue;
    					}
    					eo.getNavigator().tryMoveToXYZ(x, y, z, 1.2);
    				}
                } else if (type == 2 && payload.readableBytes() == 32) {
                	System.out.println("Packet caught");
    				/*data.writeInt(p_77659_3_.worldObj.provider.dimensionId);
    		        data.writeInt(mop.blockX);
    		        data.writeInt(mop.blockY);
    		        data.writeInt(mop.blockZ);
    				data.writeInt(1);
    		        data.writeInt(p_77659_3_.getEntityId());*/
    		        int did = payload.readInt();
                	World par2World = DimensionManager.getWorld(did);
                	int x = payload.readInt();
                	int y = payload.readInt();
                	int z = payload.readInt();
                	int np = (payload.readInt() << 3) - 1;
                	int ent = payload.readInt();
                	long time = payload.readLong();
                	try {
                		EntityPlayer p = (EntityPlayer)par2World.getEntityByID(ent);
                		long channel = p.getHeldItem().stackTagCompound.getLong("channel");
                		TileEntityRemoteReceiver r = (TileEntityRemoteReceiver) par2World.getTileEntity(x, y, z);
                    	System.out.println("My channel: " + channel + ", receiver: " + r.channel);
                		if (r.channel == channel) {
                			r.submitPower(np, time);
                		}
                	} catch (Exception ex) {
                		ex.printStackTrace();
                	}
                }
            }
        }
    }
}
