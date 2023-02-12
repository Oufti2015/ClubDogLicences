package sst.licences.report;

import sst.common.html.HTML;
import sst.common.html.HTMLBody;
import sst.common.html.HTMLHeader;
import sst.common.html.HTMLLineBreak;
import sst.common.html.table.HTMLTable;
import sst.common.html.table.HTMLTableRow;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentsReport implements IMembreReport {
    private static final String TITLE = "Rapport des derniers payments";
    private final HTML content = new HTML();

    @Override
    public IMembreReport input(List<Membre> input) {
        header();
        HTMLBody body = content(input);
        trailer(body);
        return this;
    }

    @Override
    public IMembreReport format() {
        return this;
    }

    @Override
    public String output() {
        return content.toString();
    }

    private void header() {
        content.head()
                .styleSheet("report.css")
                .title(TITLE)
                .charset(StandardCharsets.UTF_8);
    }

    private HTMLBody content(List<Membre> input) {
        HTMLBody body = content.body();
        body.addChild(new HTMLHeader(2).textContent(TITLE));
        List<Membre> alreadyReported = new ArrayList<>();

        for (Membre membre : input) {
            if (!alreadyReported.contains(membre)) {
                List<Membre> family = LicencesContainer.me().compositionFamily(membre);

                HTMLTable table = new HTMLTable().border(1).width(100);
                HTMLTableRow row = table.newRow();
                row.newCell(family.stream()
                                .map(Membre::fullName)
                                .collect(Collectors.joining("<BR>")))
                        .width(30);
                row.newCell(membre.getRue() + " " + membre.getNum() + "<BR>" + membre.getCodePostal() + " " + membre.getLocalite())
                        .width(60);
                row.newCell(membre.getDescription() != null ? membre.getDescription() : "&nbsp;")
                        .width(10);
                row = table.newRow();
                row.newCell(LicencesContainer.me().payments(membre).replace("\n", "<BR>")).colspan(3);
                body.addChild(table);
                body.addChild(new HTMLLineBreak());
                alreadyReported.addAll(family);
            }
        }
        return body;
    }

    private void trailer(HTMLBody body) {
        body.footer("Rapport généré le " + LocalDate.now());
    }
}
