package ca.tonsaker.physicsgame.engine;

import java.awt.Graphics;

public interface EngineFrame {
	
	public void init();
	public void draw(Graphics g);
	public void update();
}
