package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;

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

    public void move(int xx, int yy, int direction) {
        super.move(xx, yy);
        dir = direction;
    }

    public void makeTurn(TileWorld wrld) {
        if (!availableDir(wrld).contains(dir)) {
            dir += 2;
            dir %= 4;
        }
        move(dir);
    }

    @Override
    public void draw(TETile[][] wrld) {
        wrld[x][y] = defaultTile;
    }


}
