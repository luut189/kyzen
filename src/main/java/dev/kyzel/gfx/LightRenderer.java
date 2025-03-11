package dev.kyzel.gfx;

import dev.kyzel.engine.SceneManager;
import dev.kyzel.engine.Window;
import dev.kyzel.engine.components.LightComponent;
import dev.kyzel.engine.Transform;
import dev.kyzel.engine.GameObject;
import dev.kyzel.utils.AssetManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class LightRenderer {
    private static LightRenderer instance;
    private final List<LightComponent> lights;
    private final Shader lightShader, blurShader, postProcessShader;
    private Framebuffer lightFramebuffer;
    private final Framebuffer[] blurFramebuffers = new Framebuffer[2];

    private LightRenderer() {
        this.lights = new ArrayList<>();
        lightShader = AssetManager.getShader("assets/shaders/light.glsl");
        blurShader = AssetManager.getShader("assets/shaders/blur.glsl");
        postProcessShader = AssetManager.getShader("assets/shaders/post-process.glsl");

        lightFramebuffer =
                AssetManager.createFramebuffer("lightBuffer", Window.get().getWidth(), Window.get().getHeight());
        blurFramebuffers[0] =
                AssetManager.createFramebuffer("blurBuffer1", Window.get().getWidth(), Window.get().getHeight());
        blurFramebuffers[1] =
                AssetManager.createFramebuffer("blurBuffer2", Window.get().getWidth(), Window.get().getHeight());
    }

    public static LightRenderer getInstance() {
        if (instance == null) {
            instance = new LightRenderer();
        }
        return instance;
    }

    public void addLight(LightComponent light) {
        lights.add(light);
    }

    public void removeLight(LightComponent light) {
        lights.remove(light);
    }

    private void renderLights() {
        lightShader.use();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        for (LightComponent light : lights) {
            GameObject entity = light.gameObject;
            if (entity == null) continue;

            Transform transform = entity.transform;
            if (transform == null) continue;

            float objectWidth = transform.scale.x;
            float objectHeight = transform.scale.y;
            lightShader.uploadMatrix4f("projection",
                    SceneManager.getCurrentScene().getCamera().getProjectionMatrix());
            lightShader.uploadMatrix4f("view", SceneManager.getCurrentScene().getCamera().getViewMatrix());


            // Calculate center offset from bottom-left origin
            Vector2f centerOffset = new Vector2f(objectWidth / 2.0f, objectHeight / 2.0f);

            // Apply the offset to get the center position
            Vector2f lightCenterPos = new Vector2f(
                    transform.position.x + centerOffset.x,
                    transform.position.y + centerOffset.y
            );

            lightShader.uploadVec2f("lightPos", lightCenterPos);
            lightShader.uploadVec3f("lightColor", light.color);
            lightShader.uploadFloat("lightRadius", light.radius);
            lightShader.uploadFloat("ambientStrength", light.intensity);

            renderFullscreenQuad();
        }

        glDisable(GL_BLEND);
        lightShader.detach();
    }

    private void renderFullscreenQuad() {
        glBindVertexArray(AssetManager.getFullScreenQuadVAO());
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
    }

    public void renderToFramebuffer(Framebuffer framebuffer) {
        lightFramebuffer.bind();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glDisable(GL_DEPTH_TEST);
        renderLights();
        lightFramebuffer.unbind();

        boolean horizontal = true, firsIteration = true;
        int amount = 2;
        for (int i = 0; i < amount; i++) {
            blurFramebuffers[horizontal ? 0 : 1].bind();
            blurShader.uploadInt("horizontal", horizontal ? 1 : 0);
            glBindTexture(GL_TEXTURE_2D, firsIteration ? lightFramebuffer.getTextureID() :
                    blurFramebuffers[horizontal ? 0 : 1].getTextureID());
            renderFullscreenQuad();
            horizontal = !horizontal;
            if (firsIteration) firsIteration = false;
            blurFramebuffers[horizontal ? 0 : 1].unbind();
        }

        framebuffer.bind();
        glViewport(0,0, framebuffer.getWidth(), framebuffer.getHeight());
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        postProcessShader.use();

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, lightFramebuffer.getTextureID());
        postProcessShader.uploadTexture("uScene", 0);

        glActiveTexture(GL_TEXTURE0 + 1);
        glBindTexture(GL_TEXTURE_2D, blurFramebuffers[0].getTextureID());
        postProcessShader.uploadTexture("uBloom", 1);

        renderFullscreenQuad();
        framebuffer.unbind();

        blurShader.detach();
        postProcessShader.detach();
    }

    public void resize(int width, int height) {
        lightFramebuffer.resize(width, height);
        blurFramebuffers[0].resize(width, height);
        blurFramebuffers[1].resize(width, height);
    }
}
