package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Playlist;
import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

public class UpdatePlaylistFormController {

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

    private Playlist playlist;

    private int playlistId;

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
            Playlist newPlaylist = new Playlist(playlistId, name, description, emptySongList, coverPath);

            updatePlaylist(newPlaylist);

            playlistNameField.clear();
            playlistDescriptionArea.clear();
            errorLabel.setText("");
            System.out.println("Playlist modifiée avec succès dans la base de données.");



            Main.getInstance().layoutController.sideBarController.resetPlaylists();
            Main.getInstance().loadView("Playlist-view.fxml",playlistId);
        } catch (SQLException e) {
            errorLabel.setText("Erreur lors de l'enregistrement de la playlist : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updatePlaylist(Playlist playlist) throws SQLException {
      playlist.updatePlaylist();

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

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
        playlist = Playlist.getPlaylistById(playlistId);

        playlistNameField.setText(playlist.getName());
        playlistDescriptionArea.setText(playlist.getDescription());
        coverImagePath = playlist.getCover();
        coverImage.setImage(new Image(coverImagePath));

    }
}
