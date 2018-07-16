package a06_Gradius;
import java.awt.*;
import java.awt.geom.*;

public abstract class SpriteImpl implements Sprite {

	// drawing
	private Shape shape;
	private final Color border;
	private final Color fill;

	// movement
	private float dx, dy;
	private final Rectangle2D bounds;
	private final boolean isBoundsEnforced;

	// enhancement
	private int healthPoints;

	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color border, Color fill) {
		this.shape = shape;
		this.bounds = bounds;
		this.isBoundsEnforced = boundsEnforced;
		this.border = border;
		this.fill = fill;
	}
	protected SpriteImpl(Shape shape, Rectangle2D bounds, boolean boundsEnforced, Color fill) {
		this(shape, bounds, boundsEnforced, null, fill);
	}

	public Shape getShape() {
		return shape;
	}

	public void setVelocity(float dx, float dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void move() {
		Shape oldPos = shape;
		shape = AffineTransform.getTranslateInstance(dx, dy).createTransformedShape(shape);
		if(isBoundsEnforced && !isInBounds()){
			shape = oldPos;
		}

	}

	public boolean isOutOfBounds() {
		if(!this.getShape().getBounds().intersects(bounds)){
			return true;
		 }else return false;

	}
	public boolean isInBounds() {
		return isInBounds(bounds, shape);
	}

	private static boolean isInBounds(Rectangle2D bounds, Shape s) {

		return bounds.contains(s.getBounds());
	}

	public void draw(Graphics2D g2) {

		g2.setColor(fill);
		g2.fill(shape);
		g2.setColor(border);
		g2.draw(shape);
	}

	public boolean intersects(Sprite other) {
		return intersects(other.getShape());
	}

	private boolean intersects(Shape other) {
		return this.getShape().intersects(other.getBounds()) &&
				intersects(new Area(this.getShape()), new Area(other));
	}
	private static boolean intersects(Area a, Area b) {

		a.intersect(b);
		return !a.isEmpty();
	}

	//public void setHealthPoints(){}

	//public int getHealthPoints(){return healthPoints;}
}
