package MusicPlayer.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class ContentController {

    @FXML
    private VBox contentContainer;

    @FXML
    private BorderPane rootPane;


    private static ContentController instance;

    public static ContentController getInstance() {
        return instance;
    }

    public void initialize() {
        if(instance==null) {
            instance = this;
        }
    }

    public void ContentController() {
        instance = this;
    }



    // Méthode pour changer le contenu dynamique de la zone principale
    public void changeContent(String fxmlFile) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application_views/" + fxmlFile));

            AnchorPane view = loader.load();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
