package sst.licences.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import sst.licences.bank.BankIdentifierGenerator;
import sst.licences.container.LicencesContainer;
import sst.licences.container.SimpleMembre;
import sst.licences.excel.*;
import sst.licences.mail.EnvoyerUnEmail;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Country;
import sst.licences.model.CountryList;
import sst.licences.model.Membre;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
public class MainController {

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
    private ComboBox<Country> paysCombo;
    @FXML
    private TextField langueText;
    @FXML
    private CheckBox comiteCheck;
    @FXML
    private TextField accountText;
    @FXML
    private TextArea dscpTextArea;
    @FXML
    private TextArea paymentsTextArea;
    @FXML
    private TextField techIdLabel;
    @FXML
    private CheckBox emailOkCheckBox;

    @FXML
    public void initialize() {
        ObservableList<SimpleMembre> data = mainTableView.getItems();
        LicencesContainer.me().membres().stream().sorted().forEach(m -> data.add(new SimpleMembre(m)));
        licenceText.setDisable(true);

        paysCombo.setItems(CountryList.getCountryList());
        defaultCountry();
    }

    private void defaultCountry() {
        paysCombo.getSelectionModel().select(LicencesConstants.DEFAULT_PAYS_CODE);
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
        emailOkCheckBox.setSelected(selectedItem.isEmailOk());
        paysCombo.setValue(CountryList.country(selectedItem.getCodePays()).orElse(LicencesConstants.DEFAULT_PAYS_CODE));
        langueText.setText(selectedItem.getLangue());
        comiteCheck.setSelected(selectedItem.isComite());
        affiliationDatePicker.setValue(selectedItem.getMembre().getAffiliation());
        accountText.setText(selectedItem.getAccountId());
        affiliationDatePicker.setDisable(selectedItem.isComite());
        dscpTextArea.setText(selectedItem.getDescription());
        paymentsTextArea.setText(LicencesContainer.me().payments(selectedItem.getMembre()));
        techIdLabel.setText(selectedItem.getMembre().getTechnicalIdentifer());
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
        membre.setEmailAddress(emailText.getText(), emailOkCheckBox.isSelected());
        membre.setCodePays(paysCombo.getSelectionModel().getSelectedItem().getCode());
        membre.setLangue(langueText.getText());
        membre.setComite(comiteCheck.isSelected());
        membre.setAffiliation(affiliationDatePicker.getValue());
        membre.setAccountId(accountText.getText());
        membre.setDescription(dscpTextArea.getText());

        selectedItem.init(membre);

        LicencesContainer.me().save();
        reset();
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
        membre.setEmailAddress(emailText.getText());
        membre.setCodePays(paysCombo.getSelectionModel().getSelectedItem().getCode());
        membre.setLangue(langueText.getText());
        membre.setComite(comiteCheck.isSelected());
        membre.setAffiliation(affiliationDatePicker.getValue());
        membre.setAccountId(accountText.getText());
        membre.setDescription(dscpTextArea.getText());

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
        licenceText.clear();
        dateDeNaissancePicker.setValue(null);
        nomText.clear();
        prenomText.clear();
        rueText.clear();
        numText.clear();
        codePostalText.clear();
        localiteText.clear();
        telephoneText.clear();
        gsmText.clear();
        emailText.clear();
        emailOkCheckBox.setSelected(true);
        defaultCountry();
        langueText.setText(LicencesConstants.DEFAULT_LANGUE);
        accountText.clear();
        dscpTextArea.clear();
        paymentsTextArea.clear();
        techIdLabel.clear();
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
        final FileChooser fileChooser = csvFileChooser("Choisir le fichier de MyKKUSH");
        File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
            log.debug("file selected = " + file);
            new Import().importFromCsv(file);
        }
    }

    public void emailForAffiliation(ActionEvent actionEvent) {
        envoyerEmails(new EnvoyerUnEmail());
    }

    public void emailForAffiliationSelected(ActionEvent actionEvent) {
        SimpleMembre selectedItem = (SimpleMembre) mainTableView.getSelectionModel().getSelectedItem();
        envoyerEmails(new EnvoyerUnEmail(selectedItem.getMembre()));
    }

    private void envoyerEmails(EnvoyerUnEmail envoi) {
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


    public void parseBelfius(ActionEvent actionEvent) {
        FileChooser fileChooser = csvFileChooser("Choisir le fichier Belfius");
        List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
        BelfiusFile bf = new BelfiusFile();
        if (files != null) {
            bf.parseFiles(files);
            LicencesContainer.me().save();
        }
    }

    private FileChooser csvFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(LicencesConstants.WORKING_FOLDER));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().add(filter);

        return fileChooser;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
