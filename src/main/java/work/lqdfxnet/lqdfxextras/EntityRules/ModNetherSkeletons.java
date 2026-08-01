package work.lqdfxnet.lqdfxextras.EntityRules;

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
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

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

        BlockPos pos = BlockPos.containing(x, y, z);

        // Only operate in Nether biomes
        boolean isNether = world.getBiome(pos)
                .is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_nether")));

        if (!isNether) return;

        if (entity instanceof Skeleton) {

            if (!netherSkeletonReplacementEnabled()) return;

            if (event instanceof ICancellableEvent canCancel) canCancel.setCanceled(true);

            // Spawn a Wither Skeleton instead
            if (world instanceof ServerLevel serverLevel) {
                Entity witherSkeleton = EntityType.WITHER_SKELETON.spawn(serverLevel, pos, EntitySpawnReason.NATURAL);
                if (witherSkeleton != null) {
                    witherSkeleton.setDeltaMovement(0, 0, 0);
                }
            }
        }
    }

    private static boolean netherSkeletonReplacementEnabled() {
        return ModConfigCommon.mrNetherSkeleton.get();
    }
}



