package email.com.gmail.cosmoconsole.forge.photoniccraft.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHalogenLampHalfSlab extends BlockHalogenLampSlab {

	@Override
	public boolean isDouble() {
		return false;
	}
	
    @SideOnly(Side.CLIENT)
    protected static boolean isHalfSlab(IBlockState state)
    {
        return true;
    }
}
