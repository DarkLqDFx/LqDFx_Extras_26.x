package work.lqdfxnet.lqdfxextras.GameRules;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import work.lqdfxnet.lqdfxextras.Int.ModGameRules;
import work.lqdfxnet.lqdfxextras.Lqdfxextras;

import javax.annotation.Nullable;


@EventBusSubscriber
public class ModVexSpawn {

    @SubscribeEvent
    public static void onVexSpawned(EntityJoinLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        execute(event, event.getLevel(), event.getEntity());
    }

    // Is this even needed?

    public static void execute(LevelAccessor world, Entity entity) {
        execute(null, world, entity);
    }


    public static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
        if (entity == null) return;

        if (entity instanceof Vex) {
            Lqdfxextras.LOGGER.info("Spawning vex entity??");
            // check for false in gamerule. True is ok to spawn!
            if (world instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(ModGameRules.SPAWN_VEX.get())) {
                Lqdfxextras.LOGGER.info("Nope! Game rules disabled them!");
                // Check if Event is cancelable
                if (event instanceof ICancellableEvent cancellableEvent) {
                    cancellableEvent.setCanceled(true);
                }
            }
        }
    }
}