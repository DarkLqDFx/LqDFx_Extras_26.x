package work.lqdfxnet.lqdfxextras.Util;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import work.lqdfxnet.lqdfxextras.ModConfigCommon;

public class ModHostilityOverride {

    public static Boolean getOverride(Mob mob) {
        EntityType<?> type = mob.getType();
        Identifier id = EntityType.getKey(type);

        if (id == null) return null;

        // Passive override
        if (ModConfigCommon.pacifierExcludeAnimal.get().contains(id.toString())) {
            return Boolean.FALSE;
        }

        // Hostile override
        if (ModConfigCommon.pacifierExcludeMonster.get().contains(id.toString())) {
            return Boolean.TRUE;
        }

        if (ModConfigCommon.pacifierExcludeBoss.get().contains(id.toString())) { return Boolean.TRUE; }

        return null; // No override
    }
}
