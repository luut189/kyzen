package dev.kyzel.kyzen.game.entity;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.LightComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.game.ParticleManager;
import dev.kyzel.kyzen.game.level.Level;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.gfx.Sprite;
import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.input.ControlHandler;
import dev.kyzel.kyzen.input.MouseListener;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class Player extends Entity {

    private static final int PLAYER_SPRITE_OFFSET = Spritesheet.FONT_TEXTURE_NUM + Spritesheet.TILE_TEXTURE_NUM;

    public Player(Level level, Transform transform, float zIndex) {
        super(level, transform, zIndex);
        direction = Direction.DOWN;
        lastDirection = direction;
        entitySpeed = SceneManager.getCurrentScene().getObjectScale() * 3f;

        spriteComponent = new SpriteComponent(sheet.getSprite(PLAYER_SPRITE_OFFSET));
        this.addComponent(new LightComponent(500, ColorPalette.getDefaultRGB(1,1,1), 0.1f));
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
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_1) && direction == Direction.NONE) {
            animationComponent.setState(EntityState.ATTACKING);
            ParticleManager.createParticles(1, this, ColorPalette.getDefaultRandomRGBA(), lastDirection);
        }
        if (ControlHandler.SNAP_CAMERA_TO_PLAYER.down())
            SceneManager.getCurrentScene().getCamera().snapToPlayer(this, deltaTime);
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
