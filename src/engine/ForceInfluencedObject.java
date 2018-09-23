package engine;

import processing.core.PApplet;
import processing.core.PVector;
import tanksgame.TestingEnvironment;

import java.util.ArrayList;
import java.util.Collection;

public class ForceInfluencedObject extends GameObject {
    protected final PVector velocity;
    private ArrayList<Force> forces = new ArrayList<>();
    private double mass;

    public ForceInfluencedObject(PApplet parent, int xpos, int ypos, int xdim, int ydim, int mass, boolean destructible) {
        super(parent, xpos, ypos, xdim, ydim, destructible);
        this.velocity = new PVector(0, 0);
        this.mass = mass;
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

    public void addDrag(float k1, float k2) {
        PVector force = velocity.get() ;

        float dragCoeff = force.mag() ;
        dragCoeff = k1 * dragCoeff +
                k2 * dragCoeff * dragCoeff ;

        force.normalize() ;
        force.mult(-dragCoeff) ;
        addForce(new Force(force.x, force.y));
    }

    public void addGravity(float gravitationalConstant) {
        addForce(new Force(0, gravitationalConstant * mass));
    }

    public void addForces(Collection<Force> toAdd) {
        forces.addAll(toAdd);
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
        if(Math.abs(totalForce.getX()/mass) > 0.000001){
            velocity.x = (float) (velocity.x + totalForce.getX() / mass);
        }

        if(Math.abs(totalForce.getY()/mass) > 0.000001){
            velocity.y = (float) (velocity.y + totalForce.getY() / mass);
        }
    }

    public void move() {
        addDrag(TestingEnvironment.k1, TestingEnvironment.k2);
        System.out.println(velocity);
        updateVelocity();
        getPosition().x = getPosition().x + velocity.x;
        getPosition().y = getPosition().y + velocity.y;
    }
}
