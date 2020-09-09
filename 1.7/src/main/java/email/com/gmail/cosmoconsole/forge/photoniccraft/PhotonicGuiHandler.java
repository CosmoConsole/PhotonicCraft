package email.com.gmail.cosmoconsole.forge.photoniccraft;


import cpw.mods.fml.common.network.IGuiHandler;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.ContainerLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.ContainerLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.ContainerMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.GuiLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.GuiLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.GuiMicrowaveOven;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaser;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityLaserCharger;
import email.com.gmail.cosmoconsole.forge.photoniccraft.tileentity.TileEntityMicrowaveOven;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PhotonicGuiHandler implements IGuiHandler {
        //returns an instance of the Container you made earlier
        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityLaser){
                        return new ContainerLaser(player.inventory, (TileEntityLaser) tileEntity);
                }
                if(tileEntity instanceof TileEntityLaserCharger){
                    return new ContainerLaserCharger(player.inventory, (TileEntityLaserCharger) tileEntity);
            }
                if(tileEntity instanceof TileEntityMicrowaveOven){
                    return new ContainerMicrowaveOven(player.inventory, (TileEntityMicrowaveOven) tileEntity);
                }
                return null;
        }

        //returns an instance of the Gui you made earlier
        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world,
                        int x, int y, int z) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if(tileEntity instanceof TileEntityLaser){
                        return new GuiLaser(player.inventory, (TileEntityLaser) tileEntity);
                }
                if(tileEntity instanceof TileEntityLaserCharger){
                    return new GuiLaserCharger(player.inventory, (TileEntityLaserCharger) tileEntity);
                }
                if(tileEntity instanceof TileEntityMicrowaveOven){
                    return new GuiMicrowaveOven(player.inventory, (TileEntityMicrowaveOven) tileEntity);
            }
                return null;

        }
}