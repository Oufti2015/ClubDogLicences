package sst.licences.main.tools;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.io.File;
import java.util.List;

@Log4j2
public class CheckTechId {

    public static void main(String[] args) {
        LicencesContainer.load();

        String filename = LicencesConstants.WORKING_FOLDER + File.separator + "membres.old.json";
        LicencesContainer oldCont = LicencesContainer.load(new File(filename));

        List<Membre> membres = LicencesContainer.me().allMembers();
        for (Membre membre : membres) {
            boolean found = false;
            for (Membre oldMembre : oldCont.allMembers()) {
                if (membre.equals(oldMembre)) {
                    found = true;
                    if (!membre.getTechnicalIdentifier().equals(oldMembre.getTechnicalIdentifier())) {
                        log.info(String.format("%-35s OLD: [%14s] NEW: [%14s]",
                                membre.fullName(),
                                oldMembre.getTechnicalIdentifier(),
                                membre.getTechnicalIdentifier()));
                    }
                }
            }
            if (!found) {
                log.info("Membre not found : " + membre);
            }
        }
    }
}
