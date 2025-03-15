package dev.kyzel.kyzen.game.tiles;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.LightComponent;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class LavaTile extends LiquidTile {

    private static final Vector3f color = ColorPalette.getDefaultRGB(1, 0.427f, 0);

    public LavaTile(Transform transform, float zIndex) {
        super(transform, zIndex, new Vector4f(color, 1f));
        float radius = transform.scale.x * ((float) Math.random() * 3f + 1f);
        this.addComponent(new LightComponent(radius, color, 0.4f));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (currentDelta >= changeInterval) {
            boolean blooming = Math.random() > 0.5f;
            float bloomValue = 100f;
            LightComponent lightComponent = this.getComponent(LightComponent.class);
            float newRadius = lightComponent.getRadius() + (blooming ? deltaTime * bloomValue : -deltaTime * bloomValue);
            lightComponent.setRadius(newRadius);
            currentDelta = 0;
        }
    }
}
