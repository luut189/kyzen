package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.entity.Player;
import dev.kyzel.kyzen.game.level.tiles.*;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector2f;

public class TestLevel extends Level {

    public TestLevel(Scene scene, float x, float y) {
        super(scene, ColorPalette.getDefaultRGBA(0.6f, 0.2f, 0.6f), true, x, y, 25, 35);
    }

    @Override
    protected void addEntities() {
        Vector2f position = new Vector2f();
        Tile cur = getTile((int) Math.floor(position.x), (int) Math.floor(position.y));
        while (cur == null || !cur.isWalkable()) {
            position.x = (float) Math.random() * width;
            position.y = (float) Math.random() * height;
            cur = getTile(Math.round(position.x), Math.round(position.y));
        }
        position.x *= scene.getObjectScale();
        position.y *= scene.getObjectScale();
        Player p = new Player(
                this,
                new Transform(
                        position,
                        scene.getObjectScale()
                ), 3);
        scene.getCamera().snapToPlayer(p, 0.1f);
        entityList.add(p);
        scene.addGameObject(p);
    }

    @Override
    protected void addTiles() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                float scale = scene.getObjectScale();
                Transform transform = new Transform(new Vector2f((j + x) * scale, (i + y) * scale), scale);
                Tile tile = createAndOrientTile(transform, i, j);
                scene.addGameObject(tile);
                tileList.add(tile);
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile cur = tileList.get(j + i * width);
                if (cur instanceof BrickTile) continue;
                boolean[] dirs = checkDifferentSurroundingTiles(j, i, cur.getClass());
                cur.setToBorder(dirs, theme);
            }
        }
    }

    private Tile createAndOrientTile(Transform transform, int i, int j) {
        boolean isPerimeter = i == 0 || i == height - 1 || j == 0 || j == width - 1;

        boolean isCorner = (i == 0 && j == 0) ||
                (i == 0 && j == width - 1) ||
                (i == height - 1 && j == 0) ||
                (i == height - 1 && j == width - 1);

        if (isPerimeter) {
            Tile tile = new BrickTile(transform, 2, theme, false);

            if (isCorner) {
                tile = new BrickTile(transform, 2, theme, true);
                if (i == 0 && j == 0) {
                    tile.rotate90().flipHorizontally();
                } else if (i == 0 && j == width - 1) {
                    tile.rotate90();
                } else if (i == height - 1 && j == 0) {
                    tile.flipHorizontally();
                }
            } else {
                if (j == 0) {
                    tile.rotate90().flipHorizontally();
                } else if (j == width - 1) {
                    tile.rotate90();
                } else if (i == 0) {
                    tile.flipVertically();
                }
            }

            return tile;
        } else {
            if (j > 10 && j < width - 1 && i > 10 && i < height - 1) {
                return new LavaTile(transform, 2);
            }
            return new FloorTile(transform, 2, theme);
        }
    }
}
