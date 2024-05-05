package MusicPlayer.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerController implements Initializable {

    @FXML
    private Label songTitle, ArtistName;
    @FXML
    private Button PlayButton, ResetButton, NextButton, prevButton;
    @FXML
    private Slider VolumeSlider;
    @FXML
    private ProgressBar songProgressBar;

    private boolean isPlaying;
    private Timer timer;
    private TimerTask task;
    private boolean running;
    public ArrayList<File> songs;
    private int songNumber = 0;
    private File directory;
    private File[] files;

    private Media media;
    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
        songs = new ArrayList<File>();

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

            // Vérification pour s'assurer que des fichiers ont été récupérés avec succès
            if(files == null || files.length == 0) {
                System.err.println("Aucun fichier trouvé dans le répertoire 'music'.");
                // Ajoutez ici les instructions pour gérer ce cas d'erreur
            } else {
                // Des fichiers ont été trouvés, ajoutons-les à la liste
                for(File file : files) {
                    System.out.println(file.getName());
                    songs.add(file);
                }

                // Vérifions que la liste contient des éléments
                if (songs.isEmpty()) {
                    System.err.println("La liste des chansons est vide après l'ajout des fichiers du répertoire 'music'.");
                    // Ajoutez ici les instructions pour gérer ce cas d'erreur
                } else {
                    // La liste contient des chansons, nous pouvons maintenant initialiser la première chanson
                    System.out.println("First Song : "+ songs.get(songNumber).toURI().toString());
                    media = new Media(songs.get(songNumber).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    songTitle.setText(songs.get(songNumber).getName());
                    System.out.println("Song Title : "+ songs.get(songNumber).getName());
                }
            }
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

        PlayButton.setText("Pause");
        PlayButton.setOnAction(e -> pauseButtonClicked());
    }

    @FXML
    protected void pauseButtonClicked() {
        // Code pour mettre en pause la musique
        pauseMedia();

        isPlaying = false;
        PlayButton.setText("Play");
        PlayButton.setOnAction(e -> playButtonClicked());
    }

    @FXML
    protected void stopButtonClicked() {
        // Code pour arrêter la musique
        songTitle.setText("Now Playing: No Song Selected");
        isPlaying = false;
        PlayButton.setText("Play");
        PlayButton.setOnAction(e -> playButtonClicked());

    }

    @FXML
    protected void nextButtonClicked() {
        if(songNumber > songs.size() -2) {
            songNumber = 0;
        }else {
            songNumber++;
        }
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songTitle.setText(songs.get(songNumber).getName());
            playMedia();

    }
}
