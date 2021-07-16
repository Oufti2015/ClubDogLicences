package sst.licences.main;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.CharSet;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CreateJsonFile {

    public static final String DATA_LEDEN_MEMBRES_TEMPLATE_CSV = "data//leden-membres-template.csv";
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    public static void main(String[] args) {
        List<Membre> list = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
        //FileReader reader1 = new FileReader("data//leden-membres-template.csv");

        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(DATA_LEDEN_MEMBRES_TEMPLATE_CSV), CHARSET);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }
    }

    private static Membre member(String[] x) {
        System.out.println(String.join("|", x));
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
        membre.setEmail(x[i++]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        membre.setDateDeNaissance(LocalDate.parse(x[i++], formatter));
        membre.setCodePays(x[i++]);
        membre.setLangue(x[i++]);
        membre.setLicence(x[i]);

        membre.setComite(false);

        System.out.println(membre);

        return membre;
    }
}
