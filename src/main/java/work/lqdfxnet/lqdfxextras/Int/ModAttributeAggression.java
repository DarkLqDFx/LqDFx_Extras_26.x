package work.lqdfxnet.lqdfxextras.Int;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import work.lqdfxnet.lqdfxextras.Lqdfxextras;

@EventBusSubscriber
public class ModAttributeAggression {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Lqdfxextras.MODID);
    public static final DeferredHolder<Attribute, Attribute> AGGRESSION = REGISTRY.register("aggression", () -> new RangedAttribute("attribute.lqdfxextras.aggression", 0d, 0d, 1d).setSyncable(true));

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> event.add(entity, AGGRESSION));
    }
}