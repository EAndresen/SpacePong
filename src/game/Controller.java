/**
 * JavaFX controller to handling the settings screen.
 * Also handling all the in data from the settings window and calls
 * settings class and feeding it with information.
 * Also starts the game after all the in data have been collected.
 */

package game;

import game.pong.Pong;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller extends Settings implements Initializable {

    //Declaring all the JavaFX components.
    @FXML
    Button runBtn;
    @FXML
    ComboBox<String> ballSizeBox;
    @FXML
    ComboBox<String> paddleSizeBox;
    @FXML
    ComboBox<String> ballSpeedBox;
    @FXML
    ComboBox<String> botDifficultyBox;
    @FXML
    CheckBox botCheckBox;
    @FXML
    TextField scoreLimitInput;
    @FXML
    TextField playerOneName;
    @FXML
    TextField playerTwoName;

    //Size selections list.
    private ObservableList<String> SizeList = FXCollections.observableArrayList("Small", "Medium", "Large", "OMG");

    //Speed selection list.
    private ObservableList<String> SpeedList = FXCollections.observableArrayList("Slow", "Medium", "Fast", "TOO Fast");

    //Bot difficulty list.
    private ObservableList<String> BotList = FXCollections.observableArrayList("Easy", "Medium", "Hard");

    //Game running button that collects all the in data and sending it to the settings class,
    // then starting the game.
    public void runButton(ActionEvent e) {
        if (ballSizeBox.getValue() == null) {
            this.setBallSize("Medium");
        } else {
            this.setBallSize(ballSizeBox.getValue());
        }

        if (paddleSizeBox.getValue() == null) {
            this.setPaddleHeight("Medium");
        } else {
            this.setPaddleHeight(paddleSizeBox.getValue());
        }

        if (ballSpeedBox.getValue() == null) {
            this.setBallSpeed("Medium");
        } else {
            this.setBallSpeed(ballSpeedBox.getValue());
        }

        if (botDifficultyBox.getValue() == null) {
            this.setBotDifficulty("Medium");
        } else {
            this.setBotDifficulty(botDifficultyBox.getValue());
        }

        if (playerOneName.getText().equals("")) {
            this.setPlayerOneName("Player 1");
        } else {
            this.setPlayerOneName(playerOneName.getText());
        }

        if (playerTwoName.getText().equals("")) {
            this.setPlayerTwoName("Player 2");
        } else {
            this.setPlayerTwoName(playerTwoName.getText());
        }

        this.setScoreLimit(Integer.parseInt(scoreLimitInput.getText()));

        //Run the game
        try {
            Pong pong = new Pong();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e1) {
            e1.printStackTrace();
        }
    }

    //Set list to combo boxes.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ballSizeBox.setItems(SizeList);
        paddleSizeBox.setItems(SizeList);
        ballSpeedBox.setItems(SpeedList);
        botDifficultyBox.setItems(BotList);
    }

    //Check if the bot is enabled.
    @FXML
    private void handleCheckBoxAction() {
        if (botCheckBox.isSelected()) {
            this.setBotEnabled(true);
        } else {
            this.setBotEnabled(false);
        }
    }
}
