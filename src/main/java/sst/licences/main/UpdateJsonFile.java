package sst.licences.main;

import sst.licences.container.LicencesContainer;

import java.time.LocalDate;
import java.time.Month;

public class UpdateJsonFile {

    public static void main(String[] args) {
        LicencesContainer.load();

        LicencesContainer.me().membres().forEach(m -> {
            if (!m.isComite()) {
                m.setAffiliation(LocalDate.of(2021, Month.JANUARY, 1));
            }
        });

        LicencesContainer.me().save();
    }
}
