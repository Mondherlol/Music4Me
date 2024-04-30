module org.example.javamusicplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens MusicPlayer to javafx.fxml;
    exports MusicPlayer;
    exports MusicPlayer.Controller;
    opens MusicPlayer.Controller to javafx.fxml;
}