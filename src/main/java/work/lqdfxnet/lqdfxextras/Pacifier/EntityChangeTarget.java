package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

@EventBusSubscriber
public class EntityChangeTarget {

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {

        // Entity being targeted
        Entity newTarget = event.getNewAboutToBeSetTarget();
        if (newTarget == null) return;

        // Entity doing the targeting
        LivingEntity mobEntity = event.getEntity();
        if (!(mobEntity instanceof Mob mob)) return;

        // Maybe nothing happens!?!
        //if (shouldSkipPacify(mob)) return;

        // Only care about PLAYERS in SURVIVAL
        if (!(newTarget instanceof Player player)) return;
        if (player.gameMode() != GameType.SURVIVAL) return;

        // Check Dimension and Config Options
        Level level = newTarget.level();
        if (level.dimension() == Level.NETHER && !ModConfigCommon.pacifierInNether.get()) return;
        if (level.dimension() == Level.END && !ModConfigCommon.pacifierInEnd.get()) return;

        // Spawn reason filter
        net.minecraft.world.entity.EntitySpawnReason spawnReason = mob.getSpawnType();
        if (spawnReason == null) return;
        if (!EntitySpawnReason.canPacify(spawnReason)) return;

        // Central hostility check (includes overrides)
        if (!MobHostilityCache.isNaturallyHostile(mob, player)) return;


        // Check aggression attribute
        AttributeInstance aggressionAttr = mobEntity.getAttribute(AttributeAggression.AGGRESSION);
        boolean canAttack = aggressionAttr != null &&
                (AttributeAggression.getAggression(mobEntity) == 1);

        // Pacify or allow aggression
        event.setNewAboutToBeSetTarget(canAttack ? (LivingEntity) newTarget : null);

    }
}
