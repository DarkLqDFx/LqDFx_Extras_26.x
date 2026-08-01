package work.lqdfxnet.lqdfxextras.EntityRules;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.illager.Evoker;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

import javax.annotation.Nullable;
import java.util.Comparator;

@EventBusSubscriber
public class ModEvokeDeath {

    @SubscribeEvent
    public static void onEvokerDeath(LivingDeathEvent event) {
        execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        execute(null, world, x, y, z, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;

        if (entity instanceof Evoker) {
            // Centralized config check
            if (!vexDespawnOnEvokerDeathEnabled()) {
                return;
            }

            Vec3 center = new Vec3(x, y, z);

            for (Entity nearby : world.getEntitiesOfClass(
                    Entity.class,
                    new AABB(center, center).inflate(8), // 16/2d = 8 radius
                    e -> true
            ).stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList()) {

                if (!nearby.level().isClientSide() && nearby instanceof Vex) {
                    nearby.discard();
                }
            }
        }
    }

    private static boolean vexDespawnOnEvokerDeathEnabled() {
        return ModConfigCommon.mrEvokerDeath.get(); // Your config boolean
    }
}