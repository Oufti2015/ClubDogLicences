package sst.licences.main.tools.migrate;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import sst.licences.model.Membre;

import java.time.LocalDate;

@Data
public class OldMembre implements Comparable<OldMembre> {
    @NotNull
    @NotEmpty
    private String nom;
    @NotNull
    @NotEmpty
    private String prenom;
    @NotNull
    @NotEmpty
    private String rue;
    @NotNull
    @NotEmpty
    private String num;
    @NotNull
    @NotEmpty
    private String codePostal;
    @NotNull
    @NotEmpty
    private String localite;
    private String telephone;
    private String gsm;
    @NotNull
    private String email;
    @NotNull
    @NotEmpty
    private LocalDate dateDeNaissance;
    @NotNull
    @NotEmpty
    private String codePays;
    @NotNull
    @NotEmpty
    private String langue;
    private String licence;
    @NotNull
    @NotEmpty
    private boolean comite = false;
    private LocalDate affiliation;
    private boolean sentToMyKKusch = false;
    private String accountId;
    private String technicalIdentifer;
    private String description;

    public static String addressId(OldMembre membre) {
        return membre.getRue() + membre.getNum() + membre.getCodePostal() + membre.getLocalite();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OldMembre membre = (OldMembre) o;
        return Objects.equal(nom, membre.nom)
                && Objects.equal(prenom, membre.prenom)
                && Objects.equal(dateDeNaissance, membre.dateDeNaissance)
                && Objects.equal(codePays, membre.codePays)
                && Objects.equal(langue, membre.langue)
                && Objects.equal(rue, membre.rue)
                && Objects.equal(num, membre.num)
                && Objects.equal(codePostal, membre.codePostal)
                && Objects.equal(localite, membre.localite);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nom, prenom, dateDeNaissance, codePays, langue);
    }


    @Override
    public int compareTo(OldMembre o) {
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

    public void update(OldMembre m) {
        this.setLicence(MoreObjects.firstNonNull(m.getLicence(), ""));
        this.setSentToMyKKusch(true);
    }

    public String fullName() {
        return prenom + " " + nom;
    }

    public Membre membre() {
        Membre membre = new Membre();

        membre.setNom(getNom());
        membre.setPrenom(getPrenom());
        membre.setRue(getRue());
        membre.setNum(getNum());
        membre.setCodePostal(getCodePostal());
        membre.setLocalite(getLocalite());
        membre.setTelephone(getTelephone());
        membre.setGsm(getGsm());
        membre.setEmailAddress(getEmail());
        membre.setDateDeNaissance(getDateDeNaissance());
        membre.setCodePays(getCodePays());
        membre.setLangue(getLangue());
        membre.setLicence(getLicence());
        membre.setComite(isComite());
        membre.setAffiliation(getAffiliation());
        membre.setSentToMyKKusch(isSentToMyKKusch());
        membre.addAccount(getAccountId());
        membre.setTechnicalIdentifier(getTechnicalIdentifer());
        membre.setDescription(getDescription());

        return membre;
    }
}
