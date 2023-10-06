package com.vivi.cybernetics.event;

import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.cyberware.CyberwareSectionType;
import com.vivi.cybernetics.item.CyberwareItem;
import com.vivi.cybernetics.network.PacketHandler;
import com.vivi.cybernetics.network.packet.C2SDoubleJumpInputPacket;
import com.vivi.cybernetics.network.packet.C2SOpenCyberwarePacket;
import com.vivi.cybernetics.registry.ModCyberware;
import com.vivi.cybernetics.registry.ModKeybinds;
import com.vivi.cybernetics.registry.ModTags;
import com.vivi.cybernetics.util.CyberwareHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Cybernetics.MOD_ID)
public class ClientEvents {

    private static boolean releasedJump;
    private static boolean canDoubleJump;

    @SubscribeEvent
    public static void onRegisterKeybindignsEvent(RegisterKeyMappingsEvent event) {
        event.register(ModKeybinds.PLAYER_CYBERWARE_MENU);
    }

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;

        if(ModKeybinds.PLAYER_CYBERWARE_MENU.isDown()) {
            PacketHandler.sendToServer(new C2SOpenCyberwarePacket());
        }

        handleDoubleJump();
    }

    private static void handleDoubleJump() {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;

        if(player.isOnGround() || player.onClimbable() && !(player.isInWater())) {
            releasedJump = false;
            canDoubleJump = true;
        }
        else if(!player.input.jumping) {
            //jump key is not down, and player is in the air
            releasedJump = true;
        }
        else if(!player.getAbilities().flying && canDoubleJump && releasedJump) {
            //jump key pressed, player can double jump and has released the jump key
            canDoubleJump = false;
            PacketHandler.sendToServer(new C2SDoubleJumpInputPacket());
        }
    }
}
