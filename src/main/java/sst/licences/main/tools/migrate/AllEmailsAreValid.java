package sst.licences.main.tools.migrate;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class AllEmailsAreValid {
    public static void main(String[] args) {
        System.exit(-1);
        OldLicencesContainer.load();

        LicencesContainer.me().setLastBankIdentiferGenerated(OldLicencesContainer.me().getLastBankIdentiferGenerated());
        LicencesContainer.me().setPaymentsList(OldLicencesContainer.me().payments());

        for (OldMembre m : OldLicencesContainer.me().membres()) {
            Membre membre = m.membre();
            LicencesContainer.me().addMembre(membre);
        }

        LicencesContainer.me().save();
    }
}
