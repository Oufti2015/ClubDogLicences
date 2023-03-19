package sst.licences.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import sst.licences.exceptions.InvalidOperationException;
import sst.licences.history.History;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Log4j2
@ToString
public class Membre implements Comparable<Membre> {
    public static final String FIELD_NOTES = "Notes";
    public static final String FIELD_TECHNICAL_ID = "Technical Id";
    public static final String FIELD_ACCOUNT_NUMBER = "Account Number";
    public static final String FIELD_MY_K_KUSCH = "MyKKusch";
    public static final String FIELD_AFFILIATION = "Affiliation";
    public static final String FIELD_COMITY = "Comity";
    public static final String FIELD_LICENSE = "License";
    public static final String FIELD_LANGUAGE = "Language";
    public static final String FIELD_COUNTRY = "Country";
    public static final String FIELD_BIRTH_DATE = "Birth Date";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_GSM = "GSM";
    public static final String FIELD_TELEPHONE = "Telephone";
    public static final String FIELD_ADDRESS_CITY = "Address city";
    public static final String FIELD_ADDRESS_POST_CODE = "Address post code";
    public static final String FIELD_ADDRESS_STREET_NUMBER = "Address street number";
    public static final String FIELD_ADDRESS_POSTAL_BOX = "Address postal box";
    public static final String FIELD_ADDRESS_STREET = "Address street";
    public static final String FIELD_FIRSTNAME = "Firstname";
    public static final String FIELD_NAME = "Name";
    public static final String FIELD_ACTIVE_FLAG = "Active flag";

    @NotNull
    @NotEmpty
    @Getter
    private String nom;
    @NotNull
    @NotEmpty
    @Getter
    private String prenom;
    @NotNull
    @NotEmpty
    @Getter
    private String rue;
    @NotNull
    @NotEmpty
    @Getter
    private String num;
    @Getter
    private String box;
    @NotNull
    @NotEmpty
    @Getter
    private String codePostal;
    @NotNull
    @NotEmpty
    @Getter
    private String localite;
    @Getter
    private String telephone;
    @Getter
    private String gsm;
    @NotNull
    @Getter
    private Email email;
    @NotNull
    @NotEmpty
    @Getter
    private LocalDate dateDeNaissance;
    @NotNull
    @NotEmpty
    @Getter
    private String codePays;
    @NotNull
    @NotEmpty
    @Getter
    private String langue;
    @Getter
    private String licence;
    @NotNull
    @NotEmpty
    @Getter
    private boolean comite = false;
    @Getter
    private LocalDate affiliation;
    @Getter
    private boolean sentToMyKKusch = false;
    @Getter
    List<String> accounts = new ArrayList<>();
    @Getter
    private String technicalIdentifier;
    @Getter
    private String description;
    @Getter
    private boolean active = true;
    @Getter
    private final List<HistoryData> history = new ArrayList<>();

    public Membre() {
    }

    /* For JUnit tests */
    public Membre(String nom, String prenom, LocalDate affiliation) {
        this.nom = nom;
        this.prenom = prenom;
        this.affiliation = affiliation;
        this.comite = false;
    }

    public Membre(String nom, String prenom, LocalDate affiliation, boolean comite) {
        this.nom = nom;
        this.prenom = prenom;
        this.comite = comite;
        this.affiliation = affiliation;
    }

    public void setEmailAddress(String address) {
        email = new Email(address);
    }

    public void setEmailAddress(String address, boolean valid) {
        email = new Email(address);
        email.setOk(valid);
    }

