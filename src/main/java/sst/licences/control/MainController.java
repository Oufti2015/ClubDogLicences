package sst.licences.control;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfxtras.scene.control.gauge.linear.BasicRoundDailGauge;
import lombok.extern.log4j.Log4j2;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import sst.licences.bank.BankIdentifierGenerator;
import sst.licences.config.ConfigUtil;
import sst.licences.container.LicencesContainer;
import sst.licences.container.MemberEligibility;
import sst.licences.container.SimpleMembre;
import sst.licences.control.filters.AffiliationFilter;
import sst.licences.control.filters.ComiteFilter;
import sst.licences.copy.TCSInfos;
import sst.licences.excel.*;
import sst.licences.mail.SendAReafiliationEmail;
import sst.licences.mail.SendASignaleticCheckEmail;
import sst.licences.mail.SendAnEmail;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Country;
import sst.licences.model.CountryList;
import sst.licences.model.Membre;
import sst.licences.report.PaiementReport;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
public class MainController {

    private Stage primaryStage;
    @FXML
    private TableView<SimpleMembre> mainTableView;
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
    private TextField filterNom;
    @FXML
    private TextField filterPrenom;
    @FXML
    private TextField filterRue;
    @FXML
    private TextField filterEmail;
    @FXML
    private TextField filterDscp;
    @FXML
    private ComboBox<ComiteFilter> comiteFilterComboBox;
    @FXML
    private ComboBox<AffiliationFilter> affiliationFilterComboBox;
    @FXML
    private CheckBox activeCheck;
    @FXML
    private Button currentYearButton;
    @FXML
    private Button yearPlusOneButton;
    @FXML
    private TextField membersCount;
    @FXML
    private TextField activeMembers;
    @FXML
    private TextField inactiveMembers;
    @FXML
    private TextField membersAffiliated;
    @FXML
    private TextField membersAffiliatedNextYear;
    @FXML
    private HBox tachiBox;
    private final BasicRoundDailGauge roundDailGauge = new BasicRoundDailGauge();
    private final BasicRoundDailGauge roundDailGaugeNY = new BasicRoundDailGauge();
    @FXML
    private TextField boxText;
    @FXML
    private ListView accountsList;

