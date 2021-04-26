package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class RectSpace {

    public int width;
    public int height;
    public int locX; //x location of the bottom left corner
    public int locY; //y location of the bottom left corner
    public static Random RANDOM;
    public static int maxRooms;
    public TETile floorTile = Tileset.FLOOR;
    public TETile wallTile = Tileset.WALL;
    public TETile entranceTile = Tileset.UNLOCKED_DOOR;

    public RectSpace() {
        this(RANDOM.nextInt(WIDTH / 4) + 3 * WIDTH / 8, RANDOM.nextInt(HEIGHT / 4) + 3 * HEIGHT / 8);
    }

    public RectSpace(int x, int y) {
        locX = x;
        locY = y;
        width = RANDOM.nextInt(6) + 5;
        height = RANDOM.nextInt(6) + 5;
    }

    public RectSpace(int x, int y, int w, int h) {
        locX = x;
        locY = y;
        width = w;
        height = h;
    }

    public boolean isOverlapping(RectSpace other) {
        return (new Rectangle(locX, locY, width, height)).intersects(new Rectangle(other.locX, other.locY, other.width, other.height));
    }

    public void draw(TileWorld wrld) {
        for (int i = locX; i < locX + width; i++) {
            for (int j = locY; j < locY + width; j++) {
                wrld.setPoint(i, j, floorTile);
            }
        }
    }

    public static void initRANDOM(long seed) {
        RANDOM = new Random(seed);
        maxRooms = RANDOM.nextInt(4) + 5;
    }

    public Room addRoom(TileWorld wrld) {
        return null;
    }

    public ArrayList<Room> addHallways(TileWorld wrld) {
        return null;
    }

    public boolean equals(Object o) {
        if (!(o instanceof RectSpace)) {
            return false;
        }
        return ((RectSpace) o).locX == locX && ((RectSpace) o).locY == locY && ((RectSpace) o).width == width && ((RectSpace) o).height == height;
    }
}
