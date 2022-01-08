package sst.licences.copy;

import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

public class TCSInfos extends CopyToCliboard {
    public static final String A_COMPLETER = "<!!! A COMPLETER !!!>";

    public TCSInfos(Membre membre) {
        String pattern = "A propos du chien :\n" +
                "Nom du chien: %s\n" +
                "Race: %s\n" +
                "Date de naissance: %s\n" +
                "Sexe: %s\n" +
                "Numéro de Tatouage ou de puce: %s\n" +
                "Numéro de Pédigrée: %s\n" +
                "Club: " + LicencesConstants.BERGER_CLUB_ARLONAIS + "\n" +
                "\n" +
                "A propos du propriétaire :\n" +
                "Nom: %s\n" +
                "Prénom: %s\n" +
                "Rue + n°: %s\n" +
                "Code postal: %s\n" +
                "Commune: %s\n" +
                "Pays: %s\n" +
                "Téléphone: %s\n" +
                "Email: %s\n";

        this.setStringContent(String.format(pattern,
                membre.getDescription(),
                A_COMPLETER, /* Race */
                A_COMPLETER, /* Date de naissance */
                A_COMPLETER, /* Sexe */
                A_COMPLETER, /* Numéro de Tatouage ou de puce */
                A_COMPLETER, /* Numéro de Pédigrée */
                membre.getNom(),
                membre.getPrenom(),
                membre.getRue() + ", " + membre.getNum(),
                membre.getCodePostal(),
                membre.getLocalite(),
                membre.getCodePays(),
                membre.getGsm(),
                membre.getEmail().getAdresse()));
    }


}
