package com.vivi.cybernetics.common.mixin.client;


import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.vivi.cybernetics.client.shader.BerserkRenderer;
import com.vivi.cybernetics.client.shader.CybPostShaders;
import com.vivi.cybernetics.client.shader.ScannerRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow @Final private RenderBuffers renderBuffers;
    @Shadow @Final private Minecraft minecraft;
    @Shadow private PostChain entityEffect;
    @Shadow private int ticks;

    @Shadow @Nullable private PostChain transparencyChain;
    @Unique
    private RenderTarget depthRenderTarget;

    @ModifyVariable(method = "renderEntity", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private MultiBufferSource cybernetics$modifyMultiBufferSource(MultiBufferSource original, Entity entity, double pCamX, double pCamY, double pCamZ, float partialTick, PoseStack pPoseStack) {
//        if(ScannerRenderer.shouldRenderGlowingEntiy(entity)) {
//
//            OutlineBufferSource outlinebuffersource = renderBuffers.outlineBufferSource();
//            int color = 0xd1221f;
//            int r = color >> 16 & 255;
//            int g = color >> 8 & 255;
//            int b = color & 255;
//            outlinebuffersource.setColor(r, g, b, 255);
//
//            entityEffect.process(partialTick);
//            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
//
//            return outlinebuffersource;
//        }
        return original;
    }

    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void cybernetics$renderEntity(Entity entity, double pCamX, double pCamY, double pCamZ, float partialTick, PoseStack pPoseStack, MultiBufferSource multiBufferSource, CallbackInfo ci) {
        if(ScannerRenderer.getInstance().shouldRenderGlowingEntiy(entity)) {
            OutlineBufferSource outlinebuffersource = renderBuffers.outlineBufferSource();
            int color = 0xd1221f;
            int r = color >> 16 & 255;
            int g = color >> 8 & 255;
            int b = color & 255;
            outlinebuffersource.setColor(r, g, b, 255);
        }
    }

    private void copyDepthBuffer() {
        if(depthRenderTarget == null) {
            depthRenderTarget = new TextureTarget(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), true, Minecraft.ON_OSX);
        }
        RenderTarget main = minecraft.getMainRenderTarget();
        if(depthRenderTarget.width != main.width || depthRenderTarget.height != main.height) {
            depthRenderTarget.resize(main.width, main.height, false);
        }
        if(main.isStencilEnabled()) {
            depthRenderTarget.enableStencil();
        }
        else if(depthRenderTarget.isStencilEnabled()) {
            depthRenderTarget.destroyBuffers();
            depthRenderTarget = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
        }

        depthRenderTarget.setClearColor(0, 0, 0, 0);
        depthRenderTarget.copyDepthFrom(main);
        main.bindWrite(false);
    }


    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OutlineBufferSource;endOutlineBatch()V", shift = At.Shift.BEFORE))
    private void cybernetics$renderScan(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        copyDepthBuffer();
        ScannerRenderer.getInstance().renderScan(pPoseStack, ticks, pPartialTick, pCamera, depthRenderTarget);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V", ordinal = 1))
    private void cybernetics$renderLevelPreFabulous(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        //because of COURSE it does Minecraft nukes the depth buffer in fabulous graphics before rendering the "transparency" post shader.
        //This copies the Depth buffer before it's nuked to a new render target
        //only done on fabulous graphics
        if(transparencyChain != null) {
            copyDepthBuffer();
        }

    }

    @Inject(method = "renderLevel", at = @At("TAIL"))
    private void cybernetics$renderLevelPost(PoseStack pPoseStack, float pPartialTick, long pFinishNanoTime, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        //copies *back* the depth buffer
        if(transparencyChain != null) {
            minecraft.getMainRenderTarget().copyDepthFrom(depthRenderTarget);
        }

        //renders all post shaders
        BerserkRenderer.getInstance().render(pPoseStack, ticks, pPartialTick);
        minecraft.getMainRenderTarget().bindWrite(false);
    }
}
