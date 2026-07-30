package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.world.entity.EntitySpawnReason;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Set;

@EventBusSubscriber
public class ModEntitySpawnReason {

    // Spawn reasons that should KEEP natural aggression
    private static final Set<EntitySpawnReason> ALLOWED_REASONS = Set.of(
            EntitySpawnReason.SPAWNER,
            EntitySpawnReason.TRIAL_SPAWNER,
            EntitySpawnReason.EVENT,
            EntitySpawnReason.REINFORCEMENT,
            EntitySpawnReason.MOB_SUMMONED,
            EntitySpawnReason.JOCKEY,
            EntitySpawnReason.TRIGGERED
    );


    public static boolean canPacify(EntitySpawnReason spawnReason) {
        return !ALLOWED_REASONS.contains(spawnReason);
    }
}
