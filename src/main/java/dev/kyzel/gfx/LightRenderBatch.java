package dev.kyzel.gfx;

import dev.kyzel.engine.SceneManager;
import dev.kyzel.engine.components.LightComponent;
import dev.kyzel.engine.Transform;
import dev.kyzel.engine.GameObject;
import dev.kyzel.utils.AssetManager;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class LightRenderBatch {
    private static LightRenderBatch instance;
    private final List<LightComponent> lights;
    private final Shader lightShader;

    private LightRenderBatch() {
        this.lights = new ArrayList<>();
        lightShader = AssetManager.getShader("assets/shaders/light.glsl");
    }

    public static LightRenderBatch getInstance() {
        if (instance == null) {
            instance = new LightRenderBatch();
        }
        return instance;
    }

    public void addLight(LightComponent light) {
        lights.add(light);
    }

    public void removeLight(LightComponent light) {
        lights.remove(light);
    }

    public void render() {
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

            glBindVertexArray(AssetManager.getFullScreenQuadVAO());
            glDrawArrays(GL_TRIANGLES, 0, 6);
            glBindVertexArray(0);
        }

        glDisable(GL_BLEND);
        lightShader.detach();
    }
}
