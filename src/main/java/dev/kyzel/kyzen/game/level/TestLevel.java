package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.entity.Entity;
import dev.kyzel.kyzen.game.entity.hostile.Slime;
import dev.kyzel.kyzen.gfx.ColorPalette;
import org.joml.Vector2f;

public class TestLevel extends Level {

    public TestLevel(Scene scene, Vector2f position) {
        super(scene, ColorPalette.getDefaultRGBA(0.6f, 0.2f, 0.6f), true, position);
    }

    @Override
    protected void addEntities() {
        super.addEntities();
        Entity slime = new Slime(this, new Transform(new Vector2f(100,100), scene.getObjectScale()), 2);
        entityList.add(slime);
        scene.addGameObject(slime);
    }

    @Override
    protected void addRooms() {
        Room room = new Room(this, position, 25, 35);
        roomList.add(room);
        room.generateTiles();
    }
}
