package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.SceneManager;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.level.tiles.*;
import dev.kyzel.kyzen.utils.AssetManager;
import dev.kyzel.kyzen.utils.ExtendedMath;
import org.joml.Math;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private final Level level;
    private final int id, width, height;
    private final List<Tile> tiles;

    private Vector2f position;

    public Room(Level level, Vector2f position, int width, int height) {
        this.level = level;
        this.id = AssetManager.getNextRoomID();
        this.position = position;
        this.width = width;
        this.height = height;
        tiles = new ArrayList<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public Vector2f getMiddlePoint() {
        return new Vector2f(position.x + width * 0.5f, position.y + height * 0.5f);
    }

    public float getDistance(Room room) {
        Vector2f middlePoint = getMiddlePoint();
        Vector2f otherMiddlePoint = room.getMiddlePoint();
        return (float) Math.sqrt(ExtendedMath.pow(middlePoint.x - otherMiddlePoint.x, 2)
                + ExtendedMath.pow(middlePoint.y - otherMiddlePoint.y, 2));
    }

    public void move(Vector2f delta) {
        position.add(delta);
    }

    public void snapToGrid() {
        position.x = Math.round(position.x);
        position.y = Math.round(position.y);
    }

    public Tile getTile(int x, int y) {
        if (!isTileInBounds(x, y)) return null;
        return tiles.get(x + y * width);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public int getId() {
        return id;
    }

    protected boolean isTileInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public float getOverlapDistance(Room other) {
        float xOverlap = Math.max(0, Math.min(this.position.x + this.width, other.position.x + other.width)
                - Math.max(this.position.x, other.position.x));

        float yOverlap = Math.max(0, Math.min(this.position.y + this.height, other.position.y + other.height)
                - Math.max(this.position.y, other.position.y));

        if (xOverlap > 0 && yOverlap > 0) {
            return Math.min(xOverlap, yOverlap);
        }
        return 0;
    }

    public void generateTiles() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                float scale = SceneManager.getCurrentScene().getObjectScale();
                Vector2f gridPos = new Vector2f(j, i).add(position);
                Transform transform = new Transform(new Vector2f(gridPos).mul(scale), scale);
                Tile tile = createAndOrientTile(transform, j, i);
                tiles.add(tile);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile cur = tiles.get(j + i * width);
                if (cur instanceof BrickTile) continue;
                boolean[] dirs = checkDifferentSurroundingTiles(j, i, cur.getClass());
                cur.setToBorder(dirs, level.getTheme());
            }
        }
    }

    protected boolean[] checkDifferentSurroundingTiles(int x, int y, Class<? extends Tile> tileClass) {
        Tile top = isTileInBounds(x, y + 1) ? tiles.get(x + (y + 1) * width) : null;
        Tile bottom = isTileInBounds(x, y - 1) ? tiles.get(x + (y - 1) * width) : null;
        Tile left = isTileInBounds(x - 1, y) ? tiles.get(x - 1 + y * width) : null;
        Tile right = isTileInBounds(x + 1, y) ? tiles.get(x + 1 + y * width) : null;

        return new boolean[]{
                top != null && !tileClass.isInstance(top),
                bottom != null && !tileClass.isInstance(bottom),
                left != null && !tileClass.isInstance(left),
                right != null && !tileClass.isInstance(right)
        };
    }

    private Tile createAndOrientTile(Transform transform, int x, int y) {
        boolean isPerimeter = y == 0 || y == height - 1 || x == 0 || x == width - 1;

        boolean isCorner = (y == 0 && x == 0) ||
                (y == 0 && x == width - 1) ||
                (y == height - 1 && x == 0) ||
                (y == height - 1 && x == width - 1);

        if (isPerimeter) {
            Tile tile = new BrickTile(transform, 2, level.getTheme(), false);

            if (isCorner) {
                tile = new BrickTile(transform, 2, level.getTheme(), true);
                if (y == 0 && x == 0) {
                    tile.rotate90().flipHorizontally();
                } else if (y == 0 && x == width - 1) {
                    tile.rotate90();
                } else if (y == height - 1 && x == 0) {
                    tile.flipHorizontally();
                }
            } else {
                if (x == 0) {
                    tile.rotate90().flipHorizontally();
                } else if (x == width - 1) {
                    tile.rotate90();
                } else if (y == 0) {
                    tile.flipVertically();
                }
            }

            return tile;
        } else return new FloorTile(transform, 2, level.getTheme());
    }
}
