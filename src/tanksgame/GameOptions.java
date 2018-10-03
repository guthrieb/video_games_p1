package tanksgame;

class GameOptions {
    private int numberOfTanks = 2;
    private double gravityStrength;
    private final int maxTankNo;
    private int totalAi = 0;
    private int scoreLimit = 3;
    private int aiDifficulty = 0;
    private int maxAiDifficulty = 2;

    public GameOptions(int maxTankNo) {
        this.maxTankNo = maxTankNo;
    }

    public void incrementTankNo() {
        if(numberOfTanks < maxTankNo) {
            numberOfTanks++;
        }
    }

    public void incrementAiDifficulty() {
        if(aiDifficulty < maxAiDifficulty) {
            aiDifficulty++;
        }
    }

    public void decrementAiDifficulty() {
        if(aiDifficulty < maxAiDifficulty) {
            aiDifficulty++;
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

    public String getAiDifficultyString() {
        switch (aiDifficulty){
            case 0:
                return "Easy";
            case 1:
                return "Medium";
            case 2:
                return "Hard";
            default:
                return null;
        }
    }

    public int getAiDifficulty() {
        return aiDifficulty;
    }
}
