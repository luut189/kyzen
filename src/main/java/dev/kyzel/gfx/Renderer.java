package dev.kyzel.gfx;

import dev.kyzel.engine.components.SpriteRenderer;
import dev.kyzel.engine.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 100;
    private List<RenderBatch> batches;

    public Renderer() {
        batches = new ArrayList<>();
    }

    public void add(GameObject gameObject) {
        SpriteRenderer spr = gameObject.getComponent(SpriteRenderer.class);
        if (spr != null) {
            add(spr);
        }
    }

    private void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch renderBatch : batches) {
            if (renderBatch.hasRoom()) {
                Texture texture = sprite.getTexture();
                if (texture == null || (renderBatch.hasTexture(texture) || renderBatch.hasTexture(texture))) {
                    renderBatch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
        }
    }

    public void render() {
        for (RenderBatch renderBatch : batches) {
            renderBatch.render();
        }
    }
}
