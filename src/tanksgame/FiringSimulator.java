package tanksgame;

import engine.CollidableObject;
import engine.Force;
import processing.core.PVector;

import java.util.ArrayList;

public class FiringSimulator implements Runnable {
    private float targetAngle;
    private boolean simulationComplete = false;
    private final Tank toFire;
    private final Tank targetTank;
    private final ArrayList<Tank> tanks;
    private final ArrayList<EnvironmentBlock> environmentBlocks;
    private final float windStrength;
    private final float gravitationalConstant;
    private float targetPower;

    public boolean isSimulationComplete() {
        return simulationComplete;
    }

    public FiringSimulator(Tank toFire, Tank targetTank, ArrayList<Tank> tanks, ArrayList<EnvironmentBlock> environmentBlocks,
                           float windStrength, float gravitationalConstant) {
        this.toFire = toFire;
        this.targetTank = targetTank;
        this.tanks = tanks;
        this.environmentBlocks = environmentBlocks;
        this.windStrength = windStrength;
        this.gravitationalConstant = gravitationalConstant;
    }

    public float getTargetAngle() {
        return targetAngle;
    }

    public float getTargetPower() {
        return targetPower;
    }

    private PVector getBestFiringAngle() {
        ArrayList<EnvironmentBlock> cloneEnvironment = (ArrayList<EnvironmentBlock>) environmentBlocks.clone();

        for (EnvironmentBlock block : cloneEnvironment) {
            block.setDestructible(false);
        }

        while (toFire.getFiringPower() < GameWorld.MAXIMUM_FIREPOWER) {
            toFire.startPoweringUp();
            toFire.update();
            toFire.stopPoweringUp();
        }


        float closestShot;

        closestShot = Float.MAX_VALUE;
        float closestAngle = 0;
        float closestPower = 0;


        while (toFire.getFiringAngle() < Math.PI + GameWorld.ROTATION_LIMIT) {
            toFire.startRotatingLeft();
            toFire.update();
            toFire.stopRotatingLeft();
        }

        while (toFire.getFiringPower() > 1) {

            toFire.startPoweringDown();
            toFire.update();
        }
        toFire.stopPoweringDown();


        while (toFire.getFiringAngle() > 0 - GameWorld.ROTATION_LIMIT) {
            while (toFire.getFiringPower() < GameWorld.MAXIMUM_FIREPOWER) {
                Shell shell = toFire.fire(2);

                while (shell.collide(tanks) == CollidableObject.NO_COLLISION && shell.collide(cloneEnvironment) == CollidableObject.NO_COLLISION) {
                    shell.addGravity(gravitationalConstant);
                    shell.addForce(new Force(windStrength, 0));
                    shell.addDrag(GameWorld.K_1, GameWorld.K_2);

                    shell.move();
                }

                float distance = shell.getPosition().dist(targetTank.getPosition().get().add(targetTank.getXdim() / 2, targetTank.getYdim() / 2));

                if (distance < closestShot) {
                    closestShot = distance;
                    closestAngle = toFire.getFiringAngle();
                    closestPower = toFire.getFiringPower();
                }

                toFire.startPoweringUp();
                toFire.update();
                toFire.stopPoweringUp();
            }

            while (toFire.getFiringPower() > 1) {
                toFire.startPoweringDown();
                toFire.update();
            }
            toFire.stopPoweringDown();

            toFire.startRotatingRight();
            toFire.update();
            toFire.stopRotatingRight();
        }

        for (EnvironmentBlock block : cloneEnvironment) {
            block.setDestructible(true);
        }

        return new PVector(closestAngle, closestPower);
    }

    @Override
    public void run() {
        PVector result = getBestFiringAngle();
        targetAngle = result.x;
        targetPower = result.y;
        simulationComplete = true;
    }

}
