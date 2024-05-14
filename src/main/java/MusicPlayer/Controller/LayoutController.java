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
        private VBox musicPlayerRoot;

        public MusicPlayerController musicPlayerController;
        @FXML
        protected void changeContent() {
            // Charger la vue "content-view.fxml" dans la zone principale de contenu
            loadView("Home-view.fxml");
        }

        public void initialize()  throws  IOException {
            // Charger le fichier FXML du lecteur de musique
            FXMLLoader playerLoader = new FXMLLoader(getClass().getResource("/application_views/player-view.fxml"));
            VBox playerRoot = playerLoader.load();

            // Récupérer le contrôleur du lecteur de musique
            musicPlayerController = playerLoader.getController();

            // Ajouter le lecteur de musique à la racine de la vue principale
            musicPlayerRoot.getChildren().add(playerRoot);


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
