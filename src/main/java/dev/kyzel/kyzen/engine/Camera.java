package dev.kyzel.kyzen.engine;

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
        // Zoom range: 0.1x to 5x
        this.zoom = Math.max(0.1f, Math.min(newZoom, 5.0f));
        adjustProjection();
    }

    public void resetZoom() {
        setZoom(1.0f);
    }

    public void zoomIn() {
        setZoom(zoom * 1.1f); // Increase zoom
    }

    public void zoomOut() {
        setZoom(zoom * 0.9f); // Decrease zoom
    }
}
