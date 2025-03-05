package dev.kyzel.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    private List<Component> componentList;
    public Transform transform;

    public GameObject(String name) {
        this.name = name;
        this.componentList = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.componentList = new ArrayList<>();
        this.transform = transform;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : componentList) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
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

}
