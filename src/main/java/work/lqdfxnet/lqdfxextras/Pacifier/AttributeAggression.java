package work.lqdfxnet.lqdfxextras.Pacifier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import work.lqdfxnet.lqdfxextras.Lqdfxextras;


@EventBusSubscriber
public class AttributeAggression {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, "lqdfxextras");

    public static final DeferredHolder<Attribute, Attribute> AGGRESSION  =
            ATTRIBUTES.register("aggression",
                    () -> new RangedAttribute(
                            "attribute.lqdfxextras.aggression",
                            0.0,   // default
                            0.0,   // min
                            1.0    // max
                    ).setSyncable(true)
            );


    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        Lqdfxextras.LOGGER.info("Adding attributes");
        event.getTypes().forEach(type -> {
            Identifier id = EntityType.getKey(type);
            if (type.getCategory().isFriendly()) return;
            if (id != null) event.add(type, AttributeAggression.AGGRESSION );
        });
    }

    public static void setAggression(Mob mob, int state) {
        AttributeInstance inst = mob.getAttribute(AttributeAggression.AGGRESSION );
        if (inst == null) return;

        inst.setBaseValue(state == 1 ? 1.0 : 0.0);
    }

    public static int getAggression(LivingEntity mob) {
        AttributeInstance inst = mob.getAttribute(AttributeAggression.AGGRESSION );
        if (inst == null) return 0;
        return inst.getValue() >= 1.0 ? 1 : 0;
    }
}