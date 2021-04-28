package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 27;
    private static final int HEIGHT = 30;
    private TETile[][] tiles = new TETile[WIDTH][HEIGHT];
    private TERenderer ter;

    public HexWorld() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

    }

    private void addPoint(int x, int y, TETile tile) {
        tiles[x][y] = tile;
    }

    private void addHexagon(int x, int y, int size,TETile tile) {
        for (int i = 0; i < size; i++) {
            drawHorizontalLine(x - i, y - i + 5, size + 2 * i , tile);
        }
        for (int i = size - 1; i >= 0; i--) {
            drawHorizontalLine(x - i, y + i - 2 * size + 1 + 5, size + 2 * i, tile);
        }
    }

    private void drawHorizontalLine(int x, int y, int size, TETile tile) {
        for (int i = 0; i < size; i++) {
            tiles[x + i][y] = tile;
        }
    }

    private void fillWithHexagons() {
        for (int j = 2; j <= 22; j += 5) { // -3 * Math.abs((j - 12) / 5)
            drawVerticalLineHexagons(j, 3 * Math.abs((j - 12) / 5), 3, 5 - Math.abs((j - 12) / 5), Tileset.WALL);
        }
    }

    private void drawVerticalLineHexagons(int x, int y, int size, int count, TETile tile) {
        for (int i = 0; i < count; i++) {
            addHexagon(x, y + i * 2 * size, size, tile);
        }
    }


    private static TETile randomTile () {
        int tileNum = (int) (3 * Math.random());
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.NOTHING;
            default:
                return Tileset.NOTHING;
        }
    }

    private void fillRestWithVoid(TETile[][] world) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[i].length; j++) {
                if (world[i][j] == null) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    private void renderFrame() {
        fillRestWithVoid(tiles);
        ter.renderFrame(tiles);
    }

    public static void main (String[]args){
        HexWorld hex = new HexWorld();
        hex.fillWithHexagons();
        hex.renderFrame();
    }
}
