package dev.kyzel.kyzen.gfx;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Window;
import dev.kyzel.kyzen.engine.components.LightComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.utils.AssetManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {
    public static final int MAX_BATCH_SIZE = 100;
    private final List<RenderBatch> batches;
    private final List<GameObject> currentObjectBatch;
    private final Shader screenShader;
    private final Framebuffer lightBuffer, compositeBuffer;

    public Renderer() {
        batches = new ArrayList<>();
        currentObjectBatch = new ArrayList<>();
        screenShader = AssetManager.getShader("assets/shaders/screen.glsl");

        lightBuffer = new Framebuffer(Window.get().getWidth(), Window.get().getHeight());
        compositeBuffer = new Framebuffer(Window.get().getWidth(), Window.get().getHeight());
    }

    public static void addSpriteToBatches(SpriteComponent sprite, List<RenderBatch> batches) {
        boolean added = false;
        float spriteZIndex = sprite.getOwner().getZIndex();
        for (RenderBatch renderBatch : batches) {
            if (renderBatch.hasRoom() && renderBatch.getZIndex() == spriteZIndex) {
                Texture texture = sprite.getTexture();
                if (texture == null || (renderBatch.hasTexture(texture) || renderBatch.hasTextureRoom())) {
                    renderBatch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, spriteZIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void add(GameObject gameObject) {
        currentObjectBatch.add(gameObject);
        for (SpriteComponent spriteComponent : gameObject.getComponents(SpriteComponent.class)) {
            addSpriteToBatches(spriteComponent, batches);
        }
        LightComponent light = gameObject.getComponent(LightComponent.class);
        if (light != null) {
            LightRenderer.getInstance().addLight(light);
        }
    }

    public void remove(GameObject gameObject) {
        for (SpriteComponent spriteComponent : gameObject.getComponents(SpriteComponent.class)) {
            remove(spriteComponent);
        }
        LightComponent light = gameObject.getComponent(LightComponent.class);
        if (light != null) {
            LightRenderer.getInstance().removeLight(light);
        }
    }

    public void cleanup() {
        for (GameObject gameObject : currentObjectBatch) {
            remove(gameObject);
        }
        currentObjectBatch.clear();
    }

    private void remove(SpriteComponent sprite) {
        for (RenderBatch renderBatch : batches) {
            if (renderBatch.removeSprite(sprite)) break;
        }
    }

    public void render() {
        // clear screen
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // render everything except lights
        for (RenderBatch renderBatch : batches) {
            renderBatch.render();
        }

        // render all texts
        TextRenderer.renderAllTexts();

        // render and composite lights
        compositeBuffer.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (RenderBatch renderBatch : batches) {
            blendFramebuffer(renderBatch.getFramebuffer(), compositeBuffer);
        }

        LightRenderer.getInstance().setHasDarkness(SceneManager.getCurrentScene().getCurrentLevel().hasDarkness());
        LightRenderer.getInstance().renderToFramebuffer(compositeBuffer);

        for (int i = 0; i < TextRenderer.getRenderBatches().size(); i++) {
            blendFramebuffer(TextRenderer.getFramebuffer(i), compositeBuffer);
        }
        compositeBuffer.unbind();

        // final render to screen
        glViewport(Window.get().getVpX(), Window.get().getVpY(),
                    Window.get().getWidth(), Window.get().getHeight());
        renderToScreen(compositeBuffer);
    }

    private void blendFramebuffer(Framebuffer from, Framebuffer to) {
        to.bind();
        renderToScreen(from);
        to.unbind();
    }

    private void renderToScreen(Framebuffer framebuffer) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        screenShader.use();
        screenShader.uploadTexture("screenTexture", 0);
        glDisable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE0);
        glBindVertexArray(AssetManager.getFullScreenQuadVAO());
        glBindTexture(GL_TEXTURE_2D, framebuffer.getTextureID());
        glDrawArrays(GL_TRIANGLES, 0, 6);

        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
        screenShader.detach();
    }

    public void resize(int width, int height) {
        for (RenderBatch renderBatch : batches) {
            renderBatch.resize(width, height);
        }
        lightBuffer.resize(width, height);
        compositeBuffer.resize(width, height);
    }
}
