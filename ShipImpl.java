package a06_Gradius;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ShipImpl  extends SpriteImpl implements Ship {

	private final static Color FILL = new Color(0, 204, 102);
	private final static Color BORDER = new Color(230, 255, 242);

	private final static int HEIGHT = 20;
	private final static int WIDTH = HEIGHT;

	//Enhancement
	private int HEALTHPOINTS;
	private final static int shipDamage = 10;

	public ShipImpl(int x, int y, Rectangle2D moveBounds,int hp) {

		super(new Polygon(new int[]{x,x+WIDTH,x}, new int[]{y,y+HEIGHT/2,y+HEIGHT},3), moveBounds, true, BORDER, FILL);
		this.HEALTHPOINTS = hp;
	}

	public void setHealthPoints(){
		 if(HEALTHPOINTS>0){
		 	HEALTHPOINTS -= shipDamage;
		 } else HEALTHPOINTS = 0;
	}

	public int getHealthPoints(){return HEALTHPOINTS;}

}
