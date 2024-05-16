package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Playlist;
import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddPlaylistController {

    @FXML
    private TextField playlistNameField;

    @FXML
    private TextArea playlistDescriptionArea;

    @FXML
    private Button savePlaylistBtn;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView coverImage;
    private String coverImagePath;

    @FXML
    public void initialize() {
        savePlaylistBtn.setOnAction(event -> savePlaylist());
    }

    private void savePlaylist() {
        try {
            String name = playlistNameField.getText();
            String description = playlistDescriptionArea.getText();
            String coverPath = coverImagePath;

            if (coverPath.isEmpty()) {
                errorLabel.setText("Veuillez sélectionner une image de couverture.");
                return;
            }

            List<Song> emptySongList = new ArrayList<>();
            Playlist newPlaylist = new Playlist(0, name, description, emptySongList, coverPath);

            addPlaylist(newPlaylist);

            playlistNameField.clear();
            playlistDescriptionArea.clear();
            errorLabel.setText("");
            System.out.println("Playlist enregistrée avec succès dans la base de données.");

            Main.getInstance().layoutController.sideBarController.resetPlaylists();
            Main.getInstance().layoutController.loadView("Home-view.fxml");
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors de l'enregistrement de la playlist : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addPlaylist(Playlist playlist) throws SQLException {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué.");
            return;
        }

        String query = "INSERT INTO playlists (id, name, description, cover) VALUES (default, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, playlist.getName());
            preparedStatement.setString(2, playlist.getDescription());
            preparedStatement.setString(3, playlist.getCover());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                playlist.setId(id);
            }

            System.out.println("Playlist enregistrée avec succès dans la base de données.");
        } finally {
            connection.close();
        }
    }

    @FXML
    private void chooseCover() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image de couverture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            coverImagePath = selectedFile.getPath();
            coverImage.setImage(new Image(selectedFile.toURI().toString()));
        }
    }
}