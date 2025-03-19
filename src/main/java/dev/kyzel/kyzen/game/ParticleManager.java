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

    private static final List<Particle> particlesToAdd = new ArrayList<>();
    private static final float particleSize = 5f;

    public static void createParticles(int count, Entity creator, Vector4f color, Direction direction) {
        Vector2f velocity = new Vector2f(0, 0);
        Transform transform = creator.getTransform().copy();
        Vector2f position = transform.position;
        float adjustment = transform.scale.y * 0.3f;
        position.y += adjustment;
        if (direction == Direction.UP) {
            position.y += adjustment;
        } else if (direction == Direction.RIGHT) {
            position.x += transform.scale.x;
        }

        if (direction == Direction.DOWN) {
            position.x += transform.scale.x * 0.75f;
        }

        float variation = 0.2f;
        float offset = (float) Math.random() * variation * 2 - variation;
        float speed = (float) Math.random() * 100f + 50f;

        velocity = switch (direction) {
            case UP -> new Vector2f(offset, 1).mul(speed);
            case DOWN -> new Vector2f(offset, -1).mul(speed);
            case LEFT -> new Vector2f(-1, offset).mul(speed);
            case RIGHT -> new Vector2f(1, offset).mul(speed);
            default -> velocity;
        };
        for (int i = 0; i < count; i++) {
            position = ExtendedMath.getRandomPointInCircle(position, 5);
            particlesToAdd.add(new Particle(new Transform(position, particleSize), creator.getzIndex() - 0.01f, color, velocity));
        }
    }

    public static void addAllParticles() {
        for (Particle particle : particlesToAdd) {
            SceneManager.getCurrentScene().addGameObject(particle);
        }
        particlesToAdd.clear();
    }
}
