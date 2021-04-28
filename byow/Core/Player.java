package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player extends Entity {

    public TETile defaultTile = Tileset.AVATAR;

    public int health = 3;

    public Player(int xx, int yy) {
        super(xx, yy);
    }

    public Player(int xx, int yy, TETile defTile) {
        super(xx, yy, defTile);
    }

    public void draw(TileWorld wrld) {
        wrld.setPoint(prevX, prevY, wrld.floorTile);
        wrld.setPoint(x, y, defaultTile);
    }

    public void move(int dir, TileWorld world) {
        if (!availableDir(world).contains(dir)) {
            return;
        }
        super.move(dir);
    }
}
