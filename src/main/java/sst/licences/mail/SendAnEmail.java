package sst.licences.mail;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Email;
import sst.licences.model.Membre;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Log4j2
public abstract class SendAnEmail {

    public static final String BCARLONAIS_GMAIL_COM = "bcarlonais@gmail.com";
    public static final String BERGER_CLUB_ARLONAIS = "Berger Club Arlonais";
    static final List<String> emailExceptions = Collections.singletonList("stephane.stiennon@gmail.com");

    private final List<Membre> selectedMembers;

    protected SendAnEmail(List<Membre> selectedMembers) {
        this.selectedMembers = selectedMembers;
    }

    public static void signature(StringBuilder sb) {
        sb.append("\n");
        sb.append("Stéphane\n");
        sb.append("Le Berger Club Arlonais\n");
        sb.append(SendAnEmail.BCARLONAIS_GMAIL_COM + "\n");
    }

    public void sendAnEmail(String password) {
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
        log.info("Session created : {}", session);

        List<Email> emailSent = new ArrayList<>();

        try {
            for (Membre membre : selectedMembers) {
                String emailAddress = membre.getEmail().getAdresse();
                if (Boolean.TRUE.equals(isEligible(emailAddress)) && (!emailSent.contains(membre.getEmail()) || emailExceptions.contains(emailAddress))) {
                    String msg = String.format("%s %s (%s) : ", membre.getPrenom(), membre.getNom(), membre.getEmail());
                    createAndSendEmail(membre, session);
                    log.info("{} Sent.", msg);
                    createHistoricData(membre);
                }

                emailSent.add(membre.getEmail());
            }
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("ERROR : {}", e.getMessage(), e);
//            errors.add(membre);
        }
/*        if (!errors.isEmpty()) {
            log.error("--- Errors List ---");
            for (Membre m : errors) {
                log.error(String.format("%s %s (%s)%n", m.getPrenom(), m.getNom(), m.getEmail()));
            }
            log.error("--- ----------- ---");
        }*/
        LicencesContainer.me().save();
    }

    private void createAndSendEmail(Membre membre, Session session) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        InternetAddress bergerClubArlonais = new InternetAddress(BCARLONAIS_GMAIL_COM, BERGER_CLUB_ARLONAIS);
        message.setFrom(bergerClubArlonais);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(membre.getEmail().getAdresse(), membre.getPrenom() + " " + membre.getNom()));
        message.addRecipient(Message.RecipientType.CC, bergerClubArlonais);
        message.setSubject(messageSubject(), "UTF-8");
        message.setContent(messageBody(membre), "text/plain; charset=UTF-8");
        //message.setContent(messageBody(membre), "text/html; charset=UTF-8");
        //message.setContent(messageBody(membre), "UTF-8");
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

    protected abstract String messageBody(Membre membre);

    protected abstract String messageSubject();

    protected abstract Boolean isEligible(String emailAddress);

    protected abstract void createHistoricData(Membre membre);

    public long eligibleMembresSize() {
        return selectedMembers.size();
    }
}
