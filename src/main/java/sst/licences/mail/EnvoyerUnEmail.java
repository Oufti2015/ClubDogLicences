package sst.licences.mail;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.text.html.Option;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class EnvoyerUnEmail {

    private static final String BCARLONAIS_GMAIL_COM = "bcarlonais@gmail.com";
    private static final String BERGER_CLUB_ARLONAIS = "Berger Club Arlonais";
    private static final String MAIL_SUBJECT = "Berger Club Arlonais - Affiliation";
    private static final List<String> emailExceptions = Collections.singletonList("stephane.stiennon@gmail.com");

    private final Optional<Membre> selectedMembre;

    public EnvoyerUnEmail() {
        this.selectedMembre = Optional.empty();
    }

    public EnvoyerUnEmail(Membre membre) {
        this.selectedMembre = Optional.of(membre);
    }

    public void envoyerAffiliation(String password) {
        Properties props;
        String testMode = System.getenv(LicencesConstants.ENV_TEST_MODE);
        if (testMode == null || testMode.equalsIgnoreCase("True")) {
            props = mailProperties();
        } else {
            props = gmailProperties();
        }

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(BCARLONAIS_GMAIL_COM, password);
                    }
                });
        List<String> emailSent = new ArrayList<>();

        List<Membre> errors = new ArrayList<>();

        List<Membre> membres = eligibleMembres();
        for (Membre membre : membres) {
            if (!emailSent.contains(membre.getEmail()) || emailExceptions.contains(membre.getEmail())) {
                String msg = String.format("%s %s (%s) : ", membre.getPrenom(), membre.getNom(), membre.getEmail());
                try {
                    affiliation(membre, session);
                    log.info(msg + "Sent.");
                } catch (MessagingException | UnsupportedEncodingException e) {
                    log.error(msg + "ERROR : " + e.getMessage(), e);
                    errors.add(membre);
                }
            }

            emailSent.add(membre.getEmail().getAdresse());
        }
        if (!errors.isEmpty()) {
            log.error("--- Errors List ---");
            for (Membre m : errors) {
                log.error(String.format("%s %s (%s)%n", m.getPrenom(), m.getNom(), m.getEmail()));
            }
            log.error("--- ----------- ---");
        }
    }

    private void affiliation(Membre membre, Session session) throws MessagingException, UnsupportedEncodingException {

        sendingMailAffiliation(membre, session);
    }

    private void sendingMailAffiliation(Membre membre, Session session) throws UnsupportedEncodingException, MessagingException {
        Message message = new MimeMessage(session);
        InternetAddress bergerClubArlonais = new InternetAddress(BCARLONAIS_GMAIL_COM, BERGER_CLUB_ARLONAIS);
        message.setFrom(bergerClubArlonais);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(membre.getEmail().getAdresse(), membre.getPrenom() + " " + membre.getNom()));
        message.addRecipient(Message.RecipientType.CC, bergerClubArlonais);
        message.setSubject(MAIL_SUBJECT);
        message.setContent(messageBody(membre), "text/plain; charset=\"UTF-8\"");
        InternetAddress[] replyTo = {bergerClubArlonais,
                new InternetAddress("stephane.stiennon@gmail.com", "Stéphane Stiennon")};
        message.setReplyTo(replyTo);

        Transport.send(message);
    }

    private Properties gmailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private Properties mailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "127.0.0.1");
        props.put("mail.smtp.port", "1025");
        return props;
    }

    private String messageBody(Membre membre) {
        List<Membre> composition = LicencesContainer.me().compositionFamily(membre);
        StringBuilder sb = new StringBuilder();
        for (Membre m : composition) {
            sb.append("Bonjour ").append(m.getPrenom()).append(",\n");
        }
        sb.append("\n");
        sb.append("L'année 2022 arrivant à grand pas, il va être temps de renouveler votre affiliation à votre club canin préféré: le Berger Club Arlonais.\n");
        sb.append("\n");
        sb.append(typeOfAffiliation(composition));
        sb.append("\n");
        sb.append(String.format("IBAN: BE71 0689 0687 6669 - BIC: GKCCBEBB - avec la communication : *** %s ***%n", membre.getTechnicalIdentifer()));
        sb.append("\n");
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
                    m.getEmail()));
        }
        sb.append("\n");
        sb.append("Si vos informations sont incorrectes ou incomplètes,\nSi vous avez déjà payé l'affiliation 2022,\nmerci de prendre contact avec moi en répondant à ce mail.\n");
        //sb.append("Toute l'équipe du Berger Club Arlonais vous souhaite une très bonne année 2022 à vous et à votre famille.\n");
        sb.append("\n");
        sb.append("Stéphane\n");
        sb.append("Le Berger Club Arlonais\n");
        sb.append(BCARLONAIS_GMAIL_COM + "\n");
        return sb.toString();
    }

    private String typeOfAffiliation(List<Membre> composition) {
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

    private List<Membre> eligibleMembres() {
        if (!selectedMembre.isPresent()) {
            LocalDate dateStart = dateStart();
            LocalDate dateEnd = dateEnd();
            List<Membre> collect = LicencesContainer.me().membres()
                    .stream()
                    .filter(m -> !m.isComite() && (m.getAffiliation() == null ||
                            (m.getAffiliation().isAfter(dateStart) && m.getAffiliation().isBefore(dateEnd))))
                    .collect(Collectors.toList());
            log.debug("" + collect.size() + " membres éligibles pour email ! ");
            return collect;
        } else {
            return Collections.singletonList(selectedMembre.get());
        }
    }

    private LocalDate dateEnd() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), Month.SEPTEMBER, 1);
    }

    private LocalDate dateStart() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear() - 1, Month.SEPTEMBER, 1);
    }

    public long eligibleMembresSize() {
        return eligibleMembres().size();
    }
}
