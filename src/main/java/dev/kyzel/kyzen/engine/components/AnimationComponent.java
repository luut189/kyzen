package dev.kyzel.kyzen.engine.components;

import dev.kyzel.kyzen.engine.Component;
import dev.kyzel.kyzen.game.entity.Direction;
import dev.kyzel.kyzen.game.entity.EntityState;
import dev.kyzel.kyzen.gfx.Sprite;

import java.util.HashMap;
import java.util.Map;

public class AnimationComponent extends Component {

    private final Map<Direction, Sprite[]> idleSprites = new HashMap<>();
    private final Map<Direction, Sprite[]> animationFrames = new HashMap<>();
    private EntityState currentState;
    private float currentDelta, animationSpeed = 1f;
    private int currentFrame, maxFrames = 0;
    private boolean isReversing = false;

    public void addFrames(EntityState state, Direction direction, Sprite[] sprites) {
        if (state == EntityState.IDLE) {
            idleSprites.put(direction, sprites);
        } else {
            animationFrames.put(direction, sprites);
        }
        maxFrames = Math.max(maxFrames, sprites.length);
    }

    @Override
    public void start() {

    }

    @Override
    public void update(float deltaTime) {
        currentDelta += deltaTime;
        if (currentDelta > animationSpeed && currentState == EntityState.MOVING) {
            if (isReversing) {
                currentFrame--;
                if (currentFrame <= 0) {
                    currentFrame = 0;
                    isReversing = false;
                }
            } else {
                currentFrame++;
                if (currentFrame >= maxFrames - 1) {
                    currentFrame = maxFrames - 1;
                    isReversing = true;
                }
            }
            currentDelta = 0;
        }
    }

    public void setAnimationSpeed(float speed) {
        animationSpeed = speed;
    }

    public Sprite getSprite(Direction direction) {
        if (currentState == EntityState.IDLE) {
            return idleSprites.get(direction)[0];
        } else {
            Sprite[] sprites = animationFrames.get(direction);
            return sprites[currentFrame % sprites.length];
        }
    }

    public void setState(final EntityState state) {
        currentState = state;
    }
}
