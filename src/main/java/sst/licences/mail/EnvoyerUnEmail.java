package sst.licences.mail;

import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class EnvoyerUnEmail {

    public void envoyerAffiliation() {
        List<Membre> membres = eligibleMembres();
        for (Membre membre : membres) {
            System.out.println(membre.getNom() + " " + membre.getPrenom());
            affiliation(membre);
        }
    }

    private void affiliation(Membre membre) {
        final String username = "bcarlonais@gmail.com";
        final String password = System.getenv("MAIL_PWD");

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });


        try {
            Message msg = new MimeMessage(session);
            InternetAddress berger_club_arlonais = new InternetAddress("bcarlonais@gmail.com", "Berger Club Arlonais");
            msg.setFrom(berger_club_arlonais);
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(membre.getEmail(), membre.getPrenom() + " " + membre.getNom()));
            msg.addRecipient(Message.RecipientType.CC, berger_club_arlonais);
            msg.setSubject("Berger Club Arlonais - Affiliation");
            msg.setText(messageBody(membre));
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String messageBody(Membre membre) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bonjour " + membre.getPrenom() + ",\n");
        sb.append("\n");
        sb.append("L'année 2022 arrivant à grand pas, il va être temps de renouveler votre affiliation à votre club canin préféré: le Berger Club Arlonais.\n");
        sb.append("\n");
        sb.append("Pourriez-vous verser 37€ (affiliation simple) ou 50 € (affiliation familiale) sur le compte du club ?\n");
        sb.append("\n");
        sb.append("IBAN: BE71 0689 0687 6669 - BIC: GKCCBEBB\n");
        sb.append("\n");
        sb.append("Toute l'équipe du Berger Club Arlonais vous souhaite une très bonne année 2022 à vous et à votre famille.\n");
        sb.append("\n");
        sb.append("Le Berger Club Arlonais\n");
        sb.append("bcarlonais@gmail.com\n");
        return sb.toString();
    }

    private List<Membre> eligibleMembres() {
        return LicencesContainer.me().membres().stream().filter(m -> !m.isComite() && m.getAffiliation() == null).collect(Collectors.toList());
    }

    public long eligibleMembresSize() {
        return LicencesContainer.me().membres().stream().filter(m -> !m.isComite() && m.getAffiliation() == null).count();
    }
}
