package sst.licences.excel;

import com.google.common.base.MoreObjects;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.log4j.Log4j2;
import sst.licences.container.LicencesContainer;
import sst.licences.container.MemberEligibility;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Comite;
import sst.licences.model.Membre;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class Import {

    public void importFromCsv(File file) {
        log.info("Importing file " + file + "...");
        final List<Membre> notAffiliated = new ArrayList<>();

        List<Membre> list = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();

        LicencesContainer.me().membres().forEach(m -> m.setSentToMyKKusch(false));
        try (InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(file.toPath()), LicencesConstants.CHARSET_ANSI_CP_1252);
             CSVReader reader = new CSVReaderBuilder(inputStreamReader)
                     .withCSVParser(csvParser)   // custom CSV parser
                     .withSkipLines(1)           // skip the first line, header info
                     .build()) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                if (!x[0].equals("Nom") && !x[0].equals("Rue")) {
                    Membre member = member(x);
                    Optional<Membre> optionalMembre = LicencesContainer.me().membre(member);
                    if (!optionalMembre.isPresent()) {
                        log.warn("Member " + member.fullName() + " not found in database");
                    } else {
                        Membre membre = optionalMembre.get();
                        Membre.memberCompare(membre, member);
                        membre.update(member);
                        if (!MemberEligibility.isCurrentYearAffiliated(membre)) {
                            notAffiliated.add(membre);
                        }
                    }
                }
            });

            List<Membre> notSent = LicencesContainer.me().membres().stream()
                    .filter(MemberEligibility::isCurrentYearAffiliated)
                    .filter(m -> !m.isSentToMyKKusch())
                    .collect(Collectors.toList());
            for (Membre membre : notSent) {
                log.warn("Not Sent {}", membre.fullName());
            }

            notAffiliated.forEach(membre -> log.warn("Not affiliated {}", membre.fullName()));

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
        membre.setBox(MoreObjects.firstNonNull(x[i++], ""));
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

        membre.setSentToMyKKusch(true);

        return membre;
    }

    private static final Comite comite = Comite.load();
    public static final Charset CHARSET = StandardCharsets.ISO_8859_1;
}
