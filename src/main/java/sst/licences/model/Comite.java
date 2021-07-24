package sst.licences.model;

import com.google.gson.Gson;
import sst.licences.main.LicencesConstants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
            return this.nom.equals(membre.nom) && this.prenom.equals(membre.prenom);
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
                System.out.println(String.format("%d membres chargés.", comite.membresDuComite.size()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Cannot read JSON file " + LicencesConstants.MEMBRES_JSON_FILE);
            System.exit(-1);
        }
        return comite;
    }

    public void save() {
        try {
            // convert book object to JSON
            String json = new Gson().toJson(this);

            // print JSON string
            System.out.println(json);

            try (FileWriter myWriter = new FileWriter(LicencesConstants.COMITE_JSON_FILE)) {
                myWriter.write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Cannot write JSON file " + LicencesConstants.COMITE_JSON_FILE);
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
