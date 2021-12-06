package sst.licences.report;

import lombok.extern.log4j.Log4j2;
import sst.common.file.output.OutputFile;
import sst.licences.bank.BankIdentifierGenerator;
import sst.licences.container.LicencesContainer;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;
import sst.licences.report.members.ListType;
import sst.licences.report.members.MembersListReport;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class AllMembersReport implements Report {
    protected static final String NBSP = "&nbsp;";
    private List<Membre> input = null;

    @Override
    public Report input(List<Membre> input) {
        this.input = input;
        return this;
    }

    @Override
    public Report format() {
        try {
            eachFamilyCard(input);
            wholeSite();
        } catch (IOException e) {
            log.error("Cannot run reports", e);
            System.exit(-1);
        }
        return this;
    }

    @Override
    public String output() {
        return null;
    }

    private final List<Membre> alreadyDone = new ArrayList<>();

    private void eachFamilyCard(List<Membre> input) throws IOException {
        for (Membre member : input) {
            if (!alreadyDone.contains(member)) {
                List<Membre> family = LicencesContainer.me().compositionFamily(member)
                        .stream()
                        .sorted(Comparator.comparing(Membre::getDateDeNaissance))
                        .collect(Collectors.toList());
                String filename = familyFileName(member);
                String report = new FamilyReport().input(family).format().output();
                alreadyDone.addAll(family);
                try (OutputFile output = new OutputFile(filename)) {
                    output.println(report);
                }
            }
        }
    }

    public static String familyFileName(Membre member) {
        String technicalIdentifer = member.getTechnicalIdentifier();
        if (technicalIdentifer == null) {
            technicalIdentifer = BankIdentifierGenerator.newId(member);
            for (Membre m : LicencesContainer.me().compositionFamily(member)) {
                m.setTechnicalIdentifier(technicalIdentifer);
            }
            LicencesContainer.me().save();
        }
        technicalIdentifer = technicalIdentifer.replace("/", "_");
        return LicencesConstants.REPORT_FOLDER + File.separator + technicalIdentifer + ".html";
    }

    private void wholeSite() throws IOException {
        HashMap<ListType, List<Membre>> lists = new HashMap<>();
        lists.put(ListType.ALL_MEMBERS, LicencesContainer.me().allMembers());
        lists.put(ListType.ACTIVE_MEMBERS, LicencesContainer.me().activeMembers());
        lists.put(ListType.INACTIVE_MEMBERS, LicencesContainer.me().inactiveMembers());
        lists.put(ListType.THIS_YEAR_MEMBERS, LicencesContainer.me().thisYearMembers());
        lists.put(ListType.NEXT_YEAR_MEMBERS, LicencesContainer.me().nextYearMembers());
        lists.put(ListType.UNPAID_MEMBERS, LicencesContainer.me().unpaidMembers());
        lists.put(ListType.COMITY_MEMBERS, LicencesContainer.me().comityMembers());

        for (Map.Entry<ListType, List<Membre>> map : lists.entrySet()) {
            ListType listType = map.getKey();
            String output = new MembersListReport(listType)
                    .input(map.getValue())
                    .format()
                    .output();

            String filename = LicencesConstants.REPORT_FOLDER + File.separatorChar + listType.getFilename();
            try (OutputFile file = new OutputFile(filename)) {
                file.println(output);
            }
        }
    }
}
