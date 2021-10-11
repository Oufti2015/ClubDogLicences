package sst.licences.main;

import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.util.ArrayList;
import java.util.List;

public class DuplicateCheck {
    public static void main(String[] args) {
        LicencesContainer.load();
        List<Membre> duplicates = new ArrayList<>();
        List<Membre> membres = LicencesContainer.me().membres();
        for (Membre m : membres) {
            long count = membres
                    .stream()
                    .filter(m::equals)
                    .count();

            if (!duplicates.contains(m) && count > 1) {
                System.out.println(m);
                duplicates.add(m);
            }
        }
        System.out.println("duplicates found = " + duplicates.size());
        membres.removeAll(duplicates);
        membres.addAll(duplicates);
        System.out.println("members = " + membres.size());
        LicencesContainer.me().save();
    }
}
