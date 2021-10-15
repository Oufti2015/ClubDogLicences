package sst.licences.main;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DuplicateCheck {
    public static void main(String[] args) {
        log.trace("Starting...");
        LicencesContainer.load();
        List<Membre> duplicates = new ArrayList<>();
        List<Membre> membres = LicencesContainer.me().membres();
        for (Membre m : membres) {
            long count = membres
                    .stream()
                    .filter(m::equals)
                    .count();

            if (!duplicates.contains(m) && count > 1) {
                log.info(m);
                duplicates.add(m);
            }
        }
        log.info("duplicates found = " + duplicates.size());
        membres.removeAll(duplicates);
        membres.addAll(duplicates);
        log.info("members = " + membres.size());
        LicencesContainer.me().save();
    }
}
