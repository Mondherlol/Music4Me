package MusicPlayer.Controller;

import MusicPlayer.Model.Song;
import MusicPlayer.Utils.DBConnexion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerController implements Initializable {

    @FXML
    private Label songTitle, ArtistName;
    @FXML
    private Button PlayButton, NextButton, prevButton, likeButton;
    @FXML
    private Slider VolumeSlider;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private ImageView coverSong, heartImage, playImage;
    private boolean isPlaying;
    private Timer timer;
    private TimerTask task;
    private boolean running;
    public ArrayList<Song> songs;
    private int songNumber = 0;
    private File directory;
    private File[] files;

    @FXML
    Text totalSongTime, currentSongTime;

    private Media media;
    private MediaPlayer mediaPlayer;

    private Song currentSong;

    private static final String HEART_FULL_IMAGE = "/assets/images/heart_full.png";
    private static final String HEART_EMPTY_IMAGE = "/assets/images/heart_empty.png";

    private static final String PLAY_IMAGE = "/assets/images/play_white.png";
    private static final String PAUSE_IMAGE = "/assets/images/pause_white.png";

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songs = new ArrayList<Song>();
        isPlaying = false;

        // Disable buttons
        NextButton.setDisable(true);
        prevButton.setDisable(true);
        PlayButton.setDisable(true);
        likeButton.setDisable(true);

        currentSongTime.setText("0:00");
        totalSongTime.setText("0:00");





        directory = new File("music");
        files = directory.listFiles();


        // Vérification pour s'assurer que le répertoire est correctement identifié
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("Le répertoire 'music' n'existe pas ou n'est pas un répertoire valide.");
            // Ajoutez ici les instructions pour gérer ce cas d'erreur
        } else {
            // Le répertoire existe, affichons son chemin absolu
            System.out.println("Directory path: " + directory.getAbsolutePath());
            files = directory.listFiles();
        }
        VolumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                mediaPlayer.setVolume(VolumeSlider.getValue() * 0.01);
            }
        });

        System.out.println("All good....");
        songProgressBar.setStyle("-fx-accent: #ff7a8a;");
        VolumeSlider.setValue(50);
    }

    @FXML
    protected void likeSong(){
        if(currentSong == null)
                return;

        Song song = currentSong;



        try {
            Connection connection = DBConnexion.getConnection();
            if (connection == null) {
                System.out.println("La connexion à la base de données a échoué.");
                return;
            }

            // Exécuter une requête de mise à jour pour définir isFavorite sur true pour la chanson sélectionnée
            String updateQuery = "UPDATE songs SET isFavorite = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1, !song.getIsFavorite()); // Inverse de like actuel
            preparedStatement.setInt(2, song.getId()); // Récupérer l'ID de la chanson
            int rowsAffected = preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

            if (rowsAffected > 0) {
                System.out.println("Like ajouté / retiré avec succès.");
            } else {
                System.out.println("La chanson n'a pas été trouvée dans la base de données.");
            }

            // Mettre a jour son et interface
            if(song.getIsFavorite()){
                song.setFavorite(false);
                changeHeart(HEART_EMPTY_IMAGE);
            }else{
                song.setFavorite(true);
                changeHeart(HEART_FULL_IMAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void changeHeart(String HEART_TYPE){
        try {
            URL heartFullUrl = getClass().getResource(HEART_TYPE);
            if (heartFullUrl != null) {
                heartImage.setImage(new Image(heartFullUrl.toExternalForm()));
            } else {
                System.err.println("Impossible de charger l'image."+ HEART_TYPE);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " +  HEART_TYPE +" "+ e.getMessage());
        }
    }
    private void changePlayImage(String PLAY_TYPE){
        try {
            URL playUrl = getClass().getResource(PLAY_TYPE);
            if (playUrl != null) {
                playImage.setImage(new Image(playUrl.toExternalForm()));
            } else {
                System.err.println("Impossible de charger l'image."+ PLAY_TYPE);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " +  PLAY_TYPE +" "+ e.getMessage());
        }
    }

    public void playMedia() {
        System.out.println("Begin timer");
        beginTimer();
        System.out.println("Trying to set volume");

        mediaPlayer.setVolume(VolumeSlider.getValue() * 0.01);
        System.out.println("Playing Media");

        mediaPlayer.play();
        System.out.println("Playing Finished");

    }

    public void pauseMedia() {
        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia() {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }


    public void beginTimer() {
        timer = new Timer();
        task = new TimerTask() {

            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if(current/end == 1) {

                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {

        running = false;
        timer.cancel();
    }
    @FXML
    protected void playButtonClicked() {
        // Code pour démarrer la lecture de la musique
        playMedia();

        changePlayImage(PAUSE_IMAGE);
        PlayButton.setOnAction(e -> pauseButtonClicked());
    }

    @FXML
    protected void pauseButtonClicked() {
        // Code pour mettre en pause la musique
        pauseMedia();

        isPlaying = false;
        changePlayImage(PLAY_IMAGE);
        PlayButton.setOnAction(e -> playButtonClicked());
    }


    @FXML
    protected void nextButtonClicked() {
        songNumber = (songNumber + 1) % songs.size();
        playSong(songs.get(songNumber));

        changePlayImage(PAUSE_IMAGE);
        PlayButton.setOnAction(e -> pauseButtonClicked());
    }

    @FXML
    protected void previousButtonClicked(){
        songNumber = (songNumber - 1 + songs.size()) % songs.size();
        playSong(songs.get(songNumber));

        changePlayImage(PAUSE_IMAGE);
        PlayButton.setOnAction(e -> pauseButtonClicked());
    }



    public void playSong(Song song){
        try {
            if(currentSong != null && currentSong.equals(song))
                return;


            if(mediaPlayer != null)
                 mediaPlayer.stop();

            String filePath = URLDecoder.decode(song.getMp3().toString(), "UTF-8");
            filePath = filePath.replace("\\", "/"); // Remplacer les antislash par des slash
            filePath = filePath.replace(" ", "%20"); // Remplacer les espaces par %20


            // Replace / par \

            media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);
            coverSong.setImage(new Image(song.getCover()));
            songTitle.setText(song.getTitle());
            ArtistName.setText(song.getArtist());

            changeHeart( song.getIsFavorite() ? HEART_FULL_IMAGE : HEART_EMPTY_IMAGE);

            playMedia();
            currentSong = song;

            songs.add(song);

            mediaPlayer.setOnEndOfMedia(() -> {
                // Lorsque la lecture est terminée, supprimez la chanson actuelle de la liste
                if (currentSong != null) {
                    songs.remove(currentSong);

                    // Si la liste est vide, arrêtez la lecture
                    if (songs.isEmpty()) {
                        mediaPlayer.stop();
                        songTitle.setText("Now Playing: No Song Selected");
                        return;
                    }
                }

                // Appelez la méthode nextButtonClicked() pour jouer la prochaine chanson
                nextButtonClicked();
            });


            // Gérer les boutons
            if(songs.size() == 1)
            {
                NextButton.setDisable(true);
                prevButton.setDisable(true);
            }
            else
            {
                NextButton.setDisable(false);
                prevButton.setDisable(false);
            }
            PlayButton.setDisable(false);
            likeButton.setDisable(false);

            changePlayImage(PAUSE_IMAGE);
            PlayButton.setOnAction(e -> pauseButtonClicked());

            // Mettre à jour le temps total de la chanson
            mediaPlayer.setOnReady(() -> {
                Duration totalDuration = media.getDuration();
                totalSongTime.setText(formatDuration(totalDuration));
            });

            currentSongTime.setText("0:00");

            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
                currentSongTime.setText(formatDuration(newValue));
            });

        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private String formatDuration(Duration totalDuration) {
        int minutes = (int) totalDuration.toMinutes();
        int seconds = (int) totalDuration.toSeconds() % 60;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    public void addSong(Song song) {
        if(songs.isEmpty())
        {
            playSong(song);
        }else {
            songs.add(song);

        }

        // Gérer les boutons
        if(songs.size() == 1)
        {
            NextButton.setDisable(true);
            prevButton.setDisable(true);
        }
        else
        {
            NextButton.setDisable(false);
            prevButton.setDisable(false);
            PlayButton.setDisable(false);
        }
    };

    public void setId(int id) {
    }
}
