package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.utils.AssetManager;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ColorPalette {

    public static ColorPalette getDefaultPalette() {
        return AssetManager.getColorPalette("assets/colors/resurrect-64.hex");
    }

    public static Vector3f getDefaultRandomRGB() {
        return getDefaultPalette().getRGB(
                (float) Math.random(),
                (float) Math.random(),
                (float) Math.random());
    }

    public static Vector4f getDefaultRandomRGBA(float a) {
        return new Vector4f(getDefaultRandomRGB(), a);
    }

    public static Vector4f getDefaultRandomRGBA() {
        return getDefaultRandomRGBA(1f);
    }

    public static Vector4f getDefaultRGBA(float r, float g, float b, float a) {
        return getDefaultPalette().getRGBA(r, g, b, a);
    }

    public static Vector4f getDefaultRGBA(float r, float g, float b) {
        return getDefaultPalette().getRGBA(r, g, b);
    }

    public static Vector3f getDefaultRGB(float r, float g, float b) {
        return getDefaultPalette().getRGB(r, g, b);
    }

    private final int[] PALETTE;

    public ColorPalette(String path) {
        Path file = Path.of(path);
        try {
            List<String> lines = Files.readAllLines(file);
            PALETTE = new int[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                PALETTE[i] = Integer.parseInt(lines.get(i), 16);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Vector4f getRGBA(float r, float g, float b) {
        return getRGBA(r, g, b, 1.0f);
    }

    public Vector4f getRGBA(float r, float g, float b, float a) {
        return new Vector4f(getRGB(r, g, b), a);
    }

    public Vector3f getRGB(float r, float g, float b) {
        Vector3f inputColor = new Vector3f(r, g, b);
        Vector3f outputColor = new Vector3f();
        double minDist = Double.MAX_VALUE;

        for (int i = 0; i < PALETTE.length; i++) {
            Vector3f color = getRGB(i);
            double dist = inputColor.distance(color);
            if (dist < minDist) {
                minDist = dist;
                outputColor = color;
            }
        }

        return outputColor;
    }

    private Vector3f getRGB(int index) {
        int color = PALETTE[index];
        float r = ((color & 0xff0000) >> 16) / 255.0f;
        float g = ((color & 0xff00) >> 8) / 255.0f;
        float b = (color & 0xff) / 255.0f;
        return new Vector3f(r, g, b);
    }
}
