package a06_Gradius;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

@SuppressWarnings("serial")
public class GradiusComp extends JComponent {

	private final static int GAME_TICK = 1000 / 60; // 60 frames/sec
	private final static int ASTEROID_MAKE_TICK = 1000/4;

	private final static int SHIP_INIT_X = 10;
	private final static int SHIP_INIT_Y = Gradius.HEIGHT/3;
	private final static int SHIP_VEL_BASE = 2;
	private final static int SHIP_VEL_FAST = 4;

	private Ship ship;
	private Collection<Timer> gameTick;
	private Collection<Asteroid> roids;

	//Enhancements
	private int PLAYTIME = 1;
	private final static int INIT_HEALTH = 100;
	private final static String EXPLOSION_IMG = "src/a06_Gradius/explosion.png";
	private BufferedImage img;

	private final static int MYSTERY_BOX_TICK = 5000;
	private Collection<MysteryBox> boxs;
	public Collection<MysteryBox> collectedBox;

	public GradiusComp() {

		roids = new HashSet<Asteroid>();
		boxs = new ArrayList<MysteryBox>();
		collectedBox = new HashSet<MysteryBox>();
		gameTick = new ArrayList<Timer>();

		addKeyListener(new ShipKeyListener());
		gameTick.add(new Timer(GAME_TICK, this::update));
		gameTick.add(new Timer(ASTEROID_MAKE_TICK, e -> makeAsteroid()));

		gameTick.add(new Timer(MYSTERY_BOX_TICK, e->makeMysteryBox()));
		//gameTick.add(new Timer(MYSTERY_BOX_TICK, e->applyShipUpgrade());

		displayIMG(EXPLOSION_IMG);


	}
	private void displayIMG(String url){
		try {
			img = ImageIO.read(new File(EXPLOSION_IMG));
		} catch(IOException e) {
			JOptionPane.showMessageDialog(this,
					e.toString(),
					"Error loading image.",
					JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException(e);
		}
	}

	private void update(ActionEvent ae){
		requestFocusInWindow();

		PLAYTIME++;
		ship.move();
		roids.forEach(Asteroid::move);

/*		roids.stream()
				.peek(a->{System.out.println("examing " + a);})
				.filter(Asteroid::isOutOfBounds)
				//.collect(Collectors.toList())
				.peek(a->{System.out.println("remiving " + a);})
				.forEach(roids::remove);
				//.forEach(Collection::removeif(Asteroid::isOutOfBounds));*/

		boxs.stream()
				.forEach(MysteryBox::move);
		boxs.removeIf(MysteryBox::isOutOfBounds);

		roids.removeIf(Asteroid::isOutOfBounds);
		System.out.println(roids.size());

		/*boxs.stream()
				.filter(mysteryBox->mysteryBox.intersects(ship))
				.orElse(null);*/

		boolean roidHit = roids.stream()
				.anyMatch(asteroid -> asteroid.intersects(ship));
		if(roidHit){
			System.out.println("Ship hit");
			ship.setHealthPoints();
			int temp = ship.getHealthPoints();
			if(temp <=0){
				Stream<Timer> timerStream = gameTick.stream();
				timerStream
						.forEach(Timer::stop);
				//System.out.println("Game Over");
			}
		}

		repaint();
	}

	final Font f = new Font("Times New Roman", Font.BOLD, 86);
	final Font timeDp = new Font("SanSerif",Font.PLAIN, 18);

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		paintComponent(g2);
	}
	private void paintComponent(Graphics2D g2) {

		Stream<Timer> timerStream = gameTick.stream();
		boolean tRunning = timerStream.anyMatch(Timer::isRunning);

			g2.clearRect(0,0,getWidth(),getHeight());

			Rectangle2D background = new Rectangle2D.Float(0,0,getWidth(),getHeight());
			g2.setColor(Color.black);
			g2.fill(background);

			CubicCurve2D ring1 = new CubicCurve2D.Float(0,getHeight(),getWidth()/4,getHeight()*2/3,getHeight()*3/4,getHeight()/3,getWidth(),0);
			Color r1Color = new Color(128, 43, 0);
			g2.setColor(r1Color);
			g2.setStroke(new BasicStroke(300));
			g2.draw(ring1);
			g2.setStroke(new BasicStroke(1));

			ship.draw(g2);
			for(Asteroid a:roids){
				a.draw(g2);
			}
			for(MysteryBox m :boxs){
				m.draw(g2);
			}

			//roids.stream().forEach(a ->a.draw(g2));

		Color timeColor = new Color(255, 255, 204);
		g2.setColor(timeColor);
		g2.setFont(timeDp);
		drawPlayTime(PLAYTIME, getWidth(),getHeight(),g2);

		Color hpColor = Color.RED;
		g2.setColor(hpColor);
		g2.setFont(timeDp);
		drawHealthPoint(getWidth()/10,0,g2);

		if(!tRunning) {
			g2.drawImage(img,(int)ship.getShape().getBounds().getX(),(int)ship.getShape().getBounds().getY(),null);
			g2.setColor(Color.RED);
			g2.setFont(f);
			drawCenteredString("Game Over!", getWidth(), getHeight(), g2);
			//System.out.println("printing msg");
		}

	}

