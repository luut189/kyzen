package dev.kyzel.kyzen.engine;

import dev.kyzel.kyzen.game.entity.Player;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

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
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp
        );

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
        this.zoom = Math.max(0.1f, Math.min(newZoom, 5.0f));
        adjustProjection();
    }

    public void moveCamera(float dx, float dy) {
        this.position.add(dx, dy);
    }

    public void snapToPlayer(Player player, float deltaTime) {
        float speed = 10f;
        float width = Window.get().getWidth();
        float height = Window.get().getHeight();
        float targetX = player.transform.position.x - (width / 2f) / zoom;
        float targetY = player.transform.position.y - (height / 2f) / zoom;

        // Apply interpolation
        position.x += (targetX - position.x) * speed * deltaTime;
        position.y += (targetY - position.y) * speed * deltaTime;
    }

    public void reset() {
        setZoom(1.0f);
        position.x = 0.0f;
        position.y = 0.0f;
    }

    public void zoomIn() {
        setZoom(zoom * 1.1f);
    }

    public void zoomOut() {
        setZoom(zoom * 0.9f);
    }
}
