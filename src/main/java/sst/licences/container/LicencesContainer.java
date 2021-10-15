package sst.licences.container;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    public List<Membre> membres() {
        return membres;
    }

    public static void load() {
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            try (Reader reader = Files.newBufferedReader(Paths.get(LicencesConstants.MEMBRES_JSON_FILE))) {
                // convert JSON string to Book object
                me = gson.fromJson(reader, LicencesContainer.class);
                log.info(String.format("%d membres chargés.", me().membres.size()));
            }
        } catch (Exception ex) {
            log.fatal("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE, ex);
            System.exit(-1);
        }
    }

    public void save() {
        try {
            // convert book object to JSON
            String json = new Gson().toJson(LicencesContainer.me());
            try (FileWriter myWriter = new FileWriter(LicencesConstants.MEMBRES_JSON_FILE)) {
                myWriter.write(json);
            }
        } catch (IOException e) {
            log.fatal("Cannot write JSON file " + LicencesConstants.MEMBRES_JSON_FILE, e);
            System.exit(-1);
        }
    }
}
