package work.lqdfxnet.lqdfxextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Lqdfxextras.MODID)
public class ModConfigCommon {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Pacifier
    public static ModConfigSpec.BooleanValue pacifierEnabled;
    public static ModConfigSpec.ConfigValue<List<? extends String>> pacifierExcludeBoss;
    public static ModConfigSpec.ConfigValue<List<? extends String>> pacifierExcludeMonster;
    public static ModConfigSpec.ConfigValue<List<? extends String>> pacifierExcludeAnimal;
    public static ModConfigSpec.BooleanValue pacifierInNether;
    public static ModConfigSpec.BooleanValue pacifierInEnd;

    // Mob Rules
    public static ModConfigSpec.BooleanValue mrCreepersBurm;
    public static ModConfigSpec.BooleanValue mrVexSpawn;
    public static ModConfigSpec.BooleanValue mrEvokerDeath;

    public static ModConfigSpec.BooleanValue mrCreeperGriefing;
    public static ModConfigSpec.BooleanValue mrEndermanGriefing;
    public static ModConfigSpec.BooleanValue mrSilverfishGriefing;
    public static ModConfigSpec.BooleanValue mrGhastGriefing;
    public static ModConfigSpec.BooleanValue mrFarmLand;
    public static ModConfigSpec.BooleanValue mrNetherSkeleton;


    // Better Mining
    public static ModConfigSpec.BooleanValue bmEnabled;
    public static ModConfigSpec.IntValue bmEfficiencyLvl;
    public static ModConfigSpec.IntValue bmHasteLvl;
    public static ModConfigSpec.DoubleValue bmMiningSpeedMultiplier;
    public static ModConfigSpec.ConfigValue<List<? extends String>> bmTools;
    public static ModConfigSpec.ConfigValue<List<? extends String>> bmBlocksAffected;

    // Waxed WorkStations
    public static ModConfigSpec.BooleanValue wwsEnabled;

    static {

        // -------------------------------------------------
        // Pacifier
        // -------------------------------------------------
        BUILDER.comment("Pacifier system settings").push("Pacifier");

        pacifierEnabled = BUILDER
                .comment("Enable or disable the pacifier system")
                .define("pacifier_enabled", true);

        pacifierExcludeBoss = BUILDER
                .comment("Boss mobs excluded from pacification")
                .defineListAllowEmpty("pacifier_exclude_boss",
                        List.of("minecraft:ender_dragon", "minecraft:wither", "minecraft:elder_guardian"),null, ModConfigCommon::validateEntity);

        pacifierExcludeMonster = BUILDER
                .comment("Monster mobs excluded from pacification")
                .defineListAllowEmpty("pacifier_exclude_monster",
                        List.of("minecraft:enderman", "minecraft:pillager", "minecraft:vindicator",
                                "minecraft:evoker", "minecraft:ravager", "minecraft:witch"),null, ModConfigCommon::validateEntity);

        pacifierExcludeAnimal = BUILDER
                .comment("Animal mobs excluded from pacification")
                .defineListAllowEmpty("pacifier_exclude_animal",
                        List.of("minecraft:cow"),null, ModConfigCommon::validateEntity);

        pacifierInNether = BUILDER
                .comment("Allow pacifier system in the Nether")
                .define("pacifier_in_nether", false);

        pacifierInEnd = BUILDER
                .comment("Allow pacifier system in the End")
                .define("pacifier_in_end", false);

        BUILDER.pop();

        // -------------------------------------------------
        // Mob Rules
        // -------------------------------------------------
        BUILDER.comment("Mob behavior rule toggles").push("Mob Rules");

        mrCreepersBurm = BUILDER.comment("Creepers burn in daylight").define("mr_creepers_burm", true);
        mrVexSpawn = BUILDER.comment("Allow vexes to spawn normally (TRUE Vanilla behaviour)").define("mr_vex_spawn", true);
        mrEvokerDeath = BUILDER.comment("Evokers death trigger Vex to despawn").define("mr_evoker_death", true);

        mrCreeperGriefing = BUILDER.comment("Creeper explosions damage blocks (TRUE Vanilla behaviour)").define("mr_creeper_griefing", true);
        mrEndermanGriefing = BUILDER.comment("Endermen pick up blocks (TRUE Vanilla behaviour)").define("mr_enderman_griefing", true);
        mrSilverfishGriefing = BUILDER.comment("Silverfish infest blocks (TRUE Vanilla behaviour)").define("mr_silverfish_griefing", true);
        mrGhastGriefing = BUILDER.comment("Ghast fireballs damage terrain (TRUE Vanilla behaviour)").define("mr_ghast_griefing", true);
        mrFarmLand = BUILDER.comment("Mobs trample farmland (TRUE Vanilla behaviour)").define("mr_farm_land", true);
        mrNetherSkeleton = BUILDER.comment("Replace Skeletons in Nether with Wither Skeletons)").define("mr_farm_land", true);

        BUILDER.pop();

        // -------------------------------------------------
        // Better Mining
        // -------------------------------------------------
        BUILDER.comment("Better Mining system settings").push("Better Mining");

        bmEnabled = BUILDER.comment("Enable or disable Better Mining").define("bm_enabled", true);

        bmEfficiencyLvl = BUILDER
                .comment("Extra efficiency level applied to mining")
                .defineInRange("bm_efficiency_lvl", 1, 0, 5);

        bmHasteLvl = BUILDER
                .comment("Haste level required")
                .defineInRange("bm_haste_lvl", 2, 0, 2);

        bmMiningSpeedMultiplier = BUILDER
                .comment("Mining speed multiplier (1.25 - 2.5)")
                .defineInRange("bm_mining_speed_multiplier", 1.25, 1.25, 2.5);


        bmTools = BUILDER
                .comment("Tools affected by Better Mining")
                .defineListAllowEmpty("bm_tools",
                        List.of("minecraft:diamond_pickaxe", "minecraft:netherite_pickaxe"), null, ModConfigCommon::validateItem);

        bmBlocksAffected = BUILDER
                .comment("Blocks affected by Better Mining")
                .defineListAllowEmpty("bm_blocks_affected",
                        List.of("minecraft:deepslate","minecraft:tuff","minecraft:granite"),null, ModConfigCommon::validateBlock);

        BUILDER.pop();

        // -------------------------------------------------
        // Waxed WorkStations
        // -------------------------------------------------
        BUILDER.comment("Waxed WorkStations settings").push("Waxed WorkStations");

        wwsEnabled = BUILDER
                .comment("Enable or disable Waxed WorkStations")
                .define("wws_enabled", true);

        BUILDER.pop();
    }

    private static boolean validateItem(final Object obj) {
        return obj instanceof String toolName && BuiltInRegistries.ITEM.containsKey(Identifier.parse(toolName));
    }

    private static boolean validateEntity(final Object obj) {
        return obj instanceof String entityName && BuiltInRegistries.ENTITY_TYPE.containsKey(Identifier.parse(entityName));
    }

    private static boolean validateBlock(final Object obj) {
        return obj instanceof String blockName && BuiltInRegistries.BLOCK.containsKey(Identifier.parse(blockName));
    }

    public static Set<Block> bm_blocks_affected;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        bm_blocks_affected = bmBlocksAffected.get().stream().map(blockName -> BuiltInRegistries.BLOCK.getValue(Identifier.parse(blockName))).collect((Collectors.toSet()));
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