    public static String addressId(Membre membre) {
        return membre.getRue() + membre.getNum() + membre.getCodePostal() + membre.getLocalite();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return Objects.equal(nom.toUpperCase(Locale.ROOT), membre.nom.toUpperCase(Locale.ROOT))
                && Objects.equal(prenom.toUpperCase(Locale.ROOT), membre.prenom.toUpperCase(Locale.ROOT))
                //   && Objects.equal(dateDeNaissance, membre.dateDeNaissance)
//                && Objects.equal(codePays, membre.codePays)
//                && Objects.equal(langue, membre.langue)
//                && Objects.equal(rue.toUpperCase(Locale.ROOT), membre.rue.toUpperCase(Locale.ROOT))
//                && Objects.equal(num.toUpperCase(Locale.ROOT), membre.num.toUpperCase(Locale.ROOT))
                //   && Objects.equal(box, membre.box)
                /*&& Objects.equal(codePostal, membre.codePostal)
                && Objects.equal(localite.toUpperCase(Locale.ROOT), membre.localite.toUpperCase(Locale.ROOT))*/;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nom, prenom, dateDeNaissance, codePays, langue);
    }

    @Override
    public int compareTo(Membre o) {
        int result;
        result = nom.compareTo(o.nom);
        if (result == 0) {
            result = prenom.compareTo(o.prenom);
        }
        if (result == 0) {
            result = dateDeNaissance.compareTo(o.dateDeNaissance);
        }
        if (result == 0) {
            result = codePays.compareTo(o.codePays);
        }
        if (result == 0) {
            result = langue.compareTo(o.langue);
        }
        return result;
    }

    public void update(Membre m) {
        this.setLicence(MoreObjects.firstNonNull(m.getLicence(), ""));
        this.setSentToMyKKusch(true);
    }

    public String fullName() {
        return prenom + " " + nom;
    }

    public String fullAddress() {
        return rue + ", " + num + " " + box + " " + codePostal + " " + localite + " (" + codePays + ")";
    }

    public void setNom(String nom) {
        if (this.nom != null) {
            History.history(this, FIELD_NAME, this.nom, nom);
        }
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        if (this.prenom != null) {
            History.history(this, FIELD_FIRSTNAME, this.prenom, prenom);
        }
        this.prenom = prenom;
    }

    public void setRue(String rue) {
        if (this.rue != null) {
            History.history(this, FIELD_ADDRESS_STREET, this.rue, rue);
        }
        this.rue = rue;
    }

    public void setNum(String num) {
        if (this.num != null) {
            History.history(this, FIELD_ADDRESS_STREET_NUMBER, this.num, num);
        }
        this.num = num;
    }

    public void setBox(String box) {
        if (this.box != null) {
            History.history(this, FIELD_ADDRESS_POSTAL_BOX, this.box, box);
        }
        this.box = box;
    }

    public void setCodePostal(String codePostal) {
        if (this.codePostal != null) {
            History.history(this, FIELD_ADDRESS_POST_CODE, this.codePostal, codePostal);
        }
        this.codePostal = codePostal;
    }

    public void setLocalite(String localite) {
        if (this.localite != null) {
            History.history(this, FIELD_ADDRESS_CITY, this.localite, localite);
        }
        this.localite = localite;
    }

    public void setTelephone(String telephone) {
        if (this.telephone != null) {
            History.history(this, FIELD_TELEPHONE, this.telephone, telephone);
        }
        this.telephone = telephone;
    }

    public void setGsm(String gsm) {
        if (this.gsm != null) {
            History.history(this, FIELD_GSM, this.gsm, gsm);
        }
        this.gsm = gsm;
    }

    public void setEmail(Email email) {
        if (this.email != null) {
            History.history(this, FIELD_EMAIL, this.email, email);
        }
        this.email = email;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        if (this.dateDeNaissance != null) {
            History.history(this, FIELD_BIRTH_DATE, this.dateDeNaissance, dateDeNaissance);
        }
        this.dateDeNaissance = dateDeNaissance;
    }

    public void setCodePays(String codePays) {
        if (this.codePays != null) {
            History.history(this, FIELD_COUNTRY, this.codePays, codePays);
        }
        this.codePays = codePays;
    }

    public void setLangue(String langue) {
        if (this.langue != null) {
            History.history(this, FIELD_LANGUAGE, this.langue, langue);
        }
        this.langue = langue;
    }

    public void setLicence(String licence) {
        if (this.licence != null) {
            History.history(this, FIELD_LICENSE, this.licence, licence);
        }
        this.licence = licence;
    }

    public void setComite(boolean comite) {
        if (this.comite != comite) {
            History.history(this, FIELD_COMITY, this.comite, comite);
        }
        this.comite = comite;
    }

    public void setAffiliation(LocalDate affiliation) {
        if (affiliation != null) {
            History.affiliation(this, this.affiliation, affiliation);
        } else {
            History.history(this, FIELD_AFFILIATION, this.affiliation, null);
        }
        this.affiliation = affiliation;
    }

    public void setSentToMyKKusch(boolean sentToMyKKusch) {
        History.history(this, FIELD_MY_K_KUSCH, this.sentToMyKKusch, sentToMyKKusch);
        this.sentToMyKKusch = sentToMyKKusch;
    }

    public void addAccount(String accountId) {
        if (!this.getAccounts().contains(accountId)) {
            History.history(this, FIELD_ACCOUNT_NUMBER, null, accountId);
            this.accounts.add(accountId);
        }
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public void setTechnicalIdentifier(String technicalIdentifier) {
        setTechnicalIdentifier(technicalIdentifier, false);
    }

    public void setTechnicalIdentifier(String technicalIdentifier, boolean force) {
        if (!force && this.technicalIdentifier != null) {
            throw new InvalidOperationException(String.format("Cannot change technicalId from %s to %s on %s", this.technicalIdentifier, technicalIdentifier, fullName()));
        }
        History.history(this, FIELD_TECHNICAL_ID, this.technicalIdentifier, technicalIdentifier);
        this.technicalIdentifier = technicalIdentifier;
    }

    public void setDescription(String description) {
        History.history(this, FIELD_NOTES, this.description, description);
        this.description = description;
    }

    public void setActive(boolean active) {
        if (!active && this.active) {
            History.inactivation(this);
        } else if (active && !this.active) {
            History.history(this, FIELD_ACTIVE_FLAG, false, true);
        }
        this.active = active;
    }

    public void history(HistoryData historyData) {
        history.add(historyData);
    }

    public void clearHistoricData() {
        log.warn("Clearing Historic Data");
        history.forEach(log::warn);
        history.clear();
    }

    public long daysFromLastAffiliationEmail() {
        Optional<HistoryData> first = history
                .stream()
                .filter(h -> HistoryData.ActionType.EMAIL_AFFILIATION.equals(h.getAction()))
                .max(Comparator.comparing(HistoryData::getTime));
        return first.map(historyData -> ChronoUnit.DAYS.between(historyData.getTime(), LocalDateTime.now()))
                .orElse(9999L);
    }

    public static boolean memberCompare(Membre membre1, Membre membre2) {
        if (membre2 == membre1) return true;
        if (membre1 == null || membre2 == null) return false;
        boolean result = true;

        if (!Objects.equal(membre1.dateDeNaissance, membre2.dateDeNaissance)) {
            log.warn("{} has different birth date [{}] != [{}]", membre1.fullName(), membre1.dateDeNaissance, membre2.dateDeNaissance);
            result = false;
        }
        if (!Objects.equal(membre1.codePays, membre2.codePays)) {
            log.warn("{} has different country [{}] != [{}]", membre1.fullName(), membre1.codePays, membre2.codePays);
            result = false;
        }
        if (!Objects.equal(membre1.langue, membre2.langue)) {
            log.warn("{} has different language [{}] != [{}]", membre1.fullName(), membre1.langue, membre2.langue);
            result = false;
        }
        if (!Objects.equal(membre1.rue, membre2.rue)) {
            log.warn("{} has different street [{}] != [{}]", membre1.fullName(), membre1.rue, membre2.rue);
            result = false;
        }
        if (!Objects.equal(membre1.num, membre2.num)) {
            log.warn("{} has different street number [{}] != [{}]", membre1.fullName(), membre1.num, membre2.num);
            result = false;
        }
        if (!Objects.equal(MoreObjects.firstNonNull(membre1.box, ""), membre2.box)) {
            log.warn("{} has different box [{}] != [{}]", membre1.fullName(), membre1.box, membre2.box);
            result = false;
        }
        if (!Objects.equal(membre1.codePostal, membre2.codePostal)) {
            log.warn("{} has different postal code [{}] != [{}]", membre1.fullName(), membre1.codePostal, membre2.codePostal);
            result = false;
        }
        if (!Objects.equal(membre1.localite, membre2.localite)) {
            log.warn("{} has different city [{}] != [{}]", membre1.fullName(), membre1.localite, membre2.localite);
            result = false;
        }
        return result;
    }
}
