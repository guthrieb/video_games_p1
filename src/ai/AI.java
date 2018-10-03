package ai;

import processing.core.PApplet;
import processing.core.PVector;
import tanksgame.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TreeMap;

public class AI {
    private static final int TARGET_DISTANCE_LIMIT = 300;
    private final PApplet parent;
    private final int difficulty;
    private FiringSimulator simulator;
    private Tank tank;
    private boolean readyToFire = false;
    private boolean simulationBegun = false;
    private static final float MAX_DIFFICULTY_EPSILON = 0.000001f;
    private static final float MEDIUM_DIFFICULTY_EPSILON = (float) (0.05);
    private static final float EASY_DIFFICULTY_EPSILON = (float) (0.1);
    private float epsilon;

    public AI(PApplet parent, Tank toControl, int difficulty) {
        this.tank = toControl;
        this.parent = parent;

        this.difficulty = difficulty;
        switch (difficulty) {
            case 0:
                epsilon = EASY_DIFFICULTY_EPSILON;
                break;
            case 1:
                epsilon = MEDIUM_DIFFICULTY_EPSILON;
                break;
            case 2:
                epsilon = MAX_DIFFICULTY_EPSILON;
                break;
        }
    }

    public boolean isReadyToFire() {
        return readyToFire;
    }

    public void beginCalculations(Tank tank, Tank targetTank, ArrayList<Tank> tanks, ArrayList<EnvironmentBlock> blocks, float windStrength, float gravityStrength) {
        readyToFire = false;

        simulator = new FiringSimulator(tank.copy(), targetTank.copy(), tanks, blocks, windStrength, gravityStrength);
        Thread simThread = new Thread(simulator);
        simThread.start();
    }

    public void makeMove(ArrayList<Tank> tanks, ArrayList<EnvironmentBlock> blocks, int timeLeft, float windStrength, float gravityStrength) {
        PVector reachablePoint;
        Tank targetTank = getClosestTank(tank, tanks);
        boolean right = targetTank.getXpos() > tank.getXpos();


        if (timeLeft < 10000 || distanceToTarget(targetTank) < TARGET_DISTANCE_LIMIT) {
            tank.stopDrivingLeft();
            tank.stopDrivingRight();
            tank.stopBoosting();
            System.out.println(tank.velocity.mag());
            if(!simulationBegun && (tank.velocity.mag() < 0.01f)) {
                System.out.println("Beginning Calculations");
                beginCalculations(tank, targetTank, tanks, blocks, windStrength, gravityStrength);
                simulationBegun = true;
            }
        } else if (right) {
            System.out.println(getNearestReachableSurfaceInDirection(blocks, right) != tank.getPosition());
            if ((reachablePoint = getNearestReachableSurfaceInDirection(blocks, right)) != tank.getPosition()) {
                tank.startDrivingRight();
                if (reachablePoint.y - tank.getYdim() < tank.getYpos()) {
                    tank.startBoosting();
                } else {
                    tank.stopBoosting();
                }
            } else {
                tank.stopDrivingRight();
            }
        } else {
            if ((reachablePoint = getNearestReachableSurfaceInDirection(blocks, right)) != tank.getPosition()) {
                tank.startDrivingLeft();
                if (reachablePoint.y - tank.getYdim() < tank.getYpos()) {
                    tank.startBoosting();
                } else {
                    tank.stopBoosting();
                }
            } else {
                tank.stopDrivingLeft();
            }
        }

        if (simulationBegun && simulator.isSimulationComplete()) {
            float targetAngle;
            float targetPower;

            targetAngle = simulator.getTargetAngle();
            targetPower = simulator.getTargetPower();

            tank.endTurn();

            float turnEpsilon = parent.random(epsilon);

            if (Math.abs(targetPower - tank.getFiringPower()) < turnEpsilon) {
                tank.stopPoweringDown();
                tank.stopPoweringUp();
            } else if (targetPower > tank.getFiringPower()) {
                tank.startPoweringUp();
            } else if (targetPower < tank.getFiringPower()) {
                tank.startPoweringDown();
            }

            if (Math.abs(targetAngle - tank.getFiringAngle()) < turnEpsilon) {
                tank.stopRotatingLeft();
                tank.stopRotatingRight();
            } else if (targetAngle > tank.getFiringAngle()) {
                tank.startRotatingLeft();
            } else if (targetAngle < tank.getFiringAngle()) {
                tank.startRotatingRight();
            }



            if (Math.abs(targetPower - tank.getFiringPower()) < turnEpsilon
                    && Math.abs(targetAngle - tank.getFiringAngle()) < turnEpsilon) {
                readyToFire = true;
                simulationBegun = false;
            }
        }
    }

