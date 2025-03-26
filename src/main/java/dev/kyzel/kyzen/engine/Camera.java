package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.game.entity.Player;
import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Vector2f position;
    private float zoom = 1.0f;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        float width = Window.get().getWidth() / zoom;
        float height = Window.get().getHeight() / zoom;
        projectionMatrix.ortho(0.0f, width, 0.0f, height, 0, 100);
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity();
        viewMatrix.translate(-position.x, -position.y, 0);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float newZoom) {
        this.zoom = Math.max(0.1f, Math.min(newZoom, 2.0f));
        adjustProjection();
    }

    public void zoomIn() {
        setZoom(zoom * 1.1f);
    }

    public void zoomOut() {
        setZoom(zoom * 0.9f);
    }

    public void moveCamera(float dx, float dy) {
        this.position.add(dx, dy);
    }

    public void reset() {
        setZoom(1.0f);
        position.x = 0.0f;
        position.y = 0.0f;
    }

    public void snapToPlayer(Player player, float deltaTime) {
        float speed = 10f;
        float width = Window.get().getWidth();
        float height = Window.get().getHeight();
        float targetX = player.transform.position.x - (width / 2f) / zoom;
        float targetY = player.transform.position.y - (height / 2f) / zoom;

        // Apply interpolation
        moveCamera((targetX - position.x) * speed * deltaTime, (targetY - position.y) * speed * deltaTime);
    }

    public boolean isNotInView(GameObject gameObject) {
        int windowWidth = Window.get().getWidth();
        int windowHeight = Window.get().getHeight();
        Transform transform = gameObject.getTransform();
        float entityLeft = transform.position.x;
        float entityRight = transform.position.x + transform.scale.x;
        float entityBottom = transform.position.y;
        float entityTop = transform.position.y + transform.scale.y;

        float offset = SceneManager.getCurrentScene().getObjectScale();
        float camLeft = position.x - offset;
        float camRight = position.x + (windowWidth / zoom) + offset;
        float camBottom = position.y - offset;
        float camTop = position.y + (windowHeight / zoom) + offset;

        return (!(entityRight > camLeft) || !(entityLeft < camRight)) ||
                (!(entityTop > camBottom) || !(entityBottom < camTop));
    }
}
