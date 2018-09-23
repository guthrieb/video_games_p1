package engine;

import processing.core.PVector;

public class Force {
    private final PVector forceComponents;

    public Force(double x, double y) {
        forceComponents = new PVector((float) x, (float) y);
    }

    public static Force fromAngle(double d, double e) {
        return new Force(d * Math.cos(e), d * Math.sin(e));
    }

    public double getX() {
        return forceComponents.x;
    }

    public double getY() {
        return forceComponents.y;
    }


    @Override
    public String toString() {
        return "(" + forceComponents.x + ":" + forceComponents.y + ")";
    }
}
