package email.com.gmail.cosmoconsole.forge.photoniccraft.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class PhotonicReflections {
	public static boolean attackDragonFrom(EntityDragon ent, DamageSource source, float damage) {
		try {
			Method m = ReflectionHelper.findMethod(EntityDragon.class, PhotonicReflectionNames.EntityDragon_attackDragonFrom[0], PhotonicReflectionNames.EntityDragon_attackDragonFrom[1], DamageSource.class, float.class);
			m.setAccessible(true);
			return (boolean) (Boolean) m.invoke(ent, source, damage);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
	}

	private PhotonicReflections() {
	}
}
