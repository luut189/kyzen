package dev.kyzel.kyzen.game.entity;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.gfx.Sprite;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.input.ControlHandler;

public class Player extends Entity {

    private static final int PLAYER_SPRITE_OFFSET = Spritesheet.FONT_TEXTURE_NUM + Spritesheet.TILE_TEXTURE_NUM;

    public Player(Transform transform, float zIndex) {
        super(transform, zIndex);
        direction = Direction.DOWN;
        lastDirection = direction;
        entitySpeed = SceneManager.getCurrentScene().getObjectScale() * 3f;

        spriteComponent = new SpriteComponent(sheet.getSprite(PLAYER_SPRITE_OFFSET));
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
        Direction previousDirection = direction;

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

        if (direction != previousDirection) {
            lastDirection = previousDirection != Direction.NONE ? previousDirection : lastDirection;

            if (direction == Direction.NONE) {
                animationComponent.setState(EntityState.IDLE);
            } else {
                animationComponent.setState(EntityState.MOVING);
            }
        }
    }

    @Override
    protected void move(float deltaTime) {
        float dir = (direction == Direction.UP || direction == Direction.RIGHT) ? 1 : -1;
        boolean vertical = direction == Direction.UP || direction == Direction.DOWN;
        boolean horizontal = direction == Direction.LEFT || direction == Direction.RIGHT;

        // TODO: convert camera's coordinates to world's coordinates
        if (vertical) {
            transform.position.y += entitySpeed * deltaTime * dir;
        } else if (horizontal) {
            transform.position.x += entitySpeed * deltaTime * dir;
        }
    }
}
