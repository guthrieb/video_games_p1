//import engine.CollidableObject;
//import engine.CollisionDetection;
//import engine.Force;
//import engine.GameObject;
//import processing.core.PApplet;
//import tanksgame.*;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class Storagepackage tanksgame;
//
//import engine.*;
//        import processing.core.PApplet;
//
//        import java.util.*;
//
//public class GameWorld extends PApplet {
//
//    private static final double TURN_REFUEL_RATE = 10;
//    public static final String MAIN_MENU_ID = "main";
//    private static final String PLAY_OPTIONS_ID = "play_options";
//    private static final float DAMPING_RATE = 0.95f;
//    public static final float STARTING_ANGLE = (float) (Math.PI / 2);
//    public static final int MINIMUM_FIREPOWER = 1;
//    public static final int MAXIMUM_FIREPOWER = 50;
//    public static final float GRAVITATIONAL_CONSTANT = 0.4f;
//
//    private static final int FLOOR_HEIGHT = 30;
//    public static final int BLOCK_WIDTH = 40;
//    public static final int SHELL_MASS = 2;
//    private static final int TANK_MASS = 500;
//    public static final int MAX_BLOCKS_HEIGHT = 10;
//    public static final int MIN_BLOCKS_HEIGHT = 2;
//    public static final int BLOCK_HEIGHT = 20;
//    private static final int TANK_WIDTH = 30;
//    private static final int TANK_HEIGHT = 15;
//    public static final float WIND_STRENGTH_LIMIT = 0.1f;
//
//    public static final ArrayList<EnvironmentBlock> environmentObjects = new ArrayList<>();
//    public static final ArrayList<Tank> tanks = new ArrayList<>();
//    private static final ArrayList<GameObject> collidableObjects = new ArrayList<>();
//
//    public static float windStrength;
//    public static int currentTank = 0;
//
//    private Shell currentShell = null;
//
//    private Menu menu;
//    private tanksgame.GameWorld.GameState state = tanksgame.GameWorld.GameState.MAIN_MENU;
//    private GameInfo info;
//    private GameOptions options = new GameOptions();
//    private ArrayList<Integer> colours = new ArrayList<>(Arrays.asList(
//            color(204, 0, 0), color(51, 51,  255), color(204, 204, 0), color(255, 108, 108), color(255, 255, 255)
//    ));
//
//    private enum GameState {
//        PLAYER_MOVING, PLAYER_FIRED, GAME_OVER, MAIN_MENU;
//    }
//
//    public static void main(String[] args) {
//        PApplet.main("tanksgame.GameWorld");
//    }
//
//    public void settings() {
//        size(1200, 700);
//        menu = new Menu(this);
//        generateMenu();
//
//    }
//
//    private void play() {
//        info = new GameInfo(this);
//
//
//        EnvironmentBlock bedrock = new EnvironmentBlock(this, 0, height - FLOOR_HEIGHT,
//                width, FLOOR_HEIGHT, false, EnvironmentBlock.MaterialType.STONE);
//        environmentObjects.add(bedrock);
//
//        generateTerrain();
//        generateTanks();
//
//        collidableObjects.addAll(tanks);
//        collidableObjects.addAll(environmentObjects);
//
//        windStrength = random(0 - WIND_STRENGTH_LIMIT, WIND_STRENGTH_LIMIT);
//
//        state = tanksgame.GameWorld.GameState.PLAYER_MOVING;
//    }
//
//    private void generateTanks() {
//        int totalTanks = options.getNumberOfTanks();
//        for(int i = 0; i < totalTanks; i++) {
//            Tank tank = new Tank(this, "P" + (i + 1), (i*(width-100)/ totalTanks) +
//                    ((width-100)/ totalTanks)/2, 0, TANK_WIDTH, TANK_HEIGHT, STARTING_ANGLE, TANK_MASS,
//                    DAMPING_RATE, 100, 100, colours.get(i));
//            tanks.add(tank);
//        }
//    }
//
//
//    public void generateMenu() {
//        menu.addMenuBox(MAIN_MENU_ID, new MenuBox(this, "begin_buttom", "Begin!", width/2, 2*height/6,
//                width/6, height/10, () -> {
//            try {
//                menu.changePage(PLAY_OPTIONS_ID);
//            } catch (InvalidPageException e) {
//                e.printStackTrace();
//            }
//        }));
//
//        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "play_button", "Play" , width/2, 2*height/6,
//                width/6, height/10, this::play));
//        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no_header", "No of tanks", width/4, 3*height/6,
//                width/6, height/10, this::play));
//
//        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no", Integer.toString(options.getNumberOfTanks()),
//                7*width/14, 3*height/6, width/14, height/14));
//        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no_decrement_button", "-", 6*width/14, 3*height/6,
//                width/14, height/14, this::decrementTankNo));
//        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this,  "tank_no_decrement_button", "+", 8*width/14, 3*height/6,
//                width/14, height/14, this::incrementTankNo));
//    }
//
//    public void incrementTankNo() {
//        options.incrementTankNo();
//        updateTankNoText();
//    }
//
//    public void decrementTankNo() {
//        options.decrementTankNo();
//        updateTankNoText();
//    }
//
//    public void updateTankNoText() {
//        menu.updateMenuEntry(Integer.toString(options.getNumberOfTanks()), PLAY_OPTIONS_ID, "tank_no");
//    }
//
//
//    public void setup() {
//
//    }
//
//    private void generateTerrain() {
//        for(int i = 0; i <= width; i += BLOCK_WIDTH){
//            int seed = Math.round(random(MIN_BLOCKS_HEIGHT, MAX_BLOCKS_HEIGHT));
//            for(int j = 1; j <= seed; j++){
//                EnvironmentBlock.MaterialType type;
//
//                if(j < seed) {
//                    type = EnvironmentBlock.MaterialType.DIRT;
//                } else {
//                    type = EnvironmentBlock.MaterialType.GRASS;
//                }
//
//                environmentObjects.add(new EnvironmentBlock(this, i,
//                        height - FLOOR_HEIGHT - BLOCK_HEIGHT*j, BLOCK_WIDTH, BLOCK_HEIGHT, true,
//                        type));
//            }
//        }
//    }
//
//    public void mousePressed() {
//        if(state == tanksgame.GameWorld.GameState.MAIN_MENU) {
//            menu.handleMouseClick();
//        }
//    }
//
//    public void keyReleased() {
//        if (state == tanksgame.GameWorld.GameState.PLAYER_MOVING) {
//            if (key == 'a') {
//                tanks.get(currentTank).stopDrivingLeft();
//            }
//
//            if (key == 'd') {
//                tanks.get(currentTank).stopDrivingRight();
//            }
//
//            if (key == 'w') {
//                tanks.get(currentTank).stopBoosting();;
//            }
//
//            if (keyCode == LEFT) {
//                tanks.get(currentTank).stopRotatingLeft();
//            }
//
//            if (keyCode == RIGHT) {
//                tanks.get(currentTank).stopRotatingRight();
//            }
//
//            if (keyCode == UP) {
//                tanks.get(currentTank).stopPoweringUp();
//            }
//
//            if (keyCode == DOWN) {
//                tanks.get(currentTank).stopPoweringDown();
//            }
//        }
//    }
//
//    public void keyPressed() {
//        if (state == tanksgame.GameWorld.GameState.PLAYER_MOVING) {
//            if (key == 'a') {
//                tanks.get(currentTank).startDrivingLeft();
//            }
//
//            if (key == 'd') {
//                tanks.get(currentTank).startDrivingRight();
//            }
//
//            if (key == 'w') {
//                tanks.get(currentTank).startBoosting();;
//            }
//
//            if (keyCode == LEFT) {
//                tanks.get(currentTank).startRotatingLeft();
//            }
//
//            if (keyCode == RIGHT) {
//                tanks.get(currentTank).startRotatingRight();
//            }
//
//            if (keyCode == UP) {
//                tanks.get(currentTank).startPoweringUp();
//            }
//
//            if (keyCode == DOWN) {
//                tanks.get(currentTank).startPoweringDown();
//            }
//
//
//            if (key == ' ') {
//                currentShell = tanks.get(currentTank).fire(SHELL_MASS);
//
//                state = tanksgame.GameWorld.GameState.PLAYER_FIRED;
//                tanks.get(currentTank).endTurn();
//            }
//        }
//    }
//
//    public void draw() {
//        if(state == tanksgame.GameWorld.GameState.PLAYER_MOVING || state == tanksgame.GameWorld.GameState.PLAYER_FIRED || state == tanksgame.GameWorld.GameState.GAME_OVER) {
//            ArrayList<Force> constantForces = new ArrayList<>();
//            constantForces.add(new Force(windStrength, 0));
//
//
//            for (Tank tank : tanks) {
//                tank.addGravity(GRAVITATIONAL_CONSTANT);
//                tank.addForces(constantForces);
//                tank.update();
//                tank.move();
//                tank.collide(tanks);
//                tank.collide(environmentObjects);
//            }
//
//            for (int i = 0; i < environmentObjects.size(); i++) {
//
//                environmentObjects.get(i).addGravity(GRAVITATIONAL_CONSTANT);
//                environmentObjects.get(i).move();
//
//
//                int collide = environmentObjects.get(i).collide(tanks);
//                if(collide != CollidableObject.NO_COLLISION) {
//                    System.out.println(collide);
//                }
//                int collide1 = environmentObjects.get(i).collide(environmentObjects);
//
//
//                CollisionDetection.keepOnScreen(width, height, environmentObjects.get(i));
//            }
//
//
//            if (state == tanksgame.GameWorld.GameState.GAME_OVER) {
//                textAlign(CENTER);
//                textSize(30);
//                fill(0, 0, 0);
//                text(tanks.get(0).getId() + " WINS!", width / 2, height / 2);
//            }
//
//            handleFiring(constantForces);
//
//
//            background(135, 206, 235);
//            info.draw();
//
//            if(currentShell != null) {
//                currentShell.draw();
//            }
//
//            for(Tank tank : tanks) {
//                tank.draw();
//            }
//
//            for(EnvironmentBlock block : environmentObjects) {
//                block.draw();
//            }
//
//        } else if (state == tanksgame.GameWorld.GameState.MAIN_MENU) {
//            menu.draw();
//        }
//    }
//
//    private void handleFiring(ArrayList<Force> constantForces) {
//        if (currentShell != null) {
//            currentShell.move();
//
//            int collisionIndex;
//            boolean collisionOccurred = false;
//            if ((collisionIndex = currentShell.collide(environmentObjects)) != CollidableObject.NO_COLLISION) {
//                environmentObjects.remove(collisionIndex);
//                collisionOccurred = true;
//            } else if ((collisionIndex = currentShell.collide(tanks)) != CollidableObject.NO_COLLISION){
//                tanks.remove(collisionIndex);
//                collisionOccurred = true;
//                if(currentTank >= collisionIndex) {
//                    currentTank--;
//                }
//            }
//
//            if(!collisionOccurred){
//                currentShell.addGravity(GRAVITATIONAL_CONSTANT);
//                currentShell.addForces(constantForces);
//            } else {
//                currentShell = null;
//                if (currentTank == tanks.size() - 1) {
//                    currentTank = 0;
//                } else {
//                    currentTank += 1;
//                }
//
//                if (tanks.size() == 1) {
//                    state = tanksgame.GameWorld.GameState.GAME_OVER;
//                } else {
//                    tanks.get(currentTank).refuel(TURN_REFUEL_RATE);
//                    windStrength =  random(0 - WIND_STRENGTH_LIMIT, WIND_STRENGTH_LIMIT);
//                    state = tanksgame.GameWorld.GameState.PLAYER_MOVING;
//                }
//            }
//        }
//    }
//}
// {
//}
