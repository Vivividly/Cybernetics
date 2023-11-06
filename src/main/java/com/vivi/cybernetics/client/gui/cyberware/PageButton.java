package com.vivi.cybernetics.client.gui.cyberware;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.vivi.cybernetics.Cybernetics;
import com.vivi.cybernetics.client.gui.util.ITransparentWidget;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PageButton extends AbstractButton implements ITransparentWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Cybernetics.MOD_ID, "textures/gui/cyberware/buttons.png");

    private final CyberwareScreen<?> screen;
    private final boolean left;
    private boolean canPress;
    public PageButton(CyberwareScreen<?> screen, int pX, int pY, boolean left) {
        super(pX, pY, 12,9,Component.empty());
        this.screen = screen;
        this.left = left;
        alpha = 0.0f;
        visible = false;
        active = false;
    }

    @Override
    public void onPress() {
        if(canPress) {
            int newPage = left ? screen.getCurrentPage() - 1 : screen.getCurrentPage() + 1;
            screen.setCurrentPage(newPage);
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public float getTransparency() {
        return alpha;
    }

    @Override
    public void setTransparency(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        visible = alpha > 0.0f;
        active = visible;
        canPress = left ? screen.getCurrentPage() > 0 : screen.getCurrentPage() < 2;
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    public void renderButton(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.enableBlend();
        float color = this.isHoveredOrFocused() ? 1.0F : 0.65F;
        RenderSystem.setShaderColor(color, color, color, alpha);
        int v = left ? 0 : 10;
        int u;
        u = canPress ? 0 : 13;
        blit(pPoseStack, this.x, this.y, this.width, this.height, u, v, this.width, this.height, 32, 32);
    }
}