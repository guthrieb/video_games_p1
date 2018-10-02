package engine;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;


public class GameObject implements Drawable {
    protected String id;
	protected PVector position;
	protected PVector dimensions;
	protected final PApplet parent;
    private boolean destructible;

    GameObject(String id, PApplet parent, int xpos, int ypos, int xdim, int ydim, boolean destructible) {
        this.id = id;
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

    public void setDestructible(boolean destructible) {
		this.destructible = destructible;
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

    @Override
    public void draw() {
        parent.rectMode(PConstants.CORNER);
        parent.fill(255, 0, 0);
        parent.rect(position.x, position.y, dimensions.x, dimensions.y);
    }

    @Override
    public boolean equals(Object obj) {
	    if(obj.getClass() != GameObject.class) {
	        return false;
        }

        GameObject gameObject = (GameObject) obj;

	    return id.equals(gameObject.id);
    }

    protected String getId() {
        return id;
    }
}
