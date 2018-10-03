package tanksgame;

import engine.*;
import processing.core.PApplet;

import java.util.ArrayList;

public class Shell extends ForceInfluencedObject implements Drawable, CollidableObject {
	private static final int xdim = 8;
	private static final int ydim = 8;

	Shell(String id, PApplet parent, int xpos, int ypos, int mass, float firingAngle, float firingStrength) {
		super(id, parent, xpos, ypos, xdim, ydim, mass, true);
		
		Force firingForce = new Force(firingStrength*Math.cos(firingAngle), firingStrength*Math.sin(firingAngle));
		this.addForce(firingForce);
	}


    @Override
    public void draw() {
        parent.ellipse(getXpos(), getYpos(), getXdim(), getYdim());
    }


    @Override
    public <T extends GameObject> int collide(ArrayList<T> collideableObjects) {
	    for(int i = 0; i < collideableObjects.size(); i++) {
	        T object = collideableObjects.get(i);

            if(CollisionDetection.checkRectangleCollision(getPosition(), getDimensions(),
                    object.getPosition(), object.getDimensions())) {
                return i;
            }
        }
        return CollisionDetection.offScreenExcludingTop(parent.width, parent.height, position, dimensions)
                ? OFF_SCREEN : NO_COLLISION;
    }
}
