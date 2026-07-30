package work.lqdfxnet.lqdfxextras.Int;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import work.lqdfxnet.lqdfxextras.Lqdfxextras;

public class ModGameRules {

    public static final DeferredRegister<GameRule<?>> REGISTRY = DeferredRegister.create(Registries.GAME_RULE, Lqdfxextras.MODID);
    public static DeferredHolder<GameRule<?>, GameRule<Boolean>> CREEPERS_BURN = registerBoolean("creepers_burn", GameRuleCategory.MOBS, false);
    public static DeferredHolder<GameRule<?>, GameRule<Boolean>> SPAWN_VEX = registerBoolean("spawn_vex", GameRuleCategory.SPAWNING, true);
    public static DeferredHolder<GameRule<?>, GameRule<Boolean>> FARMLAND_TRAMPLE = registerBoolean("farmland_trample", GameRuleCategory.MOBS, true);
    public static DeferredHolder<GameRule<?>, GameRule<Boolean>> VEX_DESPAWN_ON_EVOKER_DEATH = registerBoolean("vex_despawn_on_evoker_death", GameRuleCategory.MOBS, false);
    public static DeferredHolder<GameRule<?>, GameRule<Boolean>> NETHER_SKELETONS = registerBoolean("nether_skeletons", GameRuleCategory.SPAWNING, false);

    private static DeferredHolder<GameRule<?>, GameRule<Boolean>> registerBoolean(String registryname, GameRuleCategory category, boolean value) {
        return REGISTRY.register(registryname, () -> new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, value, FeatureFlagSet.of()));
    }
}