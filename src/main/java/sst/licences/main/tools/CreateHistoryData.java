package sst.licences.main.tools;

import sst.licences.container.LicencesContainer;
import sst.licences.history.History;
import sst.licences.model.Membre;

import java.util.List;

public class CreateHistoryData {
    public static void main(String[] args) {
        LicencesContainer.load();
        List<Membre> membres = LicencesContainer.me().allMembers();
        History.historicalDataCutover = true;
        for (Membre membre : membres) {
            membre.clearHistoricData();

            History.history(membre, Membre.FIELD_NAME, null, membre.getNom());
            History.history(membre, Membre.FIELD_FIRSTNAME, null, membre.getPrenom());
            History.history(membre, Membre.FIELD_ADDRESS_STREET, null, membre.getRue());
            History.history(membre, Membre.FIELD_ADDRESS_STREET_NUMBER, null, membre.getNum());
            History.history(membre, Membre.FIELD_ADDRESS_POST_CODE, null, membre.getCodePostal());
            History.history(membre, Membre.FIELD_ADDRESS_CITY, null, membre.getLocalite());
            History.history(membre, Membre.FIELD_TELEPHONE, null, membre.getTelephone());
            History.history(membre, Membre.FIELD_GSM, null, membre.getGsm());
            History.history(membre, Membre.FIELD_EMAIL, null, membre.getEmail());
            History.history(membre, Membre.FIELD_BIRTH_DATE, null, membre.getDateDeNaissance());
            History.history(membre, Membre.FIELD_COUNTRY, null, membre.getCodePays());
            History.history(membre, Membre.FIELD_LANGUAGE, null, membre.getLangue());
            History.history(membre, Membre.FIELD_LICENSE, null, membre.getLicence());
            History.history(membre, Membre.FIELD_COMITY, false, membre.isComite());
            if (membre.getAffiliation() != null) {
                History.affiliation(membre, null, membre.getAffiliation());
            }
            History.history(membre, Membre.FIELD_MY_K_KUSCH, false, membre.isSentToMyKKusch());
            History.history(membre, Membre.FIELD_ACCOUNT_NUMBER, null, membre.getAccountId());
            History.history(membre, Membre.FIELD_TECHNICAL_ID, null, membre.getTechnicalIdentifier());
            History.history(membre, Membre.FIELD_NOTES, null, membre.getDescription());
            if (!membre.isActive()) {
                History.inactivation(membre);
            } else {
                History.history(membre, Membre.FIELD_ACTIVE_FLAG, false, true);
            }
        }
        LicencesContainer.me().save();
    }
}
