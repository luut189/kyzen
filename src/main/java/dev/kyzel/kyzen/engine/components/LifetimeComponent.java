package dev.kyzel.kyzen.engine.components;

import dev.kyzel.kyzen.engine.Component;

public class LifetimeComponent extends Component {

    private final float lifetime;
    private float timeElapsed;
    private boolean isExpired;

    public LifetimeComponent(float lifetimeSecond) {
        this.lifetime = lifetimeSecond;
        this.timeElapsed = 0f;
        this.isExpired = false;
    }

    @Override
    public void update(float deltaTime) {
        timeElapsed += deltaTime;
        isExpired = timeElapsed >= lifetime;
    }

    public boolean isExpired() {
        return isExpired;
    }
}
