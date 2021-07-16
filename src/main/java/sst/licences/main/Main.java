package sst.licences.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sst.licences.container.LicencesContainer;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        LicencesContainer.load();

        FXMLLoader loader = new FXMLLoader();
        URL resource = Main.class.getResource("/licenses.fxml");
        loader.setLocation(resource);

        Parent root = loader.load();
        primaryStage.setTitle("Berger Club Arlonais - Licences");
        primaryStage.setScene(new Scene(root, 1500, 700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
