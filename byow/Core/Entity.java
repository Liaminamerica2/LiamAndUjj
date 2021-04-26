package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Entity implements Serializable {

    public int x;
    public int y;
    public TETile defaultTile = Tileset.NOTHING;

    public int health = 100;

    public Entity(int xx, int yy) {
        x = xx;
        y = yy;
    }

    public Entity(int xx, int yy, TETile defTile) {
        this(xx, yy);
        defaultTile = defTile;
    }

    protected ArrayList<Integer> availableDir(TileWorld wrld) {
        ArrayList<Integer> dirs = new ArrayList<>();
        if (wrld.isPosAvail(wrld, x + 1, y)) {
            dirs.add(0);
        }
        if (wrld.isPosAvail(wrld, x, y + 1)) {
            dirs.add(1);
        }
        if (wrld.isPosAvail(wrld, x - 1, y)) {
            dirs.add(2);
        }
        if (wrld.isPosAvail(wrld, x, y - 1)) {
            dirs.add(3);
        }
        return dirs;
    }

    public void move(int xx, int yy) {
        x = xx;
        y = yy;
    }

    public void move(int dir) {
        switch (dir) {
            case (0):
                x++;
                break;
            case (1):
                y++;
                break;
            case (2):
                x--;
                break;
            case (3):
                y--;
                break;
        }
    }

    public void draw(TETile[][] wrld) {
        wrld[x][y] = defaultTile;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Entity)) {
            return false;
        }
        return x == ((Entity) o).x && y == ((Entity) o).y;
    }
}
