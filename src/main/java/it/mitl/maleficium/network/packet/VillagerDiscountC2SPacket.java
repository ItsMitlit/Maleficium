package it.mitl.maleficium.network.packet;

import it.mitl.maleficium.network.ServerHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class VillagerDiscountC2SPacket {
    private final UUID villagerUUID;

    public VillagerDiscountC2SPacket(UUID villagerUUID) {
        this.villagerUUID = villagerUUID;
    }

    public VillagerDiscountC2SPacket(FriendlyByteBuf buf) {
        this.villagerUUID = buf.readUUID();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(villagerUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            // Do the server-side processing
            ServerHandler.handleDiscountRequest(player, villagerUUID);
        });
        context.setPacketHandled(true);
    }
}
