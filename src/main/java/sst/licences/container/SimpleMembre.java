package sst.licences.container;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.ToString;
import sst.licences.model.Membre;

@ToString
public class SimpleMembre {
    private final SimpleStringProperty nom = new SimpleStringProperty("");
    private final SimpleStringProperty prenom = new SimpleStringProperty("");
    private final SimpleStringProperty rue = new SimpleStringProperty("");
    private final SimpleStringProperty num = new SimpleStringProperty("");
    private final SimpleStringProperty codePostal = new SimpleStringProperty("");
    private final SimpleStringProperty localite = new SimpleStringProperty("");
    private final SimpleStringProperty telephone = new SimpleStringProperty("");
    private final SimpleStringProperty gsm = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleBooleanProperty emailOk = new SimpleBooleanProperty(true);
    private final SimpleStringProperty dateDeNaissance = new SimpleStringProperty("");
    private final SimpleStringProperty codePays = new SimpleStringProperty("");
    private final SimpleStringProperty langue = new SimpleStringProperty("");
    private final SimpleStringProperty licence = new SimpleStringProperty("");
    private final SimpleBooleanProperty comite = new SimpleBooleanProperty(false);
    private final SimpleStringProperty affiliation = new SimpleStringProperty("");
    private final SimpleStringProperty accountId = new SimpleStringProperty("");
    private final SimpleStringProperty description = new SimpleStringProperty("");

    @Getter
    private Membre membre;

    public SimpleMembre(Membre membre) {
        init(membre);
    }

    public void init(Membre membre) {
        this.membre = membre;
        this.nom.set(membre.getNom());
        this.prenom.set(membre.getPrenom());
        this.rue.set(membre.getRue());
        this.num.set(membre.getNum());
        this.codePostal.set(membre.getCodePostal());
        this.localite.set(membre.getLocalite());
        this.telephone.set(membre.getTelephone());
        this.gsm.set(membre.getGsm());
        this.email.set(membre.getEmail().getAdresse());
        this.emailOk.set(membre.getEmail().getOk());
        this.dateDeNaissance.set(membre.getDateDeNaissance().toString());
        this.codePays.set(membre.getCodePays());
        this.langue.set(membre.getLangue());
        this.licence.set(membre.getLicence());
        this.comite.set(membre.isComite());
        this.accountId.set(membre.getAccountId());
        if (membre.getAffiliation() != null) {
            this.affiliation.set(membre.getAffiliation().toString());
        }
        this.description.set(membre.getDescription());
    }

    public String getNom() {
        return nom.get();
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public String getPrenom() {
        return prenom.get();
    }

    public SimpleStringProperty prenomProperty() {
        return prenom;
    }

    public String getRue() {
        return rue.get();
    }

    public SimpleStringProperty rueProperty() {
        return rue;
    }

    public String getNum() {
        return num.get();
    }

    public SimpleStringProperty numProperty() {
        return num;
    }

    public String getCodePostal() {
        return codePostal.get();
    }

    public SimpleStringProperty codePostalProperty() {
        return codePostal;
    }

    public String getLocalite() {
        return localite.get();
    }

    public SimpleStringProperty localiteProperty() {
        return localite;
    }

    public String getTelephone() {
        return telephone.get();
    }

    public SimpleStringProperty telephoneProperty() {
        return telephone;
    }

    public String getGsm() {
        return gsm.get();
    }

    public SimpleStringProperty gsmProperty() {
        return gsm;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public boolean isEmailOk() {
        return emailOk.get();
    }

    public SimpleBooleanProperty emailOkProperty() {
        return emailOk;
    }

    public String getDateDeNaissance() {
        return dateDeNaissance.get();
    }

    public SimpleStringProperty dateDeNaissanceProperty() {
        return dateDeNaissance;
    }

    public String getCodePays() {
        return codePays.get();
    }

    public SimpleStringProperty codePaysProperty() {
        return codePays;
    }

    public String getLangue() {
        return langue.get();
    }

    public SimpleStringProperty langueProperty() {
        return langue;
    }

    public String getLicence() {
        return licence.get();
    }

    public SimpleStringProperty licenceProperty() {
        return licence;
    }

    public boolean isComite() {
        return comite.get();
    }

    public SimpleBooleanProperty comiteProperty() {
        return comite;
    }

    public String getAffiliation() {
        return affiliation.get();
    }

    public SimpleStringProperty affiliationProperty() {
        return affiliation;
    }

    public String getAccountId() {
        return accountId.get();
    }

    public SimpleStringProperty accountIdProperty() {
        return accountId;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty description() {
        return description;
    }
}
