package tanksgame;

import engine.Drawable;
import engine.GameObject;
import processing.core.PApplet;

public class EnvironmentBlock extends GameObject implements Drawable {
    private final MaterialType type;

    public EnvironmentBlock(PApplet parent, int xpos, int ypos, int xdim, int ydim, boolean destructible, MaterialType type) {
        super(parent, xpos, ypos, xdim, ydim, destructible);
        this.type = type;
    }

    public enum MaterialType {
        STONE, GRASS, DIRT
    }

    @Override
    public void draw() {
        if(type == MaterialType.GRASS){
            parent.fill(40,139,34);
        } else if(type == MaterialType.STONE){
            parent.fill(128,128,128);
        } else if(type == MaterialType.DIRT){
            parent.fill(139,69,19);
        }
        parent.rect(position.x, position.y, dimensions.x, dimensions.y);
    }
}
