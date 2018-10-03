package me.candyyn.cubefarm.utils;

import java.awt.*;

public class FarmRectangle {
    public int x, y, width, height;

    //x =

    public FarmRectangle( int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public Point getCenter() {
        // (Constant.Farm_size + Constant.Void_Size + Constant.FarmSize ) / 2 = x + width / 2
        return new Point(x + width / 2, y + height / 2);
    }
    /*
        public int x, y, width, height;

    //x = index * (Constants.FARM_SIZE + Constants.VOID_GAP)
    //y = 0
    //w = Constants.FARM_SIZE
    //h = Constants.FARM_SIZE

    public FarmRectangle( int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

     */
    // getBounds = new FarmRectangle(index * (Constants.FARM_SIZE + Constants.VOID_GAP), 0, Constants.FARM_SIZE, Constants.FARM_SIZE);
    // getBounds.getCenter

    /*

            public static final int FARM_CHUNK_SIZE = 15;
            public static final int FARM_SIZE = FARM_CHUNK_SIZE * 8;
            public static final int VOID_GAP = 100;
     */
    /*  x = (index *
        x = (index * (340) / 2)
     */

    public int getX2() {
        return x + width;
    }

    public int getY2() {
        return y + height;
    }

    public Point getCord1() {
        return new Point(x, y);
    }

    public Point getCord2() {
        return new Point(getX2(), getY2());
    }

    public boolean contains(Point point) {
        Rectangle r = new Rectangle(x, y, width, height);
        return r.contains(point);
    }
}
