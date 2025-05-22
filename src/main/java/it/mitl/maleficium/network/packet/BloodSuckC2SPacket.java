package it.mitl.maleficium.network.packet;

import it.mitl.maleficium.network.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class BloodSuckC2SPacket {
    private final UUID entityUUID;

    public BloodSuckC2SPacket(UUID entityUUID) {
        this.entityUUID = entityUUID;
    }

    public BloodSuckC2SPacket(FriendlyByteBuf buf) {
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

            // Do the server-side processing
            ServerHandler.handleBloodSuckRequest(player, entityUUID);
        });
        context.setPacketHandled(true);
    }
}
