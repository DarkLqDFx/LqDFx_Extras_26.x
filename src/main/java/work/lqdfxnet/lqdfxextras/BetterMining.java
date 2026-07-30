package work.lqdfxnet.lqdfxextras;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

@EventBusSubscriber
public class BetterMining {

    @SubscribeEvent
    public static void onBetterMiningBreak(PlayerEvent.BreakSpeed event) {

        // --- Fast exits first ---
        ItemStack tool = event.getEntity().getMainHandItem();
        if (tool.isEmpty()) return;

        LevelAccessor world = event.getEntity().level();

        // --- Cache config lists once ---
        List<? extends String> allowedTools = ModConfigCommon.bmTools.get();
        List<? extends String> allowedBlocks = ModConfigCommon.bmBlocksAffected.get();

        // --- Tool ID lookup (fast path) ---
        Identifier toolId = tool.get(DataComponents.ITEM_MODEL);
        if (toolId == null) return;

        String toolKey = toolId.toString();
        if (!allowedTools.contains(toolKey)) return;

        // --- Efficiency check ---
        int requiredEfficiency = ModConfigCommon.bmEfficiencyLvl.get();

        double effLevel = tool.getEnchantmentLevel(
                world.registryAccess()
                        .lookupOrThrow(Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.EFFICIENCY)
        );

        if (effLevel < requiredEfficiency) return;

        // --- Block check ---
        Block block = event.getState().getBlock();
        String blockKey = BuiltInRegistries.BLOCK.getKey(block).toString();

        if (!allowedBlocks.contains(blockKey)) return;

        // --- Multiplier ---
        double multiplier = ModConfigCommon.bmMiningSpeedMultiplier.get();

        // --- Apply speed ---
        float baseSpeed = event.getNewSpeed();

        if (event.getEntity().hasEffect(MobEffects.HASTE)) {
            event.setNewSpeed(baseSpeed * (float) multiplier);
        } else {
            event.setNewSpeed(baseSpeed + (float)(effLevel + multiplier));
        }
    }
}
