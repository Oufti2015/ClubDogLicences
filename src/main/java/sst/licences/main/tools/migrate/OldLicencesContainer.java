package sst.licences.main.tools.migrate;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Payment;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class OldLicencesContainer {
    private static OldLicencesContainer me = new OldLicencesContainer();

    public static OldLicencesContainer me() {
        return me;
    }

    private OldLicencesContainer() {
    }

    @Getter
    @Setter
    private String lastBankIdentiferGenerated;
    private List<OldMembre> membres = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    public void setMembresList(List<OldMembre> membres) {
        this.membres = membres;
    }

    public void addMembresList(List<OldMembre> membres) {
        this.membres.addAll(membres);
    }

    public void addMembre(OldMembre membre) {
        this.membres.add(membre);
    }

    public void setPaymentsList(List<Payment> payments) {
        this.payments = payments;
    }

    public void addPaymentsList(List<Payment> payments) {
        this.payments.addAll(payments);
    }

    public void addMPayment(Payment payment) {
        this.payments.add(payment);
    }

    public List<OldMembre> membres() {
        return membres;
    }

    public List<Payment> payments() {
        return payments;
    }

    public static void load() {
        log.info("Loading JSON file...");

        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            try (Reader reader = Files.newBufferedReader(Paths.get(LicencesConstants.MEMBRES_JSON_FILE))) {
                // convert JSON string to Book object
                me = gson.fromJson(reader, OldLicencesContainer.class);
                log.info(String.format("...%5d membres chargés.", me().membres.size()));
                log.info(String.format("...%5d payements chargés.", me().payments.size()));
            }
        } catch (Exception ex) {
            log.fatal("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE, ex);
            System.exit(-1);
        }
    }
}
