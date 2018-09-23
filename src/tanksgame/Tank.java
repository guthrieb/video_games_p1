package tanksgame;

import engine.*;
import processing.core.PApplet;
import processing.core.PVector;

public class Tank extends ForceInfluencedObject implements Drawable, CollidableObject {
    boolean isDrivingLeft = false;
    boolean isDrivingRight = false;
    boolean isRotatingLeft = false;
    boolean isRotatingRight = false;
    boolean isPoweringDown = false;
    boolean isPoweringUp = false;
    private String id;

    private static final int movingSpeed = 2;
    private static final double rotationSpeed = 0.01;

    private static final int BARREL_LENGTH = 30;

    private float firingPower = 20;
    private float firingAngle;

    void stopDrivingLeft() {
        isDrivingLeft = false;
    }

    void stopDrivingRight() {
        isDrivingRight = false;
    }

    Tank(PApplet parent, String id, int xpos, int ypos, int xdim, int ydim, float firingAngle, int mass) {
        super(parent, xpos, ypos, xdim, ydim, mass, true);
        this.id = id;
        this.firingAngle = firingAngle;
    }

    public float getFiringAngle() {
        return firingAngle;
    }

    private void drive() {
        if (isDrivingLeft) {
            addForce(new Force(-10, 0));
        }

        if (isDrivingRight) {
            addForce(new Force(10, 0));
        }
    }

    void update() {
        drive();
        rotateCannon();
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

    private void rotateCannon() {
        float firingLimit = (float) (Math.PI / 10);
        if (isRotatingLeft && firingAngle < (Math.PI + firingLimit)) {
            firingAngle += rotationSpeed;
        }

        if (isRotatingRight && firingAngle > (0 - firingLimit)) {
            firingAngle -= rotationSpeed;
        }
    }

    Shell fire(int shellMass) {
        PVector cannonBase = cannonBase();
        PVector cannonEnd = cannonEnd(cannonBase, BARREL_LENGTH);

        return new Shell(parent, (int) cannonEnd.x, (int) cannonEnd.y, shellMass, (float) (2 * Math.PI - firingAngle), firingPower);
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

    private void drawCannon(PApplet parent, float f, float g, float angle, float length) {
        parent.line(f, g, f + PApplet.cos(angle) * length, g - PApplet.sin(angle) * length);
    }

    private void powerDown() {
        if (firingPower > 1) {
            firingPower -= 0.1;
        }
    }

    public String getId() {
        return id;
    }

    private void powerUp() {
        if (firingPower < 100) {
            firingPower += 0.1;
        }
    }

    public void endTurn() {
        isPoweringDown = false;
        isPoweringUp = false;
        isRotatingLeft = false;
        isRotatingRight = false;
        isDrivingLeft = false;
        isDrivingRight = false;
    }

    @Override
    public void draw() {
        parent.fill(107, 142, 35);
        parent.rect(getXpos(), getYpos(), getXdim(), getYdim());
        PVector firingVector = PVector.fromAngle(getFiringAngle());
        float tankCenterX = getXpos() + getXdim() / 2;
        float tankCenterY = getYpos() + getYdim() / 2;
        PVector centerVector = new PVector(tankCenterX, tankCenterY);
        firingVector.add(centerVector);

        drawCannon(parent, tankCenterX, tankCenterY, getFiringAngle(), BARREL_LENGTH);
    }

    @Override
    public boolean collide() {
        for (EnvironmentBlock block : TestingEnvironment.staticObjects) {
            if (CollisionDetection.checkRectangleCollision(position, dimensions, block.getPosition(), block.getDimensions())) {
                float tankBottom = position.y + dimensions.y;
                float blockBottom = block.getPosition().y + block.getDimensions().y;
                float tankRight = position.x + dimensions.x;
                float blockRight = block.getPosition().x + block.getDimensions().x;

                float bottomOverlap = blockBottom - position.y;
                float topOverlap = tankBottom - block.getPosition().y;
                float leftOverlap = tankRight - block.getPosition().x;
                float rightOverlap = blockRight - position.x;

                if (topOverlap < bottomOverlap && topOverlap < leftOverlap && topOverlap < rightOverlap) {
                    position.y = block.getPosition().y - dimensions.y;
                    velocity.y = 0;
                }

                if (bottomOverlap < topOverlap && bottomOverlap < leftOverlap && bottomOverlap < rightOverlap) {
                    position.y = block.getPosition().y;
                    velocity.y = 0;
                }

                if (leftOverlap < rightOverlap && leftOverlap < topOverlap && leftOverlap < bottomOverlap) {
                    position.x = block.getPosition().x - dimensions.x - 1;
                    velocity.x = 0;
                }

                if (rightOverlap < leftOverlap && rightOverlap < topOverlap && rightOverlap < bottomOverlap) {
                    position.x = block.getPosition().x + block.getDimensions().x;
                    velocity.x = 0;
                }
            }
        }
        return true;
    }

    public float getFiringPower() {
        return firingPower;
    }
}