    private float distanceToTarget(Tank target) {
        return tank.getPosition().dist(target.getPosition());
    }

    public Shell fire(int shellMass) {
        readyToFire = false;
        return tank.fire(shellMass);
    }

    public float calculateBoostHeight() {
        Tank tankSimulation = tank.copy();
        tankSimulation.startBoosting();

        float prevY = Float.MAX_VALUE;
        while(tankSimulation.getCurrentFuel() > 0 || prevY > tankSimulation.getYpos()) {
            tankSimulation.addGravity(GameWorld.GRAVITATIONAL_CONSTANT);
            tankSimulation.update();
            tankSimulation.move();
            prevY = tankSimulation.getYpos();
        }

        return tankSimulation.getYpos();
    }

    public PVector getNearestReachableSurfaceInDirection(ArrayList<EnvironmentBlock> blocks, boolean right) {
        float boostHeight = calculateBoostHeight();

        TreeMap<Float, Float> highestPoints = new TreeMap<>();

        for(EnvironmentBlock block : blocks) {

            if(right && block.getXpos() > tank.getXpos()) {
                if (!highestPoints.containsKey(block.getXpos()) || highestPoints.get(block.getXpos()) > block.getYpos()){
                    highestPoints.put(block.getXpos(), block.getYpos());
                }
            } else if (!right && block.getXpos() < tank.getXpos()) {
                if (!highestPoints.containsKey(block.getXpos()) || highestPoints.get(block.getXpos()) > block.getYpos()){
                    highestPoints.put(block.getXpos(), block.getYpos());
                }
            }
        }


        PVector target = null;
        if(right && highestPoints.firstEntry().getValue() > boostHeight){
            target = new PVector(highestPoints.firstEntry().getKey(), highestPoints.firstEntry().getValue());
        } else if (!right && highestPoints.lastEntry().getValue() > boostHeight) {
            target = new PVector(highestPoints.lastEntry().getKey(), highestPoints.lastEntry().getValue());
        }

        if(target != null) {
            if(scoreReachableSurface(target, highestPoints, right) < 0){
                return target;
            }
        }

        return tank.getPosition();
    }

    public float scoreReachableSurface(PVector point, TreeMap<Float, Float> highestPoints, boolean right) {
        float score = 0;

        score += tank.getYpos() - point.y;


        if(!right){
            Map.Entry<Float, Float> leftNeighbour = highestPoints.lowerEntry(point.x);
            score += point.y - leftNeighbour.getValue();
        } else {
            Map.Entry<Float, Float> rightNeighbour = highestPoints.higherEntry(point.x);
            score += point.y - rightNeighbour.getValue();
        }


        score -= (float)tank.getCurrentFuel()/ (float)tank.getMaxFuel() * 200f;
        return score;
    }

    private Tank getClosestTank(Tank toFire, ArrayList<Tank> tanks) {
        float closestDistance = Float.MAX_VALUE;
        Tank closestTank = null;
        for (Tank tank : tanks) {
            if (!tank.getId().equals(toFire.getId())) {
                float distance = toFire.getPosition().dist(tank.getPosition());

                if (distance < closestDistance) {
                    closestTank = tank;
                    closestDistance = distance;
                }
            }
        }

        return closestTank;
    }

    public int getDifficulty() {
        return difficulty;
    }
}