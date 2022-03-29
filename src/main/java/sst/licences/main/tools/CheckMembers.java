package sst.licences.main.tools;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.util.List;

@Log4j2
public class CheckMembers {
    public static void main(String[] args) {
        LicencesContainer.me().load();
        List<Membre> membres = LicencesContainer.me().allMembers();

        int nb = 0;
        for (Membre membre : membres) {
            nb++;
            log.info("" + nb + ". " + membre.getNom() + " " + membre.getPrenom());
        }
    }
}
