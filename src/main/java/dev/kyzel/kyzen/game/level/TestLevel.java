package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector2f;

public class TestLevel extends Level {

    public TestLevel(Scene scene, Vector2f position) {
        super(scene, ColorPalette.getDefaultRGBA(0.6f, 0.2f, 0.6f), true, position, 25, 35);
    }

    @Override
    protected void addTiles() {
        Room room = new Room(this, position, 25, 35);
        roomList.add(room);
        room.generateTiles();
    }
}
