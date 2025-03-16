package dev.kyzel.kyzen.game.entity;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.components.AnimationComponent;
import dev.kyzel.kyzen.engine.components.SpriteComponent;

public abstract class Entity extends GameObject {

    protected SpriteComponent spriteComponent;
    protected AnimationComponent animationComponent;
    protected Direction lastDirection, direction;

    protected float entitySpeed;

    public Entity(Transform transform, float zIndex) {
        super(transform, zIndex);
        animationComponent = new AnimationComponent();
        initAnimationFrames();
        this.addComponent(animationComponent);
    }

    protected abstract void initAnimationFrames();

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateDirection();
        move(deltaTime);
        spriteComponent.setSprite(animationComponent.getSprite(direction != Direction.NONE ? direction : lastDirection));
    }

    protected abstract void updateDirection();

    protected abstract void move(float deltaTime);
}
