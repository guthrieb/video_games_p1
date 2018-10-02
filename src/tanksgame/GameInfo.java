package tanksgame;

import engine.Drawable;
import processing.core.PConstants;

public class GameInfo implements Drawable {
    private static final float SCREEN_PERCENTAGE = 0.1f;
    private final GameWorld gameWorld;

    GameInfo(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void draw() {
        float gameInfoYdim = gameWorld.width * SCREEN_PERCENTAGE;
        int noOfTanks = gameWorld.tanks.size();
        int tankInfoXdim = gameWorld.width / (2 * noOfTanks);

        float tankInfoYpos = gameInfoYdim / 10;

        for (int i = 0; i < noOfTanks; i++) {
            int tankInfoXpos = i * gameWorld.width / noOfTanks + gameWorld.width / (4 * noOfTanks);
            int tankColour = gameWorld.tanks.get(i).getColour();
            String tankId = gameWorld.tanks.get(i).getId();


            gameWorld.textAlign(PConstants.LEFT);
            gameWorld.textSize(20);
            gameWorld.fill(100, 100, 100);
            gameWorld.rect(tankInfoXpos, tankInfoYpos,
                    tankInfoXdim, gameInfoYdim);

            if(i == gameWorld.currentTank) {
                gameWorld.fill(255, 255, 255, 20);
                gameWorld.strokeWeight(3);
                gameWorld.stroke(255, 255, 255);
                gameWorld.rect(tankInfoXpos, tankInfoYpos,
                        tankInfoXdim, gameInfoYdim);
                gameWorld.stroke(0, 0, 0);
                gameWorld.strokeWeight(1);
            }

            gameWorld.fill(tankColour);
            gameWorld.textSize(tankInfoXdim / 10f);
            gameWorld.text(tankId, tankInfoXpos + tankInfoXdim / 15f, tankInfoYpos + gameInfoYdim / 4);

            gameWorld.textSize(tankInfoXdim / 19f);
            gameWorld.fill(255, 255, 255);
            gameWorld.text("Elevation: " + (int) ((gameWorld.tanks.get(i).getFiringAngle()) * 180 / Math.PI) + "\u00b0",
                    tankInfoXpos + tankInfoXdim / 15f, tankInfoYpos + 2 * gameInfoYdim / 5);
            gameWorld.text("Firing Power: ",tankInfoXpos + tankInfoXdim / 15f, tankInfoYpos + 3 * gameInfoYdim / 5);



            gameWorld.fill(255, 0, 0);
            gameWorld.rect(tankInfoXpos + tankInfoXdim / 2.3f, tankInfoYpos + 3 * gameInfoYdim / 5,
                    tankInfoXdim / 5f, -gameInfoYdim/10);
            gameWorld.fill(0, 204, 0);
            gameWorld.rect(tankInfoXpos + tankInfoXdim / 2.3f, tankInfoYpos + 3 * gameInfoYdim / 5,
                    (tankInfoXdim / 5f)*((gameWorld.tanks.get(i).getFiringPower() / GameWorld.MAXIMUM_FIREPOWER)), -gameInfoYdim/10);

            gameWorld.fill(255, 255, 255);
            gameWorld.text("Fuel: " + (int) ((((double) gameWorld.tanks.get(i).getCurrentFuel()) /
                            ((double) gameWorld.tanks.get(i).getMaxFuel())) * 100) + "%",
                    tankInfoXpos + tankInfoXdim / 15f, tankInfoYpos + 4 * gameInfoYdim / 5);


            gameWorld.textSize(tankInfoXdim / 10f);
            gameWorld.fill(tankColour);
            gameWorld.textAlign(PConstants.CENTER, PConstants.CENTER);
            gameWorld.text(gameWorld.scores.get(gameWorld.tanks.get(i).getId()), tankInfoXpos + tankInfoXdim / 1.25f,
                    tankInfoYpos + gameInfoYdim / 2);

            gameWorld.textSize(20);


            if (gameWorld.currentTank == i) {
                gameWorld.fill(tankColour);
                gameWorld.textAlign(PConstants.CENTER);
                gameWorld.text(tankId + "'s turn", gameWorld.width / 2f, gameWorld.width * SCREEN_PERCENTAGE + gameWorld.height / 20f);
                gameWorld.text(gameWorld.timer.getTimeLeft()/1000, gameWorld.width/2f, gameWorld.width * SCREEN_PERCENTAGE + 2*gameWorld.height / 20f);
            }
        }

        if(gameWorld.state == GameWorld.GameState.GAME_SETUP) {
            gameWorld.textSize(100);
            gameWorld.fill(255, 255, 255);
            gameWorld.text((int) Math.ceil(gameWorld.startingTimer.getTimeLeft()/1000f) , gameWorld.width/2f, gameWorld.height/2f);
            gameWorld.textSize(20);
        }

        drawWindspeed();
    }

    private void drawWindspeed() {
        gameWorld.fill(0, 0, 0);
        gameWorld.textAlign(PConstants.CENTER);
        gameWorld.text("Windspeed: " + Math.abs((int) (((gameWorld.windStrength) / (GameWorld.WIND_STRENGTH_LIMIT)) * 100)), gameWorld.width / 4f, gameWorld.width * SCREEN_PERCENTAGE + gameWorld.height / 20f);

        double percentageOfMaxWindspeed = (100 * gameWorld.windStrength / GameWorld.WIND_STRENGTH_LIMIT);

        if(percentageOfMaxWindspeed != 0) {
            drawWindArrow(gameWorld.width / 4 - 20, (int) (gameWorld.width * SCREEN_PERCENTAGE + gameWorld.height / 14), 40, (int) percentageOfMaxWindspeed);
        }
    }

    private void drawWindArrow(int xpos, int ypos, int length, int proportionalDirectionAndPower) {
        int proportionAsFifth = proportionalDirectionAndPower / 20;

        gameWorld.strokeWeight(Math.abs(proportionAsFifth + 1));
        gameWorld.line(xpos, ypos, xpos + length, ypos);

        if (proportionalDirectionAndPower < 0) {
            gameWorld.line(xpos, ypos, xpos + 8, ypos + 8);
            gameWorld.line(xpos, ypos, xpos + 8, ypos - 8);
        } else if (proportionalDirectionAndPower > 0) {
            gameWorld.line(xpos + length, ypos, xpos + length - 8, ypos + 8);
            gameWorld.line(xpos + length, ypos, xpos + length - 8, ypos - 8);
        }
        gameWorld.strokeWeight(1);
    }

}
