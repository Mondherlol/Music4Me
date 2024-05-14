package MusicPlayer.Controller;

import MusicPlayer.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SideBarController {

    @FXML
    private Button homeButton;

    @FXML
    private Button favoritesButton;


    @FXML
    private Button playlist1Button;


    @FXML
    private Button addSongButton, createPlaylistBtn;


    @FXML
    public void initialize() {


        homeButton.setOnAction(event -> {
            System.out.println("Bouton Home cliqué !");
            Main.getInstance().loadView("Home-view.fxml");

        });

        playlist1Button.setOnAction(event -> {
            System.out.println("Bouton Playlist 1 cliqué !");
        });

        favoritesButton.setOnAction(event -> {
            System.out.println("Bouton Musiques Likées cliqué !");
        });

        addSongButton.setOnAction(event -> {
            System.out.println("Bouton Ajouter une musique cliqué !");
            Main.getInstance().loadView("Add-Song-Form.fxml");
        });

        createPlaylistBtn.setOnAction(event -> {
            System.out.println("Bouton Créer une playlist cliqué !");
            Main.getInstance().loadView("Add-Playlist-Form.fxml");
        });

    }
}
