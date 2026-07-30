package work.lqdfxnet.lqdfxextras.GameRules;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import work.lqdfxnet.lqdfxextras.Int.ModGameRules;

import static work.lqdfxnet.lqdfxextras.Lqdfxextras.queueServerWork;

@EventBusSubscriber
public class ModCreepersBurn {
    @SubscribeEvent
    public static void burnCreeper(EntityTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return;
        execute(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
    }

    private static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;

        if (entity instanceof Creeper) {
            if (world instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(ModGameRules.CREEPERS_BURN.get())) return;   // Only trigger if the gamerule is set to true

            boolean worldDim = world.getBiome(BlockPos.containing(x, y, z)).is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_overworld")));
            boolean worldBiome = world.getBiome(BlockPos.containing(x, y, z)).is(TagKey.create(Registries.BIOME, Identifier.parse("c:is_dry")));
            boolean worldDay = world instanceof Level level && level.isBrightOutside();
            boolean blockSeeSky = world.canSeeSkyFromBelowWater(BlockPos.containing(x, y, z));
            boolean skyBrightness;
            skyBrightness = world.getBrightness(LightLayer.SKY, BlockPos.containing(x, y, z)) == 15;

            if (worldDim && worldDay && blockSeeSky && skyBrightness) {
                if (!entity.isOnFire() && (worldBiome || !entity.isInWaterOrRain()))
                    queueServerWork(20, () -> entity.igniteForSeconds(8));
            }

        }
    }
}
