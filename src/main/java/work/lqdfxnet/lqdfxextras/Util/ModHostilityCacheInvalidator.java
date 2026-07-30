package work.lqdfxnet.lqdfxextras.Util;


import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber
public class ModHostilityCacheInvalidator {

    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            ModHostilityCache.invalidate(mob);
        }
    }

    @SubscribeEvent
    public static void onMobUnload(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Mob mob) {
            ModHostilityCache.invalidate(mob);
        }
    }
}
