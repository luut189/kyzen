package dev.kyzel.gfx;

import dev.kyzel.engine.components.LightComponent;
import dev.kyzel.engine.components.SpriteComponent;
import dev.kyzel.engine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 100;
    private final List<RenderBatch> batches;

    public Renderer() {
        batches = new ArrayList<>();
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
                if (texture == null || (renderBatch.hasTexture(texture) || renderBatch.hasTexture(texture))) {
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

    public void render() {
        for (RenderBatch renderBatch : batches) {
            renderBatch.render();
        }
    }
}
