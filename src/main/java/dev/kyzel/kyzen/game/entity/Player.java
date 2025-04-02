package dev.kyzel.kyzen.game.entity;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.LightComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.game.AABB;
import dev.kyzel.kyzen.game.ParticleManager;
import dev.kyzel.kyzen.game.level.Level;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.Sprite;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.input.ControlHandler;
import dev.kyzel.kyzen.utils.ExtendedMath;

public class Player extends Entity {

    private static final int PLAYER_SPRITE_OFFSET = Spritesheet.TILE_TEXTURE_NUM;

    public Player(Level level, Transform transform, float zIndex) {
        super(level, transform, zIndex);
        entitySpeed = SceneManager.getCurrentScene().getObjectScale() * 3f;
        health = 10;
        aabb = new AABB(16, 0, transform.scale.x - 16 * 2, transform.scale.y - 32);

        spriteComponent = new SpriteComponent(sheet.getSprite(PLAYER_SPRITE_OFFSET));
        this.addComponent(new LightComponent(500, ExtendedMath.toVector3f(level.getTheme()), 0.4f));
        this.addComponent(spriteComponent);
    }

    @Override
    public void initAnimationFrames() {
        animationComponent.addFrames(EntityState.IDLE, Direction.DOWN,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET)});
        animationComponent.addFrames(EntityState.IDLE, Direction.UP,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + 1)});
        animationComponent.addFrames(EntityState.IDLE, Direction.RIGHT,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + 2)});
        animationComponent.addFrames(EntityState.IDLE, Direction.LEFT,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + 2).flipHorizontally()});

        animationComponent.addFrames(EntityState.ATTACKING, Direction.DOWN,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + Spritesheet.PLAYER_BASE_TEXTURE_NUM)});
        animationComponent.addFrames(EntityState.ATTACKING, Direction.UP,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + Spritesheet.PLAYER_BASE_TEXTURE_NUM + 1)});
        animationComponent.addFrames(EntityState.ATTACKING, Direction.RIGHT,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + Spritesheet.PLAYER_BASE_TEXTURE_NUM + 2)});
        animationComponent.addFrames(EntityState.ATTACKING, Direction.LEFT,
                new Sprite[]{sheet.getSprite(PLAYER_SPRITE_OFFSET + Spritesheet.PLAYER_BASE_TEXTURE_NUM + 2).flipHorizontally()});

        animationComponent.addFrames(EntityState.MOVING, Direction.DOWN,
                new Sprite[]{
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 3),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 3).flipHorizontally()
                }
        );
        animationComponent.addFrames(EntityState.MOVING, Direction.UP,
                new Sprite[]{
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 4),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 1),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 4).flipHorizontally()
                }
        );
        animationComponent.addFrames(EntityState.MOVING, Direction.RIGHT,
                new Sprite[]{
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 5),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 2),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 6)
                }
        );
        animationComponent.addFrames(EntityState.MOVING, Direction.LEFT,
                new Sprite[]{
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 5).flipHorizontally(),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 2).flipHorizontally(),
                        sheet.getSprite(PLAYER_SPRITE_OFFSET + 6).flipHorizontally()
                }
        );
        animationComponent.setAnimationSpeed(0.2f);
    }

    @Override
    protected void updateDirection() {
        if (ControlHandler.UP.down()) {
            direction = Direction.UP;
        } else if (ControlHandler.DOWN.down()) {
            direction = Direction.DOWN;
        } else if (ControlHandler.LEFT.down()) {
            direction = Direction.LEFT;
        } else if (ControlHandler.RIGHT.down()) {
            direction = Direction.RIGHT;
        } else {
            direction = Direction.NONE;
        }
    }

    @Override
    protected void attack() {
        if (ControlHandler.PLAYER_ATTACK.down() && direction == Direction.NONE) {
            animationComponent.setState(EntityState.ATTACKING);
            ParticleManager.createParticles(1, this, ColorPalette.getDefaultRandomRGBA(), lastDirection);
        }
    }
}
