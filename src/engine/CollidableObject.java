package engine;

import java.util.ArrayList;

public interface CollidableObject {
    int OFF_SCREEN = -2;
    int NO_COLLISION = -1;

    <T extends GameObject> int collide(ArrayList<T> collidableObjects);
}
