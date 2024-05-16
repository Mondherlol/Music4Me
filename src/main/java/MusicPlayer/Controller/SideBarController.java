package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Playlist;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class SideBarController {

    @FXML
    private Button homeButton;

    @FXML
    private Button favoritesButton;

    @FXML
    private VBox playlistsVBox;



    @FXML
    private Button addSongButton, createPlaylistBtn;


    @FXML
    public void initialize() {


        homeButton.setOnAction(event -> {
            System.out.println("Bouton Home cliqué !");
            Main.getInstance().loadView("Home-view.fxml");

        });


        favoritesButton.setOnAction(event -> {
            System.out.println("Bouton Musiques Likées cliqué !");
            Main.getInstance().loadView("Favorites-songs.fxml");
        });

        addSongButton.setOnAction(event -> {
            System.out.println("Bouton Ajouter une musique cliqué !");
            Main.getInstance().loadView("Add-Song-Form.fxml");
        });

        createPlaylistBtn.setOnAction(event -> {
            System.out.println("Bouton Créer une playlist cliqué !");
            Main.getInstance().loadView("Add-Playlist-Form.fxml");
        });

        loadPlaylists();

    }

    public void loadPlaylists() {
        // Charger les playlists depuis la base de données
        System.out.println("Chargement des playlists...");

        List<Playlist> playlists = Playlist.getAllPlaylists();
        System.out.println(playlists);
        for (Playlist playlist : playlists) {
            Button playlistButton = createPlaylistButton(playlist);
            playlistsVBox.getChildren().add(playlistButton);
        }
    }


    private Button createPlaylistButton(Playlist playlist) {
        Button playlistButton = new Button(playlist.getName());
        playlistButton.setStyle("-fx-background-color: #1D1F3E;");
        playlistButton.setTextFill(Color.WHITE);
        playlistButton.setFont(Font.font("Open Sans Regular", 12));
        playlistButton.setAlignment(Pos.TOP_LEFT);
        playlistButton.setPrefWidth(200);

        ImageView imageView = new ImageView(new Image(playlist.getCover()));
        imageView.setFitHeight(46);
        imageView.setFitWidth(46);


        playlistButton.setGraphic(imageView);

        playlistButton.setOnAction(event -> {
            System.out.println("Playlist " + playlist.getName() + " cliquée !");
            Main.getInstance().layoutController.loadView("Playlist-view.fxml", playlist.getId());
        });

        return playlistButton;
    }

    public void resetPlaylists() {
        playlistsVBox.getChildren().clear();
        loadPlaylists();
    }
}
