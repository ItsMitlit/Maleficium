package it.mitl.mclegacies.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static it.mitl.mclegacies.MCLegacies.MOD_ID;

/**
 * Handles network messages and server-side work queuing for the mod.
 */
public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    /**
     * Registers a network message with the SimpleChannel.
     *
     * @param messageType     The class type of the message.
     * @param encoder         A function to encode the message into a buffer.
     * @param decoder         A function to decode the message from a buffer.
     * @param messageConsumer A function to handle the message when received.
     * @param <T>             The type of the message.
     */
    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    // A thread-safe queue for scheduling server-side tasks.
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    /**
     * Queues a task to be executed on the server after a specified number of ticks.
     *
     * @param tick   The number of ticks to wait before executing the task.
     * @param action The task to execute.
     */
    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    /**
     * Handles the server tick event to process queued tasks.
     *
     * @param event The server tick event.
     */
    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

    /**
     * Sends a packet/message to the server.
     *
     * @param message The message to send.
     * @param <T> The message type.
     */
    public static <T> void sendToServer(T message) {
        PACKET_HANDLER.sendToServer(message);
    }
}
