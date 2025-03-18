package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.entity.Entity;
import dev.kyzel.kyzen.game.level.tiles.Tile;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {

    protected Scene scene;
    protected Vector4f theme;
    protected List<Entity> entityList;
    protected List<Tile> tileList;

    public Level(Scene scene, Vector4f theme) {
        this.scene = scene;
        this.theme = theme;
        entityList = new ArrayList<>();
        tileList = new ArrayList<>();
        generateLevel();
    }

    protected abstract void addEntities();

    protected abstract void addTiles();

    protected void generateLevel() {
        addTiles();
        addEntities();
    }

    public boolean collide(Entity e) {
        Transform transform = e.getTransform();
        float entityX = transform.position.x;
        float entityY = transform.position.y;
        float entityWidth = transform.scale.x;
        float entityHeight = transform.scale.y;
        for (Tile t : tileList) {
            if (t.isWalkable()) continue;
            Transform tileTransform = t.getTransform();
            float tileX = tileTransform.position.x;
            float tileY = tileTransform.position.y;
            float tileWidth = tileTransform.scale.x;
            float tileHeight = tileTransform.scale.y;
            if (
                    entityX < tileX + tileWidth &&
                    entityX + entityWidth > tileX &&
                    entityY < tileY + tileHeight &&
                    entityY + entityHeight > tileY
            ) {
                return true;
            }
        }
        return false;
    }
}
