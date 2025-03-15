package dev.kyzel.game.tiles;

import dev.kyzel.engine.Transform;
import dev.kyzel.engine.components.SpriteComponent;

public abstract class AnimatedTile extends Tile {
    protected int numSprites, spriteOffset, currentIndex;
    protected int[] spriteIndices;
    protected float currentDelta, changeInterval = 1.4f;

    public AnimatedTile(Transform transform, float zIndex, int numSprites, int spriteOffset) {
        super(transform, zIndex);
        this.numSprites = numSprites;
        this.spriteOffset = spriteOffset;
        currentIndex = (int) Math.floor(Math.random() * numSprites);
        spriteIndices = new int[numSprites];
        for (int i = 0; i < numSprites; i++) {
            spriteIndices[i] = spriteOffset + i;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentDelta += deltaTime;
        if (currentDelta >= changeInterval) {
            currentIndex = (int) Math.floor(Math.random() * numSprites);
            currentDelta = 0.0f;
        }
        this.getComponent(SpriteComponent.class).setSprite(sheet.getSprite(spriteIndices[currentIndex]));
    }
}
