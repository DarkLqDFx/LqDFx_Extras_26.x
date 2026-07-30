package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import work.lqdfxnet.lqdfxextras.Int.ModAttributeAggression;
import work.lqdfxnet.lqdfxextras.Lqdfxextras;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;
import work.lqdfxnet.lqdfxextras.Util.ModHostilityCache;

@EventBusSubscriber
public class ModLivingChangeTarget {

    @SubscribeEvent
    public static void onLivingChangeTarget(LivingChangeTargetEvent event) {

        // Entity being targeted
        Entity newTarget = event.getNewAboutToBeSetTarget();
        if (newTarget == null) return;

        // Entity doing the targeting
        LivingEntity mobEntity = event.getEntity();
        if (!(mobEntity instanceof Mob mob)) return;

        // Only care about PLAYERS in SURVIVAL
        if (!(newTarget instanceof Player player)) return;
        if (player.gameMode() != GameType.SURVIVAL) return;

        // Check Dimension and Config Options
        Level level = newTarget.level();
        if (level.dimension() == Level.NETHER && !ModConfigCommon.pacifierInNether.get()) return;
        if (level.dimension() == Level.END && !ModConfigCommon.pacifierInEnd.get()) return;

        // Spawn reason filter
        EntitySpawnReason spawnReason = mob.getSpawnType();
        if (spawnReason == null) return;
        if (!ModEntitySpawnReason.canPacify(spawnReason)) return;

        // Central hostility check (includes overrides)
        if (!ModHostilityCache.isNaturallyHostile(mob, player)) return;


        // Check aggression attribute
        AttributeInstance aggressionAttr = mobEntity.getAttribute(ModAttributeAggression.AGGRESSION);
        boolean canAttack = aggressionAttr != null &&
                aggressionAttr.hasModifier(Identifier.parse("lqdfxextras:hostile"));

        // Pacify or allow aggression
        event.setNewAboutToBeSetTarget(canAttack ? (LivingEntity) newTarget : null);
    }
}

