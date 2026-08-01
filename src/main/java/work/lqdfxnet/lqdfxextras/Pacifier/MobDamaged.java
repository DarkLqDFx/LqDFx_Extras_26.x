package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class MobDamaged {

    private static final Map<UUID, Long> aggressionCooldown = new HashMap<>();

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        DamageSource source = event.getSource();
        Entity entity = event.getEntity();

        if (!(entity instanceof Mob mob)) return;
        net.minecraft.world.entity.EntitySpawnReason spawnReason = mob.getSpawnType();
        if (spawnReason == null) return;
        if (!EntitySpawnReason.canPacify(spawnReason)) return;

        // Figure out if Player was the damage source
        if (isPlayerAttack(source)) {
            AttributeAggression.setAggression(mob, 1);  // set Aggression State 0 = Passive 1 = Hostile
            // Extend cooldown
            long expireTick = mob.level().getGameTime() + 100;
            aggressionCooldown.put(mob.getUUID(), expireTick);
        } else if (isMobAttack(source)) {
            AttributeAggression.setAggression(mob, 1);
            aggressionCooldown.remove(mob.getUUID());
        }


    }

    public static boolean isPlayerAttack(DamageSource source) {
        Entity direct = source.getDirectEntity();
        Entity attacker = source.getEntity();

        // Melee
        if (source.is(DamageTypes.PLAYER_ATTACK)) return true;

        // Direct attacker
        if (attacker instanceof Player) return true;

        // Projectile owner
        if (direct instanceof Projectile proj &&
                proj.getOwner() instanceof Player) return true;

        // Thrown items
        return direct instanceof ThrowableItemProjectile tip &&
                tip.getOwner() instanceof Player;
    }

    public static boolean isMobAttack(DamageSource source) {
        Entity direct = source.getDirectEntity();
        Entity attacker = source.getEntity();

        // Melee
        if (source.is(DamageTypes.PLAYER_ATTACK)) return false;

        // Direct attacker
        if (attacker instanceof Player) return false;

        // Projectile/Thrown owner
        if (direct instanceof Projectile proj && proj.getOwner() instanceof Mob) return true;
        if (direct instanceof ThrowableItemProjectile tip && tip.getOwner() instanceof Mob) return true;

        return attacker instanceof Mob;
    }

    @SubscribeEvent
    public static void onMobTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Mob mob)) return;

        long now = mob.level().getGameTime();
        long expireTick = aggressionCooldown.getOrDefault(mob.getUUID(), 0L);

        if (expireTick != 0 && now >= expireTick) {
            AttributeAggression.setAggression(mob, 0);  // set Aggression State 0 = Passive 1 = Hostile
            aggressionCooldown.remove(mob.getUUID());
        }
    }
}
