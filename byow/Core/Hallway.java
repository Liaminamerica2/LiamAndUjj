package byow.Core;

import byow.TileEngine.TETile;

public class Hallway extends RectSpace {

    public int dir; // same dir
    public boolean isDouble;

    public Hallway(int x, int y, int direction, boolean isDoub) {
        super(x, y);
        isDouble = isDoub;
        dir = direction;
        switch (dir) {
            case (0):
            case (2):
                width = RANDOM.nextInt(8) + 2;
                height = isDouble ? 4 : 3;
                break;
            case (1):
            case (3):
                width = isDouble ? 4 : 3;
                height = RANDOM.nextInt(8) + 2;// size of hallway
                break;
        }
//        setEndPoint();
    }

    public Hallway(int x, int y, int direction, boolean isD, TETile floor, TETile wall) {
        this(x, y, direction, isD);
        wallTile = wall;
        floorTile = floor;
    }

    public void setEndPoint() {
        int x;
        int y;
        switch (dir) {
            case (0):
                x = locX + width - 1;
                y = locY + 1;
                break;
            case (1):
                x = locX + 1;
                y = locY + height - 1;
                break;
            case (2):
                x = locX - width + 1;
                y = locY + 1;
            case (3):
        }
//        endPoint = new EndPoint(x, y);
    }

    @Override
    public void draw(TileWorld wrld) {
        if (isDouble == true) {
            switch (dir) {
                case (0):
                case (2):
                    wrld.drawLine(locX, locY, width, dir, wallTile);
                    wrld.drawLine(locX, locY + 1, width, dir, floorTile);
                    wrld.drawLine(locX, locY + 2, width, dir, floorTile);
                    wrld.drawLine(locX, locY + 3, width, dir, wallTile);
                    break;
                case (1):
                case (3):
                    wrld.drawLine(locX, locY, height, dir, wallTile);
                    wrld.drawLine(locX + 1, locY, height, dir, floorTile);
                    wrld.drawLine(locX + 2, locY, height, dir, floorTile);
                    wrld.drawLine(locX + 3, locY, height, dir, wallTile);
                    break;
            }
            return;
        }
        switch (dir) {
            case (0):
            case (2):
                wrld.drawLine(locX, locY, width, dir, wallTile);
                wrld.drawLine(locX, locY + 1, width, dir, floorTile);
                wrld.drawLine(locX, locY + 2, width, dir, wallTile);
                break;
            case (1):
            case (3):
                wrld.drawLine(locX, locY, height, dir, wallTile);
                wrld.drawLine(locX + 1, locY, height, dir, floorTile);
                wrld.drawLine(locX + 2, locY, height, dir, wallTile);
                break;
        }

    }

    public void setDrawnTrue(EndPoint end) {
        end.isDrawn = true;
    }

    @Override
    public Room addRoom(TileWorld wrld) {
        switch (dir) {
            case (0):
                int h = RANDOM.nextInt(6) + 5;
                Room r = new Room(locX + width, locY - RANDOM.nextInt(h - 3), RANDOM.nextInt(6) + 5, h, this);
                Room.setDrawnTrue(r.addEntrance(locX + width, locY + 1, isDouble));
//                r.draw(wrld);
                return r;
            case (1):
                int w = RANDOM.nextInt(6) + 5;
                r = new Room(locX - RANDOM.nextInt(w - 3), locY + height, w, RANDOM.nextInt(6) + 5, this);
                Room.setDrawnTrue(r.addEntrance(locX + 1, r.locY, isDouble));
//                r.draw(wrld);
                return r;
            case (2):
                h = RANDOM.nextInt(6) + 5;
                w = RANDOM.nextInt(6) + 5;
                r = new Room(locX - width - w + 1, locY - RANDOM.nextInt(h - 3), w, h, this);
                Room.setDrawnTrue(r.addEntrance(r.locX + r.width - 1, locY + 1, isDouble));
//                r.draw(wrld);
                return r;
            case (3):
                w = RANDOM.nextInt(6) + 5;
                h = RANDOM.nextInt(6) + 5;
                r = new Room(locX - RANDOM.nextInt(w - 3), locY - height - h + 1, w, h, this);
                Room.setDrawnTrue(r.addEntrance(locX + 1, r.locY + r.height - 1, isDouble));
//                r.draw(wrld);
                return r;
        }
        return null;
    }

    public void closeHallway(TileWorld wrld) {
        switch (dir) {
            case (0):
                wrld.drawLine(locX + width, locY, 1, height, wallTile);
                break;
            case (1):
                break;
            case (2):
                break;
            case (3):
                break;
        }
    }

    private class EndPoint {
        private int x;
        private int y;
        private boolean isDrawn = false;

        private EndPoint(int xx, int yy) {
            x = xx;
            y = yy;
        }
    }
}
