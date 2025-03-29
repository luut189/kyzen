package dev.kyzel.kyzen.game.entity;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.EntityAnimationComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;
import dev.kyzel.kyzen.game.level.Level;

public abstract class Entity extends GameObject {

    protected SpriteComponent spriteComponent;
    protected EntityAnimationComponent animationComponent;
    protected Direction lastDirection, direction;
    protected float entitySpeed;

    protected Level level;

    public Entity(Level level, Transform transform, float zIndex) {
        super(transform, zIndex);
        this.level = level;
        animationComponent = new EntityAnimationComponent();
        initAnimationFrames();
        this.addComponent(animationComponent);
    }

    protected abstract void initAnimationFrames();

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateDirection();
        float lastPosX = transform.position.x;
        float lastPosY = transform.position.y;
        move(deltaTime);
        attack();
        if (level.collide(this)) {
            transform.position.x = lastPosX;
            transform.position.y = lastPosY;
        }
        spriteComponent.setSprite(animationComponent.getSprite(direction != Direction.NONE ? direction : lastDirection));
    }

    protected abstract void updateDirection();

    protected abstract void move(float deltaTime);

    protected abstract void attack();
}
