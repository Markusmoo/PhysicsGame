package ca.tonsaker.simplegameengine.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ca.tonsaker.simplegameengine.engine.EngineFrame;
import ca.tonsaker.simplegameengine.engine.GameEngine;
import ca.tonsaker.simplegameengine.engine.graphics.Sprite;
import ca.tonsaker.simplegameengine.engine.util.DebugOverlay;
import ca.tonsaker.simplegameengine.engine.util.DebugOverlay.DebugInfo;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SpriteTestMain extends GameEngine implements EngineFrame{
	
	public SpriteTestMain(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	int i = 0x0032f;

	public static void main(String[] args){
		SpriteTestMain main = new SpriteTestMain(0,0,800,600); //Creates the JFrame at X,Y, and then sets the WIDTH and HEIGHT
		main.centerWindow(); //Centers window on screen
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Sets the default close operation to erase all memory and close the Program
		main.setResizable(false); //Disallows the parent JFrame to be resized
		main.setTitle("Sprite Example"); //Changes the title of the parent JFrame
		main.setFPS(90); //The FPS (Frames per second)
		main.setUPS(100); //The UPS (Updates per second)
		main.run(); //Starts the update and drawing loop at the set FPS
	}
	
	DebugInfo d1;
	Sprite sprite;
	
	@Override
	public void init(){
		sprite = new Sprite(100, 100, "res/TestPic.png", false);
		d1 = new DebugInfo("Sprite x:"+sprite.getX()+" Sprite y:"+sprite.getY(), Color.red, 1);
		DebugOverlay.addDebug(d1);
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				Point p = getMousePosition();
				sprite.moveTo(p.x, p.y, 2.0f);
			}

			@Override
			public void mouseReleased(MouseEvent e) {}
			
		});
	}
	
	@Override
	public void update() {
		d1.setDebugText("Sprite x:"+sprite.getX()+" Sprite y:"+sprite.getY());
		sprite.update();
	}

	@Override
	public void render(Graphics2D g) {
		sprite.render(g);
		if(DebugOverlay.debugging()){
			Point lastPoint = null;
			Point nextPoint = sprite.getNextMove();
			if(nextPoint != null){
				g.drawString("1("+nextPoint.x+", "+nextPoint.y+")", (float) nextPoint.x, (float) nextPoint.y); 
				g.drawLine(sprite.getX(), sprite.getY(), nextPoint.x, nextPoint.y);
			}
			int idx = 0;
			for(Point p : sprite.getQueuedMoves()){
				g.drawString(idx+2+"("+p.x+", "+p.y+")", (float) p.x, (float) p.y); 
				if(lastPoint != null){
					g.drawLine(lastPoint.x, lastPoint.y, p.x, p.y);
				}else if(nextPoint != null){
					g.drawLine(nextPoint.x, nextPoint.y, p.x, p.y);
				}
				lastPoint = p;
				idx++;
			}
		}
		debug.render(g);
		g.dispose();
	}

}