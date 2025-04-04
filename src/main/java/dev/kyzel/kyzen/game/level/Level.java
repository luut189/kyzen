package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.engine.Transform;
import dev.kyzel.kyzen.game.CollisionDetection;
import dev.kyzel.kyzen.game.entity.Entity;
import dev.kyzel.kyzen.game.entity.Player;
import dev.kyzel.kyzen.game.level.tiles.Tile;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class Level {

    protected Scene scene;
    protected final Vector4f theme;
    protected List<Entity> entityList;
    protected Player player;
    protected List<Room> roomList;
    protected Room currentRoom;

    protected Vector2f position;
    protected final boolean hasDarkness;

    public Level(Scene scene, Vector4f theme, boolean hasDarkness, Vector2f position) {
        this.scene = scene;
        this.theme = theme;
        this.hasDarkness = hasDarkness;
        entityList = new ArrayList<>();
        roomList = new ArrayList<>();
        this.position = position;
        generateLevel();
    }

    public boolean hasDarkness() {
        return hasDarkness;
    }

    public Vector4f getTheme() {
        return theme;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isAnyRoomOverlap() {
        for (Room room : roomList) {
            for (Room otherRoom : roomList) {
                if (room.equals(otherRoom)) continue;
                if (room.getOverlapDistance(otherRoom) > 0) return true;
            }
        }
        return false;
    }

    private void generateLevel() {
        addRooms();
        addRoomsToScene();
        addEntities();
    }

    protected void addEntities() {
        addPlayer();
    }

    public void removeEntity(Entity entity) {
        entityList.remove(entity);
    }

    protected abstract void addRooms();

    private void addPlayer() {
        currentRoom = roomList.getFirst();
        Vector2f position = new Vector2f();
        Tile cur = currentRoom.getTile((int) Math.floor(position.x), (int) Math.floor(position.y));
        while (cur == null || !cur.isWalkable()) {
            position.x = (float) Math.floor(Math.random() * currentRoom.getWidth());
            position.y = (float) Math.floor(Math.random() * currentRoom.getHeight());
            cur = currentRoom.getTile(Math.round(position.x), Math.round(position.y));
        }
        position.add(currentRoom.getPosition()).mul(scene.getObjectScale());
        this.player = new Player(
                this,
                new Transform(
                        position,
                        scene.getObjectScale()
                ), 3);
        scene.getCamera().snapToPlayer(player, 0.101f);
        entityList.add(player);
        scene.addGameObject(player);
    }

    private void addRoomsToScene() {
        for (Room room : roomList) {
            for (Tile tile : room.getTiles()) {
                scene.addGameObject(tile);
            }
        }
    }

    public boolean collide(Entity e) {
        for (Tile t : currentRoom.getTiles()) {
            if (t.isWalkable()) continue;
            if (CollisionDetection.collide(e, t)) return true;
        }
        for (Entity entity : entityList) {
            if (e.equals(entity)) continue;
            if (CollisionDetection.collide(e, entity)) return true;
        }
        return false;
    }
}
