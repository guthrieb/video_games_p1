package engine;

import processing.core.PApplet;
import processing.core.PVector;


public class GameObject {
	protected PVector position;
	protected PVector dimensions;
	protected PApplet parent;
    private final boolean destructible;

	public GameObject(PApplet parent, int xpos, int ypos, int xdim, int ydim, boolean destructible) {
	    this.parent = parent;
	    this.destructible = destructible;
		this.setPosition(new PVector(xpos, ypos));
		this.setDimensions(new PVector(xdim, ydim));
    }
	
	public float getXpos() {
		return getPosition().x;
	}
	
	public float getYpos() {
		return getPosition().y;
	}
	
	public float getXdim() {
		return getDimensions().x;
	}
	
	public float getYdim() {
		return getDimensions().y;
	}

    public boolean isDestructible() {
        return destructible;
    }
	

	@Override
	public String toString() {
		return getPosition() + " " + getDimensions();
	}

	public PVector getPosition() {
		return position;
	}

	private void setPosition(PVector position) {
		this.position = position;
	}

	public PVector getDimensions() {
		return dimensions;
	}

	private void setDimensions(PVector dimensions) {
		this.dimensions = dimensions;
	}
}
