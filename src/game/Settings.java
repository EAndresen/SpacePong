/**
 * Settings class that collects all the information about the game,
 * then the game is geting it's setings from here.
 * It's handling player names, ball size and speed, paddle hight,
 * if bot is activated and it's difficulty.
 */

package game;

/**
 * Created by Eric on 2016-11-01.
 */
public class Settings {

    //Instance variables.
    private static int ballSize;
    private static int paddleHeight;
    private static int ballSpeed;
    private static int botDifficulty;
    private static int scoreLimit;

    private static boolean botEnabled = false;

    private static String playerOneName;
    private static String playerTwoName;

    //Setters and getters for all the game settings.
    protected String getPlayerTwoName() {
        return playerTwoName;
    }
    void setPlayerTwoName(String playerTwoName) {
        Settings.playerTwoName = playerTwoName;
    }

    protected String getPlayerOneName() {
        return playerOneName;
    }
    void setPlayerOneName(String playerOneName) {
        Settings.playerOneName = playerOneName;
    }

    protected int getScoreLimit() {
        return scoreLimit;
    }
    void setScoreLimit(int scoreLimit) {
        Settings.scoreLimit = scoreLimit;
    }

    protected int getBotDifficulty() {
        return botDifficulty;
    }
    void setBotDifficulty(String botDifficulty) {
        switch (botDifficulty) {
            case "Easy":
                Settings.botDifficulty = 0;
                break;
            case "Medium":
                Settings.botDifficulty = 1;
                break;
            case "Hard":
                Settings.botDifficulty = 2;
        }
    }

    void setBotEnabled(boolean check) {
        botEnabled = check;
    }
    protected boolean getBotEnabled() {
        return botEnabled;
    }

    protected int getBallSpeed() {
        return ballSpeed;
    }
    void setBallSpeed(String ballSpeed) {
        switch (ballSpeed) {
            case "Slow":
                Settings.ballSpeed = 1;
                break;
            case "Medium":
                Settings.ballSpeed = 3;
                break;
            case "Fast":
                Settings.ballSpeed = 5;
                break;
            case "TOO Fast":
                Settings.ballSpeed = 10;
                break;
        }
    }

    void setBallSize(String ballSize) {
        switch (ballSize) {
            case "Small":
                Settings.ballSize = 20;
                break;
            case "Medium":
                Settings.ballSize = 60;
                break;
            case "Large":
                Settings.ballSize = 90;
                break;
            case "OMG":
                Settings.ballSize = 300;
                break;
            default:
                Settings.ballSize = 60;
                break;
        }
    }
    protected int getBallSize() {
        return ballSize;
    }

    void setPaddleHeight(String paddleHeight) {
        switch (paddleHeight) {
            case "Small":
                Settings.paddleHeight = 150;
                break;
            case "Medium":
                Settings.paddleHeight = 200;
                break;
            case "Large":
                Settings.paddleHeight = 300;
                break;
            case "OMG":
                Settings.paddleHeight = 500;
                break;
            default:
                Settings.paddleHeight = 200;
                break;
        }
    }
    protected int getPaddleHeight() {
        return paddleHeight;
    }
}
