package tanksgame;

import engine.*;
import processing.core.PApplet;

public class Shell extends ForceInfluencedObject implements Drawable, CollidableObject {
	private static final int xdim = 5;
	private static final int ydim = 2;

	Shell(PApplet parent, int xpos, int ypos, int mass, float firingAngle, float firingStrength) {
		super(parent, xpos, ypos, xdim, ydim, mass, true);
		
		Force firingForce = new Force(firingStrength*Math.cos(firingAngle), firingStrength*Math.sin(firingAngle));
		this.addForce(firingForce);
	}


    @Override
    public void draw() {
        parent.rect(getXpos(), getYpos(), getXdim(), getYdim());
    }

    @Override
    public boolean collide() {
        for(int i = 0; i < TestingEnvironment.tanks.size(); i++){
            Tank player = TestingEnvironment.tanks.get(i);

            if(CollisionDetection.checkRectangleCollision(getPosition(), getDimensions(),
                    player.getPosition(), player.getDimensions())) {
                TestingEnvironment.tanks.remove(i);
                return true;
            }
        }

        for(int i = 0; i < TestingEnvironment.staticObjects.size(); i++){
            GameObject object = TestingEnvironment.staticObjects.get(i);

            if(CollisionDetection.checkRectangleCollision(getPosition(), getDimensions(),
                    object.getPosition(), object.getDimensions())) {
                if(object.isDestructible()){
                    TestingEnvironment.staticObjects.remove(i);
                }
                return true;
            }
        }

        return CollisionDetection.offScreen(position, dimensions, parent);

    }
}
