package work.lqdfxnet.lqdfxextras.EntityRules;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ModFarmTrample {

    @SubscribeEvent
    public static void farmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        execute(event, event.getEntity());
    }

    private static void execute(@Nullable Event event, @Nullable Entity entity) {
        if (entity == null) {
            if (event instanceof ICancellableEvent canCancel && farmlandTrampleDisabled()) {
                canCancel.setCanceled(true);
            }
            return;
        }

        if (entity instanceof Player) return;

        if (!farmlandTrampleDisabled()) return;

        if (event instanceof ICancellableEvent canCancel) {
            canCancel.setCanceled(true);
        }
    }

    private static boolean farmlandTrampleDisabled() {
        return ModConfigCommon.mrFarmLand.get();
        // TRUE  = cancel mob trampling
        // FALSE = allow default behavior
    }
}
