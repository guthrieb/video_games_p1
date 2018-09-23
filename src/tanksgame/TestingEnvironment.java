package tanksgame;

import engine.Force;
import engine.ForceInfluencedObject;
import processing.core.PApplet;

import java.util.ArrayList;

public class TestingEnvironment extends PApplet {

    public static final int SCREEN_HEIGHT = 500;
    public static final int SCREEN_WIDTH = 1000;
    public static final float GRAVITATIONAL_CONSTANT = 0.4f;
    private static final ArrayList<Force> constantForces = new ArrayList<>();
    private static final int FLOOR_HEIGHT = 30;
    private static final ArrayList<ForceInfluencedObject> objects = new ArrayList<>();
    public static final ArrayList<EnvironmentBlock> staticObjects = new ArrayList<>();
    public static final ArrayList<Tank> tanks = new ArrayList<>();
    private static final ArrayList<Shell> shells = new ArrayList<>();
    public static final int BLOCK_HEIGHT = 15;
    public static final int BLOCK_WIDTH = 15;
    private static final int TERRAIN_ENDING_POSITION = 800;
    private static final int TERRAIN_STARTING_POSITION = 200;
    private static final int STARTING_HEIGHT = 200;
    private static final int SHELL_MASS = 2;
    private static final int TANK_MASS = 500;
    public static float k2 = 0.1f;
    public static float k1 = 0.1f;
    private int currentTank = 0;
    private static final int TANK_WIDTH = 25;
    private static final int TANK_HEIGHT = 15;

    private enum GameState {
        PLAYER_MOVING, PLAYER_FIRED, GAME_OVER;
    }
    GameState state = GameState.PLAYER_MOVING;
    GameInfo info;

    public static void main(String[] args) {
        PApplet.main("tanksgame.TestingEnvironment");
    }

    public void settings() {
        size(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public void setup() {
//        constantForces.add(new Force(0.02, 0));
        info = new GameInfo(this);
        EnvironmentBlock bedrock = new EnvironmentBlock(this, 0, SCREEN_HEIGHT - FLOOR_HEIGHT,
                SCREEN_WIDTH, FLOOR_HEIGHT, false, EnvironmentBlock.MaterialType.STONE);
        staticObjects.add(bedrock);

        generateTerrain();

        tanks.add(new Tank(this, "p1", 100, SCREEN_HEIGHT - FLOOR_HEIGHT - TANK_HEIGHT - STARTING_HEIGHT, TANK_WIDTH, TANK_HEIGHT, 0, TANK_MASS));
        tanks.add(new Tank(this, "p2", 900, SCREEN_HEIGHT - FLOOR_HEIGHT - TANK_HEIGHT - STARTING_HEIGHT, TANK_WIDTH, TANK_HEIGHT, (float) Math.PI, TANK_MASS));
    }

    private void generateTerrain() {
        for(int i = TERRAIN_STARTING_POSITION; i <= TERRAIN_ENDING_POSITION; i += BLOCK_WIDTH){
            int seed = Math.round(random(5, 15));
            System.out.println(seed);
            for(int j = 1; j <= seed; j++){
                EnvironmentBlock.MaterialType type;

                if(j < seed) {
                    type = EnvironmentBlock.MaterialType.DIRT;
                } else {
                    type = EnvironmentBlock.MaterialType.GRASS;
                }

                staticObjects.add(new EnvironmentBlock(this, i,
                        SCREEN_HEIGHT - FLOOR_HEIGHT - BLOCK_HEIGHT*j, BLOCK_WIDTH, BLOCK_HEIGHT, true,
                        type));
            }
        }
    }

    public void keyReleased() {
        if (state == GameState.PLAYER_MOVING) {
            if (key == 'a') {
                tanks.get(currentTank).stopDrivingLeft();
            }

            if (key == 'd') {
                tanks.get(currentTank).stopDrivingRight();
            }

            if (keyCode == LEFT) {
                tanks.get(currentTank).isRotatingLeft = false;
            }

            if (keyCode == RIGHT) {
                tanks.get(currentTank).isRotatingRight = false;
            }

            if (keyCode == UP) {
                tanks.get(currentTank).isPoweringUp = false;
            }

            if (keyCode == DOWN) {
                tanks.get(currentTank).isPoweringDown = false;
            }
        }
    }

    public void keyPressed() {
        if (state == GameState.PLAYER_MOVING) {
            if (key == 'a') {
                tanks.get(currentTank).isDrivingLeft = true;
            }

            if (key == 'd') {
                tanks.get(currentTank).isDrivingRight = true;
            }

            if (keyCode == LEFT) {
                tanks.get(currentTank).isRotatingLeft = true;
            }

            if (keyCode == RIGHT) {
                tanks.get(currentTank).isRotatingRight = true;
            }

            if (keyCode == UP) {
                tanks.get(currentTank).isPoweringUp = true;
            }

            if (keyCode == DOWN) {
                tanks.get(currentTank).isPoweringDown = true;
            }


            if (key == ' ') {
                Shell toFire = tanks.get(currentTank).fire(SHELL_MASS);
                objects.add(tanks.get(currentTank).fire(SHELL_MASS));
                shells.add(toFire);
                state = GameState.PLAYER_FIRED;
                tanks.get(currentTank).endTurn();
            }
        }
    }

    public void draw() {
        background(135, 206, 235);
        info.draw();

        for(Tank tank : tanks){
            tank.addGravity(GRAVITATIONAL_CONSTANT);
            tank.addForces(constantForces);
            tank.move();
            tank.collide();
            tank.update();
        }

        if (state == GameState.GAME_OVER){
            textAlign(CENTER);
            textSize(30);
            fill(0, 0, 0);
            text(tanks.get(0).getId() + " WINS!", SCREEN_WIDTH/2,  SCREEN_HEIGHT/2);
        }

        for (EnvironmentBlock block : staticObjects) {
            block.draw();
        }

        for (int i = 0; i < shells.size(); ) {
            Shell shell = shells.get(i);
            shell.move();
            if (!shell.collide()) {
                shell.addGravity(GRAVITATIONAL_CONSTANT);
                shell.addForces(constantForces);
                shell.draw();
                i++;
            } else {
                shells.set(i, shells.get(shells.size() - 1));
                shells.remove(shells.size()-1);
                if(currentTank == tanks.size() - 1){
                    currentTank = 0;
                } else {
                    currentTank += 1;
                }

                if(tanks.size() == 1){
                    state = GameState.GAME_OVER;
                } else {
                    state = GameState.PLAYER_MOVING;
                }
            }
        }

        for (Tank tank : tanks) {
            tank.draw();
        }
    }


}
