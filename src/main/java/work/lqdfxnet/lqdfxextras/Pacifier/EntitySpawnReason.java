package work.lqdfxnet.lqdfxextras.Pacifier;

import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Set;

@EventBusSubscriber
public class EntitySpawnReason {

    // Spawn reasons that should KEEP natural aggression
    private static final Set<net.minecraft.world.entity.EntitySpawnReason> ALLOWED_REASONS = Set.of(
            net.minecraft.world.entity.EntitySpawnReason.SPAWNER,
            net.minecraft.world.entity.EntitySpawnReason.TRIAL_SPAWNER,
            net.minecraft.world.entity.EntitySpawnReason.EVENT,
            net.minecraft.world.entity.EntitySpawnReason.REINFORCEMENT,
            net.minecraft.world.entity.EntitySpawnReason.MOB_SUMMONED,
            net.minecraft.world.entity.EntitySpawnReason.JOCKEY,
            net.minecraft.world.entity.EntitySpawnReason.TRIGGERED
    );

    public static boolean canPacify(net.minecraft.world.entity.EntitySpawnReason spawnReason) {
        return !ALLOWED_REASONS.contains(spawnReason);
    }
}
