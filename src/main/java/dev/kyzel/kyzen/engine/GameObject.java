package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.utils.AssetManager;

import java.util.HashMap;
import java.util.Map;

public class GameObject {

    protected Spritesheet sheet;
    protected final Transform transform;

    private final int id;
    private final Map<Class<? extends Component>, Component> componentMap;
    private final float zIndex;

    public GameObject(Transform transform, float zIndex) {
        this.id = AssetManager.getNextID();
        this.componentMap = new HashMap<>();
        this.transform = transform;
        this.zIndex = zIndex;
        this.sheet = SceneManager.getCurrentScene().getSpritesheet();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        Component component = componentMap.get(componentClass);
        if (component == null) return null;
        assert componentClass.isAssignableFrom(component.getClass()) :
                componentClass + " is not a subclass of " + componentClass.getName();
        return componentClass.cast(component);
    }

    public GameObject addComponent(Component component) {
        if (componentMap.containsKey(component.getClass())) return this;
        componentMap.put(component.getClass(), component);
        component.gameObject = this;
        return this;
    }

    public <T extends Component> GameObject removeComponent(Class<T> componentClass) {
        Component component = componentMap.remove(componentClass);
        if (component != null) {
            component.gameObject = null;
        }
        return this;
    }

    public void removeAllComponents() {
        componentMap.clear();
    }

    public void start() {
        for (Component component : componentMap.values()) {
            component.start();
        }
    }

    public void update(float deltaTime) {
        for (Component component : componentMap.values()) {
            component.update(deltaTime);
        }
    }

    public Transform getTransform() {
        return transform;
    }

    public int getID() {
        return id;
    }

    public float getzIndex() {
        return zIndex;
    }
}
