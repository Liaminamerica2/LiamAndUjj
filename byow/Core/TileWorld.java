package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import static byow.Core.Engine.HEIGHT;
import static byow.Core.Engine.WIDTH;

/**
 * class to represent the map.
 */
public class TileWorld implements Serializable {

    private TETile[][] world;
    private static Random RANDOM;
    public Player player;
    public ArrayList<Enemy> enemies = new ArrayList<>();


    public TileWorld() {
        world = new TETile[WIDTH][HEIGHT];
    }

    public TileWorld(int lenX, int lenY) {
        world = new TETile[lenX][lenY];
    }

    public TileWorld(TETile[][] w) {
        world = w;
    }

    public static TileWorld externalize() {
        TileWorld w = null;
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream("world");
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            w = (TileWorld) in.readObject();

            in.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return w;
    }

    /**
     * Sets the point at X,Y to TILE
     */
    public void setPoint(int x, int y, TETile tile) {
        if (getTile(x, y) != null && !getTile(x, y).equals(Tileset.NOTHING)) {
            try {
                throw new Exception("illegal operation this is drawing over something else  " + x + "   " + y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        world[x][y] = tile;
    }

    public void clearTile(int x, int y) {
        world[x][y] = Tileset.NOTHING;
    }

    /**
     * draws a line at X,Y moving in the positive X direction (right) for LENGTH blocks
     */
    private void drawHorizontalLine(int x, int y, int length, TETile tile) {
        for (int i = 0; i < length; i++) {
            world[x + i][y] = tile;
        }
    }

    /**
     * draws a line at X,Y moving in the positive Y direction (up) for LENGTH blocks
     */
    private void drawVerticalLine(int x, int y, int length, TETile tile) {
        for (int i = 0; i < length; i++) {
            world[x][y + i] = tile;
        }
    }

    public TETile[][] getTiles() {
        fillRestWithVoid();
        return addEntities();
    }

    private TETile[][] addEntities() {
        TETile[][] tiles = world.clone();
        if (player != null) {
            player.draw(tiles);
        }
        for (Enemy i : enemies) {
            i.draw(tiles);
        }
        return tiles;
    }

    public void randomlyGenerateWold() {
        RectSpace.initRANDOM(Engine.seed);
        Renderer r = new Renderer(this);
    }

    /**
     * anything that's null is replaced with a nothing tile.
     */
    public void fillRestWithVoid() {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (world[i][j] == null) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    public void serialize() {
        try {
            FileOutputStream out = new FileOutputStream("world");
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(this);
            out.close();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        TileWorld world = new TileWorld();
        world.drawVerticalLine(0, 0, 5, Tileset.FLOWER);
        world.serialize();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(TileWorld.externalize().getTiles());
    }

    public TETile getTile(int i, int j) {
        return world[i][j];
    }

    public void drawLine(int x, int y, int width, int dir, TETile tile) {
        switch (dir) {
            case 0:
                drawHorizontalLine(x, y, width, tile);
                break;
            case 1:
                drawVerticalLine(x, y, width, tile);
                break;
            case 2:
                for (int i = x - width + 1; i <= x; i++) {
                    setPoint(i, y, tile);
                }
                break;
            case 3:
                for (int j = y - width + 1; j <= y; j++) {
                    setPoint(x, j, tile);
                }
                break;
        }
    }

    public boolean isPosAvail(TileWorld wrld, int xx, int yy) {
        return (wrld.getTile(xx, yy) == null || wrld.getTile(xx, yy).equals(Renderer.floorTile) || wrld.getTile(xx, yy).equals(Renderer.entranceTile));
    }

    public void setPlayer(Player player1) {
        player = player1;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
}
