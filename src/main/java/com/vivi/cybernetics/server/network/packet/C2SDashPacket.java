package com.vivi.cybernetics.server.network.packet;

import com.vivi.cybernetics.common.item.DashCyberwareItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SDashPacket extends Packet {

    public C2SDashPacket() {

    }

    public C2SDashPacket(FriendlyByteBuf buf) {

    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            DashCyberwareItem.dash(player);
        });
        return true;
    }
}
