package dev.deltavisuals.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.Vec3d;

/**
 * Version-sensitive Minecraft calls that are not plain Screen/DrawContext usage.
 * If a Minecraft/Yarn update breaks compilation, the fix is most likely in this file,
 * in render/Pipelines.java or in render/WorldHook.java.
 */
public final class Compat {
    private Compat() {}

    /** Camera position in world space. (Older Yarn: camera.getPos().) */
    public static Vec3d cameraPos(Camera camera) { return camera.getCameraPos(); }

    /** Render partial tick (0..1). */
    public static float partialTick(MinecraftClient mc) { return mc.getRenderTickCounter().getTickProgress(false); }

    /** True if a vanilla Minecraft control is currently bound to the same physical key / button. */
    public static boolean conflictsWithVanilla(MinecraftClient mc, boolean mouse, int code) {
        try {
            InputUtil.Type t = mouse ? InputUtil.Type.MOUSE : InputUtil.Type.KEYSYM;
            String key = t.createFromCode(code).getTranslationKey();
            for (KeyBinding kb : mc.options.allKeys) {
                if (key.equals(kb.getBoundKeyTranslationKey())) return true;
            }
        } catch (Throwable ignored) { }
        return false;
    }

    // ---------------------------------------------------------------- custom sky helpers

    /** Render distance in chunks (an option value, not the effective distance). */
    public static int viewDistanceChunks(MinecraftClient mc) { return mc.options.getViewDistance().getValue(); }

    private static CloudRenderMode savedClouds;

    /**
     * Hides / restores the vanilla clouds through the normal video option, so they do not float over a custom sky.
     * The previous value is restored when the sky is switched off and when the client stops.
     */
    public static void hideClouds(MinecraftClient mc, boolean hide) {
        try {
            var opt = mc.options.getCloudRenderMode();
            if (hide) {
                if (savedClouds == null && opt.getValue() != CloudRenderMode.OFF) {
                    savedClouds = opt.getValue();
                    opt.setValue(CloudRenderMode.OFF);
                }
            } else if (savedClouds != null) {
                opt.setValue(savedClouds);
                savedClouds = null;
            }
        } catch (Throwable ignored) { }
    }
}
