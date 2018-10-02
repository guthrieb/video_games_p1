package tanksgame;

class GameOptions {
    private int numberOfTanks = 2;
    private double gravityStrength;
    private final int maxTankNo;
    private int totalAi = 0;
    private int scoreLimit = 3;

    public GameOptions(int maxTankNo) {
        this.maxTankNo = maxTankNo;
    }

    public double getGravityStrength() {
        return gravityStrength;
    }

    public void setGravityStrength(double gravityStrength) {
        this.gravityStrength = gravityStrength;
    }

    public void incrementTankNo() {
        if(numberOfTanks < maxTankNo) {
            numberOfTanks++;
        }
    }

    public void decrementTankNo() {
        if(numberOfTanks > 2){
            numberOfTanks--;
        }

        if(totalAi > numberOfTanks) {
            totalAi = numberOfTanks;
        }
    }

    public void incrementNoOfAi() {
        if(totalAi < numberOfTanks) {
            totalAi++;
        }
    }

    public void decrementNoOfAi() {
        if(totalAi > 0){
            totalAi--;
        }
    }

    public int getNumberOfTanks() {
        return numberOfTanks;
    }

    public int getTotalAi() {
        return totalAi;
    }

    public void decrementScoreLimit() {
        scoreLimit--;
    }

    public void incrementScoreLimit() {
        scoreLimit++;
    }

    public int getScoreLimit() {
        return scoreLimit;
    }
}
