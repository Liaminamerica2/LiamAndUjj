package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

public class Enemy extends Entity {

    private int dir;
    private TETile defaultTile = Tileset.DEFAULT_ENEMY;

    public Enemy(int xx, int yy) {
        super(xx, yy);
        dir = RectSpace.RANDOM.nextInt(4);
    }

    public Enemy(int xx, int yy, int direction) {
        super(xx, yy);
        dir = direction;
    }

    public void move(int direction, TileWorld world) {
        if (!availableDir(world).contains(dir)) {
            return;
        }
        super.move(direction);
    }

    public void makeTurn(TileWorld wrld) {
        if (!availableDir(wrld).contains(dir)) {
            dir += 2;
            dir %= 4;
        }
        move(dir, wrld);
    }

    public void draw(TileWorld wrld) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return;
        }
        wrld.setPoint(prevX, prevY, wrld.floorTile);
        wrld.setPoint(x, y, defaultTile);
    }
}
