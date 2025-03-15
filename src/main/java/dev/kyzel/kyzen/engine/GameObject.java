package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.utils.AssetManager;

import java.util.HashMap;
import java.util.Map;

public class GameObject {

    private final int id;
    private final Map<Class<? extends Component>, Component> componentMap;
    private final Transform transform;

    private final float zIndex;

    public GameObject(Transform transform, float zIndex) {
        this.id = AssetManager.getNextID();
        this.componentMap = new HashMap<>();
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        Component component = componentMap.get(componentClass);
        if (component == null) return null;
        assert componentClass.isAssignableFrom(component.getClass()) :
                componentClass + " is not a subclass of " + componentClass.getName();
        return componentClass.cast(component);
    }

    public void addComponent(Component component) {
        if (componentMap.containsKey(component.getClass())) return;
        componentMap.put(component.getClass(), component);
        component.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        Component component = componentMap.remove(componentClass);
        if (component != null) {
            component.gameObject = null;
        }
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
