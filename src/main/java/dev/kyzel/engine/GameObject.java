package dev.kyzel.engine;

import dev.kyzel.utils.AssetManager;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final int id;
    private final List<Component> componentList;
    private final Transform transform;

    private final int zIndex;

    public GameObject(Transform transform, int zIndex) {
        this.id = AssetManager.getNextID();
        this.componentList = new ArrayList<>();
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : componentList) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    assert false : componentClass + " is not a subclass of " + componentClass.getName();
                }
            }
        }
        return null;
    }

    public void addComponent(Component component) {
        componentList.add(component);
        component.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < componentList.size(); i++) {
            if (componentClass.isAssignableFrom(componentList.get(i).getClass())) {
                componentList.remove(i);
                return;
            }
        }
    }

    public void start() {
        for (Component component : componentList) {
            component.start();
        }
    }

    public void update(float deltaTime) {
        for (Component component : componentList) {
            component.update(deltaTime);
        }
    }

    public Transform getTransform() {
        return transform;
    }

    public int getID() {
        return id;
    }

    public int getzIndex() {
        return zIndex;
    }
}
