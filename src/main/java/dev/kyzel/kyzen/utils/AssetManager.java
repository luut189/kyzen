package dev.kyzel.kyzen.utils;

import dev.kyzel.kyzen.gfx.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class AssetManager {
    private static int currentID = 0;
    private static final Map<String, Shader> shaders = new HashMap<>();
    private static final Map<String, Texture> textures = new HashMap<>();
    private static final Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static final Map<String, Framebuffer> framebuffers = new HashMap<>();
    private static final Map<String, ColorPalette> colorPalettes = new HashMap<>();
    private static int fullScreenQuadVAO, fullScreenQuadVBO;

    public static void init() {
        AssetManager.getShader("assets/shaders/default.glsl");
        AssetManager.getShader("assets/shaders/screen.glsl");
        AssetManager.getShader("assets/shaders/blur.glsl");
        AssetManager.getShader("assets/shaders/post-process.glsl");
        AssetManager.getShader("assets/shaders/light.glsl");
        AssetManager.addSpritesheet("assets/textures/99c-font.png", 16, 16, 0);
        AssetManager.addSpritesheet("assets/textures/spritesheet.png", 16, 16, 0);
    }

    public static int getNextID() {
        return currentID++;
    }

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

    public static void addSpritesheet(String name, int spriteWidth, int spriteHeight, int spacing) {
        File file = new File(name);
        if (!spritesheets.containsKey(file.getAbsolutePath())) {
            spritesheets.put(file.getAbsolutePath(),
                             new Spritesheet(AssetManager.getTexture(name), spriteWidth, spriteHeight, spacing));
        }
    }

    public static Spritesheet getSpritesheet(String name) {
        File file = new File(name);
        assert spritesheets.containsKey(file.getAbsolutePath()) : "Error: Spritesheet not found";
        return spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }

    public static ColorPalette getColorPalette(String name) {
        File file = new File(name);
        if (!colorPalettes.containsKey(file.getAbsolutePath())) {
            ColorPalette colorPalette = new ColorPalette(name);
            colorPalettes.put(file.getAbsolutePath(), colorPalette);
            return colorPalette;
        }
        return colorPalettes.get(file.getAbsolutePath());
    }

    public static Framebuffer createFramebuffer(String name, int width, int height) {
        if (framebuffers.containsKey(name)) {
            framebuffers.get(name).resize(width, height);
            return framebuffers.get(name);
        }
        Framebuffer framebuffer = new Framebuffer(width, height);
        framebuffers.put(name, framebuffer);
        return framebuffer;
    }

    public static Framebuffer getFramebuffer(String name) {
        assert framebuffers.containsKey(name) : "Error: Framebuffer not found";
        return framebuffers.get(name);
    }

    public static int getFullScreenQuadVAO() {
        float[] quadVertices = {
                // positions   // texCoords
                -1.0f,  1.0f,  0.0f, 1.0f,
                -1.0f, -1.0f,  0.0f, 0.0f,
                 1.0f, -1.0f,  1.0f, 0.0f,

                -1.0f,  1.0f,  0.0f, 1.0f,
                 1.0f, -1.0f,  1.0f, 0.0f,
                 1.0f,  1.0f,  1.0f, 1.0f
        };
        if (fullScreenQuadVAO == 0) {
            fullScreenQuadVAO = glGenVertexArrays();
            fullScreenQuadVBO = glGenBuffers();
            glBindVertexArray(fullScreenQuadVAO);
            glBindBuffer(GL_ARRAY_BUFFER, fullScreenQuadVBO);
            glBufferData(GL_ARRAY_BUFFER, quadVertices, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0L);
            glEnableVertexAttribArray(1);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2L * Float.BYTES);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        return fullScreenQuadVAO;
    }
}
