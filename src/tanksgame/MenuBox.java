package tanksgame;

import engine.Drawable;
import processing.core.PApplet;
import processing.core.PConstants;

public class MenuBox implements Drawable {
    private final PApplet parent;
    private String text;
    private final String id;
    private final int xpos;
    private final int ypos;
    private final int xdim;
    private final int ydim;
    private Runnable menuFunction;
    private final boolean clickable;
    private boolean dynamic;
    private boolean surroundingRectangle = true;
    private int textSize = 20;

    public MenuBox(PApplet parent, String id, String text, int xpos, int ypos, int xdim, int ydim, Runnable menuFunction){
        this.parent = parent;
        this.id = id;
        this.text = text;
        this.xpos = xpos;
        this.ypos = ypos;
        this.xdim = xdim;
        this.ydim = ydim;
        this.menuFunction = menuFunction;
        this.clickable = true;
    }


    public void removeRectangle() {
        surroundingRectangle = false;
    }

    public void addRectangle() {
        surroundingRectangle = true;
    }

    public MenuBox(PApplet parent, String id, String text, int xpos, int ypos, int xdim, int ydim){
        this.parent = parent;
        this.id = id;
        this.text = text;
        this.xpos = xpos;
        this.ypos = ypos;
        this.xdim = xdim;
        this.ydim = ydim;
        this.clickable = false;
    }

    @Override
    public void draw() {
        if(surroundingRectangle) {
            if(clickable && mouseInBox()){
                parent.fill(50, 50, 50);
            } else {
                parent.fill(0, 0,0);
            }
            parent.rectMode(PConstants.CENTER);
            parent.rect(xpos, ypos, xdim, ydim);
        }

        parent.textAlign(PConstants.CENTER, PConstants.CENTER);
        parent.fill(255, 255, 255);
        parent.textSize(textSize);
        parent.text(text, xpos, ypos);
    }

    public boolean mouseInBox() {
        int x = parent.mouseX;
        int y = parent.mouseY;

        return x > xpos - xdim/2
                && x < xpos + xdim/2
                && y > ypos - ydim/2
                && y < ypos + ydim/2;
    }

    public void click() {
        if(clickable) {
            menuFunction.run();
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }
}
