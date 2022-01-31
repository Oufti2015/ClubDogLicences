package sst.licences.excel;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Comite;
import sst.licences.model.Membre;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Import {

    public void importFromCsv(File file) {
        log.info("Importing file " + file + "...");

        List<Membre> list = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();

        LicencesContainer.me().membres().forEach(m -> m.setSentToMyKKusch(false));
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), LicencesConstants.CHARSET_ANSI_CP_1252);
             CSVReader reader = new CSVReaderBuilder(inputStreamReader)
                     .withCSVParser(csvParser)   // custom CSV parser
                     .withSkipLines(1)           // skip the first line, header info
                     .build()) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                if (!x[0].equals("Nom") && !x[0].equals("Rue")) {
                    Membre member = member(x);
                    if (!LicencesContainer.me().allMembers().contains(member)) {
                        list.add(member);
                    } else {
                        List<Membre> membres = LicencesContainer.me().membres();
                        Membre membre = membres.get(membres.indexOf(member));
                        membre.update(member);
                    }
                }
            });

            log.info("Adding " + list.size() + " members...");
            for (Membre membre : list) {
                log.info("ADDED : " + membre);
                LicencesContainer.me().membres().stream()
                        .filter(m -> m.getNom().equals(membre.getNom()))
                        .filter(m -> m.getPrenom().equals(membre.getPrenom()))
                        .forEach(m -> log.info("----- : " + m));
            }
            LicencesContainer.me().addMembresList(list);
            log.info("... done.");
        } catch (IOException | CsvException e) {
            log.error("Error parsing file " + file, e);
        }
    }

    private Membre member(String[] x) {
        log.debug(String.join("|", x));

        Membre membre = new Membre();
        int i = 0;
        membre.setNom(x[i++]);
        membre.setPrenom(x[i++]);
        membre.setRue(x[i++]);
        membre.setNum(x[i++]);
        membre.setBox(x[i++]);
        membre.setCodePostal(x[i++]);
        membre.setLocalite(x[i++]);
        membre.setTelephone(x[i++]);
        membre.setGsm(x[i++]);
        membre.setEmailAddress(x[i++]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        membre.setDateDeNaissance(LocalDate.parse(x[i++], formatter));
        membre.setCodePays(x[i++]);
        membre.setLangue(x[i++]);
        membre.setLicence(x[i]);

        //membre.setComite(comite.isMembreDuComite(membre));
        membre.setSentToMyKKusch(true);
        //membre.setAffiliation(LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1));

        return membre;
    }

    private static final Comite comite = Comite.load();
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;
}
