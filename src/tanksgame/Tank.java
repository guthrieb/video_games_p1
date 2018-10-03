package tanksgame;

import ai.AI;
import engine.CollidableObject;
import engine.Drawable;
import engine.Force;
import engine.ForceInfluencedObject;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Tank extends ForceInfluencedObject implements Drawable, CollidableObject {
    private static final int NO_OF_BOOSTER_FLAMES = 4;
    private static final int TANK_MOVEMENT_POWER = 100;
    private boolean isDrivingLeft = false;
    private boolean isDrivingRight = false;
    private boolean isRotatingLeft = false;
    private boolean isRotatingRight = false;
    private boolean isPoweringDown = false;
    private boolean isPoweringUp = false;
    private int score = 0;
    private boolean aiControlled;
    private AI ai;

    private static final double rotationSpeed = 0.01;

    private static final int BARREL_LENGTH = 30;

    private float firingPower = 20;
    private float firingAngle;
    private int fuel;
    private final int maxFuel;
    private boolean isBoosting;
    private final int colour;

    public void startRotatingLeft() {
        isRotatingLeft = true;
    }

    public void stopRotatingLeft() {
        isRotatingLeft = false;
    }

    public void startRotatingRight() {
        isRotatingRight = true;
    }

    public void stopRotatingRight() {
        isRotatingRight = false;
    }

    public void startPoweringDown() {
        isPoweringDown = true;
    }

    public void stopPoweringDown() {
        isPoweringDown = false;
    }

    public void startPoweringUp() {
        isPoweringUp = true;
    }

    public void stopPoweringUp() {
        isPoweringUp = false;
    }

    public void startDrivingLeft() {
        isDrivingLeft = true;
    }

    public void startDrivingRight() {
        isDrivingRight = true;
    }

    public void stopDrivingLeft() {
        isDrivingLeft = false;
    }

    public void stopDrivingRight() {
        isDrivingRight = false;
    }

    public Tank(String id, boolean aiControlled, int aiDifficulty, PApplet parent, int xpos, int ypos, int xdim, int ydim, float firingAngle, double mass,
                float dampingRate, int maxFuel, int initialFuel, int colour) {
        super(id, parent, xpos, ypos, xdim, ydim, mass, dampingRate, true);
        this.id = id;
        this.firingAngle = firingAngle;
        this.maxFuel = maxFuel;
        this.fuel = initialFuel;
        this.colour = colour;
        this.aiControlled = aiControlled;
        if(aiControlled) {
            this.ai = new AI(parent, this, aiDifficulty);
        }
    }

    public Tank(String id, boolean aiControlled, PApplet parent, int xpos, int ypos, int xdim, int ydim, float firingAngle, double mass,
                float dampingRate, int maxFuel, int initialFuel, int colour) {
        super(id, parent, xpos, ypos, xdim, ydim, mass, dampingRate, true);
        this.id = id;
        this.firingAngle = firingAngle;
        this.maxFuel = maxFuel;
        this.fuel = initialFuel;
        this.colour = colour;
        this.aiControlled = aiControlled;
        if(aiControlled) {
            this.ai = new AI(parent, this, 0);
        }
    }

    public Tank copy() {
        return new Tank(id, aiControlled, ai.getDifficulty(), parent, (int) getXpos(), (int) getYpos(), (int) getXdim(), (int) getYdim(), firingAngle, mass, dampingRate, maxFuel, fuel, colour);
    }

    public float getFiringAngle() {
        return firingAngle;
    }

    private void drive(int movementPower) {
        if (isDrivingLeft) {
            addForce(new Force(- movementPower, 0));
        }

        if (isDrivingRight) {
            addForce(new Force(movementPower, 0));
        }
    }

    public void startBoosting() {
        if (fuel > 0) {
            isBoosting = true;
        }
    }

    public void stopBoosting() {
        isBoosting = false;
    }

    private void boost() {
        if (isBoosting && fuel > 0) {
            addForce(new Force(0, -1200));
            fuel--;
        }
    }

    public void update() {
        drive(TANK_MOVEMENT_POWER);
        boost();
        rotateCannon(GameWorld.ROTATION_LIMIT);
        handlePower();
    }

    private void handlePower() {
        if (isPoweringUp) {
            powerUp();
        }

        if (isPoweringDown) {
            powerDown();
        }
    }

    private void rotateCannon(float rotationLimit) {
        if (isRotatingLeft && firingAngle < (Math.PI + rotationLimit)) {
            firingAngle += rotationSpeed;
        }

        if (isRotatingRight && firingAngle > (0 - rotationLimit)) {
            firingAngle -= rotationSpeed;
        }
    }

    public Shell fire(int shellMass) {
        PVector cannonBase = cannonBase();
        PVector cannonEnd = cannonEnd(cannonBase, BARREL_LENGTH);

        endTurn();
        return new Shell("shell", parent, (int) cannonEnd.x, (int) cannonEnd.y, shellMass, (float) (2 * Math.PI - firingAngle), firingPower);
    }

    private PVector cannonBase() {
        float tankCenterX = this.getXpos() + this.getXdim() / 2;
        float tankCenterY = this.getYpos() + this.getYdim() / 2;
        return new PVector(tankCenterX, tankCenterY);
    }

    private PVector cannonEnd(PVector cannonBase, int cannonLength) {
        return new PVector(cannonBase.x + (float) Math.cos(firingAngle) * cannonLength,
                cannonBase.y - (float) Math.sin(firingAngle) * cannonLength);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Tank.class) {
            return false;
        }

        Tank tankObj = (Tank) obj;

        return tankObj.id.equals(this.id);
    }

    private void drawCannon(PApplet parent, float xpos, float ypos, float angle, float length) {
        parent.ellipseMode(PConstants.CENTER);
        parent.ellipse(xpos, ypos, 10, 10);
        parent.strokeWeight(3);
        parent.line(xpos, ypos, xpos + PApplet.cos(angle) * length, ypos - PApplet.sin(angle) * length);
        parent.strokeWeight(1);
    }

    private void powerDown() {
        if (firingPower > GameWorld.MINIMUM_FIREPOWER) {
            firingPower -= 0.2;
        }
    }

    public String getId() {
        return id;
    }

    private void powerUp() {
        if (firingPower < GameWorld.MAXIMUM_FIREPOWER) {
            firingPower += 0.2;
        }
    }

    public AI getAi() {
        return ai;
    }

    public void endTurn() {
        stopBoosting();

        isPoweringDown = false;
        isPoweringUp = false;
        isRotatingLeft = false;
        isRotatingRight = false;
        isDrivingLeft = false;
        isDrivingRight = false;
    }

    @Override
    public void draw() {
        parent.rectMode(PConstants.CORNER);
        if (isBoosting && fuel > 0) {
            parent.fill(255, 0, 0);
            parent.ellipseMode(PConstants.CORNER);

            float flameYPosition = getYpos() + 3 * getDimensions().y / 4;
            parent.ellipse(getXpos(), flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 10);
            parent.ellipse(getXpos() + getXdim() / 4, flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 10);
            parent.ellipse(getXpos() + getXdim() / 2, flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 10);
            parent.ellipse(getXpos() + 3 * getXdim() / 4, flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 10);

            parent.fill(255, 255, 0);
            parent.ellipse(getXpos(), flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 6);
            parent.ellipse(getXpos() + getXdim() / 4, flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 6);
            parent.ellipse(getXpos() + getXdim() / 2, flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 6);
            parent.ellipse(getXpos() + 3 * getXdim() / 4, flameYPosition, getXdim() / NO_OF_BOOSTER_FLAMES, 6);
        }
        parent.fill(colour);

        parent.rect(getXpos(), getYpos(), getXdim(), getYdim());
        PVector firingVector = PVector.fromAngle(getFiringAngle());
        float tankCenterX = getXpos() + getXdim() / 2;
        float tankCenterY = getYpos() + getYdim() / 2;
        PVector centerVector = new PVector(tankCenterX, tankCenterY);
        firingVector.add(centerVector);

        drawCannon(parent, tankCenterX, tankCenterY, getFiringAngle(), BARREL_LENGTH);
    }


    public float getFiringPower() {
        return firingPower;
    }

    public int getCurrentFuel() {
        return fuel;
    }

    public int getMaxFuel() {
        return maxFuel;
    }

    public void refuel(double fuelPercentage) {
        fuel += (int) (((double) (maxFuel)) * (fuelPercentage / 100));
        if (fuel > maxFuel) {
            fuel = maxFuel;
        }
    }

    public int getColour() {
        return colour;
    }

    public boolean isAiControlled() {
        return aiControlled;
    }
}
