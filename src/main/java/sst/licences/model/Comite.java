package sst.licences.model;

import com.google.gson.Gson;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import sst.licences.main.LicencesConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public class Comite {

    public static class MembreDuComite {
        String nom;
        String prenom;

        public MembreDuComite(String nom, String prenom) {
            this.nom = nom;
            this.prenom = prenom;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Membre) {
                Membre membre = (Membre) obj;
                return this.nom.equals(membre.getNom()) && this.prenom.equals(membre.getPrenom());
            }
            MembreDuComite membre = (MembreDuComite) obj;
            if (membre != null) {
                return this.nom.equals(membre.nom) && this.prenom.equals(membre.prenom);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(nom, prenom);
        }
    }

    private List<MembreDuComite> membresDuComite = new ArrayList<>();

    public static Comite load() {
        Comite comite = null;
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            try (Reader reader = Files.newBufferedReader(Paths.get(LicencesConstants.COMITE_JSON_FILE))) {
                // convert JSON string to Book object
                comite = gson.fromJson(reader, Comite.class);
                log.info(String.format("%d membres chargés.", comite.membresDuComite.size()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE, ex);
            System.exit(-1);
        }
        return comite;
    }

    public void save() {
        try {
            // convert book object to JSON
            String json = new Gson().toJson(this);

            // print JSON string
            log.debug(json);

            try (FileWriter myWriter = new FileWriter(LicencesConstants.COMITE_JSON_FILE)) {
                myWriter.write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Cannot write JSON file " + LicencesConstants.COMITE_JSON_FILE, e);
            System.exit(-1);
        }
    }

    public void addMembre(MembreDuComite membreDuComite) {
        membresDuComite.add(membreDuComite);
    }

    public boolean isMembreDuComite(Membre membre) {
        return membresDuComite.contains(new MembreDuComite(membre.getNom(), membre.getPrenom()));
    }
}
