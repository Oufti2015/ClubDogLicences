package sst.licences.mail;

import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.util.List;

public class SendASignaleticCheckEmail extends SendAnEmail {
    public SendASignaleticCheckEmail(List<Membre> selectedMembers) {
        super(selectedMembers);
    }

    public static void helloWorld(StringBuilder sb, Membre m) {
        sb.append("Bonjour ").append(m.getPrenom()).append(",\n");
    }

    public static void signaleticData(Membre membre, List<Membre> composition, StringBuilder sb) {
        sb.append(String.format("Votre adresse postale : %s, %s - %s %s%n", membre.getRue(), membre.getNum(), membre.getCodePostal(), membre.getLocalite()));
        sb.append("\n");
        sb.append("La composition de votre famille inscrite au club :\n");
        sb.append("\n");
        String[] mois = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};

        for (Membre m : composition) {
            sb.append(String.format("%s %s né(e) le %d %s %d (%s)%n",
                    m.getPrenom(),
                    m.getNom(),
                    m.getDateDeNaissance().getDayOfMonth(),
                    mois[m.getDateDeNaissance().getMonth().getValue() - 1],
                    m.getDateDeNaissance().getYear(),
                    m.getEmail().getAdresse()));
        }
        sb.append("\n");
    }

    @Override
    protected String messageBody(Membre membre) {
        List<Membre> composition = LicencesContainer.me().compositionFamily(membre);
        StringBuilder sb = new StringBuilder();
        for (Membre m : composition) {
            SendASignaleticCheckEmail.helloWorld(sb, m);
        }
        sb.append("\n");
        sb.append("Votre carte de membre du Berger Club Arlonais qui vous a été envoyée par la poste nous est revenue,\n");
        sb.append("l'adresse n'étant apparemment pas correcte.\n");

        sb.append("Pouvez-vous vérifier que les données que nous possèdons vous concernant sont correctes ?\n");
        sb.append("\n");
        SendASignaleticCheckEmail.signaleticData(membre, composition, sb);

        sb.append("Si vos informations sont incorrectes ou incomplètes,\nmerci de prendre contact avec moi en répondant à ce mail.\n");

        SendAnEmail.signature(sb);

        return sb.toString();
    }

    @Override
    protected String messageSubject() {
        return SendAnEmail.BERGER_CLUB_ARLONAIS + " - Vérifions ensemble vos données";
    }

    @Override
    protected Boolean isEligible(String emailAddress) {
        return true;
    }
}
