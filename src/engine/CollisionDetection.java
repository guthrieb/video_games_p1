package engine;

import processing.core.PVector;

public class CollisionDetection {
    public enum Direction {
        LEFT, RIGHT, TOP, BOTTOM, NO_COLLISION
    }

    public static boolean checkRectangleCollision(PVector rectPos1, PVector rectDim1, PVector rectPos2, PVector rectDim2) {
        return(rectPos2.x < rectPos1.x + rectDim1.x
                && rectPos2.x + rectDim2.x > rectPos1.x
                && rectPos2.y < rectPos1.y + rectDim1.y
                && rectPos2.y + rectDim2.y > rectPos1.y);
    }

    public static boolean offScreen(int width, int height, PVector position, PVector dimensions){
        return position.x < 0
                || position.x + dimensions.x > width
                || position.y < 0
                || position.y + dimensions.y > height;
    }

    public static boolean offScreenExcludingTop(int width, int height, PVector position, PVector dimensions){
        return position.x < 0
                || position.x + dimensions.x > width
                || position.y + dimensions.y > height;
    }

    public static Direction offScreenDirection(int width, int height, PVector position, PVector dimensions) {
        if(position.x < 0) {
            return Direction.LEFT;
        }

        if(position.x + dimensions.x > width){
            return Direction.RIGHT;
        }

        if(position.y < 0) {
            return Direction.TOP;
        }

        if(position.y + dimensions.y > height) {
            return Direction.BOTTOM;
        }
        return Direction.NO_COLLISION;
    }

    public static void keepOnScreen(int width, int height, ForceInfluencedObject object){
        PVector position = object.position;
        PVector dimensions = object.dimensions;
        PVector velocity = object.velocity;

        CollisionDetection.Direction direction = CollisionDetection.offScreenDirection(width, height, position, dimensions);
        if (direction == CollisionDetection.Direction.LEFT) {
            position.x = 0;
            velocity.x = 0;
        }

        if (direction == CollisionDetection.Direction.RIGHT) {
            position.x = width - dimensions.x;
            velocity.x = 0;
        }

        if(direction == CollisionDetection.Direction.TOP) {
            position.y = 0;
            velocity.y = 0;
        }

        if(direction == CollisionDetection.Direction.BOTTOM) {
            position.y = height - dimensions.y;
            velocity.y = 0;
        }
    }

    public static Direction getRectangleCollisionDirection(PVector position1, PVector dimensions1, PVector position2,
                                                           PVector dimensions2) {

        float block1BottomY = (int) (position1.y + dimensions1.y);
        float block2BottomY = (int) (position2.y + dimensions2.y);
        float block1RightX = (int) (position1.x + dimensions1.x);
        float block2RightX = (int) (position2.x + dimensions2.x);

        float bottomOverlap = (int) (block2BottomY - position1.y);
        float topOverlap = (int) (block1BottomY - position2.y);
        float leftOverlap = (int) (Math.abs(block1RightX - position2.x));
        float rightOverlap = (int) (Math.abs(block2RightX - position1.x));

        if (topOverlap < bottomOverlap && topOverlap < leftOverlap && topOverlap < rightOverlap) {
            return Direction.BOTTOM;
        }

        if (bottomOverlap < topOverlap && bottomOverlap < leftOverlap && bottomOverlap < rightOverlap) {
            return Direction.TOP;
        }


        if (leftOverlap < rightOverlap && leftOverlap < topOverlap && leftOverlap < bottomOverlap) {
            return Direction.LEFT;
        }

        if (rightOverlap < leftOverlap && rightOverlap < topOverlap && rightOverlap < bottomOverlap) {
            return Direction.RIGHT;
        }

        return Direction.NO_COLLISION;
    }

}
