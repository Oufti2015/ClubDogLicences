package sst.licences.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import lombok.Data;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.apache.commons.beanutils.BeanUtils;

import java.time.LocalDate;

@Data
public class Membre implements Comparable<Membre> {
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
    @NotEmpty
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return Objects.equal(MoreObjects.firstNonNull(licence, ""), MoreObjects.firstNonNull(membre.licence, ""))
                && Objects.equal(nom, membre.nom)
                && Objects.equal(prenom, membre.prenom)
                && Objects.equal(dateDeNaissance, membre.dateDeNaissance)
                && Objects.equal(codePays, membre.codePays)
                && Objects.equal(langue, membre.langue);
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
}
