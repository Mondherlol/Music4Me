package MusicPlayer.Controller;

import MusicPlayer.Main;
import MusicPlayer.Model.Song;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

import static javafx.scene.image.Image.*;


public class AddSongFormController {
    @FXML
    private Button addArtistButton, btnAddCover,playSongBtn, btnAddSong;
    @FXML
    private TextField albumField, titleField, artistField, genreField, yearField;
    @FXML
    private Label songPathLabel, errorLabel;
    @FXML
    private ImageView coverImage;

    private Song song;

    @FXML
    public void initialize() {
        addArtistButton.setOnAction(event -> {
            Main.getInstance().loadView("Add-Artist.fxml");
        });
        song  = new Song();
        playSongBtn.setDisable(true);
        songPathLabel.setText("");
        errorLabel.setText("");
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose cover image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            System.out.println(imagePath);
            Image image = new Image(imagePath);
            coverImage.setImage(image);

        }
    }

    @FXML
    private void chooseSong(){
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
