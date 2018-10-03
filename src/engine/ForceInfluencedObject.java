package engine;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collection;

public class ForceInfluencedObject extends GameObject implements CollidableObject {
    public final PVector velocity;
    private ArrayList<Force> forces = new ArrayList<>();
    protected final double mass;
    protected float dampingRate = 1;

    protected ForceInfluencedObject(String id, PApplet parent, int xpos, int ypos, int xdim, int ydim, int mass, boolean destructible) {
        super(id, parent, xpos, ypos, xdim, ydim, destructible);
        this.velocity = new PVector(0, 0);
        this.mass = mass;
    }

    protected ForceInfluencedObject(String id, PApplet parent, int xpos, int ypos, int xdim, int ydim, double mass,
                                    float dampingRate, boolean destructible) {
        super(id, parent, xpos, ypos, xdim, ydim, destructible);
        this.velocity = new PVector(0, 0);
        this.mass = mass;
        this.dampingRate = dampingRate;
    }

    public float getXpos() {
        return getPosition().x;
    }

    public float getYpos() {
        return getPosition().y;
    }

    public void addForce(Force toAdd) {
        forces.add(toAdd);
    }

    public void addGravity(float gravitationalConstant) {
        addForce(new Force(0, gravitationalConstant * mass));
    }

    public void addForces(Collection<Force> toAdd) {
        forces.addAll(toAdd);
    }

    public void addDrag(float k1, float k2) {
        PVector force = velocity.get();

        float dragCoeff = force.mag();
        dragCoeff = k1*dragCoeff + k2*dragCoeff*dragCoeff;

        force.normalize();
        force.mult(-dragCoeff);
        addForce(new Force(force.x, force.y));
    }


    private Force getTotalForces() {
        double x = 0, y = 0;

        for (Force force : forces) {
            x += force.getX();
            y += force.getY();
        }

        forces = new ArrayList<>();
        return new Force(x, y);
    }

    private void updateVelocity() {
        Force totalForce = getTotalForces();
        if (Math.abs(totalForce.getX() / mass) > 0.000001) {
            velocity.x = (float) (velocity.x + totalForce.getX() / mass);
        }

        if (Math.abs(totalForce.getY() / mass) > 0.000001) {
            velocity.y = (float) (velocity.y + totalForce.getY() / mass);
        }
    }

    public void move() {
        updateVelocity();

        getPosition().x = getPosition().x + velocity.x;
        getPosition().y = getPosition().y + velocity.y;
        velocity.mult(dampingRate);
    }

    private void snapToObject(GameObject block, CollisionDetection.Direction directionOfCollision) {
        if (directionOfCollision == CollisionDetection.Direction.BOTTOM) {
            position.y = block.getPosition().y - dimensions.y;
            velocity.y = 0;
        }

        if (directionOfCollision == CollisionDetection.Direction.TOP) {
            position.y = block.getPosition().y;
            velocity.y = 0;
        }

        if (directionOfCollision == CollisionDetection.Direction.LEFT) {
            position.x = block.getPosition().x - dimensions.x;
            velocity.x = 0;
        }

        if (directionOfCollision == CollisionDetection.Direction.RIGHT) {
            position.x = block.getPosition().x + block.getDimensions().x;
            velocity.x = 0;
        }
    }

    @Override
    public <T extends GameObject> int collide(ArrayList<T> collidableObjects) {
        for (T collidableObject : collidableObjects) {
            GameObject object = collidableObject;

            if (object != this && CollisionDetection.checkRectangleCollision(position, dimensions, object.getPosition(), object.getDimensions())) {

                CollisionDetection.Direction directionOfCollision = CollisionDetection.getRectangleCollisionDirection(
                        position, dimensions, object.getPosition(), object.getDimensions());


                snapToObject(object, directionOfCollision);
            }
        }

        CollisionDetection.Direction direction = CollisionDetection.offScreenDirection(parent.width, parent.height, position, dimensions);
        if (direction == CollisionDetection.Direction.LEFT) {
            position.x = 0;
            velocity.x = 0;
            return OFF_SCREEN;
        }

        if (direction == CollisionDetection.Direction.RIGHT) {
            position.x = parent.width - dimensions.x;
            velocity.x = 0;
            return OFF_SCREEN;
        }

        if(direction == CollisionDetection.Direction.TOP) {
            position.y = 0;
            velocity.y = 0;
            return OFF_SCREEN;
        }

        if(direction == CollisionDetection.Direction.BOTTOM) {
            position.y = parent.height - dimensions.y;
            velocity.y = 0;
            return OFF_SCREEN;
        }

        return NO_COLLISION;
    }


    public float distanceBetweenOnXAxis(GameObject interaction) {
        float distanceGuessOne = (interaction.position.x + interaction.dimensions.x) - position.x;
        float distanceGuessTwo = interaction.position.x - (position.x + dimensions.x);

        if (Math.abs(distanceGuessOne) > Math.abs(distanceGuessTwo)) {
            return distanceGuessTwo;
        }

        return distanceGuessOne;
    }

    public float distanceBetweenOnYAxis(GameObject interaction) {
        float distanceGuessOne = interaction.position.y - (position.y + dimensions.y);
        float distanceGuessTwo = (interaction.position.y + interaction.dimensions.y) - position.y;

        if (Math.abs(distanceGuessOne) > Math.abs(distanceGuessTwo)) {
            return distanceGuessTwo;
        }

        return distanceGuessOne;
    }
}
