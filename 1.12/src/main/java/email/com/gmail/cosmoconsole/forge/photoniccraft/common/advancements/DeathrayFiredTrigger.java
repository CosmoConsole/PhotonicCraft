package email.com.gmail.cosmoconsole.forge.photoniccraft.common.advancements;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import email.com.gmail.cosmoconsole.forge.photoniccraft.ModPhotonicCraft;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

/**
 * The custom advancement criteria trigger for the Wireless Charging
 * advancement.
 */
public class DeathrayFiredTrigger implements ICriterionTrigger<DeathrayFiredTrigger.Instance> {
	public static class Instance extends AbstractCriterionInstance {
		public Instance() {
			super(DeathrayFiredTrigger.ID);
		}

		public boolean test(EntityPlayer plyr) {
			return true;
		}
	}

	static class Listeners {
		private final PlayerAdvancements playerAdvancements;
		private final Set<ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance>>newHashSet();

		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void add(ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void remove(ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(EntityPlayer plyr) {
			List<ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance>> list = null;

			for (ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(plyr)) {
					if (list == null) {
						list = Lists.<ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance>>newArrayList();
					}

					list.add(listener);
				}
			}

			if (list != null) {
				for (ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance> listener1 : list) {
					listener1.grantCriterion(this.playerAdvancements);
				}
			}
		}
	}

	private static final ResourceLocation ID = new ResourceLocation(ModPhotonicCraft.MODID, "deathray_fire");

	private final Map<PlayerAdvancements, DeathrayFiredTrigger.Listeners> listeners = Maps.<PlayerAdvancements, DeathrayFiredTrigger.Listeners>newHashMap();

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance> listener) {
		DeathrayFiredTrigger.Listeners deathraytrigger$listeners = this.listeners.get(playerAdvancementsIn);
		if (deathraytrigger$listeners == null) {
			deathraytrigger$listeners = new DeathrayFiredTrigger.Listeners(playerAdvancementsIn);
			this.listeners.put(playerAdvancementsIn, deathraytrigger$listeners);
		}
		deathraytrigger$listeners.add(listener);
	}

	@Override
	public DeathrayFiredTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new DeathrayFiredTrigger.Instance();
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
		this.listeners.remove(playerAdvancementsIn);
	}

	@Override
	public void removeListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<DeathrayFiredTrigger.Instance> listener) {
		DeathrayFiredTrigger.Listeners deathraytrigger$listeners = this.listeners.get(playerAdvancementsIn);

		if (deathraytrigger$listeners != null) {
			deathraytrigger$listeners.remove(listener);

			if (deathraytrigger$listeners.isEmpty()) {
				this.listeners.remove(playerAdvancementsIn);
			}
		}
	}

	public void trigger(EntityPlayerMP player) {
		DeathrayFiredTrigger.Listeners enterblocktrigger$listeners = this.listeners.get(player.getAdvancements());

		if (enterblocktrigger$listeners != null) {
			enterblocktrigger$listeners.trigger(player);
		}
	}
}
