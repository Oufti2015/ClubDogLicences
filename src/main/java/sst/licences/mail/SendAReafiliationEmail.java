package sst.licences.mail;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.history.History;
import sst.licences.model.Membre;

import java.util.List;

@Log4j2
public class SendAReafiliationEmail extends SendAnEmail {
    static final String MAIL_SUBJECT = "Affiliation";
    private static final String NEXT_YEAR = "2026";
    private static final String currentYear = Integer.toString(java.time.LocalDate.now().getYear());

    public SendAReafiliationEmail(List<Membre> selectedMembers) {
        super(selectedMembers);
    }

    protected static String typeOfAffiliation(List<Membre> composition) {
        StringBuilder sb = new StringBuilder();
        boolean simple = composition.size() == 1;
        sb.append("Pourriez-vous verser ");
        if (simple) {
            sb.append("37€ (affiliation simple)");
        } else {
            sb.append("50 € (affiliation familiale), une seule fois,");
        }
        sb.append(" sur le compte du club ?\n");
        return sb.toString();
    }

    @Override
    protected String messageBody(Membre membre) {
        List<Membre> composition = LicencesContainer.me().compositionFamily(membre);
        StringBuilder sb = new StringBuilder();
        for (Membre m : composition) {
            SendASignaleticCheckEmail.helloWorld(sb, m);
        }
        sb.append("\n");
        if (!NEXT_YEAR.equals(currentYear)) {
            sb.append("Nous sommes presque en " + NEXT_YEAR + ", il va être temps de renouveler votre affiliation à votre club canin préféré: le Berger Club Arlonais.\n");
        } else {
            sb.append("Nous sommes en " + NEXT_YEAR + ", il va être temps de renouveler votre affiliation à votre club canin préféré: le Berger Club Arlonais.\n");
        }
        body(membre, sb, composition);
        return sb.toString();
    }

    protected static void body(Membre membre, StringBuilder sb, List<Membre> composition) {
        sb.append("\n");
        SendASignaleticCheckEmail.signaleticData(membre, composition, sb);
        sb.append(typeOfAffiliation(composition));
        sb.append("\n");
        sb.append(String.format("IBAN: BE71 0689 0687 6669 - BIC: GKCCBEBB - avec la communication : *** %s ***%n", membre.getTechnicalIdentifier()));
        sb.append("\n");
        sb.append("Si vos informations sont incorrectes ou incomplètes,\nSi vous avez déjà payé l'affiliation " + NEXT_YEAR + ",\nSi vous ne comptez plus fréquenter notre club,\nmerci de prendre contact avec moi en répondant à ce mail.\n");
        if (!NEXT_YEAR.equals(currentYear)) {
            sb.append("Toute l'équipe du Berger Club Arlonais vous souhaite de très bonnes fêtes et une très bonne année " + NEXT_YEAR + " à vous et à votre famille.\n");
        } else {
            sb.append("Toute l'équipe du Berger Club Arlonais vous souhaite une très bonne année " + NEXT_YEAR + " à vous et à votre famille.\n");
        }
        signature(sb);
    }

    @Override
    protected String messageSubject() {
        return SendAnEmail.BERGER_CLUB_ARLONAIS + " - " + MAIL_SUBJECT;
    }

    @Override
    protected Boolean isEligible(String emailAddress) {
        return true;
    }

    @Override
    protected void createHistoricData(Membre membre) {
        History.emailAffiliation(membre);
        LicencesContainer.me().save();
    }
}
