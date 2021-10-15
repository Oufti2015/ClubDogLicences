package sst.licences.control;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j2;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import sst.licences.bank.BankIdentifierGenerator;
import sst.licences.container.LicencesContainer;
import sst.licences.container.SimpleMembre;
import sst.licences.excel.AllMembersExporter;
import sst.licences.excel.ExcelExporter;
import sst.licences.excel.Import;
import sst.licences.excel.NewMembersExporter;
import sst.licences.mail.EnvoyerUnEmail;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
public class MainController {
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    private Stage primaryStage;
    @FXML
    private TableView mainTableView;
    @FXML
    private TextField licenceText;
    @FXML
    private DatePicker dateDeNaissancePicker;
    @FXML
    private DatePicker affiliationDatePicker;
    @FXML
    private TextField nomText;
    @FXML
    private TextField prenomText;
    @FXML
    private TextField rueText;
    @FXML
    private TextField numText;
    @FXML
    private TextField codePostalText;
    @FXML
    private TextField localiteText;
    @FXML
    private TextField telephoneText;
    @FXML
    private TextField gsmText;
    @FXML
    private TextField emailText;
    @FXML
    private TextField paysText;
    @FXML
    private TextField langueText;
    @FXML
    private CheckBox comiteCheck;
    @FXML
    private TextField accountText;

    @FXML
    public void initialize() {
        ObservableList<SimpleMembre> data = mainTableView.getItems();
        LicencesContainer.me().membres().stream().sorted().forEach(m -> data.add(new SimpleMembre(m)));
        licenceText.setDisable(true);
    }

    @FXML
    public void rowselected(MouseEvent mouseEvent) {
        SimpleMembre selectedItem = (SimpleMembre) mainTableView.getSelectionModel().getSelectedItem();

        licenceText.setText(selectedItem.getLicence());
        dateDeNaissancePicker.setValue(selectedItem.getMembre().getDateDeNaissance());
        nomText.setText(selectedItem.getNom());
        prenomText.setText(selectedItem.getPrenom());
        rueText.setText(selectedItem.getRue());
        numText.setText(selectedItem.getNum());
        codePostalText.setText(selectedItem.getCodePostal());
        localiteText.setText(selectedItem.getLocalite());
        telephoneText.setText(selectedItem.getTelephone());
        gsmText.setText(selectedItem.getGsm());
        emailText.setText(selectedItem.getEmail());
        paysText.setText(selectedItem.getCodePays());
        langueText.setText(selectedItem.getLangue());
        comiteCheck.setSelected(selectedItem.isComite());
        affiliationDatePicker.setValue(selectedItem.getMembre().getAffiliation());
        accountText.setText(selectedItem.getAccountId());
        affiliationDatePicker.setDisable(selectedItem.isComite());
    }

    @FXML
    public void updateAction(javafx.event.ActionEvent actionEvent) {
        SimpleMembre selectedItem = (SimpleMembre) mainTableView.getSelectionModel().getSelectedItem();
        Membre membre = selectedItem.getMembre();
        membre.setNom(nomText.getText());
        membre.setPrenom(prenomText.getText());
        membre.setDateDeNaissance(dateDeNaissancePicker.getValue());
        membre.setRue(rueText.getText());
        membre.setNum(numText.getText());
        membre.setCodePostal(codePostalText.getText());
        membre.setLocalite(localiteText.getText());
        membre.setTelephone(telephoneText.getText());
        membre.setGsm(gsmText.getText());
        membre.setEmail(emailText.getText());
        membre.setCodePays(paysText.getText());
        membre.setLangue(langueText.getText());
        membre.setComite(comiteCheck.isSelected());
        membre.setAffiliation(affiliationDatePicker.getValue());
        membre.setAccountId(accountText.getText());

        LicencesContainer.me().save();
    }

    @FXML
    public void createAction(ActionEvent actionEvent) {
        Membre membre = new Membre();

        membre.setNom(nomText.getText());
        membre.setPrenom(prenomText.getText());
        membre.setDateDeNaissance(dateDeNaissancePicker.getValue());
        membre.setRue(rueText.getText());
        membre.setNum(numText.getText());
        membre.setCodePostal(codePostalText.getText());
        membre.setLocalite(localiteText.getText());
        membre.setTelephone(telephoneText.getText());
        membre.setGsm(gsmText.getText());
        membre.setEmail(emailText.getText());
        membre.setCodePays(paysText.getText());
        membre.setLangue(langueText.getText());
        membre.setComite(comiteCheck.isSelected());
        membre.setAffiliation(affiliationDatePicker.getValue());
        membre.setAccountId(accountText.getText());

        membre.setLicence(null);
        membre.setSentToMyKKusch(false);
        membre.setTechnicalIdentifer(BankIdentifierGenerator.newId(membre));

        if (validate(membre)) {
            LicencesContainer.me().addMembre(membre);
            ObservableList<SimpleMembre> data = mainTableView.getItems();
            data.add(new SimpleMembre(membre));

            reset();
        }
    }

