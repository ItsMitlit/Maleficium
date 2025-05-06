package it.mitl.mclegacies.network;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

/**
 * Handles custom player variables and their synchronization across the network.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModVariables {

    /**
     * Registers the network message for syncing player variables.
     *
     * @param event The setup event.
     */
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        ModMessages.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
    }

    /**
     * Registers the custom capability for player variables.
     *
     * @param event The capability registration event.
     */
    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent event) {
        event.register(PlayerVariables.class);
    }

    /**
     * Handles events related to syncing and cloning player variables.
     */
    @Mod.EventBusSubscriber
    public static class EventBusVariableHandlers {

        /**
         * Syncs player variables when a player logs in.
         *
         * @param event The player login event.
         */
        @SubscribeEvent
        public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
            if (!event.getEntity().level().isClientSide())
                ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getEntity());
        }

        /**
         * Syncs player variables when a player respawns.
         *
         * @param event The player respawn event.
         */
        @SubscribeEvent
        public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
            if (!event.getEntity().level().isClientSide())
                ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getEntity());
        }

        /**
         * Syncs player variables when a player changes dimensions.
         *
         * @param event The player dimension change event.
         */
        @SubscribeEvent
        public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (!event.getEntity().level().isClientSide())
                ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(event.getEntity());
        }

        /**
         * Clones player variables when a player is cloned (e.g., during respawn).
         *
         * @param event The player clone event.
         */
        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            event.getOriginal().revive();
            PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
            PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
            clone.species = original.species;
            clone.faction = original.faction;
            clone.buffToggle = original.buffToggle;
            if (!event.isWasDeath()) {
            }
        }
    }

    /**
     * Capability instance for player variables.
     */
    public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerVariables>() {
    });

    /**
     * Provides the player variables capability and handles serialization/deserialization.
     */
    @Mod.EventBusSubscriber
    private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {

        /**
         * Attaches the player variables capability to players.
         *
         * @param event The capability attachment event.
         */
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
                event.addCapability(new ResourceLocation("mclegacies", "player_variables"), new PlayerVariablesProvider());
        }

        private final PlayerVariables playerVariables = new PlayerVariables();
        private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public Tag serializeNBT() {
            return playerVariables.writeNBT();
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            playerVariables.readNBT(nbt);
        }
    }

    /**
     * Stores custom player variables such as species and faction.
     */
    public static class PlayerVariables {
        public String species = "human";
        public String faction = "none";
        public boolean buffToggle = false;

        /**
         * Syncs the player variables to the client.
         *
         * @param entity The player entity.
         */
        public void syncPlayerVariables(Entity entity) {
            if (entity instanceof ServerPlayer serverPlayer)
                ModMessages.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
        }

        /**
         * Writes the player variables to NBT.
         *
         * @return The NBT tag containing the player variables.
         */
        public Tag writeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("species", species);
            nbt.putString("faction", faction);
            nbt.putBoolean("buffToggle", buffToggle);
            return nbt;
        }

        /**
         * Reads the player variables from NBT.
         *
         * @param tag The NBT tag containing the player variables.
         */
        public void readNBT(Tag tag) {
            CompoundTag nbt = (CompoundTag) tag;
            species = nbt.getString("species");
            faction = nbt.getString("faction");
            buffToggle = nbt.getBoolean("buffToggle");
        }
    }

    /**
     * Handles the synchronization of player variables between the server and client.
     */
    public static class PlayerVariablesSyncMessage {
        private final PlayerVariables data;

        /**
         * Constructs the message from a buffer.
         *
         * @param buffer The buffer containing the serialized data.
         */
        public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
            this.data = new PlayerVariables();
            this.data.readNBT(buffer.readNbt());
        }

        /**
         * Constructs the message with the given player variables.
         *
         * @param data The player variables to sync.
         */
        public PlayerVariablesSyncMessage(PlayerVariables data) {
            this.data = data;
        }

        /**
         * Serializes the message to a buffer.
         *
         * @param message The message to serialize.
         * @param buffer  The buffer to write to.
         */
        public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeNbt((CompoundTag) message.data.writeNBT());
        }

        /**
         * Handles the message on the client side.
         *
         * @param message          The received message.
         * @param contextSupplier  The context supplier for the network event.
         */
        public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer()) {
                    PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
                    variables.species = message.data.species;
                    variables.faction = message.data.faction;
                    variables.buffToggle = message.data.buffToggle;
                }
            });
            context.setPacketHandled(true);
        }
    }
}
