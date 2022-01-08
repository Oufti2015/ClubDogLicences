package sst.licences.copy;

import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

public class TCSInfos extends CopyToCliboard {
    public static final String A_COMPLETER = "<!!! A COMPLETER !!!>";

    public TCSInfos(Membre membre) {
        String pattern = "Bonjour %s,\n" +
                "\n" +
                "Pouvez-vous vérifier que ces infos sont correctes ?\n" +
                "\nA propos du chien :\n" +
                "Nom du chien: <b>%s</b>\n" +
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
                "Email: %s\n" +
                "\n" +
                "Bien à vous,\n" +
                "Stéphane\n";

        this.setStringContent(String.format(pattern,
                membre.getPrenom(),
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
