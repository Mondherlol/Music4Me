package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.sql.*;
import java.util.Optional;

public class HomeController {

    private Main main;
    @FXML
    private ListView<String> songListView;
    @FXML
    private HBox newSonsHBox;
    private ObservableList<String> songList = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        // Appeler la méthode pour charger les chansons au démarrage de la vue
        loadSongsFromDatabase();
    }


    public void loadSongsFromDatabase() {
        try {
            // Connexion à la base de données
            Connection connection = DBConnexion.getConnection();

            // Requête pour sélectionner tous les titres et les IDs des chansons de la base de données
            String query = "SELECT * FROM songs";

            // Exécution de la requête
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Effacer la liste des chansons existantes
            songList.clear();

            // Effacer les éléments existants dans la HBox
            newSonsHBox.getChildren().clear();

            // Ajout des titres des chansons à la liste observable
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String artist = resultSet.getString("artist");
                String cover = resultSet.getString("cover");
                boolean isFavorite = resultSet.getBoolean("isFavorite");
                File mp3 = new File(resultSet.getString("mp3"));

                Song song = new Song();
                song.setId(id);
                song.setTitle(title);
                song.setArtist(artist);
                song.setCover(cover);
                song.setMp3(mp3);
                song.setFavorite(isFavorite);



                songList.add(id + " - " + title);

                // Créer un nouvel élément visuel pour chaque chanson
                VBox songBox = new VBox();
                songBox.setAlignment(Pos.CENTER);
                songBox.setSpacing(5);
                songBox.setMaxWidth(150.0);
                songBox.setPrefHeight(181.0);
                songBox.setPrefWidth(138.0);
                songBox.setStyle("-fx-background-color: #1D1F3E;");
                songBox.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        PlaySong(song);
                    }
                });


                // Créer le bouton avec l'image
                Button button = new Button();
                button.setContentDisplay(ContentDisplay.CENTER);
                button.setStyle("-fx-background-color: transparent;");
                ImageView imageView = new ImageView(new Image(cover));
                imageView.setFitHeight(120.0);
                imageView.setFitWidth(120.0);
                imageView.setPreserveRatio(true);
                button.setGraphic(imageView);

                // Créer le label pour le titre de la chanson
                Label titleLabel = new Label(title);
                titleLabel.setPrefHeight(28.0);
                titleLabel.setPrefWidth(118.0);
                titleLabel.setStyle("-fx-font-weight: bold;");
                titleLabel.setTextFill(Color.WHITE);
                titleLabel.setFont(Font.font("Open Sans Regular", 19.0));

                // Créer le menu contextuel pour le titre de la chanson
                ContextMenu contextMenu = new ContextMenu();
                MenuItem playMenuItem = new MenuItem("Play Song");
                playMenuItem.setOnAction( event -> PlaySong(song) );

                MenuItem updateMenuItem = new MenuItem("Update");
                MenuItem deleteMenuItem = new MenuItem("Delete");
                deleteMenuItem.setOnAction(event -> confirmDeleteButtonClicked(song));
                MenuItem addToPlaylistMenuItem = new MenuItem("Add To Playlist");
                MenuItem addToMusicQueueMenuItem = new MenuItem("Add To Music Queue");
                addToMusicQueueMenuItem.setOnAction(event -> AddSongToQueue(song));
                contextMenu.getItems().addAll(playMenuItem, updateMenuItem, deleteMenuItem, addToPlaylistMenuItem, addToMusicQueueMenuItem);
                titleLabel.setContextMenu(contextMenu);

                // Créer le label pour le nom de l'artiste
                Label artistLabel = new Label(artist);
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
                newSonsHBox.getChildren().add(songBox);

            }

            // Fermeture des ressources
            resultSet.close();
            statement.close();
            DBConnexion.closeConnection();

            // Mise à jour de la ListView avec la liste des chansons
            songListView.setItems(songList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        this.loadSongsFromDatabase();
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

    public String getSelectedSong() {
        return songListView.getSelectionModel().getSelectedItem();
    }

    @FXML
    protected void OpenPlaylists() {
        System.out.println("Open Playlist !");
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


}
