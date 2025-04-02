package dev.kyzel.kyzen.game.level;

import dev.kyzel.kyzen.engine.Scene;
import dev.kyzel.kyzen.gfx.ColorPalette;
import dev.kyzel.kyzen.utils.ExtendedMath;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class DungeonLevel extends Level {

    public DungeonLevel(Scene scene, Vector2f position) {
        super(scene, ColorPalette.getDefaultRandomRGBA(), false, position);
    }

    @Override
    protected void addRooms() {
        final int numRooms = ExtendedMath.getRandomInt(10, 20);
        final int minWidth = 5;
        final int minHeight = 10;
        for (int i = 0; i < numRooms; i++) {
            Vector2f pos = ExtendedMath.getRandomPointsInCircle(position, 1);
            int width = ExtendedMath.getRandomInt(minWidth, 25);
            int height = ExtendedMath.getRandomInt(minHeight, width * 2);
            roomList.add(new Room(this, pos, width, height));
        }

        roomList.sort((r1, r2) -> {
            int xCompare = Float.compare(r1.getPosition().x, r2.getPosition().x);
            if (xCompare != 0) return xCompare;
            return Float.compare(r1.getPosition().y, r2.getPosition().y);
        });

        do {
            for (Room room : roomList) {
                for (Room otherRoom : roomList) {
                    if (room.equals(otherRoom)) continue;
                    float overlap = room.getOverlapDistance(otherRoom);
                    Vector2f dir = new Vector2f(room.getMiddlePoint())
                            .sub(otherRoom.getMiddlePoint())
                            .normalize()
                            .mul(overlap * 0.5f + 0.1f);

                    room.move(dir);
                    otherRoom.move(dir.negate());
                }
            }
        } while (isAnyRoomOverlap());

        List<Room> toRemove = new ArrayList<>();
        float lastX = roomList.getFirst().getMiddlePoint().x + roomList.getFirst().getWidth();
        float lastY = roomList.getFirst().getMiddlePoint().y + roomList.getFirst().getHeight();
        for (Room room : roomList) {
            float x = room.getMiddlePoint().x + room.getWidth();
            float y = room.getMiddlePoint().y + room.getHeight();
            if (room.getWidth() < 10 || room.getHeight() < 10 ||
                    Math.abs(x - lastX) < 5 || Math.abs(y - lastY) < 5) {
                toRemove.add(room);
            }
            lastX = x;
            lastY = y;
        }
        roomList.removeAll(toRemove);

        for (Room room : roomList) {
            room.snapToGrid();
            room.generateTiles();
        }
    }
}
