package work.lqdfxnet.lqdfxextras;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;
import work.lqdfxnet.lqdfxextras.Int.ModAttributeAggression;
import work.lqdfxnet.lqdfxextras.Int.ModGameRules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mod(Lqdfxextras.MODID)
public class Lqdfxextras {

    public static final String MODID = "lqdfxextras";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Tick scheduler queue
    private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public Lqdfxextras(IEventBus modEventBus, ModContainer modContainer) {

        // Register setup events
        modEventBus.addListener(this::commonSetup);

        // Register global event listeners
        NeoForge.EVENT_BUS.register(this);

        // Register deferred registries
        ModAttributeAggression.REGISTRY.register(modEventBus);
        ModGameRules.REGISTRY.register(modEventBus);

        // Register config
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigCommon.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Lqdfxextras common setup complete.");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Server starting — Lqdfxextras active.");
    }

    // ---------------------------
    // Client-only events
    // ---------------------------
    @EventBusSubscriber(modid = MODID)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("Client setup complete.");
            LOGGER.info("Logged-in player: {}", Minecraft.getInstance().getUser().getName());
        }
    }

    // ---------------------------
    // Tick Scheduler
    // ---------------------------
    public static void queueServerWork(int ticks, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            workQueue.add(new Tuple<>(action, ticks));
        }
    }

    @SubscribeEvent
    public void tick(ServerTickEvent.Post event) {

        List<Tuple<Runnable, Integer>> ready = new ArrayList<>();

        workQueue.forEach(work -> {
            work.setB(work.getB() - 1);
            if (work.getB() <= 0) {
                ready.add(work);
            }
        });

        ready.forEach(e -> e.getA().run());
        workQueue.removeAll(ready);
    }
}
