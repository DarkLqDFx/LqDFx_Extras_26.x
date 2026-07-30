package work.lqdfxnet.lqdfxextras.GameRules;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import work.lqdfxnet.lqdfxextras.Int.ModGameRules;

import javax.annotation.Nullable;

@EventBusSubscriber
public class ModNetherSkeletons {

    @SubscribeEvent
    public static void onNetherSkeletonSpawned(EntityJoinLevelEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        execute(event, event.getLevel(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;
        if (!world.getBiome(BlockPos.containing(x, y, z)).is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_nether"))))
            return;
        if (entity instanceof Skeleton) {
            if (world instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(ModGameRules.NETHER_SKELETONS.get())) return;
            if (event instanceof ICancellableEvent canCancel) {
                canCancel.setCanceled(true);
            }
            if (world instanceof ServerLevel serverLevel) {
                Entity entityToSpawn = EntityType.WITHER_SKELETON.spawn(serverLevel, BlockPos.containing(x, y, z), EntitySpawnReason.NATURAL);
                if (entityToSpawn != null) entityToSpawn.setDeltaMovement(0, 0, 0);
            }
        }
    }
}



