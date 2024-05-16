package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Playlist;
import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FavoritesSongsController {

    @FXML
    private FlowPane songList;

    @FXML
    public void initialize() {
        loadsSongs();
    }


    public void loadSongBox(Song song){
        // Créer un nouvel élément visuel pour chaque chanson
        VBox songBox = new VBox();
        songBox.setAlignment(Pos.CENTER);
        songBox.setSpacing(5);
        songBox.setMaxWidth(150.0);
        songBox.setPrefHeight(181.0);
        songBox.setPrefWidth(138.0);
        songBox.setStyle("-fx-background-color:  #111536;");
        songBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                PlaySong(song);
            }
        });


        // Créer le bouton avec l'image
        Button button = new Button();
        button.setContentDisplay(ContentDisplay.CENTER);
        button.setStyle("-fx-background-color: transparent;");
        ImageView imageView = new ImageView(new Image(song.getCover()));
        imageView.setFitHeight(120.0);
        imageView.setFitWidth(120.0);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);

        // Créer le label pour le titre de la chanson
        Label titleLabel = new Label(song.getTitle());
        titleLabel.setPrefHeight(28.0);
        titleLabel.setPrefWidth(118.0);
        titleLabel.setStyle("-fx-font-weight: bold;");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setFont(Font.font("Open Sans Regular", 19.0));

        // Créer le menu contextuel pour le titre de la chanson
        ContextMenu contextMenu = new ContextMenu();

        MenuItem playMenuItem = new MenuItem("Play Song");
        playMenuItem.setOnAction(event -> PlaySong(song));

        MenuItem updateMenuItem = new MenuItem("Update");
        updateMenuItem.setOnAction(event -> EditSong(song));

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> confirmDeleteButtonClicked(song));

        MenuItem addToMusicQueueMenuItem = new MenuItem("Add To Music Queue");
        addToMusicQueueMenuItem.setOnAction(event -> AddSongToQueue(song));

        // Ajoutez ces items directement au ContextMenu
        contextMenu.getItems().addAll(playMenuItem, updateMenuItem, deleteMenuItem, addToMusicQueueMenuItem);

        // Créer le menu pour "Add To Playlist"
        Menu addToPlaylistMenu = new Menu("Add To Playlist");

        // Créer un MenuItem pour "Add To Playlist" qui sera ajouté au Menu "addToPlaylistMenu"
        MenuItem addToPlaylistMenuItem = new MenuItem("Add To Playlist");
        contextMenu.setOnShowing(event -> {
            List<Playlist> playlists = getPlaylistsFromDatabase();
            addToPlaylistMenu.getItems().clear(); // Effacez les éléments existants du sous-menu
            for (Playlist playlist : playlists) {
                MenuItem playlistMenuItem = new MenuItem(playlist.getName());
                playlistMenuItem.setOnAction(e -> addSongToPlaylist(song, playlist));
                addToPlaylistMenu.getItems().add(playlistMenuItem); // Ajoutez chaque playlist comme MenuItem au sous-menu
            }
            addToPlaylistMenuItem.setDisable(playlists.isEmpty()); // Désactivez le MenuItem principal s'il n'y a pas de playlists disponibles
        });

        addToPlaylistMenu.getItems().add(addToPlaylistMenuItem); // Ajoutez le MenuItem principal au sous-menu

        // Ajoutez le sous-menu "Add To Playlist" au ContextMenu principal
        contextMenu.getItems().add(addToPlaylistMenu);

        // Définissez le menu contextuel pour le titre de la chanson
        titleLabel.setContextMenu(contextMenu);


        // Créer le label pour le nom de l'artiste
        Label artistLabel = new Label(song.getArtist());
        artistLabel.setPrefHeight(13.0);
        artistLabel.setPrefWidth(118.0);
        artistLabel.setStyle("-fx-font-weight: bold;");
        artistLabel.setTextFill(Color.WHITE);
        artistLabel.setFont(Font.font("Open Sans Regular", 12.0));

        // Ajouter les éléments à la VBox
        songBox.getChildren().addAll(button, titleLabel, artistLabel);

        // Définir la marge pour la VBox
        HBox.setMargin(songBox, new Insets(10.0, 15.0, 25.0, 15.0));


        // Ajouter la VBox à la HBox
        songList.getChildren().add(songBox);
    }



    protected EventHandler<ActionEvent> PlaySong(Song song) {
        System.out.println("Playing song : " + song.getTitle());
        Main.getInstance().layoutController.musicPlayerController.playSong(song);
        return null;
    }

    protected EventHandler<ActionEvent> AddSongToQueue(Song song) {
        System.out.println("Adding song to queue : " + song.getTitle());
        Main.getInstance().layoutController.musicPlayerController.addSong(song);
        return null;
    }

    protected EventHandler<ActionEvent> EditSong(Song song) {
        System.out.println("Editing song infos : " + song.getTitle());
        Main.getInstance().loadView( "Update-Song-Form.fxml", song.getId());
        return null;
    }

    private List<Playlist> getPlaylistsFromDatabase() {
        List<Playlist> liste = Playlist.getAllPlaylists();
        return liste;
    }

    protected EventHandler<ActionEvent> addSongToPlaylist(Song song, Playlist playlist) {
        System.out.println("Adding song to playlist : " + song.getTitle());

        // Vérifier si la musique est déjà dans la playlist
        if (isSongAlreadyInPlaylist(song.getId(), playlist.getId()) ){
            // Afficher une alerte à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("La musique est déjà dans la playlist !");
            alert.showAndWait();
            return null;
        }

        // Ajouter la musique à la playlist
        System.out.println("Musique non trouvée dans la playlist. Ajout de la musique à la playlist...");
        playlist.addSongToPlaylist(song.getId());

        System.out.println(" Musique ajoutée avec succès à la playlist : " + playlist.getName());

        // Afficher une alerte à l'utilisateur
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Musique ajoutée avec succès à la playlist : " + playlist.getName());
        alert.showAndWait();

        return null;
    }

    private boolean isSongAlreadyInPlaylist(int songId, int playlistId){
        Playlist playlist = Playlist.getPlaylistById(playlistId);
        List<Song> playlist_songs = playlist.getSongs();

        for (Song song : playlist_songs) {
            if (song.getId() == songId) {
                return true;
            }
        }
        return false;
    }


    protected void confirmDeleteButtonClicked(Song song) {
        if (song != null ) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer cette chanson ? : "+song.getTitle());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection connection = DBConnexion.getConnection();
                    if (connection == null) {
                        System.out.println("La connexion à la base de données a échoué.");
                        return;
                    }

                    String deleteQuery = "DELETE FROM songs WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setString(1, String.valueOf(song.getId()));
                    int rowsAffected = preparedStatement.executeUpdate();

                    preparedStatement.close();
                    connection.close();

                    System.out.println("Nombre de lignes affectées : " + rowsAffected);

                    if (rowsAffected > 0) {
                        loadsSongs();
                        System.out.println("Chanson supprimée avec succès de la base de données.");
                    } else {
                        System.out.println("La chanson n'a pas été trouvée dans la base de données.");

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Veuillez sélectionner une chanson à supprimer.");
            errorAlert.showAndWait();
        }
    }

    private void loadsSongs() {
        songList.getChildren().clear();
        List<Song> songs = Song.getFavoriteSongs();
        for (Song song : songs) {
            loadSongBox(song);
        }
    }

}
