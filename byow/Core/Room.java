package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Room extends RectSpace {

    public static int roomNum = 0;
    public ArrayList<Entrance> entrances;
    public ArrayList<Hallway> hallways = new ArrayList<>();
    public Hallway prevHallway;

    public Room() {
        super();
        roomNum++;
        prevHallway = null;
        entrances = generateEntrances();
    }

    public Room(int x, int y, int w, int h, Hallway prev) {
        super(x, y, w, h);
        roomNum++;
        prevHallway = prev;
        entrances = generateEntrances();
    }

    public Room(int x, int y, int w, int h, Hallway prev, TETile floor, TETile wall, TETile ent) {
        this(x, y, w, h, prev);
        floorTile = floor;
        wallTile = wall;
        entranceTile = ent;
    }

    public Room(TETile floor, TETile wall, TETile entrance) {
        this();
        floorTile = floor;
        wallTile = wall;
        entranceTile = entrance;
    }

    public static int getXEnt(Entrance ent) {
        return ent.x;
    }

    public static int getYEnt(Entrance ent) {
        return ent.y;
    }

    public static int getDirEnt(Entrance ent) {
        return ent.dir;
    }

    public static boolean isDrawn(Entrance ent) {
        return ent.isDrawn;
    }

    public static void setDrawnTrue(Entrance ent) {
        ent.isDrawn = true;
    }

    public static Integer[] locationOfHallway(Entrance ent) {
        switch (ent.dir) {
            case (0):
                return new Integer[]{ent.x + 1, ent.y - 1};
            case (1):
                return new Integer[]{ent.x - 1, ent.y + 1};
            case (2):
            case (3):
                return new Integer[]{ent.x - 1, ent.y - 1};
            default:
                return null;
        }
    }

    private ArrayList<Entrance> generateEntrances() {
        ArrayList<Entrance> ret = new ArrayList<>();
        int numEnt = RANDOM.nextInt(2);
        if (roomNum <= 2) {
            numEnt = RANDOM.nextInt(2) + 3;
        }
        Set<Integer> sides = new HashSet<>();
        int j = 0;
        while (j < numEnt) {
            switch (RANDOM.nextInt(4)) {
                case (0): // right
                    if (sides.add(0)) {
                        ret.add(new Entrance((width - 1) + locX, RANDOM.nextInt(height - 3) + 1 + locY, 0));
                        j++;
                        addEntranceIfDouble(ret.get(ret.size() - 1));
                    }
                    break;
                case (1): // top
                    if (sides.add(1)) {
                        ret.add(new Entrance(RANDOM.nextInt(width - 3) + 1 + locX, (height - 1) + locY, 1));
                        j++;
                        addEntranceIfDouble(ret.get(ret.size() - 1));
                    }
                    break;
                case (2): // left
                    if (sides.add(2)) {
                        ret.add(new Entrance(locX, RANDOM.nextInt(height - 3) + 1 + locY, 2));
                        j++;
                        addEntranceIfDouble(ret.get(ret.size() - 1));
                    }
                    break;
                case (3): // bottom
                    if (sides.add(3)) {
                        ret.add(new Entrance(RANDOM.nextInt(width - 3) + 1 + locX, locY, 3));
                        j++;
                        addEntranceIfDouble(ret.get(ret.size() - 1));
                    }
                    break;
            }
        }
        return ret;
    }

    private void addEntranceIfDouble(Entrance e) {
        if (RANDOM.nextInt(4) != 3) {
            return;
        }
        makeDoubleEntrance(e);
    }

    private void makeDoubleEntrance(Entrance e) {
        switch (e.dir) {
            case (0):
            case (2):
                e.makeDouble(new Entrance(e.x, e.y + 1, e.dir));
                break;
            case (1):
            case (3):
                e.makeDouble(new Entrance(e.x + 1, e.y, e.dir));
                break;
        }
    }

    @Override
    public void draw(TileWorld wrld) {
        for (int i = locX; i < locX + width; i++) {
            for (int j = locY; j < locY + height; j++) {
                if (i == locX || j == locY || i == locX + width - 1 || j == locY + height - 1) {
                    wrld.setPoint(i, j, wallTile);
                    continue;
                }
                wrld.setPoint(i, j, floorTile);
            }
        }
        for (Entrance i : entrances) {
            wrld.clearTile(i.x, i.y);
            wrld.setPoint(i.x, i.y, entranceTile);
            if (i.op != null) {
                wrld.clearTile(i.op.x, i.op.y);
                wrld.setPoint(i.op.x, i.op.y, entranceTile);
            }
        }
    }

    public Entrance addEntrance(int x, int y, boolean isDoub) {
        boolean inThere = false;
        Entrance ent = null;
        if (!(x == locX || y == locY || x == locX + width - 1 || y == locY + height - 1)) {
            System.out.println("cannot add Entrance because entrance not on wall");
        }
        if (x == locX + width - 1) {
            if (inThere) {
                System.out.println("cannot add Entrance because entrance in the corner");
            }
            inThere = true;
            ent = new Entrance(x, y, 0);
        }
        if (y == locY + height - 1) {
            if (inThere) {
                System.out.println("cannot add Entrance because entrance in the corner");
            }
            inThere = true;
            ent = new Entrance(x, y, 1);
        }
        if (x == locX) {
            if (inThere) {
                System.out.println("cannot add Entrance because entrance in the corner");
            }
            inThere = true;
            ent = new Entrance(x, y, 2);
        }
        if (y == locY) {
            if (inThere) {
                System.out.println("cannot add Entrance because entrance in the corner");
            }
            ent = new Entrance(x, y, 3);
        }
        for (int i = 0; i < entrances.size(); i++) {
            if (entrances.get(i).dir == ent.dir) {
                entrances.remove(i);
            }
        }
        entrances.add(ent);
        if (isDoub) {
            makeDoubleEntrance(ent);
        }
        return ent;
    }

    public Entrance getEntrance(int x, int y) {
        for (int i = 0; i < entrances.size(); i++) {
            if (entrances.get(i).x == x || entrances.get(i).y == y) {
                return entrances.get(i);
            }
        }
        return null;
    }

    @Override
    public ArrayList<Room> addHallways(TileWorld wrld) {
        ArrayList<Room> arr = new ArrayList<>();
        for (int i = 0; i < entrances.size(); i++) {
            if (Room.isDrawn(entrances.get(i))) {
                continue;
            }
            Room.setDrawnTrue(entrances.get(i));
            Integer[] loc = Room.locationOfHallway(entrances.get(i));
            Hallway hall = new Hallway(loc[0], loc[1], Room.getDirEnt(entrances.get(i)), entrances.get(i).op != null);
            hallways.add(hall);
//            hall.draw(wrld);
            arr.add(hall.addRoom(wrld));
        }
        return arr;
    }

    public ArrayList<Integer[]> removeExcessEntrances(TileWorld wrld) {
        ArrayList<Integer[]> arrayList = new ArrayList<>();
        int j = 0;
        for (Entrance i : entrances) {
            if (isEntranceConnected(i, wrld)) {
                continue;
            }
            arrayList.add(new Integer[]{i.x, i.y});
            if (i.op != null) {
                arrayList.add(new Integer[]{i.op.x, i.op.y});
            }
        }
        return arrayList;
    }

    public boolean isEntranceConnected(Entrance ent, TileWorld wrld) {
        for (int i = -1; i < 3; i += 2) {
            for (int j = -1; j < 3; j += 2) {
                if (wrld.getTile(ent.x + i, ent.y + j) == null || wrld.getTile(ent.x + i, ent.y + j).equals(Tileset.NOTHING)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Each entrance had a direction and a position.
     * Only Rooms can have entrances.
     */
    private class Entrance {

        public int x;
        public int y;
        public int dir;
        public boolean isDrawn = false;
        public Entrance op = null;

        public Entrance(int xx, int yy, int direction) {
            x = xx;
            y = yy;
            dir = direction;
        }

        public void makeDouble(Entrance e) {
            op = e;
        }
    }

}
