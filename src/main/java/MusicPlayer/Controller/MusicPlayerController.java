package MusicPlayer.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MusicPlayerController {

    @FXML
    private Label songTitle;

    @FXML
    private Button playButton;
    private boolean isPlaying;

    @FXML
    protected void playButtonClicked() {
        // Code pour démarrer la lecture de la musique
        songTitle.setText("Now Playing: Song Title");
        isPlaying = true;
        playButton.setText("Pause");
        playButton.setOnAction(e -> pauseButtonClicked());

    }

    @FXML
    protected void pauseButtonClicked() {
        // Code pour mettre en pause la musique
        isPlaying = false;
        playButton.setText("Play");
        playButton.setOnAction(e -> playButtonClicked());

    }

    @FXML
    protected void stopButtonClicked() {
        // Code pour arrêter la musique
        songTitle.setText("Now Playing: No Song Selected");
        isPlaying = false;
        playButton.setText("Play");
        playButton.setOnAction(e -> playButtonClicked());

    }
}
