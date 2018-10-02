package tanksgame;

import engine.Drawable;
import engine.ForceInfluencedObject;
import processing.core.PApplet;

public class EnvironmentBlock extends ForceInfluencedObject implements Drawable {
    private final MaterialType type;
    private boolean fixedInPlace;

    public EnvironmentBlock(String id, PApplet parent, int xpos, int ypos, int xdim, int ydim, boolean destructible, MaterialType type) {
        super(id, parent, xpos, ypos, xdim, ydim, 100, destructible);
        this.type = type;
    }

    public enum MaterialType {
        STONE, GRASS, DIRT
    }

    @Override
    public void draw() {
        if(type == MaterialType.GRASS){
            parent.fill(0,100,0);
        } else if(type == MaterialType.STONE){
            parent.fill(128,128,128);
        } else if(type == MaterialType.DIRT){
            parent.fill(101, 67, 33);
        }
        parent.rect(position.x, position.y, dimensions.x, dimensions.y);
    }
}
