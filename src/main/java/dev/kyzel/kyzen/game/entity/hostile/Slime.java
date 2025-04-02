package dev.kyzel.kyzen.game.entity.hostile;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.game.AABB;
import dev.kyzel.kyzen.game.entity.Direction;
import dev.kyzel.kyzen.game.entity.EntityState;
import dev.kyzel.kyzen.game.level.Level;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.Sprite;
import org.joml.Vector4f;

public class Slime extends Hostile {

    public Slime(Level level, Transform transform, float zIndex) {
        super(level, transform, zIndex);
        health = 10;
        entitySpeed = SceneManager.getCurrentScene().getObjectScale() * 1.5f;
        aabb = new AABB(16, 20, transform.scale.x - 16 * 2, transform.scale.y - 20 * 2);

        Vector4f color = ColorPalette.getDefaultRandomRGBA();
        spriteComponent = new SpriteComponent(sheet.getSprite(ENEMY_SPRITE_OFFSET), color);
        this.addComponent(spriteComponent);
    }

    @Override
    protected void initAnimationFrames() {
        for (Direction direction : Direction.values()) {
            if (direction == Direction.NONE) continue;
            animationComponent.addFrames(EntityState.IDLE, direction,
                    new Sprite[]{sheet.getSprite(ENEMY_SPRITE_OFFSET)}
            );
        }

        animationComponent.addFrames(EntityState.MOVING, Direction.UP,
                new Sprite[]{
                        sheet.getSprite(ENEMY_SPRITE_OFFSET + 1),
                        sheet.getSprite(ENEMY_SPRITE_OFFSET)
                }
        );
        animationComponent.addFrames(EntityState.MOVING, Direction.DOWN,
                new Sprite[]{
                        sheet.getSprite(ENEMY_SPRITE_OFFSET + 1),
                        sheet.getSprite(ENEMY_SPRITE_OFFSET)
                }
        );
        animationComponent.addFrames(EntityState.MOVING, Direction.LEFT,
                new Sprite[]{
                        sheet.getSprite(ENEMY_SPRITE_OFFSET + 2).flipHorizontally(),
                        sheet.getSprite(ENEMY_SPRITE_OFFSET)
                }
        );
        animationComponent.addFrames(EntityState.MOVING, Direction.RIGHT,
                new Sprite[]{
                        sheet.getSprite(ENEMY_SPRITE_OFFSET + 2),
                        sheet.getSprite(ENEMY_SPRITE_OFFSET)
                }
        );

        animationComponent.addFrames(EntityState.ATTACKING, Direction.UP,
                new Sprite[]{sheet.getSprite(ENEMY_SPRITE_OFFSET + 1)});
        animationComponent.addFrames(EntityState.ATTACKING, Direction.DOWN,
                new Sprite[]{sheet.getSprite(ENEMY_SPRITE_OFFSET + 1)});
        animationComponent.addFrames(EntityState.ATTACKING, Direction.LEFT,
                new Sprite[]{sheet.getSprite(ENEMY_SPRITE_OFFSET + 2).flipHorizontally()});
        animationComponent.addFrames(EntityState.ATTACKING, Direction.RIGHT,
                new Sprite[]{sheet.getSprite(ENEMY_SPRITE_OFFSET + 2)});

        animationComponent.setAnimationSpeed(0.2f);
    }
}
