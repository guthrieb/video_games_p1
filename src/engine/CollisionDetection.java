package engine;

import processing.core.PApplet;
import processing.core.PVector;
import tanksgame.TestingEnvironment;

public class CollisionDetection {
    public static boolean checkRectangleCollision(PVector rectPos1, PVector rectDim1, PVector rectPos2, PVector rectDim2) {
        return(rectPos2.x < rectPos1.x + rectDim1.x
                && rectPos2.x + rectDim2.x > rectPos1.x
                && rectPos2.y < rectPos1.y + rectDim1.y
                && rectPos2.y + rectDim2.y > rectPos1.y);
    }

    public static boolean offScreen(PVector position, PVector dimensions, PApplet parent){
        return position.x < 0
                || position.x + dimensions.x > TestingEnvironment.SCREEN_WIDTH
                || position.y < 0
                || position.y + dimensions.y > TestingEnvironment.SCREEN_HEIGHT;
    }
}
