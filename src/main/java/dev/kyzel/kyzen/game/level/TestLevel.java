package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.engine.Window;
import dev.kyzel.kyzen.game.entity.Player;
import dev.kyzel.kyzen.game.level.tiles.*;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector2f;

public class TestLevel extends Level {

    public TestLevel(Scene scene) {
        super(scene, ColorPalette.getDefaultRGBA(0.1f, 0.1f, 0.2f));
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
        int x = 0, y = 0, roomSize = 25;
        for (int i = y; i < y + roomSize; i++) {
            for (int j = x; j < x + roomSize; j++) {
                float scale = scene.getObjectScale();
                Transform transform = new Transform(new Vector2f(j * scale, i * scale), scale);
                Tile tile = createAndOrientTile(transform, i, j, x, y, roomSize);
                scene.addGameObject(tile);
                tileList.add(tile);
            }
        }
        for (int i = 0; i < roomSize; i++) {
            for (int j = 0; j < roomSize; j++) {
                Tile cur = tileList.get(j + i * roomSize);
                if (cur instanceof BrickTile) continue;
                boolean[] dirs = checkSurroundingTiles(j, i, roomSize, cur.getClass());
                cur.setToBorder(dirs, theme);
            }
        }
    }

    private boolean[] checkSurroundingTiles(int j, int i, int roomSize, Class<? extends Tile> tileClass) {
        Tile top = tileList.get(j + (i + 1) * roomSize);
        Tile bottom = tileList.get(j + (i - 1) * roomSize);
        Tile left = tileList.get(j - 1 + i * roomSize);
        Tile right = tileList.get(j + 1 + i * roomSize);
        return new boolean[]{!tileClass.isInstance(top),
                             !tileClass.isInstance(bottom),
                             !tileClass.isInstance(left),
                             !tileClass.isInstance(right)};
    }

    private Tile createAndOrientTile(Transform transform, int i, int j, int x, int y, int roomSize) {
        boolean isPerimeter = i == y || i == y + roomSize - 1 || j == x || j == x + roomSize - 1;

        boolean isCorner = (i == y && j == x) ||
                (i == y && j == x + roomSize - 1) ||
                (i == y + roomSize - 1 && j == x) ||
                (i == y + roomSize - 1 && j == x + roomSize - 1);

        if (isPerimeter) {
            Tile tile = new BrickTile(transform, 2, theme, false);

            if (isCorner) {
                tile = new BrickTile(transform, 2, theme, true);
                if (i == y && j == x) {
                    tile.rotate90().flipHorizontally();
                } else if (i == y && j == x + roomSize - 1) {
                    tile.rotate90();
                } else if (i == y + roomSize - 1 && j == x) {
                    tile.flipHorizontally();
                }
            } else {
                if (j == x) {
                    tile.rotate90().flipHorizontally();
                } else if (j == x + roomSize - 1) {
                    tile.rotate90();
                } else if (i == y) {
                    tile.flipVertically();
                }
            }

            return tile;
        } else {
            if (Math.random() > 0.8) {
                return new LavaTile(transform, 2);
            }
            return new FloorTile(transform, 2, theme);
        }
    }
}
