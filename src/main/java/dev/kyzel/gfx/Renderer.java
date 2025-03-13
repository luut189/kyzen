package dev.kyzel.gfx;

import dev.kyzel.engine.Window;
import dev.kyzel.engine.components.LightComponent;
import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.engine.GameObject;
import dev.kyzel.utils.AssetManager;

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
    private final Shader screenShader;
    private final Framebuffer lightBuffer, compositeBuffer;

    public Renderer() {
        batches = new ArrayList<>();
        screenShader = AssetManager.getShader("assets/shaders/screen.glsl");

        lightBuffer = new Framebuffer(Window.get().getWidth(), Window.get().getHeight());
        compositeBuffer = new Framebuffer(Window.get().getWidth(), Window.get().getHeight());
    }

    public void add(GameObject gameObject) {
        SpriteComponent spr = gameObject.getComponent(SpriteComponent.class);
        if (spr != null) {
            add(spr);
        }
        LightComponent light = gameObject.getComponent(LightComponent.class);
        if (light != null) {
            LightRenderer.getInstance().addLight(light);
        }
    }

    private void add(SpriteComponent sprite) {
        boolean added = false;
        for (RenderBatch renderBatch : batches) {
            if (renderBatch.hasRoom() && renderBatch.getzIndex() == sprite.gameObject.getzIndex()) {
                Texture texture = sprite.getTexture();
                if (texture == null || (renderBatch.hasTexture(texture) || renderBatch.hasTextureRoom())) {
                    renderBatch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getzIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void remove(GameObject gameObject) {
        SpriteComponent spr = gameObject.getComponent(SpriteComponent.class);
        if (spr != null) {
            remove(spr);
        }
        LightComponent light = gameObject.getComponent(LightComponent.class);
        if (light != null) {
            LightRenderer.getInstance().removeLight(light);
        }
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

        // render and composite lights
        compositeBuffer.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        for (RenderBatch renderBatch : batches) {
            blendFramebuffer(renderBatch.getFramebuffer(), compositeBuffer);
        }
        LightRenderer.getInstance().renderToFramebuffer(compositeBuffer);
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
