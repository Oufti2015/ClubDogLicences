package sst.licences.excel;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class BelfiusFile {
    public int parseFiles(List<File> files) {
        int size = files.size();
        log.info("Parsing " + size + " Belfius files...");
        log.info("Actual payments count : " + LicencesContainer.me().payments().size());

        List<Payment> incompletePayments = new ArrayList<>();

        List<Payment> paymentList = LicencesContainer.me().payments().stream()
                .filter(Payment::isComplete)
                .collect(Collectors.toList());
        LicencesContainer.me().setPaymentsList(paymentList);

        List<File> fileList = files.stream().sorted(Comparator.comparing(File::getAbsolutePath)).collect(Collectors.toList());
        for (File file : fileList) {
            log.info("Importing Belfius file : " + file.getAbsolutePath() + "...");
            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
            try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), LicencesConstants.CHARSET_ANSI_CP_1252);
                 CSVReader reader = new CSVReaderBuilder(inputStreamReader)
                         .withCSVParser(csvParser)   // custom CSV parser
                         .withSkipLines(13)           // skip the first line, header info
                         .build()) {
                List<String[]> lines = reader.readAll();

                for (String[] data : lines) {
                    processLine(incompletePayments, data);
                }
                updateMembres(LicencesContainer.me().payments());
                updateMembres(incompletePayments);
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        }
        log.info("Actual payments count : " + LicencesContainer.me().payments().size());
        log.info("Belfius files import done.");

        return size;
    }

    private void processLine(List<Payment> incompletePayments, String[] data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Double montant = Double.parseDouble(data[10].replace(",", "."));
        LocalDate parse = LocalDate.parse(data[1], formatter);
        if (montant.compareTo(0.00) > 0) {
            Payment vd = new Payment();

            vd.setValueDate(data[1]);
            vd.setExtractnNumber(data[2]);
            vd.setPaymentNumber(data[3]);
            vd.setDate(parse);
            vd.setCompte(data[4]);
            vd.setNom(data[5]);
            vd.setMontant(montant);
            vd.setCommunications(data[14]);

            if (vd.isComplete()) {
                if (!LicencesContainer.me().payments().contains(vd)) {
                    LicencesContainer.me().payments().add(vd);
                }
            } else {
                LicencesContainer.me().payments().add(vd);
            }
        }
    }

    private final List<Double> amountsAffiliation = Arrays.asList(37.00, 50.00, 62.00, 75.00);

    private void updateMembres(List<Payment> belfius) {
        for (Membre membre : LicencesContainer.me().allMembers()) {
            List<Payment> vds = belfius.stream()
                    .filter(vd -> membre.getTechnicalIdentifier() != null
                            && vd.getCommunications() != null
                            && vd.getCommunications().contains(membre.getTechnicalIdentifier()))
                    .collect(Collectors.toList());
            for (Payment vd : vds) {
                if (membre.getAffiliation() == null || membre.getAffiliation().isBefore(vd.getDate())
                        && (amountsAffiliation.contains(vd.getMontant()))) {
                    membre.setAffiliation(vd.getDate());
                    membre.addAccount(vd.getCompte());
                    membre.setActive(true);
                    log.info(membre.getPrenom() + " " + membre.getNom() + " is reaffiliated (" + vd.getDate() + ")");
                }
            }
        }
    }
}
