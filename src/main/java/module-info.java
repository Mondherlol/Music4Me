module org.example.javamusicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens MusicPlayer to javafx.fxml;
    exports MusicPlayer;
    exports MusicPlayer.Controller;

    // Ouvrir le package MusicPlayer.Model à javafx.base
    opens MusicPlayer.Model to javafx.base;

    opens MusicPlayer.Controller to javafx.fxml;
}