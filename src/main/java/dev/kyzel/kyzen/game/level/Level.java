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
    protected List<Integer> lightMap;

    protected int x, y;
    protected final int width, height;

    public Level(Scene scene, Vector4f theme, int x, int y, int width, int height) {
        this.scene = scene;
        this.theme = theme;
        entityList = new ArrayList<>();
        tileList = new ArrayList<>();
        lightMap = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        generateLevel();
    }

    protected boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    protected abstract void addEntities();

    protected abstract void addTiles();

    protected void generateLevel() {
        addTiles();
        addEntities();
    }

    protected boolean[] checkSurroundingTiles(int j, int i, Class<? extends Tile> tileClass) {
        Tile top = isInBounds(j, i + 1) ? tileList.get(j + (i + 1) * width) : null;
        Tile bottom = isInBounds(j, i - 1) ? tileList.get(j + (i - 1) * width) : null;
        Tile left = isInBounds(j - 1, i) ? tileList.get(j - 1 + i * width) : null;
        Tile right = isInBounds(j + 1, i) ? tileList.get(j + 1 + i * width) : null;

        return new boolean[]{
                top != null && !tileClass.isInstance(top),
                bottom != null && !tileClass.isInstance(bottom),
                left != null && !tileClass.isInstance(left),
                right != null && !tileClass.isInstance(right)
        };
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
                    entityX < tileX + tileWidth / 2 &&
                            entityX + entityWidth / 2 > tileX &&
                            entityY < tileY + tileHeight &&
                            entityY + entityHeight / 2 > tileY
            ) {
                return true;
            }
        }
        return false;
    }
}
