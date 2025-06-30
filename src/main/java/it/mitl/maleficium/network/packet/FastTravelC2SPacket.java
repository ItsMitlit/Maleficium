package it.mitl.maleficium.network.packet;

import it.mitl.maleficium.network.serverhandler.VampireRequests;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class FastTravelC2SPacket {

    public FastTravelC2SPacket() {
    }

    public FastTravelC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            // Do the server-side processing
            VampireRequests.handleFastTravelRequest(player);
        });
        context.setPacketHandled(true);
    }
}
