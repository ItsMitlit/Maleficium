package it.mitl.mclegacies.network.packet;

import it.mitl.mclegacies.network.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ToggleBuffedC2SPacket {

    public ToggleBuffedC2SPacket() {
    }

    public ToggleBuffedC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            // Do the server-side processing
            ServerHandler.handleBuffToggleRequest(player);
        });
        context.setPacketHandled(true);
    }
}
