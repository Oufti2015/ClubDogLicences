package sst.licences.container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Log4j2
public class LicencesContainer {
    private static LicencesContainer me = new LicencesContainer();

    public static LicencesContainer me() {
        return me;
    }

    private LicencesContainer() {
    }

    @Getter
    @Setter
    private String lastBankIdentiferGenerated;
    private List<Membre> membres = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    public void setMembresList(List<Membre> membres) {
        this.membres = membres;
        save();
    }

    public void addMembresList(List<Membre> membres) {
        this.membres.addAll(membres);
        save();
    }

    public void addMembre(Membre membre) {
        this.membres.add(membre);
        save();
    }

    public void setPaymentsList(List<Payment> payments) {
        this.payments = payments;
        save();
    }

    public void addPaymentsList(List<Payment> payments) {
        this.payments.addAll(payments);
        save();
    }

    public void addMPayment(Payment payment) {
        this.payments.add(payment);
        save();
    }

    public List<Membre> membres() {
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
            try(            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(LicencesConstants.MEMBRES_JSON_FILE),
                    StandardCharsets.UTF_8.newDecoder()
            )) {
          //  try (Reader reader = Files.newBufferedReader(Paths.get(LicencesConstants.MEMBRES_JSON_FILE), StandardCharsets.ISO_8859_1)) {
                // convert JSON string to Book object
                me = gson.fromJson(reader, LicencesContainer.class);
                log.info(String.format("...%5d membres chargés.", me().membres.size()));
                log.info(String.format("...%5d payements chargés.", me().payments.size()));
            }
        } catch (Exception ex) {
            log.fatal("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE, ex);
            System.exit(-1);
        }
    }

    public void save() {
        log.info("Saving JSON file...");
        log.debug("Saving JSON file...", new Throwable());
        try {
            // convert book object to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(LicencesContainer.me());

            try (OutputStreamWriter myWriter = new OutputStreamWriter(
                    new FileOutputStream(LicencesConstants.MEMBRES_JSON_FILE),
                    StandardCharsets.UTF_8.newEncoder())) {
                myWriter.write(json);
            }
        } catch (IOException e) {
            log.fatal("Cannot write JSON file " + LicencesConstants.MEMBRES_JSON_FILE, e);
            System.exit(-1);
        }
        log.info("...file saved.");
    }

    public String payments(Membre membre) {
        String result = "";
        if (Strings.isNotEmpty(membre.getAccountId())) {
            result = payments.stream()
                    .filter(p -> p.getCompte().equals(membre.getAccountId()))
                    .map(Payment::toString)
                    .collect(Collectors.joining("\n"));
        } else {
            result = payments.stream()
                    .filter(p -> p.getNom().toLowerCase(Locale.ROOT).contains(membre.getNom().toLowerCase(Locale.ROOT))
                            && LicencesContainer.me.membres().stream().map(Membre::getAccountId).filter(a -> a != null && a.equals(p.getCompte())).count() == 0)
                    .map(Payment::toFullString)
                    .collect(Collectors.joining("\n"));
        }
        return result;
    }

    public List<Membre> compositionFamily(Membre membre) {
        return LicencesContainer.me().membres()
                .stream()
                .filter(m -> Membre.addressId(m).equals(Membre.addressId(membre)))
                .collect(Collectors.toList());
    }
}
