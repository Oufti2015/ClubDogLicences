package sst.licences.main.tools;

import lombok.extern.log4j.Log4j2;
import sst.licences.bank.BankIdentifierGenerator;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

@Log4j2
public class FillWrongTechId {
    public static void main(String[] args) {
        LicencesContainer.load();
        for (Membre m : LicencesContainer.me().membres()) {
            if (m.isComite()) {
                if (m.getTechnicalIdentifier() != null) {
                    log.warn(m.fullName() + " " + m.getTechnicalIdentifier() + " est dans le comité...");
                    BankIdentifierGenerator.reset(m);
                    m.setTechnicalIdentifier(null, true);
                }
            } else {
                if (!BankIdentifierGenerator.correctTechId(m.getTechnicalIdentifier())) {
                    log.warn(m.fullName() + " " + m.getTechnicalIdentifier());
                    BankIdentifierGenerator.reset(m);
                    m.setTechnicalIdentifier(BankIdentifierGenerator.newId(m), true);
                }
            }
        }
    }
}
