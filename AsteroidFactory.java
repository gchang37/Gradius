package a06_Gradius;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class AsteroidFactory {

	private final static int ASTEROID_SIZE_MIN = 10;
	private final static int ASTEROID_SIZE_MAX = 40;
	private final static int ASTEROID_VEL_MIN = 1;
	private final static int ASTEROID_VEL_MAX = 4;

	private static Rectangle startBounds;
	private static Rectangle moveBounds;

	private AsteroidFactory() {}

	public static void setStartBounds(Rectangle r) {
		startBounds = r;
	}
	public static void setMoveBounds(Rectangle r) {
		// TODO
		moveBounds = r.union(startBounds);
		moveBounds.width = moveBounds.width + 1;
	}

	public static Asteroid makeAsteroid() {
		// TODO
		int x = random((int)startBounds.getX(), (int)startBounds.getWidth());
		int y = random((int)startBounds.getY(), (int)startBounds.getHeight());
		int w = random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX);
		int h = random(ASTEROID_SIZE_MIN, ASTEROID_SIZE_MAX);
		float v= random(ASTEROID_VEL_MIN,ASTEROID_VEL_MAX);

		return new AsteroidImpl(x,y,w,h,v);
	}

	private static int random(int min, int max) {
		if(max-min == 0) { return min; }
		Random rand = java.util.concurrent.ThreadLocalRandom.current();
		return min + rand.nextInt(max + 1);
	}

	private static class AsteroidImpl extends SpriteImpl implements Asteroid {

		private final static Color COLOR = Color.DARK_GRAY;
		private final static Color BORDER = Color.BLUE;

		public AsteroidImpl(int x, int y, int w, int h, float v) {
			// TODO
			super(new Ellipse2D.Float(x,y,w,h), moveBounds, false, BORDER,COLOR);
			setVelocity(-v,0);
		}

	}
}
