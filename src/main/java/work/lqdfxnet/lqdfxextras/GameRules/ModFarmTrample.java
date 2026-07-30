package work.lqdfxnet.lqdfxextras.GameRules;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import work.lqdfxnet.lqdfxextras.Int.ModGameRules;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ModFarmTrample {

    @SubscribeEvent
    public static void farmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        execute(event, event.getEntity());
    }

    private static void execute(@Nullable Event event, Entity entity) {
        if (entity == null) return;                 // Why no entity and this is fired?
        if (entity instanceof Player) return;       // Players are allowed to trample!
        //if (!ConfigMain.mobsTrampleFarmland) return;    // if this is false, allow default game rule
        if (entity.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ModGameRules.FARMLAND_TRAMPLE.get())) return;
        // if you made it this far, well... we are canceling the event! Sorry!
        if (event instanceof ICancellableEvent canCancel) canCancel.setCanceled(true);


    }
}
