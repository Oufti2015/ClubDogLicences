package sst.licences.mail;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SendAReafiliationEmail extends SendAnEmail {
    static final String MAIL_SUBJECT = "Affiliation";

    public SendAReafiliationEmail(List<Membre> selectedMembers) {
        super(selectedMembers);
    }

    private static String typeOfAffiliation(List<Membre> composition) {
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
        sb.append("L'année 2022 arrivant à grand pas, il va être temps de renouveler votre affiliation à votre club canin préféré: le Berger Club Arlonais.\n");
        sb.append("\n");
        sb.append(typeOfAffiliation(composition));
        sb.append("\n");
        sb.append(String.format("IBAN: BE71 0689 0687 6669 - BIC: GKCCBEBB - avec la communication : *** %s ***%n", membre.getTechnicalIdentifer()));
        sb.append("\n");
        SendASignaleticCheckEmail.signaleticData(membre, composition, sb);
        sb.append("Si vos informations sont incorrectes ou incomplètes,\nSi vous avez déjà payé l'affiliation 2022,\nmerci de prendre contact avec moi en répondant à ce mail.\n");
        //sb.append("Toute l'équipe du Berger Club Arlonais vous souhaite une très bonne année 2022 à vous et à votre famille.\n");
        signature(sb);
        return sb.toString();
    }

    @Override
    protected String messageSubject() {
        return SendAnEmail.BERGER_CLUB_ARLONAIS + " - " + MAIL_SUBJECT;
    }

    @Override
    protected Boolean isEligible(String emailAddress) {
        return true;
    }

    public static List<Membre> eligibleMembres() {
        LocalDate dateStart = dateStart();
        LocalDate dateEnd = dateEnd();
        List<Membre> collect = LicencesContainer.me().membres()
                .stream()
                .filter(m -> !m.isComite() && (m.getAffiliation() == null ||
                        (m.getAffiliation().isAfter(dateStart) && m.getAffiliation().isBefore(dateEnd))))
                .collect(Collectors.toList());
        log.debug("" + collect.size() + " membres éligibles pour email ! ");
        return collect;
    }

    private static LocalDate dateEnd() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), Month.SEPTEMBER, 1);
    }

    private static LocalDate dateStart() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear() - 1, Month.SEPTEMBER, 1);
    }
}
