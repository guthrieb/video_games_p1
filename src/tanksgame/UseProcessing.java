package tanksgame;

import processing.core.PApplet;

public class UseProcessing extends PApplet {
	boolean[] keys = new boolean[128];
	
	private static final int SCREEN_HEIGHT = 500;
	private static final int SCREEN_WIDTH = 1000;
	
	private int tankWidth = 20;
	private int tankHeight = -20;
	private int barrelLength = 30;
	
	private int floorHeight = 100;
	
	private int tankEdgeMargin = 100;
	int currentTank = 0;
	
	
	Tank[] tanks = new Tank[2];
	public static void main(String[] args) {
		PApplet.main("tanksgame.UseProcessing");
	}
	
	public void settings(){
		size(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public void setup(){
    	tanks[0] = new Tank(0 + tankEdgeMargin, SCREEN_HEIGHT - floorHeight, 0);
    	tanks[1] = new Tank(SCREEN_WIDTH - tankEdgeMargin, SCREEN_HEIGHT - floorHeight, Math.PI);
    }
    
    public void keyPressed() {
    	if (key == 'a') {
    		tanks[currentTank].drivingLeft = true;
    	} 
    	
    	if (key == 'd') {
    		tanks[currentTank].drivingRight = true;
    	}
    	
    	if (keyCode == LEFT) {
    		tanks[currentTank].rotateLeft = true;
    	}
    	
    	if (keyCode == RIGHT) {
    		tanks[currentTank].rotateRight = true;
    	}
    }
    
    public void keyReleased() {
    	System.out.println(keyCode == LEFT);
    	if(key == 'a') {
    		tanks[currentTank].drivingLeft = false;
    	}
    	
    	if (key == 'd') {
    		tanks[currentTank].drivingRight = false;
    	}
    	
    	if (keyCode == LEFT) {
    		tanks[currentTank].rotateLeft = false;
    	}
    	
    	if (keyCode == RIGHT) {
    		tanks[currentTank].rotateRight = false;
    	}
    }

    public void draw(){
    	background(135, 206, 235);
    	rectMode(CORNERS);
    	fill(0,100,0);
    	rect(0, SCREEN_HEIGHT - floorHeight,SCREEN_WIDTH,SCREEN_HEIGHT);

    	rectMode(CORNER);
    	fill(100,100,0);
    	text("Firing angle: " + tanks[currentTank].getFiringAngle(), 10, 10);
    	
    	for(Tank tank : tanks) {
    		tank.drive();
    		tank.rotateCannon();
    		rect(tank.getXpos(), tank.getYpos(), tankWidth, tankHeight);
    		
    		lineAngle(tank.getXpos() + tankWidth/2, tank.getYpos() + tankHeight/2, tank.getFiringAngle(), barrelLength);
    	}
    }
    
    private void lineAngle(int x, int y, double angle, float length) {
    	line(x, y, x + cos((float) angle)*length, y - sin((float) angle)*length);
    }

}
