package a06_Gradius;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 * Created by GChang on 2018-03-07.
 */
public class MysteryBoxFactory {

    private final static int MBOX_SIZE_MIN = 10;
    private final static int MBOX_SIZE_MAX = 20;
    private final static int MBOX_VEL_MIN = 1;
    private final static int MBOX_VEL_MAX = 8;

    private static Rectangle startBounds;
    private static Rectangle moveBounds;

    private MysteryBoxFactory() {}

    public static void setStartBounds(Rectangle r) {
        startBounds = r;
    }
    public static void setMoveBounds(Rectangle r) {
        // TODO
        moveBounds = r.union(startBounds);
        moveBounds.width = moveBounds.width + 1;
    }

    public static MysteryBox makeMysteryBox() {
        // TODO
        int x = random((int)startBounds.getX(), (int)startBounds.getWidth());
        int y = random((int)startBounds.getY(), (int)startBounds.getHeight());
        int w = random(MBOX_SIZE_MIN, MBOX_SIZE_MAX);
        int h = random(MBOX_SIZE_MIN, MBOX_SIZE_MAX);
        float v= random(MBOX_VEL_MIN, MBOX_VEL_MAX);

        return new MysteryBoxImpl(x,y,w,h,v);
    }

    private static int random(int min, int max) {
        if(max-min == 0) { return min; }
        Random rand = java.util.concurrent.ThreadLocalRandom.current();
        return min + rand.nextInt(max + 1);
    }

    private static class MysteryBoxImpl extends SpriteImpl implements MysteryBox {
        private final static Color BCOLOR = Color.yellow;
        private final static Color BBORDER = Color.orange;
        private int type;

        public MysteryBoxImpl(int x, int y, int w, int h, float v) {
            // TODO
            super(new Rectangle2D.Float(x, y, w, h), moveBounds, false, BBORDER, BCOLOR);
            setVelocity(-v, 0);
            // type = getBoxType();
        }

    }

    public enum BoxType {
        FORCEFIELD, INVINCIBLE, SHRINK;

        public static BoxType randomBox() {
            return BoxType.values()[random(0, BoxType.values().length)];
        }
    }


}



