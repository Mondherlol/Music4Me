package MusicPlayer.Controller;

import MusicPlayer.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AddSongFormController {
    @FXML
    private Button addArtistButton;

    @FXML
    public void initialize() {
        addArtistButton.setOnAction(event -> {
            Main.getInstance().loadView("Add-Artist.fxml");
        });
    }
}
