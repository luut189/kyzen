package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.gfx.Spritesheet;
import dev.kyzel.kyzen.utils.AssetManager;

import java.util.*;

public class GameObject {

    protected Spritesheet sheet;
    protected final Transform transform;

    private final int id;
    private final Map<Class<? extends Component>, List<Component>> componentMap;
    private final float zIndex;

    public GameObject(Transform transform, float zIndex) {
        this.id = AssetManager.getNextID();
        this.componentMap = new HashMap<>();
        this.transform = transform;
        this.zIndex = zIndex;
        this.sheet = SceneManager.getCurrentScene().getSpritesheet();
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        List<Component> components = componentMap.get(componentClass);
        if (components == null || components.isEmpty()) return null;
        return componentClass.cast(components.getFirst());
    }

    public <T extends Component> List<T> getComponents(Class<T> componentClass) {
        List<Component> components = componentMap.get(componentClass);
        if (components == null) return Collections.emptyList();
        List<T> typedComponents = new ArrayList<>();
        for (Component component : components) {
            typedComponents.add(componentClass.cast(component));
        }
        return typedComponents;
    }

    public GameObject addComponent(Component component) {
        componentMap.computeIfAbsent(component.getClass(), k -> new ArrayList<>()).add(component);
        component.owner = this;
        return this;
    }

    public <T extends Component> GameObject removeComponent(Class<T> componentClass) {
        List<Component> components = componentMap.get(componentClass);
        if (components != null && !components.isEmpty()) {
            Component removedComponent = components.removeFirst();
            removedComponent.owner = null;
            if (components.isEmpty()) {
                componentMap.remove(componentClass);
            }
        }
        return this;
    }

    public <T extends Component> GameObject removeAllComponentsOfType(Class<T> componentClass) {
        List<Component> components = componentMap.remove(componentClass);
        if (components != null) {
            for (Component component : components) {
                component.owner = null;
            }
        }
        return this;
    }

    public void removeAllComponents() {
        for (List<Component> components : componentMap.values()) {
            for (Component component : components) {
                component.owner = null;
            }
        }
        componentMap.clear();
    }

    public void start() {
        for (List<Component> components : componentMap.values()) {
            for (Component component : components) {
                component.start();
            }
        }
    }

    public void update(float deltaTime) {
        for (List<Component> components : componentMap.values()) {
            for (Component component : components) {
                component.update(deltaTime);
            }
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
