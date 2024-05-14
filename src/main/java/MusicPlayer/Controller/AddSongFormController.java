package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.image.Image.*;
import MusicPlayer.Main;
import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddSongFormController {

    @FXML
    private Button addArtistButton, btnAddCover, playSongBtn, btnAddSong, saveSongBtn;

    @FXML
    private TextField albumField, titleField, genreField, yearField;

    @FXML
    private Label songPathLabel, errorLabel;

    @FXML
    private ImageView coverImage;
    private String coverImagePath;

    @FXML
    private ChoiceBox<String> artistChoiceBox;

    private Song song;

    @FXML
    public void initialize() {
        addArtistButton.setOnAction(event -> {
            Main.getInstance().loadView("Add-Artist.fxml");
        });
        song = new Song();
        playSongBtn.setDisable(true);
        songPathLabel.setText("");
        errorLabel.setText("");
        ObservableList<String> artistNames = FXCollections.observableArrayList();
        List<String> artists = getArtistsFromDatabase();
        artistNames.addAll(artists);
        artistChoiceBox.setItems(artistNames);
        artistChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Artiste sélectionné : " + newValue);
        });
        saveSongBtn.setOnAction(event -> saveSong());
    }

    private List<String> getArtistsFromDatabase() {
        List<String> artistNames = new ArrayList<>();
        try {
            Connection connection = DBConnexion.getConnection();
            if (connection == null) {
                System.out.println("La connexion à la base de données n'est pas établie.");
                return artistNames;
            }

            String query = "SELECT name FROM artists";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String artistName = resultSet.getString("name");
                    artistNames.add(artistName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artistNames;
    }

    private void saveSong() {
        try {
            // Récupérer les valeurs des champs du formulaire
            String title = titleField.getText();
            String artist = artistChoiceBox.getValue();
            String album = albumField.getText();
            String genre = genreField.getText();
            int year = Integer.parseInt(yearField.getText());
            String songPath = songPathLabel.getText();
            String coverPath = coverImagePath; // Utiliser la variable coverImagePath pour stocker le chemin de la pochette
            int duration = 0; // Durée de la chanson, à récupérer à partir du formulaire

            // Vérifier si le chemin de la chanson et la pochette sont sélectionnés
            if (songPath.isEmpty() || coverPath.isEmpty()) {
                errorLabel.setText("Veuillez sélectionner une chanson et une pochette.");
                return;
            }

            // Créer une instance de File à partir du chemin du fichier mp3
            File mp3 = new File(songPath);

            // Créer une instance de Song avec les valeurs récupérées
            int id = 0; // Remplacer 0 par l'ID réel de la chanson
            Song newSong = new Song(id, title, artist, album, genre, year, duration, mp3, coverPath);


            // Appeler la méthode pour ajouter la chanson à la base de données
            addSong(newSong);

            // Réinitialiser les champs après l'enregistrement de la chanson
            clearFields();
            errorLabel.setText(""); // Réinitialiser le message d'erreur
            System.out.println("Chanson enregistrée avec succès dans la base de données.");
        } catch (SQLException | NumberFormatException e) {
            errorLabel.setText("Erreur lors de l'enregistrement de la chanson : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addSong(Song song) throws SQLException {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return;
        }

        System.out.println("Chanson à ajouter : " + song.toString());

        String query = "INSERT INTO songs (id,title, artist, album, genre, year, duration, mp3, cover,isFavorite) VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?,false)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, song.getTitle());
            preparedStatement.setString(2, song.getArtist());
            preparedStatement.setString(3, song.getAlbum());
            preparedStatement.setString(4, song.getGenre());
            preparedStatement.setInt(5, song.getYear());
            preparedStatement.setInt(6, song.getDuration());
            preparedStatement.setString(7, song.getMp3().toString());
            preparedStatement.setString(8, song.getCover());


            System.out.println("Song path in addSong : "+ song.getMp3().toString());
            preparedStatement.executeUpdate();

            System.out.println("Chanson enregistrée avec succès dans la base de données.");

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    private void clearFields() {
        titleField.clear();
        artistChoiceBox.setValue(null);
        albumField.clear();
        genreField.clear();
        yearField.clear();
        songPathLabel.setText("");
        coverImage.setImage(null);
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose cover image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            coverImagePath = selectedFile.getPath(); // Stocker le chemin de la pochette sélectionnée
            String imagePath = selectedFile.toURI().toString();
            System.out.println(imagePath);
            Image image = new Image(imagePath);
            coverImage.setImage(image);
        }
    }
    @FXML
    private void chooseSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose song");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String songPath = selectedFile.toURI().toString();
            songPathLabel.setText(songPath);
            song.setMp3(selectedFile);
            playSongBtn.setDisable(false);
        }
    }
}
