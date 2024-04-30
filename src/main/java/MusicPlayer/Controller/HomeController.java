package MusicPlayer.Controller;

import MusicPlayer.Main;
import javafx.fxml.FXML;

public class HomeController {

    private Main main;


    @FXML
    protected void OpenPlaylists() {
    System.out.println("Open Playlist !");
    }

    @FXML
    protected void PlaySong() {
    Main.getInstance().loadView("player-view.fxml");
        }
}
