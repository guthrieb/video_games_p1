//package tanksgame;
//
//import processing.core.PVector;
//
//public class Tank {
//	PVector position;
//	boolean drivingLeft = false;
//	boolean drivingRight = false;
//	boolean rotateLeft = false;
//	boolean rotateRight = false;
//	
//	private static final int movingSpeed = 2;
//	private static final double rotationSpeed = 0.01;
//	double rotateCount = 0.0;
//	
//	float firingAngle;
//	float firingLimit = (float) (Math.PI/10);
//	
//	Tank(int xpos, int ypos, float firingAngle){
//		this.position = new PVector(xpos, ypos);
//		this.firingAngle = firingAngle;
//	}
//  
//	float getXpos(){
//		return position.x;
//	}
//  
//	float getYpos(){
//		return position.y; 
//	}
//	
//	float getFiringAngle() {
//		return firingAngle;
//	}
//	
//	void drive() {
//		if(drivingLeft) {
//			position.x -= movingSpeed;
//		}
//		
//		if(drivingRight) {
//			position.x += movingSpeed;
//		}
//	}
//	
//	void rotateCannon() {
//		if(rotateLeft && firingAngle < (Math.PI + firingLimit)) {
//			firingAngle += rotationSpeed;
//		}
//		
//		if(rotateRight && firingAngle > (0 - firingLimit)) {
//			firingAngle -= rotationSpeed;
//		}
//	}
//}
