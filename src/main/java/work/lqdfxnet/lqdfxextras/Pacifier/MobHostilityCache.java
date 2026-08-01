package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MobHostilityCache {

    private static final Map<UUID, Boolean> CACHE = new ConcurrentHashMap<>();

    public static boolean isNaturallyHostile(Mob mob, Player player) {

        Boolean override = MobHostilityOverride.getOverride(mob);
        if (override != null) return override;

        // Cached result?
        UUID id = mob.getUUID();
        Boolean cached = CACHE.get(id);
        if (cached != null) return cached;

        // Compute hostility once
        boolean hostile = computeHostility(mob, player);

        // Store result
        CACHE.put(id, hostile);

        return hostile;
    }

    private static boolean computeHostility(Mob mob, Player player) {
        // Always-hostile mobs
        if (mob instanceof Monster) return true;
        if (mob instanceof Slime) return true;
        if (mob instanceof MagmaCube) return true;
        if (mob instanceof Ghast) return true;
        if (mob instanceof Blaze) return true;
        if (mob instanceof Shulker) return true;
        if (mob instanceof Endermite) return true;
        if (mob instanceof Silverfish) return true;

        // Bosses
        if (mob instanceof WitherBoss) return true;
        if (mob instanceof EnderDragon) return true;

        // Special hostiles
        if (mob instanceof Warden) return true;
        if (mob instanceof Ravager) return true;
        if (mob instanceof Vex) return true;
        if (mob instanceof Phantom) return true;

        // Conditional hostility (neutral mobs)
        if (mob instanceof EnderMan ||
                mob instanceof Bee ||
                mob instanceof Wolf ||
                mob instanceof ZombifiedPiglin ||
                mob instanceof PolarBear ||
                mob instanceof IronGolem) {
            return isAngryAtPlayer(mob, player);
    }

        // Everything else is passive
        return false;
    }

    private static boolean isAngryAtPlayer(Mob mob, Player player) {
        Brain<?> brain = mob.getBrain();

        Optional<UUID> angryTarget = brain.getMemory(MemoryModuleType.ANGRY_AT);
        return angryTarget.equals(player.getUUID());
    }

    public static void invalidate(Mob mob) {
        CACHE.remove(mob.getUUID());
    }
}