	private void drawCenteredString(String s, int w, int h, Graphics2D g2){
		FontMetrics fm = g2.getFontMetrics();
		//System.out.println("Step 1 in printing");
		int x = (w - fm.stringWidth(s))/2;
		int y = (fm.getAscent() + (h - (fm.getAscent()+fm.getDescent()))/2);
		//System.out.println("last step to printing");
		g2.drawString(s,x,y);
	}

	private void drawPlayTime(int i, int w, int h, Graphics2D g2){
		FontMetrics fm = g2.getFontMetrics();
		String s = playtTime(i);
		//System.out.println("Step 1 in printing");
		int x = (w - fm.stringWidth(s))/2;
		int y = fm.getAscent()+fm.getDescent()/2;
		//System.out.println("last step to printing");
		g2.drawString(s,x,y);
	}

	private String playtTime(int i){
		String time = "";
		int tmp = i*60;
		int min = i/3600;
		int sec = i/60;

		return time = min + "min : " + sec + "sec";
	}

	private void drawHealthPoint(int w,int h, Graphics2D g2){
		FontMetrics fm = g2.getFontMetrics();
		String s = "Health Point:";
		int x = (w + fm.stringWidth(s))/6;
		int y = h+ fm.getAscent()+fm.getDescent();
		g2.drawString(s,x,y);

		Rectangle2D healthBar = new Rectangle2D.Float(4*x+10,0,100,getHeight()/20);
		g2.draw(healthBar);
		int currentHP = ship.getHealthPoints();
		g2.drawRect(4*x+10,0,currentHP,getHeight()/20);
	}

	public void start() {
		ship = new ShipImpl(SHIP_INIT_X, SHIP_INIT_Y,new Rectangle2D.Float(0,0,getWidth(),getHeight()), INIT_HEALTH);
		Stream<Timer> timerStream = gameTick.stream();
		timerStream
				.forEach(Timer::start);
		AsteroidFactory.setStartBounds( new Rectangle(getWidth(),0,1,getHeight()));
		AsteroidFactory.setMoveBounds(new Rectangle(0,0,getWidth(), getHeight()));
		MysteryBoxFactory.setStartBounds(new Rectangle(getWidth(), 0,1,getHeight()));
		MysteryBoxFactory.setMoveBounds(new Rectangle(0,0,1,getHeight()));

	}
	//private int getHealthPoints(){}

	private void makeAsteroid(){
		roids.add(AsteroidFactory.makeAsteroid());
	}

	private void makeMysteryBox() {boxs.add(MysteryBoxFactory.makeMysteryBox());
	}

	private class ShipKeyListener extends KeyAdapter {

		private boolean up;
		private boolean down;
		private boolean left;
		private boolean right;

		private void setVelocity(KeyEvent ke){

			setDirection(ke);

			int speed = ke.isShiftDown()? SHIP_VEL_FAST : SHIP_VEL_BASE;

			int dx = 0;
			int dy = 0;

			if(up && !down){
				dy = -speed;
			}else if(!up && down){
				dy = speed;
			}

			if(left && !right){
				dx = -speed;
			}else if(!left && right){
				dx = speed;
			}

			ship.setVelocity(dx, dy);
		}

		private void setDirection(KeyEvent ke){
			boolean state = false;

			switch(ke.getID()){
				case KeyEvent.KEY_PRESSED:
					state = true;
					break;

				case KeyEvent.KEY_RELEASED:
					state = false;
					break;
			}

			switch (ke.getKeyCode()){
				case KeyEvent.VK_W:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_KP_UP:
					up = state;
					break;

				case KeyEvent.VK_S:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_KP_DOWN:
					down = state;
					break;

				case KeyEvent.VK_A:
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_KP_LEFT:
					left = state;
					break;

				case KeyEvent.VK_D:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_KP_RIGHT:
					right = state;
					break;
			}
		}

		@Override
		public void keyPressed(KeyEvent ke) {
			setVelocity(ke);
		}
		@Override
		public void keyReleased(KeyEvent ke) {
			setVelocity(ke);
		}
	};
}