    private boolean validate(Membre membre) {
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(membre);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation violation : violations) {
                stringBuilder.append(message(violation.getMessage()));
                stringBuilder.append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Création d'un nouveau membre");
            alert.setHeaderText("Nous ne pouvons pas créer un nouveau membre");
            alert.setContentText(stringBuilder.toString());

            alert.showAndWait();

            return false;
        }
        return true;
    }

    private static Map<String, String> fields = new HashMap<>();

    static {
        fields.put("sst.licences.model.Membre.nom", "Le champ 'Nom'");
        fields.put("sst.licences.model.Membre.prenom", "Le champ 'Prénom'");
        fields.put("sst.licences.model.Membre.rue", "Le champ 'Rue'");
        fields.put("sst.licences.model.Membre.num", "Le champ 'Numéro'");
        fields.put("sst.licences.model.Membre.codePostal", "Le champ 'Code Postal'");
        fields.put("sst.licences.model.Membre.localite", "Le champ 'Localité'");
        fields.put("sst.licences.model.Membre.email", "Le champ 'E-Mail'");
        fields.put("sst.licences.model.Membre.dateDeNaissance", "Le champ 'Date De Naissance'");
        fields.put("sst.licences.model.Membre.codePays", "Le champ 'Pays'");
        fields.put("sst.licences.model.Membre.langue", "Le champ 'Langue'");
    }

    private String message(String message) {
        String result = message;
        for (String key : fields.keySet()) {
            result = result.replace(key, fields.get(key));
        }
        return result;
    }

    @FXML
    public void resetAction(ActionEvent actionEvent) {
        reset();
    }

    @FXML
    private void reset() {
        licenceText.setText("");
        dateDeNaissancePicker.setValue(null);
        nomText.setText("");
        prenomText.setText("");
        rueText.setText("");
        numText.setText("");
        codePostalText.setText("");
        localiteText.setText("");
        telephoneText.setText("");
        gsmText.setText("");
        emailText.setText("");
        paysText.setText("");
        langueText.setText("");
        accountText.setText("");

        comiteCheck.setSelected(false);
        affiliationDatePicker.setValue(null);
    }

    public void exportFileForMYKKUSH(ActionEvent actionEvent) {
        ExcelExporter excel = new NewMembersExporter();
        try {
            excel.export();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportCompleteFile(ActionEvent actionEvent) {
        ExcelExporter excel = new AllMembersExporter();
        try {
            excel.export();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importFileFromMYKKUSH(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
            log.debug("file selected = " + file);
            new Import().importFromCsv(file);
        }
    }

    public void emailForAffiliation(ActionEvent actionEvent) {
        EnvoyerUnEmail envoi = new EnvoyerUnEmail();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir envoyer " + envoi.eligibleMembresSize() + " e-mail ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            envoi.envoyerAffiliation();
            alert = new Alert(Alert.AlertType.INFORMATION, envoi.eligibleMembresSize() + " e-mail envoyés !",
                    ButtonType.OK);
            alert.showAndWait();
        }
    }

    class VirementData {
        LocalDate date;
        String compte;
        String nom;
        Double montant;
        String communications;
    }

    public void parseBelfius(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
        parseFiles(files);
        LicencesContainer.me().save();
    }

    private void parseFiles(List<File> files) {
        List<VirementData> belfius = new ArrayList<>();
        for (File file : files) {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), CHARSET);
                 CSVReader reader = new CSVReaderBuilder(inputStreamReader)
                         .withCSVParser(csvParser)   // custom CSV parser
                         .withSkipLines(13)           // skip the first line, header info
                         .build()) {
                List<String[]> lines = reader.readAll();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (String[] data : lines) {
                    Double montant = Double.parseDouble(data[10].replace(",", "."));

                    if (montant.compareTo(0.00) > 0) {
                        VirementData vd = new VirementData();
                        vd.date = LocalDate.parse(data[1], formatter);
                        vd.compte = data[4];
                        vd.nom = data[5];
                        vd.montant = montant;
                        vd.communications = data[14];

                        belfius.add(vd);
                    }
                }

                updateMembres(belfius);
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMembres(List<VirementData> belfius) {
        for (Membre membre : LicencesContainer.me().membres()) {
            List<VirementData> vds = belfius.stream()
                    .filter(vd -> membre.getTechnicalIdentifer() != null
                            && vd.communications != null
                            && vd.communications.contains(membre.getTechnicalIdentifer()))
                    .collect(Collectors.toList());
            for (VirementData vd : vds) {
                if (membre.getAffiliation() == null || membre.getAffiliation().isBefore(vd.date)) {
                    membre.setAffiliation(vd.date);
                    membre.setAccountId(vd.compte);

                    log.debug(membre.getPrenom() + " " + membre.getNom() + " est réaffilié " + vd.date);
                }
            }
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
