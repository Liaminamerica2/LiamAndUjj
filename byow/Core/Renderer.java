package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;

public class Renderer {

    public static TETile floorTile = Tileset.FLOOR;
    public static TETile wallTile = Tileset.WALL;
    public TileWorld wrld;
    public Room initSpace;
    public ArrayList<Room> rooms;
    public Player player;
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();


    public Renderer(TileWorld world) {
        setTiles();
        wrld = world;
        wrld.setTiles(floorTile, wallTile);
        Room.roomNum = 0;
        ArrayList<Room> arr = new ArrayList<>();
        initSpace = new Room(floorTile, wallTile, floorTile);
        ArrayList<Room> queue = new ArrayList<>();
        queue.add((Room) initSpace);
        int j = 0;
        while (queue.size() > 0 && j++ <= RectSpace.maxRooms) {
            Room currRoom = queue.remove(0);
            for (int i = 0; i < arr.size(); i++) {
                if (currRoom.prevHallway != null && areOverlapping(currRoom, arr.get(i))) {
                    continue;
                }
            }
            currRoom.draw(wrld);
            if (currRoom.prevHallway != null) {
                currRoom.prevHallway.draw(wrld);
            }
            ArrayList<Room> addedRooms = new ArrayList<>();
            addedRooms.addAll(currRoom.addHallways(wrld));
            queue.addAll(addedRooms);
            arr.add(currRoom);
        }
        rooms = arr;
        removeExcessEntrancesFromWrld(arr);
        renderPlayer();
        renderEnemies();
    }

    private void setTiles() {
        int rand = RectSpace.RANDOM.nextInt(6);
        switch (rand) {
            case (0):
                floorTile = Tileset.FLOOR;
                wallTile = Tileset.WALL;
                break;
            case (1):
                floorTile = Tileset.GRASS;
                wallTile = Tileset.FLOWER;
                break;
            case (2):
                floorTile = Tileset.SAND;
                wallTile = Tileset.TREE;
                break;
            case (3):
                floorTile = Tileset.WATER;
                wallTile = Tileset.MOUNTAIN;
                break;
            case (4):
                floorTile = Tileset.SAND;
                wallTile = Tileset.MOUNTAIN;
                break;
            case (5):
                floorTile = Tileset.UNLOCKED_DOOR;
                wallTile = Tileset.LOCKED_DOOR;
                break;
        }
    }

    private void renderPlayer() {
        Integer[] pos = randomTileInRoom(initSpace);
        player = new Player(pos[0], pos[1]);
        wrld.setPlayer(player);
    }

    private void renderEnemies() {
        int numEnemies = RectSpace.RANDOM.nextInt(3) + 2;
        ArrayList<Room> arr = new ArrayList<>();
        for (int i = 0; i < numEnemies; i++) {
            Room randRoom = rooms.get(RectSpace.RANDOM.nextInt(rooms.size()));
            arr.add(randRoom);
            Integer[] pos = randomTileInRoom(randRoom);
            Enemy e = new Enemy(pos[0], pos[1]);
            wrld.addEnemy(e);
        }
    }

    private Integer[] randomTileInRoom(Room room) {
        return new Integer[]{room.locX + 1 + RectSpace.RANDOM.nextInt(room.width - 2), room.locY + 1 + RectSpace.RANDOM.nextInt(room.height - 2)};
    }

    private boolean areOverlapping(Room currRoom, Room room) {
        if (room == null || currRoom == null || room.prevHallway == null || currRoom.prevHallway == null) {
            return false;
        }
        return (currRoom.prevHallway.isOverlapping(room.prevHallway) || currRoom.isOverlapping(room) || currRoom.isOverlapping(room.prevHallway) || currRoom.prevHallway.isOverlapping(room) || currRoom.isInsideBounds());
    }

    private ArrayList<Integer[]> getExcessEntrancesFromWrld(ArrayList<Room> arr) {
        ArrayList<Integer[]> list = new ArrayList<>();
        for (Room i : arr) {
            list.addAll(i.removeExcessEntrances(wrld));
        }
        return list;
    }

    private void removeExcessEntrancesFromWrld(ArrayList<Room> arr) {
        for (Integer[] i : getExcessEntrancesFromWrld(arr)) {
            wrld.clearTile(i[0], i[1]);
            wrld.setPoint(i[0], i[1], wallTile);
        }
    }
}
