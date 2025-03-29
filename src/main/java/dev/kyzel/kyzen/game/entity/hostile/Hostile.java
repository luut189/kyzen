package dev.kyzel.kyzen.game.entity.hostile;

import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.entity.Direction;
import dev.kyzel.kyzen.game.entity.Entity;
import dev.kyzel.kyzen.game.level.Level;
import dev.kyzel.kyzen.gfx.Spritesheet;

public abstract class Hostile extends Entity {

    protected static int ENEMY_SPRITE_OFFSET =
            Spritesheet.FONT_TEXTURE_NUM + Spritesheet.TILE_TEXTURE_NUM + Spritesheet.PLAYER_SPRITE_NUM;

    protected float currentDelta = 0f;
    protected float directionChangeInterval = 2f;

    public Hostile(Level level, Transform transform, float zIndex) {
        super(level, transform, zIndex);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentDelta += deltaTime;
    }

    @Override
    protected void updateDirection() {
        if (currentDelta >= directionChangeInterval) {
            direction = Direction.values()[(int) (Math.random() * Direction.values().length)];
            currentDelta = 0;
        }
    }

    @Override
    protected void move(float deltaTime) {
        super.move(deltaTime);
        if (level.collide(this)) {
            direction = Direction.values()[(int) (Math.random() * Direction.values().length)];
        }
    }

    @Override
    protected void attack() {

    }
}