    @FXML
    public void initialize() {
        resetFilter();
        licenceText.setDisable(true);

        paysCombo.setItems(CountryList.getCountryList());
        defaultCountry();

        comiteFilterComboBox.setItems(ComiteFilter.getList());
        affiliationFilterComboBox.setItems(AffiliationFilter.getList());
        defaultComiteFilter();
        defaultAffiliationFilter();

        filterNom.textProperty().addListener((observable, oldValue, newValue) -> filter());
        filterPrenom.textProperty().addListener((observable, oldValue, newValue) -> filter());
        filterRue.textProperty().addListener((observable, oldValue, newValue) -> filter());
        filterEmail.textProperty().addListener((observable, oldValue, newValue) -> filter());
        filterDscp.textProperty().addListener((observable, oldValue, newValue) -> filter());
        comiteFilterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filter());
        affiliationFilterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> filter());

        currentYearButton.setText("" + LocalDate.now().getYear());
        yearPlusOneButton.setText("" + (LocalDate.now().getYear() + 1));

        membersCount();

        tachiBox.getChildren().add(roundDailGauge);
        tachiBox.getChildren().add(roundDailGaugeNY);

        BankIdentifierGenerator.checkTechnicalId();
    }

    private void membersCount() {
        membersCount.setText(LicencesContainer.me().allMembers().size() + " membres");
        int members = LicencesContainer.me().activeMembers().size();
        activeMembers.setText(members + " membres actifs");
        inactiveMembers.setText(LicencesContainer.me().inactiveMembers().size() + " membres inactifs");
        long affiliated = LicencesContainer.me().membres()
                .stream()
                .filter(MemberEligibility::isCurrentYearAffiliated)
                .count();
        membersAffiliated.setText(affiliated + " membres affiliés en " + (LocalDate.now().getYear()));
        long affiliatedNY = LicencesContainer.me().membres()
                .stream()
                .filter(MemberEligibility::isNextYearAffiliated)
                .count();
        membersAffiliatedNextYear.setText(affiliatedNY + " membres affiliés en " + (LocalDate.now().getYear() + 1));

        roundDailGauge.setMaxValue(members);
        roundDailGauge.setValue(affiliated);
        roundDailGaugeNY.setMaxValue(members);
        roundDailGaugeNY.setValue(affiliatedNY);
    }

    private void filter() {
        ObservableList<SimpleMembre> data = mainTableView.getItems();
        data.clear();
        filter(data);
    }

    private void filter(ObservableList<SimpleMembre> data) {
        Stream<Membre> stream = LicencesContainer.me().membres().stream();
        if (!filterNom.getText().isEmpty()) {
            stream = stream.filter(m -> m.getNom().toUpperCase(Locale.ROOT).contains(filterNom.getText().toUpperCase(Locale.ROOT)));
        }
        if (!filterPrenom.getText().isEmpty()) {
            stream = stream.filter(m -> m.getPrenom().toUpperCase(Locale.ROOT).contains(filterPrenom.getText().toUpperCase(Locale.ROOT)));
        }
        if (!filterRue.getText().isEmpty()) {
            stream = stream.filter(m -> m.getRue().toUpperCase(Locale.ROOT).contains(filterRue.getText().toUpperCase(Locale.ROOT)));
        }
        if (!filterEmail.getText().isEmpty()) {
            stream = stream.filter(m -> m.getEmail().getAdresse().toUpperCase(Locale.ROOT).contains(filterEmail.getText().toUpperCase(Locale.ROOT)));
        }
        if (!filterDscp.getText().isEmpty()) {
            stream = stream.filter(m -> m.getDescription() != null && m.getDescription().toUpperCase(Locale.ROOT).contains(filterDscp.getText().toUpperCase(Locale.ROOT)));
        }
        if (comiteFilterComboBox.getSelectionModel().getSelectedItem().equals(ComiteFilter.COMITE)) {
            stream = stream.filter(Membre::isComite);
        } else if (comiteFilterComboBox.getSelectionModel().getSelectedItem().equals(ComiteFilter.MEMBER)) {
            stream = stream.filter(m -> !m.isComite());
        }
        if (affiliationFilterComboBox.getSelectionModel().getSelectedItem().equals(AffiliationFilter.CURRENT)) {
            stream = stream.filter(m -> m.getAffiliation() != null && m.getAffiliation().isAfter(LocalDate.of(LocalDate.now().getYear() - 1, Month.SEPTEMBER, 1)));
        } else if (affiliationFilterComboBox.getSelectionModel().getSelectedItem().equals(AffiliationFilter.NEXT)) {
            stream = stream.filter(m -> m.getAffiliation() != null && m.getAffiliation().isAfter(LocalDate.of(LocalDate.now().getYear(), Month.SEPTEMBER, 1)));
        } else if (affiliationFilterComboBox.getSelectionModel().getSelectedItem().equals(AffiliationFilter.NOPE)) {
            stream = stream.filter(m -> !m.isComite())
                    .filter(m -> m.getAffiliation() == null || m.getAffiliation().isBefore(LocalDate.of(LocalDate.now().getYear() - 1, Month.SEPTEMBER, 1)))
                    .filter(m -> {
                        String payments = LicencesContainer.me().payments(m);
                        return payments.contains("" + LocalDate.now().getYear());
                    });
        }
        stream.forEach(m -> data.add(new SimpleMembre(m)));
    }

    @FXML
    private void resetFilter() {
        filterNom.clear();
        filterPrenom.clear();
        filterRue.clear();
        filterEmail.clear();
        filterDscp.clear();
        defaultComiteFilter();
        defaultAffiliationFilter();

        filter();
    }

    private void defaultCountry() {
        paysCombo.getSelectionModel().select(LicencesConstants.DEFAULT_PAYS_CODE);
    }

    private void defaultComiteFilter() {
        comiteFilterComboBox.getSelectionModel().select(ComiteFilter.ALL);
    }

    private void defaultAffiliationFilter() {
        affiliationFilterComboBox.getSelectionModel().select(AffiliationFilter.ALL);
    }

    @FXML
    public void rowselected(MouseEvent ignoredMouseEvent) {
        SimpleMembre selectedItem = mainTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            licenceText.setText(selectedItem.getLicence());
            dateDeNaissancePicker.setValue(selectedItem.getMembre().getDateDeNaissance());
            nomText.setText(selectedItem.getNom());
            prenomText.setText(selectedItem.getPrenom());
            rueText.setText(selectedItem.getRue());
            numText.setText(selectedItem.getNum());
            boxText.setText(selectedItem.getBox());
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
            techIdLabel.setText(selectedItem.getMembre().getTechnicalIdentifier());
            activeCheck.setSelected(selectedItem.isActive());
            accountsList.getItems().clear();
            accountsList.getItems().addAll(selectedItem.getMembre().getAccounts());

            colorButton(selectedItem);
        }
    }

    private void colorButton(SimpleMembre selectedItem) {
        Membre member = selectedItem.getMembre();
        if (member.isComite() || MemberEligibility.isCurrentYearAffiliated(member)) {
            setGreenButton(currentYearButton);
        } else {
            setRedButton(currentYearButton);
        }
        if (member.isComite() || MemberEligibility.isNextYearAffiliated(member)) {
            setGreenButton(yearPlusOneButton);
        } else {
            setRedButton(yearPlusOneButton);
        }
    }

    @FXML
    public void updateAction(javafx.event.ActionEvent ignoredActionEvent) {
        SimpleMembre selectedItem = mainTableView.getSelectionModel().getSelectedItem();
        Membre membre = selectedItem.getMembre();
        membre.setNom(nomText.getText());
        membre.setPrenom(prenomText.getText());
        membre.setDateDeNaissance(dateDeNaissancePicker.getValue());
        membre.setRue(rueText.getText());
        membre.setNum(numText.getText());
        membre.setBox(boxText.getText());
        membre.setCodePostal(codePostalText.getText());
        membre.setLocalite(localiteText.getText());
        membre.setTelephone(telephoneText.getText());
        membre.setGsm(gsmText.getText());
        membre.setEmailAddress(emailText.getText(), emailOkCheckBox.isSelected());
        membre.setCodePays(paysCombo.getSelectionModel().getSelectedItem().getCode());
        membre.setLangue(langueText.getText());
        membre.setComite(comiteCheck.isSelected());
        membre.setAffiliation(affiliationDatePicker.getValue());
        membre.setDescription(dscpTextArea.getText());
        membre.setActive(activeCheck.isSelected());
        membre.getAccounts().clear();
        membre.getAccounts().addAll(accountsList.getItems());

        updateFamily(membre);

        selectedItem.init(membre);

        LicencesContainer.me().saveJSON();
        reset();
        membersCount();
    }

    private void updateFamily(Membre membre) {
        List<Membre> family = LicencesContainer.me().compositionFamily(membre);
        for (Membre m : family) {
            if (m != membre) {
                m.setRue(membre.getRue());
                m.setNum(membre.getNum());
                m.setBox(membre.getBox());
                m.setCodePostal(membre.getCodePostal());
                m.setLocalite(membre.getLocalite());
                m.setDescription(membre.getDescription());
                m.getAccounts().clear();
                m.getAccounts().addAll(membre.getAccounts());
                m.setAffiliation(membre.getAffiliation());
            }
        }
    }

    @FXML
    public void createAction(ActionEvent ignoredActionEvent) {
        Membre membre = new Membre();

        membre.setNom(nomText.getText());
        membre.setPrenom(prenomText.getText());
        membre.setDateDeNaissance(dateDeNaissancePicker.getValue());
        membre.setRue(rueText.getText());
        membre.setNum(numText.getText());
        membre.setBox(boxText.getText());
        membre.setCodePostal(codePostalText.getText());
        membre.setLocalite(localiteText.getText());
        membre.setTelephone(telephoneText.getText());
        membre.setGsm(gsmText.getText());
        membre.setEmailAddress(emailText.getText());
        membre.setCodePays(paysCombo.getSelectionModel().getSelectedItem().getCode());
        membre.setLangue(langueText.getText());
        membre.setComite(comiteCheck.isSelected());
        membre.setAffiliation(affiliationDatePicker.getValue());
        ObservableList<String> items = accountsList.getItems();
        for (String account : items) {
            membre.addAccount(account);
        }
        membre.setDescription(dscpTextArea.getText());

        membre.setLicence(null);
        membre.setSentToMyKKusch(false);
        membre.setTechnicalIdentifier(BankIdentifierGenerator.newId(membre));

        if (validate(membre)) {
            LicencesContainer.me().addMembre(membre);
            ObservableList<SimpleMembre> data = mainTableView.getItems();
            data.add(new SimpleMembre(membre));

            reset();
            membersCount();
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

    private static final Map<String, String> fields = new HashMap<>();

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
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            result = result.replace(entry.getKey(), fields.get(entry.getValue()));
        }
        return result;
    }

    @FXML
    public void resetAction(ActionEvent ignoredActionEvent) {
        reset();
    }

    @FXML
    private void reset() {
        reset(false);
        BankIdentifierGenerator.checkTechnicalId();
        membersCount();
    }

    private void reset(boolean justCleanForm) {
        licenceText.clear();
        dateDeNaissancePicker.setValue(null);
        nomText.clear();
        prenomText.clear();
        rueText.clear();
        numText.clear();
        boxText.clear();
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
        activeCheck.setSelected(true);
        resetButton(currentYearButton);
        resetButton(yearPlusOneButton);

        if (!justCleanForm) {
            filter();
        }
    }

    private void export(ExcelExporter exporter) {
        try {
            exporter.export();
        } catch (IOException e) {
            e.printStackTrace();
            errorDialog(e.getClass().getSimpleName(), e.getMessage());
        }

        messageDialog("L'export des " + exporter.exportName(), "L'Export est fini.");
    }

    public void exportNewMembers(ActionEvent ignoredActionEvent) {
        export(new NewMembersExporter());
    }

    public void exportCompleteFile(ActionEvent ignoredActionEvent) {
        export(new AllMembersExporter());
    }

    public void exportAffiliateMembers(ActionEvent ignoredActionEvent) {
        export(new AffiliateMembersExporter());
    }

    public void exportNonAffiliateMembers(ActionEvent ignoredActionEvent) {
        export(new NonAffiliateMembersExporter());
    }

    public void importFileFromMYKKUSH(ActionEvent ignoredActionEvent) {
        final FileChooser fileChooser = csvFileChooser("Choisir le fichier de MyKKUSH");
        File file = fileChooser.showOpenDialog(this.primaryStage);
        if (file != null) {
            new Import().importFromCsv(file);
        }

        messageDialog("Import File From MYKKUSH", "MYKKUSH file imported.");
    }

    public void signalticCheck(ActionEvent ignoredActionEvent) {
        SimpleMembre selectedItem = mainTableView.getSelectionModel().getSelectedItem();
        envoyerEmails(new SendASignaleticCheckEmail(Collections.singletonList(selectedItem.getMembre())));
    }

    public void emailForAffiliation(ActionEvent ignoredActionEvent) {
        envoyerEmails(new SendAReafiliationEmail(MemberEligibility.eligibleMembresForAffiliationEmail()));
    }

    public void emailForAffiliationSelected(ActionEvent ignoredActionEvent) {
        SimpleMembre selectedItem = mainTableView.getSelectionModel().getSelectedItem();
        envoyerEmails(new SendAReafiliationEmail(Collections.singletonList(selectedItem.getMembre())));
    }

    private void envoyerEmails(SendAnEmail envoi) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir envoyer " + envoi.eligibleMembresSize() + " e-mail ?",
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (Objects.equals(alert.getResult(), ButtonType.YES)) {
            Optional<String> enterAPassword = enterAPassword();
            if (enterAPassword.isPresent()) {
                envoi.sendAnEmail(enterAPassword.get());
                messageDialog("Email sending", String.format("%d e-mail envoyés !.", envoi.eligibleMembresSize()));
            }
        }
    }

    public void parseBelfius(ActionEvent ignoredActionEvent) {
        try {
            int fileCount = 0;
            FileChooser fileChooser = csvFileChooser("Choisir le fichier Belfius");
            List<File> files = fileChooser.showOpenMultipleDialog(primaryStage);
            BelfiusFile bf = new BelfiusFile();
            if (files != null) {
                fileCount = bf.parseFiles(files);
                LicencesContainer.me().save();
            }

            membersCount();
            messageDialog("Belfius file import", String.format("%d Belfius file(s) processed.", fileCount));
        } catch (Exception e) {
            e.printStackTrace();
            errorDialog(e.getClass().getSimpleName(), e.getMessage());
        }
    }

    private void errorDialog(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Error occured ! Please check log files");
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private void messageDialog(String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("You have a message!");
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    private FileChooser csvFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(ConfigUtil.me().directoriesDownload()));
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().add(filter);

        return fileChooser;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void selectFamily() {
        log.info("Select Family");
        SimpleMembre selectedItem = mainTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            resetFilter();
            ObservableList<SimpleMembre> data = mainTableView.getItems();
            data.clear();
            LicencesContainer.me().compositionFamily(selectedItem.getMembre()).forEach(m -> data.add(new SimpleMembre(m)));
            reset(true);
        }
    }

    private Optional<String> enterAPassword() {
        String password = System.getenv(LicencesConstants.ENV_MAIL_PWD);
        if (password == null) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Send an email");
            dialog.setHeaderText("We need a password to send emails...");
            //dialog.setGraphic(new Circle(15, Color.RED)); // Custom graphic
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            PasswordField pwd = new PasswordField();
            HBox content = new HBox();
            content.setAlignment(Pos.CENTER_LEFT);
            content.setSpacing(10);
            content.getChildren().addAll(new Label("Please enter your email password :"), pwd);
            dialog.getDialogPane().setContent(content);
            dialog.setResultConverter(dialogButton -> {
                if (Objects.equals(dialogButton, ButtonType.OK)) {
                    return pwd.getText();
                }
                return null;
            });

            return dialog.showAndWait();
        }
        return Optional.of(password);
    }

    private String defaultButtonStyle = null;

    private void setGreenButton(Button button) {
        if (defaultButtonStyle == null) {
            defaultButtonStyle = button.getStyle();
        }
        button.setStyle("-fx-background-color: linear-gradient(#3fc135, #a9ff00);\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-text-fill: black;");
    }

    private void setRedButton(Button button) {
        if (defaultButtonStyle == null) {
            defaultButtonStyle = button.getStyle();
        }
        button.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);\n" +
                "    -fx-background-radius: 30;\n" +
                "    -fx-background-insets: 0;\n" +
                "    -fx-text-fill: white;");
    }

    private void resetButton(Button button) {
        if (defaultButtonStyle != null) {
            button.setStyle(defaultButtonStyle);
        }

    }

    public void copyTCSInfos() {
        SimpleMembre selectedItem = mainTableView.getSelectionModel().getSelectedItem();
        new TCSInfos(selectedItem.getMembre()).process();
    }

    private static final List<Double> anounts = Arrays.asList(25.0, 37.0, 50.0, 62.0, 75.0);

    public void coursAndAffiliations(ActionEvent ignoredActionEvent) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        try {
            new PaiementReport().input(LicencesContainer.me().payments()
                            .stream()
                            .filter(p -> p.getDate().compareTo(threeMonthsAgo) > 0)
                            .filter(p -> anounts.contains(p.getMontant())).sorted()
                            .collect(Collectors.toList()))
                    .format();

            messageDialog("Cours et Affiliations", "Rapport généré !");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void addAccount() {
        accountsList.getItems().add(accountText.getText());
        accountText.clear();
    }
}
