package tanksgame;

import engine.CollidableObject;
import engine.CollisionDetection;
import engine.Force;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GameWorld extends PApplet {
    private static final String MAIN_MENU_ID = "main";
    private static final String PLAY_OPTIONS_ID = "play_options";

    static final float K_1 = 0.004f;
    static final float K_2 = 0.004f;

    private static final double TURN_REFUEL_RATE = 10;
    private static final float DAMPING_RATE = 0.95f;
    private static final float STARTING_ANGLE = (float) (Math.PI / 2);
    static final int MINIMUM_FIREPOWER = 1;
    static final int MAXIMUM_FIREPOWER = 50;
    public static final float GRAVITATIONAL_CONSTANT = 0.4f;

    private static final int FLOOR_HEIGHT = 30;
    private static final int BLOCK_WIDTH = 40;
    private static final int SHELL_MASS = 2;
    private static final int TANK_MASS = 2000;
    private static final int MAX_BLOCKS_HEIGHT = 20;
    private static final int MIN_BLOCKS_HEIGHT = 10;
    private static final int BLOCK_HEIGHT = 15;
    private static final int TANK_WIDTH = 30;
    private static final int TANK_HEIGHT = 15;
    static final float WIND_STRENGTH_LIMIT = 0.15f;
    static final float ROTATION_LIMIT = (float) (Math.PI / 10);
    private static final String INSTRUCTIONS_TEXT = "CONTROLS" +
            "\nmove left: a" +
            "\nmove right: d" +
            "\nboost: w" +
            "\nrotate cannon left: left arrow" +
            "\nrotate cannon right: right arrow" +
            "\nincrease firing power: up arrow" +
            "\ndecrease firing power: down arrow";
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;

    ArrayList<EnvironmentBlock> environmentObjects = new ArrayList<>();
    public ArrayList<Tank> tanks = new ArrayList<>();

    public float windStrength;
    public int currentTank = -1;
    private Shell currentShell = null;

    final HashMap<String, Integer> scores = new HashMap<>();

    private Menu menu;
    GameState state = GameState.MAIN_MENU;
    private GameInfo info;

    private final ArrayList<Integer> colours = new ArrayList<>(Arrays.asList(
            color(204, 0, 0), color(51, 51, 255), color(204, 204, 0), color(255, 108, 108)
    ));

    final Timer timer = new Timer(this, 15000);
    final Timer startingTimer = new Timer(this, 3000);
    private final GameOptions options = new GameOptions(colours.size());
    private int scoreLimit = 3;


    enum GameState {
        PLAYER_MOVING, PLAYER_FIRED, GAME_OVER, MAIN_MENU, GAME_SETUP
    }

    public static void main(String[] args) {
        PApplet.main("tanksgame.GameWorld");
    }

    public void settings() {
        size(WIDTH, HEIGHT);
        menu = new Menu(this);
        generateMenus();

    }

    private void newGame() {
        play();
        this.scoreLimit = options.getScoreLimit();
        for(Tank tank : tanks) {
            scores.put(tank.getId(), 0);
        }
    }

    private void play() {
        info = new GameInfo(this);

        generateTerrain();
        generateTanks();
        startingTimer.reset();
        state = GameState.GAME_SETUP;
    }

    private void generateTanks() {
        tanks = new ArrayList<>();
        int totalTanks = options.getNumberOfTanks();
        int totalAI = options.getTotalAi();
        int totalPlayers = totalTanks - totalAI;
        int tanksAdded = 0;


        for (int i = 0; i < totalPlayers; i++) {
            Tank tank = new Tank("P" + (i + 1),
                    false, this, (i * (width - 100) / totalTanks) +
                    ((width - 100) / totalTanks) / 2, 0, TANK_WIDTH, TANK_HEIGHT, STARTING_ANGLE, TANK_MASS,
                    DAMPING_RATE, 100, 100, colours.get(i));
            tanks.add(tank);
            tanksAdded++;
        }

        for (int i = 0; i < totalAI; i++) {
            Tank tank = new Tank("AI" + (i + 1),
                    true, options.getAiDifficulty(), this, (tanksAdded * (width - 100) / totalTanks) +
                    ((width - 100) / totalTanks) / 2, 0, TANK_WIDTH, TANK_HEIGHT, STARTING_ANGLE, TANK_MASS,
                    DAMPING_RATE, 100, 100, colours.get(tanksAdded));
            tanks.add(tank);
            tanksAdded++;
        }
    }


    private void generateMenus() {
        menu.addMenuBox(MAIN_MENU_ID, new MenuBox(this, "begin_button", "Begin!", width / 2, 2 * height / 7,
                width / 6, height / 10, () -> {
            try {
                menu.changePage(PLAY_OPTIONS_ID);
            } catch (InvalidPageException e) {
                e.printStackTrace();
            }
        }));

        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "play_button", "Play", width / 2, 2 * height / 7,
                width / 6, height / 10, this::newGame));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no_header", "No of tanks", width / 4, 3 * height / 7,
                width / 6, height / 10));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_no_header", "No of AI", width / 4, 4 * height / 7,
                width / 6, height / 10));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "score_limit_header", "Score Limit", width / 4, 5 * height / 7,
                width / 6, height / 10));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_difficulty_header", "AI Difficulty", width / 4, 6 * height / 7,
                width / 6, height / 10));

        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no", Integer.toString(options.getNumberOfTanks()),
                7 * width / 14, 3 * height / 7, width / 14, height / 14));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no_decrement_button", "-", 6 * width / 14, 3 * height / 7,
                width / 14, height / 14, this::decrementTankNo));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "tank_no_decrement_button", "+", 8 * width / 14, 3 * height / 7,
                width / 14, height / 14, this::incrementTankNo));

        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_no", Integer.toString(options.getTotalAi()),
                7 * width / 14, 4 * height / 7, width / 14, height / 14));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_no_decrement_button", "-", 6 * width / 14, 4 * height / 7,
                width / 14, height / 14, this::decrementAiNo));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_no_decrement_button", "+", 8 * width / 14, 4 * height / 7,
                width / 14, height / 14, this::incrementAiNo));

        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "score_limit", Integer.toString(options.getScoreLimit()),
                7 * width / 14, 5 * height / 7, width / 14, height / 14));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "score_limit_decrement_button", "-", 6 * width / 14, 5 * height / 7,
                width / 14, height / 14, this::decrementScoreLimit));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "score_limit_increment_button", "+", 8 * width / 14, 5 * height / 7,
                width / 14, height / 14, this::incrementScoreLimit));

        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_difficulty", options.getAiDifficultyString(),
                7 * width / 14, 6 * height / 7, width / 14, height / 14));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_difficulty_decrement_button", "-", 6 * width / 14, 6 * height / 7,
                width / 14, height / 14, this::decrementAiDifficulty));
        menu.addMenuBox(PLAY_OPTIONS_ID, new MenuBox(this, "ai_difficulty_increment_button", "+", 8 * width / 14, 6 * height / 7,
                width / 14, height / 14, this::incrementAiDifficulty));

        MenuBox instructionsBox = new MenuBox(this, "instructions_text", INSTRUCTIONS_TEXT,
                12 * width / 14, height / 2,
                width / 14, height / 14);
        instructionsBox.removeRectangle();
        instructionsBox.setTextSize(20);
        menu.addMenuBox(PLAY_OPTIONS_ID, instructionsBox);
        menu.addMenuBox(MAIN_MENU_ID, instructionsBox);

    }

    private void incrementAiDifficulty() {
        options.incrementAiDifficulty();
        updateAiDifficultyText();
    }

    private void updateAiDifficultyText() {
        menu.updateMenuEntry(options.getAiDifficultyString(), PLAY_OPTIONS_ID, "ai_difficulty");
    }

    private void decrementAiDifficulty() {
        options.decrementAiDifficulty();
        updateAiDifficultyText();
    }

    private void incrementScoreLimit() {
        options.incrementScoreLimit();
        updateScoreLimitText();
    }

    private void decrementScoreLimit() {
        options.decrementScoreLimit();
        updateScoreLimitText();
    }


    private void incrementTankNo() {
        options.incrementTankNo();
        updateTankNoText();
    }

    private void decrementTankNo() {
        options.decrementTankNo();
        updateTankNoText();
        updateAiNoText();
    }

    private void incrementAiNo() {
        options.incrementNoOfAi();
        updateAiNoText();
    }

    private void decrementAiNo() {
        options.decrementNoOfAi();
        updateAiNoText();
    }

    private void updateTankNoText() {
        menu.updateMenuEntry(Integer.toString(options.getNumberOfTanks()), PLAY_OPTIONS_ID, "tank_no");
    }

    private void updateAiNoText() {
        menu.updateMenuEntry(Integer.toString(options.getTotalAi()), PLAY_OPTIONS_ID, "ai_no");
    }

    private void updateScoreLimitText() {
        menu.updateMenuEntry(Integer.toString(options.getScoreLimit()), PLAY_OPTIONS_ID, "score_limit");
    }

    public void setup() {

    }

    private void generateTerrain() {
        environmentObjects = new ArrayList<>();
        EnvironmentBlock bedrock = new EnvironmentBlock("bedrock", this, 0, height - FLOOR_HEIGHT,
                width, FLOOR_HEIGHT, false, EnvironmentBlock.MaterialType.STONE);
        environmentObjects.add(bedrock);

        for (int i = 0; i <= width; i += BLOCK_WIDTH) {
            int seed = Math.round(random(MIN_BLOCKS_HEIGHT, MAX_BLOCKS_HEIGHT));
            for (int j = 1; j <= seed; j++) {
                EnvironmentBlock.MaterialType type;

                if (j < seed) {
                    type = EnvironmentBlock.MaterialType.DIRT;
                } else {
                    type = EnvironmentBlock.MaterialType.GRASS;
                }

                environmentObjects.add(new EnvironmentBlock("envblock_" + i, this, i,
                        height - FLOOR_HEIGHT - BLOCK_HEIGHT * j, BLOCK_WIDTH, BLOCK_HEIGHT, true,
                        type));
            }
        }
    }

    public void mousePressed() {
        if (state == GameState.MAIN_MENU) {
            menu.handleMouseClick();
        }
    }

    public void keyReleased() {
        if (state == GameState.PLAYER_MOVING && !tanks.get(currentTank).isAiControlled()) {
            if (key == 'a') {
                tanks.get(currentTank).stopDrivingLeft();
            }

            if (key == 'd') {
                tanks.get(currentTank).stopDrivingRight();
            }

            if (key == 'w') {
                tanks.get(currentTank).stopBoosting();
            }

            if (keyCode == LEFT) {
                tanks.get(currentTank).stopRotatingLeft();
            }

            if (keyCode == RIGHT) {
                tanks.get(currentTank).stopRotatingRight();
            }

            if (keyCode == UP) {
                tanks.get(currentTank).stopPoweringUp();
            }

            if (keyCode == DOWN) {
                tanks.get(currentTank).stopPoweringDown();
            }
        }
    }

    public void keyPressed() {
        if (state == GameState.PLAYER_MOVING && !tanks.get(currentTank).isAiControlled()) {
            if (key == 'a') {
                tanks.get(currentTank).startDrivingLeft();
            }

            if (key == 'd') {
                tanks.get(currentTank).startDrivingRight();
            }

            if (key == 'w') {
                tanks.get(currentTank).startBoosting();
            }

            if (keyCode == LEFT) {
                tanks.get(currentTank).startRotatingLeft();
            }

            if (keyCode == RIGHT) {
                tanks.get(currentTank).startRotatingRight();
            }

            if (keyCode == UP) {
                tanks.get(currentTank).startPoweringUp();
            }

            if (keyCode == DOWN) {
                tanks.get(currentTank).startPoweringDown();
            }


            if (key == ' ') {
                currentShell = tanks.get(currentTank).fire(SHELL_MASS);

                state = GameState.PLAYER_FIRED;
            }
        } else if (state == GameState.GAME_OVER) {
            state = GameState.MAIN_MENU;
        }
    }

    public void draw() {

        if (state == GameState.PLAYER_MOVING || state == GameState.PLAYER_FIRED || state == GameState.GAME_OVER || state == GameState.GAME_SETUP) {
            ArrayList<Force> constantForces = new ArrayList<>();
            constantForces.add(new Force(windStrength, 0));

            if (state == GameState.PLAYER_MOVING) {
                timer.countdown();

                if (tanks.get(currentTank).isAiControlled()) {
                    tanks.get(currentTank).getAi().makeMove(tanks, environmentObjects, timer.getTimeLeft(), windStrength, GRAVITATIONAL_CONSTANT);

                    if (tanks.get(currentTank).getAi().isReadyToFire()) {
                        currentShell = tanks.get(currentTank).getAi().fire(SHELL_MASS);
                        state = GameState.PLAYER_FIRED;
                    }
                }

                if (timer.getTimeLeft() <= 0) {
                    beginNextTurn();
                }
            } else if (state == GameState.GAME_SETUP) {
                startingTimer.countdown();


                if(startingTimer.getTimeLeft() <= 0) {
                    currentTank = -1;
                    beginNextTurn();
                }
            }


            for (Tank tank : tanks) {
                tank.addGravity(GRAVITATIONAL_CONSTANT);
                tank.addForces(constantForces);
                tank.update();
                tank.move();
                tank.collide(tanks);
                tank.collide(environmentObjects);
            }

            for (int i = 0; i < environmentObjects.size(); i++) {

                environmentObjects.get(i).addGravity(GRAVITATIONAL_CONSTANT);
                environmentObjects.get(i).move();


                environmentObjects.get(i).collide(tanks);
                environmentObjects.get(i).collide(environmentObjects);

                CollisionDetection.keepOnScreen(width, height, environmentObjects.get(i));
            }

            if (state == GameState.GAME_OVER) {
                textAlign(CENTER);
                textSize(30);
                fill(0, 0, 0);
                text(tanks.get(0).getId() + " WINS!", width / 2f, height / 2f);
            }

            handleFiring();


            background(135, 206, 235);
            info.draw();


            if (currentShell != null) {
                currentShell.draw();
            }


            for (Tank tank : tanks) {
                tank.draw();
            }

            for (EnvironmentBlock block : environmentObjects) {
                block.draw();
            }

//            if(currentTank >= 0)  {
//                AI newAI = new AI(this, tanks.get(currentTank));
//
//                point(tanks.get(currentTank).getXpos(), newAI.calculateBoostHeight());
//
//                PVector nearest = newAI.getNearestReachableSurfaceInDirection(environmentObjects, false);
//                fill(255, 255, 255);
//                rect(nearest.x, nearest.y, BLOCK_WIDTH, BLOCK_HEIGHT);
//            }


            if(state == GameState.GAME_OVER) {
                textSize(100);
                fill(tanks.get(currentTank).getColour());
                text("GAME OVER - " + tanks.get(currentTank).getId() + " WINS", width/2f, height/2f);
                textSize(40);
                text("press any key to return to the main menu", width/2f, 5*height/8f);
                textSize(20);
            }

            text(frameRate, width - 200, height - 200);
        } else if (state == GameState.MAIN_MENU) {
            menu.draw();
        }
    }

    private void handleFiring() {
        if (currentShell != null) {
            currentShell.addGravity(GRAVITATIONAL_CONSTANT);
            currentShell.addDrag(K_1, K_2);
            currentShell.addForce(new Force(windStrength, 0));
            currentShell.move();

            int collisionIndex;
            if ((collisionIndex = currentShell.collide(tanks)) != CollidableObject.NO_COLLISION && collisionIndex != CollidableObject.OFF_SCREEN) {
                tanks.remove(collisionIndex);
                if (collisionIndex <= currentTank) {
                    currentTank--;
                }
            } else if ((collisionIndex = currentShell.collide(environmentObjects)) != CollidableObject.NO_COLLISION
                    && collisionIndex != CollidableObject.OFF_SCREEN) {
                if (environmentObjects.get(collisionIndex).isDestructible()) {
                    environmentObjects.remove(collisionIndex);
                }
            }

            if (collisionIndex != CollidableObject.NO_COLLISION) {
                beginNextTurn();
            }
        }
    }

    private void beginNextTurn() {
        //Reset shell
        currentShell = null;

        //Select next tank
        if (currentTank >= tanks.size() - 1) {
            currentTank = 0;
        } else {
            currentTank += 1;
        }

        if (tanks.size() == 1) {
            //Do scoring
            scores.put(tanks.get(0).getId(), scores.get(tanks.get(0).getId()) + 1);

            if(scores.get(tanks.get(0).getId()) == scoreLimit){
                state = GameState.GAME_OVER;
            } else {
                play();
            }
        } else {
            //Prep next turn
            tanks.get(currentTank).refuel(TURN_REFUEL_RATE);
            state = GameState.PLAYER_MOVING;

            windStrength = random(0 - WIND_STRENGTH_LIMIT, WIND_STRENGTH_LIMIT);

//            if (tanks.get(currentTank).isAiControlled()) {
//                tanks.get(currentTank).getAi().beginCalculations(tanks.get(currentTank), tanks, environmentObjects, windStrength, GRAVITATIONAL_CONSTANT);
//            }

            timer.reset();
        }
    }
}
