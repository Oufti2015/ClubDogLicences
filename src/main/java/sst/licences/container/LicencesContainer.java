package sst.licences.container;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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
    private String lastBankIdentifierGenerated;

    @Getter
    private final Config config = new Config();
    private final List<Membre> membres = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();

    public void setMembresList(List<Membre> membres) {
        this.membres.clear();
        this.membres.addAll(membres);
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
        this.payments.clear();
        this.payments.addAll(payments);
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
        return activeMembers();
    }

    public List<Membre> activeMembers() {
        return membres.stream().filter(Membre::isActive).collect(Collectors.toList());
    }

    public List<Membre> inactiveMembers() {
        return membres.stream().filter(m -> !m.isActive()).collect(Collectors.toList());
    }

    public List<Membre> allMembers() {
        return membres;
    }

    public List<Payment> payments() {
        return payments;
    }

    public static void load() {
        loadJSON();
    }

    public static void loadJSON() {
        log.info("Loading JSON file...");
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            try (InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(LicencesConstants.MEMBRES_JSON_FILE),
                    StandardCharsets.UTF_8.newDecoder())) {
                // convert JSON string to Book object
                me = gson.fromJson(reader, LicencesContainer.class);
                log.info(String.format("...%5d members loaded.", me().membres.size()));
                log.info(String.format("...%5d payements loaded.", me().payments.size()));
            }
        } catch (Exception ex) {
            log.fatal("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE, ex);
            System.exit(-1);
        }

        log.info("lastBankIdentifierGenerated = " + me().getLastBankIdentifierGenerated());
        me().getConfig().setLastBankIdentifierGenerated(me().lastBankIdentifierGenerated);
    }

    public static void loadYAML() {
        log.info("Loading YAML files...");
        loadConfigYAML();
        loadMembersYAML();
        loadPaymentsYAML();
    }

    private static void loadConfigYAML() {
        try {
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(LicencesConstants.CONFIG_YAML_FILE)).getFile());

            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();

            // Mapping the employee from the YAML file to the Employee class
            LicencesContainer.me().getConfig().init(mapper.readValue(file, Config.class));

            // Printing out the information
            log.info("Config loaded : " + me().config);
        } catch (IOException e) {
            log.fatal("Cannot read YAML file " + LicencesConstants.CONFIG_YAML_FILE, e);
            System.exit(-1);
        }
    }

    private static void loadMembersYAML() {
        try {
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(LicencesConstants.MEMBRES_YAML_FILE)).getFile());

            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();

            // Mapping the employee from the YAML file to the Employee class
            LicencesContainer.me().setMembresList(mapper.readerForListOf(Membre.class).readValue(file));

            // Printing out the information
            log.info(" " + LicencesContainer.me().membres().size() + " members loaded");
        } catch (IOException e) {
            log.fatal("Cannot read YAML file " + LicencesConstants.MEMBRES_YAML_FILE, e);
            System.exit(-1);
        }
    }

    private static void loadPaymentsYAML() {
        try {
            // Loading the YAML file from the /resources folder
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(LicencesConstants.PAYMENTS_YAML_FILE)).getFile());

            // Instantiating a new ObjectMapper as a YAMLFactory
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            // Mapping the employee from the YAML file to the Employee class
            LicencesContainer.me().setPaymentsList(mapper.readerForListOf(Payment.class).readValue(file));

            // Printing out the information
            log.info(" " + LicencesContainer.me().payments().size() + " payments loaded");
        } catch (IOException e) {
            log.fatal("Cannot read YAML file " + LicencesConstants.PAYMENTS_YAML_FILE, e);
            System.exit(-1);
        }
    }


    public static LicencesContainer load(File file) {
        log.info("Loading JSON file...");
        LicencesContainer result = null;
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file),
                    StandardCharsets.UTF_8.newDecoder())) {
                // convert JSON string to Book object
                result = gson.fromJson(reader, LicencesContainer.class);
                log.info(String.format("...%5d members loaded.", me().membres.size()));
                log.info(String.format("...%5d payements loaded.", me().payments.size()));
            }
        } catch (Exception ex) {
            log.fatal("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE, ex);
            System.exit(-1);
        }
        return result;
    }

    public void save() {
        saveJSON();
    }

    public void saveJSON() {
        log.info("Saving JSON file...");
        log.debug("Saving JSON file...", new Throwable());
        try {
            // convert book object to JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(LicencesContainer.me());
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(LicencesConstants.MEMBRES_JSON_FILE),
                    StandardCharsets.UTF_8.newEncoder())) {
                writer.write(json);
            }
        } catch (IOException e) {
            log.fatal("Cannot write JSON file " + LicencesConstants.MEMBRES_JSON_FILE, e);
            System.exit(-1);
        }
        log.info("...file saved.");
    }

    public void saveYAML() {
        log.info("Writing YAML files...");
        saveConfigYAML();
        saveMembersYAML();
        savePaymentsYAML();
    }

    private void saveConfigYAML() {
        try {
            log.info("Config : " + getConfig());
            // ObjectMapper is instantiated just like before
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            mapper.writeValue(new File(LicencesConstants.CONFIG_YAML_FILE), getConfig());
        } catch (IOException e) {
            log.fatal("Cannot write YAML file " + LicencesConstants.CONFIG_YAML_FILE, e);
            System.exit(-1);
        }
    }

    private void saveMembersYAML() {
        try {
            // ObjectMapper is instantiated just like before
            YAMLFactory yamlFactory = new YAMLFactory();
            ObjectMapper mapper = new ObjectMapper(yamlFactory);
            mapper.findAndRegisterModules();
            mapper.writeValue(new File(LicencesConstants.MEMBRES_YAML_FILE), membres());
        } catch (IOException e) {
            log.fatal("Cannot write YAML file " + LicencesConstants.MEMBRES_YAML_FILE, e);
            System.exit(-1);
        }
    }

    private void savePaymentsYAML() {
        try {
            // ObjectMapper is instantiated just like before
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.findAndRegisterModules();
            mapper.writeValue(new File(LicencesConstants.PAYMENTS_YAML_FILE), payments());
        } catch (IOException e) {
            log.fatal("Cannot write YAML file " + LicencesConstants.PAYMENTS_YAML_FILE, e);
            System.exit(-1);
        }
    }

    public String payments(Membre membre) {
        String result = "";
        if (!Strings.isNullOrEmpty(membre.getAccountId())) {
            result = payments.stream()
                    .filter(p -> p.getCompte().equals(membre.getAccountId()))
                    .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                    .map(Payment::toString)
                    .collect(Collectors.joining("\n"));
        } else {
            result = payments.stream()
                    .filter(p -> p.getNom().toLowerCase(Locale.ROOT).contains(membre.getNom().toLowerCase(Locale.ROOT))
                            && LicencesContainer.me.membres().stream().map(Membre::getAccountId).filter(a -> a != null && a.equals(p.getCompte())).count() == 0)
                    .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                    .map(Payment::toFullString)
                    .collect(Collectors.joining("\n"));
        }
        return result;
    }

    public List<Membre> compositionFamily(Membre membre) {
        return LicencesContainer.me().allMembers()
                .stream()
                .filter(m -> Membre.addressId(m).equals(Membre.addressId(membre)))
                .filter(Membre::isActive)
                .collect(Collectors.toList());
    }

    public List<Membre> thisYearMembers() {
        return MemberEligibility.eligibleMembres(LocalDate.now().getYear());
    }

    public List<Membre> nextYearMembers() {
        return MemberEligibility.eligibleMembres(LocalDate.now().getYear() + 1);
    }

    public List<Membre> unpaidMembers() {
        return MemberEligibility.unpaid(LocalDate.now().getYear());
    }

    public List<Membre> comityMembers() {
        return membres().stream().filter(Membre::isComite).collect(Collectors.toList());
    }
}
