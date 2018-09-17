package tanksgame;

public class Tank {
	int xpos, ypos;
	boolean drivingLeft = false;
	boolean drivingRight = false;
	boolean rotateLeft = false;
	boolean rotateRight = false;
	
	private static final int movingSpeed = 2;
	private static final double rotationSpeed = 0.01;
	double rotateCount = 0.0;
	
	double firingAngle;
	double firingLimit = Math.PI/10;
	
	Tank(int xpos, int ypos, double firingAngle){
		this.xpos = xpos;
		this.ypos = ypos;
		this.firingAngle = firingAngle;
	}
  
	int getXpos(){
		return xpos;
	}
  
	int getYpos(){
		return ypos; 
	}
	
	double getFiringAngle() {
		System.out.println(firingAngle);
		return firingAngle;
	}
	
	void drive() {
		if(drivingLeft) {
			xpos -= movingSpeed;
		}
		
		if(drivingRight) {
			xpos += movingSpeed;
		}
	}
	
	void rotateCannon() {
		if(rotateLeft && firingAngle < (Math.PI + firingLimit)) {
			firingAngle += rotationSpeed;
		}
		
		if(rotateRight && firingAngle > (0 - firingLimit)) {
			firingAngle -= rotationSpeed;
		}
	}
}
