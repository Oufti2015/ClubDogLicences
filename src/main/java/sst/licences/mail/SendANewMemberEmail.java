package sst.licences.mail;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.util.List;

@Log4j2
public class SendANewMemberEmail extends SendAReafiliationEmail {
    static final String MAIL_SUBJECT = "Affiliation";

    public SendANewMemberEmail(List<Membre> selectedMembers) {
        super(selectedMembers);
    }

    @Override
    protected String messageBody(Membre membre) {
        List<Membre> composition = LicencesContainer.me().compositionFamily(membre);
        StringBuilder sb = new StringBuilder();
        for (Membre m : composition) {
            SendASignaleticCheckEmail.helloWorld(sb, m);
        }
        sb.append("\n");
        sb.append("Bienvenue au Berger Club Arlonais, nous avons bien reçu votre inscription.  Pouvez-vous, s'il vous plaît, vérifier vos informations et payer votre affiliation ?\n");
        body(membre, sb, composition);
        return sb.toString();
    }
}
