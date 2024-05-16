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
        private VBox musicPlayerRoot, sideBarRooter;


        public MusicPlayerController musicPlayerController;

        public SideBarController sideBarController;
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


            // Charger la sidebar
            FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("/application_views/SideBar.fxml"));
            AnchorPane sidebarRoot = sidebarLoader.load();

            // Récupérer le contrôleur de la sidebar
            sideBarController = sidebarLoader.getController();

            // Ajouter la sidebar à la racine de la vue principale
            sideBarRooter.getChildren().add(sidebarRoot);



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

        // Méthode pour charger une vue dans la zone principale de contenu avec un ID
        public void loadView(String fxmlFileName, int id) {
            try {
                // Charger la vue depuis un fichier FXML externe
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application_views/" + fxmlFileName));
                AnchorPane anchorPane = loader.load();


                // Passer l'ID au contrôleur de la vue chargée
                Object controller = loader.getController();
                if (controller instanceof UpdateSongFormController) {
                    UpdateSongFormController updateSongController = (UpdateSongFormController) controller;
                    updateSongController.setId(id);
                    System.out.println("Passed id to update song controller");
                } else if (controller instanceof PlayListViewController) {
                    PlayListViewController playListController = (PlayListViewController) controller;
                    ((PlayListViewController) controller).setPlaylistId(id);
                    // Handle PlayListViewController here if needed
                }else if(controller instanceof  UpdatePlaylistFormController){
                    UpdatePlaylistFormController updatePlaylistController = (UpdatePlaylistFormController) controller;
                    updatePlaylistController.setPlaylistId(id);
                }

                // Remplacer le contenu actuel par la nouvelle vue
                rootPane.setCenter(anchorPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
