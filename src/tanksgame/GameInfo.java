package tanksgame;

import engine.Drawable;
import processing.core.PApplet;

public class GameInfo implements Drawable {
    private static final float SCREEN_PERCENTAGE = 0.1f;
    PApplet parent;

    public GameInfo(PApplet parent) {
        this.parent = parent;
    }

    @Override
    public void draw() {
        parent.fill(0, 0,0);
        parent.rect(0, 0, TestingEnvironment.SCREEN_HEIGHT, TestingEnvironment.SCREEN_WIDTH*SCREEN_PERCENTAGE);
        for(int i = 0; i < TestingEnvironment.tanks.size(); i++) {
            parent.fill(100, 100, 100);
            parent.rect(i*TestingEnvironment.SCREEN_WIDTH/TestingEnvironment.tanks.size(), 0,
                    TestingEnvironment.SCREEN_WIDTH/TestingEnvironment.tanks.size(), TestingEnvironment.SCREEN_WIDTH*SCREEN_PERCENTAGE);
            parent.fill(255, 255, 255);
            parent.text(TestingEnvironment.tanks.get(i).getId(), i*TestingEnvironment.SCREEN_WIDTH/TestingEnvironment.tanks.size() + 10,  20);
            parent.text(TestingEnvironment.tanks.get(i).getFiringAngle(), i*TestingEnvironment.SCREEN_WIDTH/TestingEnvironment.tanks.size() + 10,  40);
            parent.text(TestingEnvironment.tanks.get(i).getFiringPower(), i*TestingEnvironment.SCREEN_WIDTH/TestingEnvironment.tanks.size() + 10,  60);
        }

    }
}
