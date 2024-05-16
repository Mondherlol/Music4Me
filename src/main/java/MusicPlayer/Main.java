package MusicPlayer;

import MusicPlayer.Controller.LayoutController;
import MusicPlayer.Controller.MusicPlayerController;
import MusicPlayer.Utils.DBConnexion;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Main extends Application {

    private static Main instance;
    private Stage primaryStage;

    public static Main getInstance() {
        return instance;
    }
    public LayoutController layoutController;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        primaryStage = stage;

        ConnectToDb(); // Connexion à la base de données


        // Charger le fichier FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application_views/main-layout.fxml"));
        Parent root = loader.load();

        // Récupérer le contrôleur de la vue principale
        layoutController = loader.getController();

        layoutController.loadView("Home-view.fxml");

        // Créer une scène
        Scene scene = new Scene(root, 850, 670);

        // Définir le titre de la fenêtre
        primaryStage.setTitle("Music Player");

        // Définir la scène dans la fenêtre principale
        primaryStage.setScene(scene);

        // Afficher la fenêtre
        primaryStage.show();
    }

    public void ConnectToDb() {
        Connection connection = DBConnexion.getConnection();
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué. L'application se ferme.");
            return;
        }
        System.out.println("Connexion à la base de données établie avec succès.");
    }

    public void loadView(String fxmlFileName) {
        layoutController.loadView(fxmlFileName);
    }

    public void loadView(String fxmlFileName, int id) {
        layoutController.loadView(fxmlFileName, id);
    }
}
