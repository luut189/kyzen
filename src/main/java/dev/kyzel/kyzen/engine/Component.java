package dev.kyzel.kyzen.engine;

public abstract class Component {

    protected GameObject owner = null;

    public abstract void start();

    public abstract void update(float deltaTime);

    public GameObject getOwner() {
        return owner;
    }
}
