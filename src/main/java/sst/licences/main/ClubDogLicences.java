package sst.licences.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.control.MainController;
import sst.licences.report.PaiementReport;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Log4j2
public class ClubDogLicences extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        log.info("---------------------------------------------------------------------------------------------------");
        final Properties properties = new Properties();
        properties.load(ClubDogLicences.class.getResourceAsStream("/licences.properties"));
        log.info("application.version = " + properties.getProperty("application.version"));
        log.info("project.name        = " + properties.getProperty("project.name"));
        log.info("Starting ...");
        checkEnvironmentVariable(LicencesConstants.ENV_MAIL_PWD, false);
        checkEnvironmentVariable(LicencesConstants.ENV_TEST_MODE, true);
        LicencesContainer.loadJSON();

        FXMLLoader loader = new FXMLLoader();
        URL fxml = ClubDogLicences.class.getResource("/licenses.fxml");
        loader.setLocation(fxml);

        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        primaryStage.setTitle(LicencesConstants.APPLICATION_TITLE + " " + properties.getProperty("application.version"));
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.setMaximized(true);
        InputStream icon = ClubDogLicences.class.getResourceAsStream("/icon.png");
        if (icon != null) {
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(icon)));
        }
        primaryStage.setOnCloseRequest(event -> onClose());
        primaryStage.show();
    }

    private void checkEnvironmentVariable(String variable, boolean showValue) {
        String value = System.getenv(variable);
        String result = "OK";
        if (value == null) {
            result = "Not OK";
            log.info(String.format("Env. variable %30s : %6s", variable, result));
        } else {
            log.info(String.format("Env. variable %30s : %6s (Value = %s)", variable, result,
                    showValue ? value : "*****"));

        }
    }

    List<Double> anounts = Arrays.asList(25.0, 37.0, 50.0, 62.0, 75.0);

    private void onClose() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        try {
            new PaiementReport().input(LicencesContainer.me().payments()
                            .stream()
                            .filter(p -> p.getDate().compareTo(threeMonthsAgo) > 0)
                            .filter(p -> anounts.contains(p.getMontant())).sorted()
                            .collect(Collectors.toList()))
                    .format();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        String output = new AllMembersReport().input(LicencesContainer.me().allMembers()).format().output();
//        log.info("output = " + output);
        log.info("... Leaving");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
