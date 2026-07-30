package work.lqdfxnet.lqdfxextras.GameRules;


import net.minecraft.server.level.ServerLevel;
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
import work.lqdfxnet.lqdfxextras.Int.ModGameRules;

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
        if (entity == null)
            return;
        if (entity instanceof Evoker) {
            if ((world instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(ModGameRules.VEX_DESPAWN_ON_EVOKER_DEATH.get()))) {
                {
                    final Vec3 _center = new Vec3(x, y, z);
                    for (Entity entity1 : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
                        if (!entity.level().isClientSide())
                            if (entity1 instanceof Vex && !entity1.level().isClientSide()) entity1.discard();
                    }
                }
            }
        }
    }
}

