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
public class TerahertzTrigger implements ICriterionTrigger<TerahertzTrigger.Instance> {
	public static class Instance extends AbstractCriterionInstance {
		public Instance() {
			super(TerahertzTrigger.ID);
		}

		public boolean test(EntityPlayer plyr) {
			return true;
		}
	}

	static class Listeners {
		private final PlayerAdvancements playerAdvancements;
		private final Set<ICriterionTrigger.Listener<TerahertzTrigger.Instance>> listeners = Sets.<ICriterionTrigger.Listener<TerahertzTrigger.Instance>>newHashSet();

		public Listeners(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void add(ICriterionTrigger.Listener<TerahertzTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void remove(ICriterionTrigger.Listener<TerahertzTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(EntityPlayer plyr) {
			List<ICriterionTrigger.Listener<TerahertzTrigger.Instance>> list = null;

			for (ICriterionTrigger.Listener<TerahertzTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(plyr)) {
					if (list == null) {
						list = Lists.<ICriterionTrigger.Listener<TerahertzTrigger.Instance>>newArrayList();
					}

					list.add(listener);
				}
			}

			if (list != null) {
				for (ICriterionTrigger.Listener<TerahertzTrigger.Instance> listener1 : list) {
					listener1.grantCriterion(this.playerAdvancements);
				}
			}
		}
	}

	private static final ResourceLocation ID = new ResourceLocation(ModPhotonicCraft.MODID, "terahertz_received");

	private final Map<PlayerAdvancements, TerahertzTrigger.Listeners> listeners = Maps.<PlayerAdvancements, TerahertzTrigger.Listeners>newHashMap();

	@Override
	public void addListener(PlayerAdvancements playerAdvancementsIn,
			ICriterionTrigger.Listener<TerahertzTrigger.Instance> listener) {
		TerahertzTrigger.Listeners terahertztrigger$listeners = this.listeners.get(playerAdvancementsIn);
		if (terahertztrigger$listeners == null) {
			terahertztrigger$listeners = new TerahertzTrigger.Listeners(playerAdvancementsIn);
			this.listeners.put(playerAdvancementsIn, terahertztrigger$listeners);
		}
		terahertztrigger$listeners.add(listener);
	}

	@Override
	public TerahertzTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new TerahertzTrigger.Instance();
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
			ICriterionTrigger.Listener<TerahertzTrigger.Instance> listener) {
		TerahertzTrigger.Listeners terahertztrigger$listeners = this.listeners.get(playerAdvancementsIn);

		if (terahertztrigger$listeners != null) {
			terahertztrigger$listeners.remove(listener);

			if (terahertztrigger$listeners.isEmpty()) {
				this.listeners.remove(playerAdvancementsIn);
			}
		}
	}

	public void trigger(EntityPlayerMP player) {
		TerahertzTrigger.Listeners enterblocktrigger$listeners = this.listeners.get(player.getAdvancements());

		if (enterblocktrigger$listeners != null) {
			enterblocktrigger$listeners.trigger(player);
		}
	}
}
