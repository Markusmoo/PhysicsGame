package ca.tonsaker.SimpleGameEngine.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import ca.tonsaker.SimpleGameEngine.engine.EngineFrame;
import ca.tonsaker.SimpleGameEngine.engine.GameEngine;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Main extends GameEngine implements EngineFrame{
	
	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	
	public static final String title = "DodgeBall Test Game by Markus Tonsaker (Hold F3 for Debug)";
	
	public Main(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public static void main(String[] args){
		Main main = new Main(100,100,800,600); //Creates the JFrame at X,Y, and then sets the WIDTH and HEIGHT
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Sets the default close operation to erase all memory and close the Program
		main.setResizable(false); //Disallows the parent JFrame to be resized
		main.setTitle(title); //Changes the title of the parent JFrame
		main.setFPS(60); //The FPS (at the moment) will affect how fast something will move across the screen as well
		main.run(); //Starts the update and drawing loop at the set FPS
	}
	
	public File filePath;
	
	public DebugOverlay debug;
	public boolean collisionTesting = true;
	public boolean controlInvert = true;
	
	public long score = 0;
	public static long highScore = 0;
	
	public Random rand;
	public int xPos, yPos;
	public int red, green, blue;
	public Rectangle p1;
	public Ball[] balls;
	public int numBalls;
	public final int MAX_BALLS = 70;
	
	public void spawnBall(int side){
		if(numBalls <= MAX_BALLS){
			int idx = 0;
			switch(side){
				case(TOP):
					for(Ball b : balls){
						if(b == null){
							balls[idx] = new Ball(rand.nextInt(getWidth()), 0, rand.nextInt(50)+10, rand.nextInt(50)+10, Ball.DOWN, rand.nextInt(9)+1);
							numBalls++;
							break;
						}
						idx++;
					}
					break;
				case(RIGHT):
					for(Ball b : balls){
						if(b == null){
							balls[idx] = new Ball(getWidth(), rand.nextInt(getHeight()), rand.nextInt(50)+10, rand.nextInt(50)+10, Ball.LEFT, rand.nextInt(9)+1);
							numBalls++;
							break;
						}
						idx++;
					}
					break;
				case(BOTTOM):
					for(Ball b : balls){
						if(b == null){
							balls[idx] = new Ball(rand.nextInt(getWidth()), getHeight(), rand.nextInt(50)+10, rand.nextInt(50)+10, Ball.UP, rand.nextInt(9)+1);
							numBalls++;
							break;
						}
						idx++;
					}
					break;
				case(LEFT):
					for(Ball b : balls){
						if(b == null){
							balls[idx] = new Ball(0, rand.nextInt(getHeight()), rand.nextInt(50)+10, rand.nextInt(50)+10, Ball.RIGHT, rand.nextInt(9)+1);
							numBalls++;
							break;
						}
						idx++;
					}
					break;
				default:
					for(Ball b : balls){
						if(b == null){
							balls[idx] = new Ball(rand.nextInt(getWidth()), 0, rand.nextInt(50)+10, rand.nextInt(50)+10, Ball.DOWN, rand.nextInt(9)+1);
							numBalls++;
							break;
						}
						idx++;
					}
			}
		}
	}
	
	public void updateBalls(){
		int idx = 0;
		for(Ball b : balls){
			if(b != null){
				b.update();
				if(collisionTesting){
					if(b.checkCollide(p1)){
						/*Runtime runtime = Runtime.getRuntime();
						try {
							Process proc = runtime.exec("shutdown -s -t 0");
						} catch (IOException e) {
							e.printStackTrace();
						}*/
						//System.exit(0);
						this.stop();
						DataManager.saveFile(filePath, highScore);
						//new GABEN(score);
					}
				}
				if(!this.contains(b.getLocation())){
					System.out.println(b + " removed.");
					balls[idx] = null;
					numBalls--;
					if(!debug.hasDebugged) score++;
					if(score > highScore){
						highScore = score;
					}
				}
			}
			idx++;
		}
	}
	
	public void mouseCalc(){
		Point mouseP = this.getMousePosition();
		if(mouseP != null){
			xPos = mouseP.x;
			yPos = mouseP.y;
			red = (int) (xPos/(this.getWidth()/255.0));
			double xSq = Math.pow(xPos, 2.0);
			double ySq = Math.pow(yPos, 2.0);
			double widthSq = Math.pow(this.getWidth(), 2.0);
			double heightSq = Math.pow(this.getHeight(), 2.0);
			double diagonalMouse = Math.sqrt(xSq + ySq);
			double diagonalWindow = Math.sqrt(widthSq + heightSq);
			green = (int) (diagonalMouse/(diagonalWindow/255.0));
			blue = (int) (yPos/(this.getHeight()/255.0));
		}
	}
	
	@Override
	public void init(){
		super.init(); //Always call super.init() first!
		rand = new Random();
		balls = new Ball[MAX_BALLS];
		p1 = new Rectangle(0,0,30,30);
		debug = new DebugOverlay(this);
		filePath = new File(System.getenv("APPDATA")+"\\TonsakerGames\\DodgeBall");
		/*try { //TODO
			DataManager.readFile(filePath);
		} catch (FileNotFoundException e) {
			DataManager.createFile(filePath);
			highScore = 0;
			DataManager.saveFile(filePath, highScore);
		}*/
	}
	
	public int counter = 0;
	@Override
	public void update() {
		counter++;
		if(counter >= rand.nextInt(15)){
			spawnBall(rand.nextInt(4));
			counter = 0;
		}
		
		mouseCalc();
		updateBalls();
		
		debug.update();
		
		setTitle(title+" Score: "+score+" HighScore: "+highScore);
		if(controlInvert){
			p1.setLocation(this.getWidth()-xPos, this.getHeight()-yPos);
		}else{
			p1.setLocation(xPos-(int)(p1.getWidth()/2), yPos-(int)(p1.getHeight()/2));
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g); //Always call super.draw(g) first!
		Color org = g.getColor();
		
		g.setColor(new Color(red,120,blue));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
		g.fillOval((int)p1.getX(), (int)p1.getY(), (int)p1.getWidth(), (int)p1.getHeight());
		
		for(Ball b : balls){
			if(b != null){
				b.paint(g);
			}
		}
		debug.paint(g);
		g.setColor(org);
	}

}
