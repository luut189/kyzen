package dev.kyzel.utils;

import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.gfx.Shader;
import dev.kyzel.gfx.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShader(String name) {
        File file = new File(name);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        }

        Shader shader = new Shader(name);
        shader.compileShader();
        shaders.put(file.getAbsolutePath(), shader);
        return shader;
    }

    public static Texture getTexture(String name) {
        File file = new File(name);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        }

        Texture texture = new Texture(name);
        textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static void addSpritesheet(String name, Spritesheet spritesheet) {
        File file = new File(name);
        if (!spritesheets.containsKey(file.getAbsolutePath())) {
            spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String name) {
        File file = new File(name);
        assert spritesheets.containsKey(file.getAbsolutePath()) : "Error: Spritesheet not found";
        return spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
