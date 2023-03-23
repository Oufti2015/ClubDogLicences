package sst.licences.main.tools;

import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class CheckMembers {
    public static void main(String[] args) {
        LicencesContainer.load();
        List<Membre> membres = LicencesContainer.me().thisYearMembers().stream()
                .filter(m -> m.getAffiliation() != null && !m.isComite() && m.getAffiliation().isAfter(LocalDate.of(2022, 8, 31)))
                .filter(m -> m.getAffiliation() != null && m.getAffiliation().isBefore(LocalDate.of(2022, 10, 1)))
                .collect(Collectors.toList());

        int nb = 0;
        for (Membre membre : membres) {
            nb++;
            System.out.println("" + nb + ". " + membre.getNom() + " " + membre.getPrenom() + " " + membre.getDescription() + " " + membre.getAffiliation());
        }
    }
}
