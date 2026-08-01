package work.lqdfxnet.lqdfxextras.EntityRules;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

import javax.annotation.Nullable;


@EventBusSubscriber
public class ModVexSpawn {

    @SubscribeEvent
    public static void onVexSpawned(EntityJoinLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        execute(event, event.getLevel(), event.getEntity());
    }

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }


    public static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null) return;

        if (entity instanceof Vex) {
            if (!vexSpawnEnabled()) {
                if (event instanceof ICancellableEvent cancellableEvent) {
                    cancellableEvent.setCanceled(true);
                }
            }
        }
    }

    private static boolean vexSpawnEnabled() {
        return ModConfigCommon.mrVexSpawn.get();
    }

}