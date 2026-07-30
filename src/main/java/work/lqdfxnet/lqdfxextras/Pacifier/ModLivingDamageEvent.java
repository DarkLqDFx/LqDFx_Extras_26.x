package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.resources.Identifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import work.lqdfxnet.lqdfxextras.Int.ModAttributeAggression;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static work.lqdfxnet.lqdfxextras.Lqdfxextras.queueServerWork;


@EventBusSubscriber
public class ModLivingDamageEvent {

    private static final Map<UUID, Long> aggressionCooldown = new HashMap<>();

    private static final Identifier HOSTILE_ID = Identifier.parse("lqdfxextras:hostile");
    private static final AttributeModifier HOSTILE_MODIFIER =
            new AttributeModifier(HOSTILE_ID, 1, AttributeModifier.Operation.ADD_VALUE);

    @SubscribeEvent
    public static void onEntityAttacked(LivingDamageEvent.Pre event)  {
        DamageSource source = event.getSource();
        Entity direct = source.getDirectEntity();
        Entity entity = event.getEntity();

        if(!(entity instanceof Mob mob))  return;                       // Make sure mobEntity is a Mob
        EntitySpawnReason spawnReason = mob.getSpawnType();             // Get mobEntity SpawnReason
        if (spawnReason == null) return;
        if (!ModEntitySpawnReason.canPacify(spawnReason))  return;      // If this spawn reason should keep natural hostility

        //if (source == null || entity == null) return;

        Entity attacker = source.getEntity();

        boolean isPlayerAttack =
                source.is(DamageTypes.PLAYER_ATTACK) ||
                        attacker instanceof Player ||
                        (direct instanceof net.minecraft.world.entity.projectile.Projectile projectile &&
                                projectile.getOwner() instanceof Player);

        if (!isPlayerAttack) return;

        // Add aggression modifier
        Objects.requireNonNull(mob.getAttribute(ModAttributeAggression.AGGRESSION))
                .addOrReplacePermanentModifier(HOSTILE_MODIFIER);

        // Schedule pacification after 100 ticks
        //queueServerWork(100, () -> pacifyMob(living));

        // Extend cooldown
        long expireTick = mob.level().getGameTime() + 100;
        aggressionCooldown.put(mob.getUUID(), expireTick);

        // Schedule pacification
        queueServerWork(100,
                () -> {
                    long now = mob.level().getGameTime();
                    long storedExpire = aggressionCooldown.getOrDefault(mob.getUUID(), 0L);

                    if (now >= storedExpire) {
                        pacifyMob(mob);
                    }
                });
    }

    public static void pacifyMob(LivingEntity entity) {
        var attr = entity.getAttribute(ModAttributeAggression.AGGRESSION);
        assert attr != null;
        if (attr.hasModifier(HOSTILE_ID)) {
            attr.removeModifier(HOSTILE_ID);
        }
    }
}