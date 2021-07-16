package sst.licences.control;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sst.licences.container.LicencesContainer;
import sst.licences.container.SimpleMembre;

import javax.xml.soap.Text;
import java.awt.event.ActionEvent;

public class MainController {
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
    public void initialize() {
        ObservableList<SimpleMembre> data = mainTableView.getItems();
        LicencesContainer.me().membres().forEach(m -> data.add(new SimpleMembre(m)));
    }

    @FXML
    public void updateAction(javafx.event.ActionEvent actionEvent) {
    }

    @FXML
    public void rowselected(MouseEvent mouseEvent) {
        System.out.println("rowSelected");
        SimpleMembre selectedItem = (SimpleMembre) mainTableView.getSelectionModel().getSelectedItem();
        System.out.println("membre selected : "+selectedItem);

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

        affiliationDatePicker.setDisable(selectedItem.isComite());
    }
}
