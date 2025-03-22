package dev.kyzel.kyzen.game;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.entity.Direction;
import dev.kyzel.kyzen.game.entity.Entity;
import dev.kyzel.kyzen.utils.ExtendedMath;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class ParticleManager {

    private static final int MAX_PARTICLES = 100;
    private static final float PARTICLE_SIZE = 5f;

    private static final List<Particle> particlesToAdd = new ArrayList<>();

    public static void addAllParticles() {
        for (Particle particle : particlesToAdd) {
            SceneManager.getCurrentScene().addGameObject(particle);
        }
        particlesToAdd.clear();
    }

    public static void createParticles(int count, Entity creator, Vector4f color, Direction direction) {
        if (particlesToAdd.size() > MAX_PARTICLES) return;
        Vector2f velocity = new Vector2f(0, 0);
        Transform transform = creator.getTransform().copy();
        Vector2f position = getPosition(direction, transform);

        float spread = 0.2f;
        float offset = (float) Math.random() * spread * 2 - spread;
        float speed = (float) Math.random() * 100f + 50f;

        velocity = switch (direction) {
            case UP -> new Vector2f(offset, 1).mul(speed);
            case DOWN -> new Vector2f(offset, -1).mul(speed);
            case LEFT -> new Vector2f(-1, offset).mul(speed);
            case RIGHT -> new Vector2f(1, offset).mul(speed);
            default -> velocity;
        };

        for (int i = 0; i < count; i++) {
            position = ExtendedMath.getRandomPointsInCircle(position, 5);
            particlesToAdd.add(new Particle(new Transform(position, PARTICLE_SIZE), creator.getZIndex(), color, velocity));
        }
    }

    private static Vector2f getPosition(Direction direction, Transform transform) {
        Vector2f scale = transform.scale;
        return switch (direction) {
            case UP -> new Vector2f(transform.position.x, transform.position.y + scale.y * 1.3f);
            case DOWN -> new Vector2f(transform.position.x + scale.x * 0.8f, transform.position.y - scale.y * 0.3f);
            case LEFT -> new Vector2f(transform.position.x - scale.x * 0.3f, transform.position.y + scale.y * 0.3f);
            case RIGHT -> new Vector2f(transform.position.x + scale.x * 1.3f, transform.position.y + scale.y * 0.3f);
            default -> transform.position;
        };
    }
}
