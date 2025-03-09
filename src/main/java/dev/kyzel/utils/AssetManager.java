package dev.kyzel.utils;

import dev.kyzel.engine.components.Spritesheet;
import dev.kyzel.gfx.Framebuffer;
import dev.kyzel.gfx.Shader;
import dev.kyzel.gfx.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class AssetManager {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Framebuffer> framebuffers = new HashMap<>();
    private static int quadVAO, quadVBO;

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

    public static Framebuffer getFramebuffer(String name, int width, int height) {
        if (framebuffers.containsKey(name)) {
            return framebuffers.get(name);
        }
        Framebuffer framebuffer = new Framebuffer(width, height);
        framebuffers.put(name, framebuffer);
        return framebuffer;
    }

    public static void resizeFramebuffer(String name, int width, int height) {
        assert framebuffers.containsKey(name) : "Error: Framebuffer not found";

        framebuffers.get(name).resize(width, height);
    }

    public static int getQuadVAO() {
        float[] quadVertices = {
                // positions   // texCoords
                -1.0f,  1.0f,  0.0f, 1.0f,
                -1.0f, -1.0f,  0.0f, 0.0f,
                 1.0f, -1.0f,  1.0f, 0.0f,

                -1.0f,  1.0f,  0.0f, 1.0f,
                 1.0f, -1.0f,  1.0f, 0.0f,
                 1.0f,  1.0f,  1.0f, 1.0f
        };
        if (quadVAO == 0) {
            quadVAO = glGenVertexArrays();
            quadVBO = glGenBuffers();
            glBindVertexArray(quadVAO);
            glBindBuffer(GL_ARRAY_BUFFER, quadVBO);
            glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0L);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2L * Float.BYTES);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        return quadVAO;
    }
}
