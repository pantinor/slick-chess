package chess.core;

import org.newdawn.slick.geom.Polygon;

import chess.media.Direction;

public class Block {
	
	private boolean over;
	private Direction direction = Direction.NORTH;
	private Polygon shape;
	
	public Block(int x, int y, int width, int height) {
		
		float[] points = 
        {	
	         x, y,  // upper left point
             x+width, y,  // upper right
             x+width, y+height, // lower right
             x, y+height   // lower left
		}; 
		
		this.shape = new Polygon(points);
		
		this.shape.setLocation(x, y);

	}
	
	public boolean isMouseOver(int x, int y) {
		over = shape.contains(x, y);
		return over;
	}
	
	public int getHeight() {
		return (int) (shape.getMaxY() - shape.getY());
	}


	public int getWidth() {
		return (int) (shape.getMaxX() - shape.getX());
	}
	
	public void setLocation(int x, int y) {
		setLocation((float) x,(float) y);
	}
	
	public void setLocation(float x, float y) {
		if (shape != null) {
			shape.setX(x);
			shape.setY(y);
		}
	}

	public Polygon getShape() {
		return shape;
	}

	public void setShape(Polygon shape) {
		this.shape = shape;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}



}
