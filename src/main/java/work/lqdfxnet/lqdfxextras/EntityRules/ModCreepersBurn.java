package work.lqdfxnet.lqdfxextras.EntityRules;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

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

            if (!creepersBurnEnabled()) return;

            BlockPos pos = BlockPos.containing(x, y, z);

            boolean worldDim = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("minecraft:is_overworld")));
            boolean worldBiome = world.getBiome(pos).is(TagKey.create(Registries.BIOME, Identifier.parse("c:is_dry")));
            boolean worldDay = world instanceof Level level && level.isBrightOutside();
            boolean blockSeeSky = world.canSeeSkyFromBelowWater(pos);
            boolean skyBrightness = world.getBrightness(LightLayer.SKY, pos) == 15;

            if (worldDim && worldDay && blockSeeSky && skyBrightness) {
                if (!entity.isOnFire() && (worldBiome || !entity.isInWaterOrRain()))
                    queueServerWork(20, () -> entity.igniteForSeconds(8));
            }

        }
    }

    private static boolean creepersBurnEnabled() {
        return ModConfigCommon.mrCreepersBurm.get();
    }
}
