package sst.licences.excel;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BelfiusFile {
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    public void parseFiles(List<File> files) {
        List<Payment> incompletePayments = new ArrayList<>();

        for (File file : files) {
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), CHARSET);
                 CSVReader reader = new CSVReaderBuilder(inputStreamReader)
                         .withCSVParser(csvParser)   // custom CSV parser
                         .withSkipLines(13)           // skip the first line, header info
                         .build()) {
                List<String[]> lines = reader.readAll();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                for (String[] data : lines) {
                    Double montant = Double.parseDouble(data[10].replace(",", "."));

                    if (montant.compareTo(0.00) > 0) {
                        Payment vd = new Payment();

                        vd.setValueDate(data[1]);
                        vd.setExtractnNumber(data[2]);
                        vd.setPaymentNumber(data[3]);
                        vd.setDate(LocalDate.parse(data[1], formatter));
                        vd.setCompte(data[4]);
                        vd.setNom(data[5]);
                        vd.setMontant(montant);
                        vd.setCommunications(data[14]);

                        if (vd.isComplete()) {
                            if (!LicencesContainer.me().payments().contains(vd)) {
                                LicencesContainer.me().payments().add(vd);
                            }
                        } else {
                            incompletePayments.add(vd);
                        }
                    }
                }
                updateMembres(LicencesContainer.me().payments());
                updateMembres(incompletePayments);
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMembres(List<Payment> belfius) {
        for (Membre membre : LicencesContainer.me().membres()) {
            List<Payment> vds = belfius.stream()
                    .filter(vd -> membre.getTechnicalIdentifer() != null
                            && vd.getCommunications() != null
                            && vd.getCommunications().contains(membre.getTechnicalIdentifer()))
                    .collect(Collectors.toList());
            for (Payment vd : vds) {
                if (membre.getAffiliation() == null || membre.getAffiliation().isBefore(vd.getDate())) {
                    membre.setAffiliation(vd.getDate());
                    membre.setAccountId(vd.getCompte());

                    log.info(membre.getPrenom() + " " + membre.getNom() + " est réaffilié " + vd.getDate());
                }
            }
        }
    }
}
