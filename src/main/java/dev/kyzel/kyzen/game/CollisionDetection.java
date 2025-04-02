package dev.kyzel.kyzen.game;

import dev.kyzel.kyzen.engine.GameObject;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.entity.Entity;
import dev.kyzel.kyzen.game.level.tiles.Tile;

public class CollisionDetection {

    public static boolean collide(Entity e, GameObject other) {
        if (!(other instanceof Tile || other instanceof Entity)) return false;

        Transform transform = e.getTransform();
        Transform otherTransform = other.getTransform();
        AABB aabb = e.getAABB();
        AABB otherAABB = other instanceof Entity ? ((Entity) other).getAABB() : ((Tile) other).getAABB();
        float x = transform.position.x + aabb.x();
        float y = transform.position.y + aabb.y();
        float otherX = otherTransform.position.x + otherAABB.x();
        float otherY = otherTransform.position.y + otherAABB.y();
        return x < otherX + otherAABB.width() &&
                x + aabb.width() > otherX &&
                y < otherY + otherAABB.height() &&
                y + aabb.height() > otherY;
    }

}
