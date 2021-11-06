package sst.licences.main.tools;

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
public class CreateJsonFile {

    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;
    private static final Comite comite = Comite.load();

    public static void main(String[] args) {
        List<Membre> list = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        //FileReader reader1 = new FileReader("data//leden-membres-template.csv");

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(LicencesConstants.DATA_LEDEN_MEMBRES_TEMPLATE_CSV), CHARSET);
             CSVReader reader = new CSVReaderBuilder(inputStreamReader)
                     .withCSVParser(csvParser)   // custom CSV parser
                     .withSkipLines(1)           // skip the first line, header info
                     .build()) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                if (!x[0].equals("Nom") && !x[0].equals("Rue")) {
                    Membre m = member(x);
                    list.add(m);
                }
            });

            LicencesContainer.me().addMembresList(list);
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private static Membre member(String[] x) {
        log.debug(String.join("|", x));
        Membre membre = new Membre();
        int i = 0;
        membre.setNom(x[i++]);
        membre.setPrenom(x[i++]);
        membre.setRue(x[i++]);
        membre.setNum(x[i++]);
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

        membre.setComite(comite.isMembreDuComite(membre));
        membre.setSentToMyKKusch(true);
        membre.setAffiliation(LocalDate.of(LocalDate.now().getYear(), Month.JANUARY, 1));

        log.info(membre);

        return membre;
    }
}
