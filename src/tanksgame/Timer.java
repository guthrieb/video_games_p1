package tanksgame;

import processing.core.PApplet;

class Timer {
    private final int timeLimit;
    private int initialTime;
    private int timePassed;
    private final PApplet parent;

    public Timer(PApplet parent, int timeLimit) {
        this.parent = parent;
        this.timeLimit = timeLimit;
    }

    public void reset() {
        initialTime = parent.millis();
    }

    public void countdown() {
        timePassed = parent.millis() - initialTime;
    }

    public int getTimeLeft() {
        return timeLimit - timePassed;
    }
}
