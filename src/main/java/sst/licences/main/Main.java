package sst.licences.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sst.licences.container.LicencesContainer;
import sst.licences.control.MainController;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LicencesContainer.load();

        FXMLLoader loader = new FXMLLoader();
        URL fxml = Main.class.getResource("/licenses.fxml");
        loader.setLocation(fxml);

        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Berger Club Arlonais - Licences");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.setMaximized(true);
        InputStream icon = Main.class.getResourceAsStream("/icon.png");
        if (icon != null) {
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(icon)));
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
