package ai;

import tanksgame.EnvironmentBlock;
import tanksgame.FiringSimulator;
import tanksgame.Tank;

import java.util.ArrayList;

public class AI {
    private FiringSimulator simulator;
    private final Tank tank;
    private boolean readyToFire = false;

    public AI(Tank toControl) {
        this.tank = toControl;
    }

    public boolean isReadyToFire() {
        return readyToFire;
    }

    public void beginCalculations(Tank tank, ArrayList<Tank> tanks, ArrayList<EnvironmentBlock> blocks, float windStrength, float gravityStrength) {
        readyToFire = false;
        simulator = new FiringSimulator(tank, tanks, blocks, windStrength, gravityStrength);
        Thread simThread = new Thread(simulator);
        simThread.start();
    }

    public void makeMovements() {
        if (simulator.isSimulationComplete()) {
            float targetAngle;
            float targetPower;

            targetAngle = simulator.getTargetAngle();
            targetPower = simulator.getTargetPower();

            tank.endTurn();
            float epsilon = 0.000001f;
            if (Math.abs(targetPower - tank.getFiringPower()) < epsilon) {
                tank.stopPoweringDown();
                tank.stopPoweringUp();
            } else if (targetPower > tank.getFiringPower()) {
                tank.startPoweringUp();
            } else if (targetPower < tank.getFiringPower()) {
                tank.startPoweringDown();
            }

            if (Math.abs(targetAngle - tank.getFiringAngle()) < epsilon) {
                tank.stopRotatingLeft();
                tank.stopRotatingRight();
            } else if (targetAngle > tank.getFiringAngle()) {
                tank.startRotatingLeft();
            } else if (targetAngle < tank.getFiringAngle()) {
                tank.startRotatingRight();
            }

            if (Math.abs(targetPower - tank.getFiringPower()) < epsilon
                    && Math.abs(targetAngle - tank.getFiringAngle()) < epsilon) {
                readyToFire = true;
            }
        }
    }

}