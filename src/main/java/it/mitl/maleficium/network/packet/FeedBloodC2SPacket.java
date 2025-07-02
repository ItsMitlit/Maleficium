package it.mitl.maleficium.network.packet;

import it.mitl.maleficium.network.serverhandler.VampireRequests;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class FeedBloodC2SPacket {
    private final UUID entityUUID;

    public FeedBloodC2SPacket(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public FeedBloodC2SPacket(FriendlyByteBuf buf) {
        this.entityUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(entityUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            VampireRequests.handleFeedBloodRequest(player, entityUUID);
        });
        context.setPacketHandled(true);
    }

}
