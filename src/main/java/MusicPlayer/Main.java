package MusicPlayer;

import MusicPlayer.Controller.LayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Main instance;
    private Stage primaryStage;

    public static Main getInstance() {
        return instance;
    }
    private LayoutController layoutController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        primaryStage = stage;

        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application_views/main-layout.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur de la vue principale
        layoutController = loader.getController();

        layoutController.loadView("Home-view.fxml");

        // Créer une scène
        Scene scene = new Scene(root, 750, 510);

        // Définir le titre de la fenêtre
        primaryStage.setTitle("Music Player");

        // Définir la scène dans la fenêtre principale
        primaryStage.setScene(scene);

        // Afficher la fenêtre
        primaryStage.show();
    }

    public void loadView(String fxmlFileName) {
        layoutController.loadView(fxmlFileName);
    }
}
