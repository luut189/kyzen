package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.Window;
import dev.kyzel.kyzen.game.entity.Player;
import dev.kyzel.kyzen.game.level.tiles.*;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector2f;

public class TestLevel extends Level {

    public TestLevel(Scene scene, int x, int y) {
        super(scene, ColorPalette.getDefaultRGBA(0.1f, 0.1f, 0.2f), x, y, 25, 35);
    }

    @Override
    protected void addEntities() {
        Player p = new Player(
                this,
                new Transform(
                        new Vector2f(
                                Window.get().getWidth() / 2f,
                                Window.get().getHeight() / 2f),
                        scene.getObjectScale()
                ), 3);
        scene.addGameObject(p);
    }

    @Override
    protected void addTiles() {
        for (int i = y; i < y + height; i++) {
            for (int j = x; j < x + width; j++) {
                float scale = scene.getObjectScale();
                Transform transform = new Transform(new Vector2f(j * scale, i * scale), scale);
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
        boolean isPerimeter = i == y || i == y + height - 1 || j == x || j == x + width - 1;

        boolean isCorner = (i == y && j == x) ||
                (i == y && j == x + width - 1) ||
                (i == y + height - 1 && j == x) ||
                (i == y + height - 1 && j == x + width - 1);

        if (isPerimeter) {
            Tile tile = new BrickTile(transform, 2, theme, false);

            if (isCorner) {
                tile = new BrickTile(transform, 2, theme, true);
                if (i == y && j == x) {
                    tile.rotate90().flipHorizontally();
                } else if (i == y && j == x + width - 1) {
                    tile.rotate90();
                } else if (i == y + height - 1 && j == x) {
                    tile.flipHorizontally();
                }
            } else {
                if (j == x) {
                    tile.rotate90().flipHorizontally();
                } else if (j == x + width - 1) {
                    tile.rotate90();
                } else if (i == y) {
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
