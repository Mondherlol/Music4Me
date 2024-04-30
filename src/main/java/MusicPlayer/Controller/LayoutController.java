    package MusicPlayer.Controller;

    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.control.Button;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.layout.VBox;

    import java.io.IOException;

    public class LayoutController {


        @FXML
        private BorderPane rootPane; // référence à votre BorderPane dans le fichier FXML

        @FXML
        protected void changeContent() {
            // Charger la vue "content-view.fxml" dans la zone principale de contenu
            loadView("Home-view.fxml");
        }

        // Méthode pour charger une vue dans la zone principale de contenu
        public void loadView(String fxmlFileName) {
            try {
                // Charger la vue depuis un fichier FXML externe
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application_views/" + fxmlFileName));

                AnchorPane anchorPane = loader.load();

                // Remplacer le contenu actuel par la nouvelle vue
                rootPane.setCenter(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
