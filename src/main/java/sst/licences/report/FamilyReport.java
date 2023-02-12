package sst.licences.report;

import com.google.common.base.MoreObjects;
import sst.common.html.AbstractHTMLElement;
import sst.common.html.HTML;
import sst.common.html.HTMLBody;
import sst.common.html.HTMLDiv;
import sst.common.html.table.HTMLTable;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

public class FamilyReport implements IMembreReport {
    public static final String OUI = "Oui";
    public static final String NON = "Non";
    private List<Membre> input = null;
    private final HTML htmlContent = new HTML();

    @Override
    public IMembreReport input(List<Membre> input) {
        this.input = input;
        return this;
    }

    @Override
    public IMembreReport format() {
        if (!input.isEmpty()) {
            htmlContent.head().css("report.css");
            HTMLBody body = htmlContent.body();
            Membre first = input.get(0);
            HTMLTable table = new HTMLTable();
            table.width(80);
            table.newRow().newHead(input.stream().map(Membre::getNom).distinct().collect(Collectors.joining(" - "))).colspan(3);
            table.newRow().height(10).newCell().colspan(3).classId("line").addChild(new HTMLDiv());
            table.newRow().newCell(MoreObjects.firstNonNull(first.getDescription(), AllMembersReport.NBSP)).colspan(3);
            table.newRow().height(10).newCell().colspan(3).classId("line").addChild(new HTMLDiv());
            table.newRow().newCell().colspan(3).addChild(familyTable(input));
            table.newRow().height(10).newCell().colspan(3).classId("line").addChild(new HTMLDiv());
            table.newRow().newCell().colspan(3).addChild(addressTable(first));
            table.newRow().height(10).newCell().colspan(3).classId("line").addChild(new HTMLDiv());
            table.newRow().newCell().colspan(3).addChild(techDataTable(first));
            table.newRow().height(10).newCell().colspan(3).classId("line").addChild(new HTMLDiv());
            table.newRow().newCell().colspan(3).addChild(paymentsTable(first)).classId("payments");

            body.addChild(table);
        }
        return this;
    }

    private AbstractHTMLElement paymentsTable(Membre first) {
        HTMLDiv div = new HTMLDiv();
        div.textContent(LicencesContainer.me().payments(first).replace("\n", "<BR>"));
        return div;
    }

    private AbstractHTMLElement techDataTable(Membre member) {
        HTMLTable table = new HTMLTable().width(60);
        table.newRow().newHead("Licence");
        table.row().newCell(MoreObjects.firstNonNull(member.getLicence(), AllMembersReport.NBSP));
        table.newRow().newHead("Compte");
        table.row().newCell(MoreObjects.firstNonNull(member.getAccountId(), AllMembersReport.NBSP));
        table.newRow().newHead("Comm. struct.");
        table.row().newCell(member.getTechnicalIdentifier());
        table.newRow().newHead("Affilié depuis");
        table.row().newCell(affiliation(member));
        table.newRow().newHead("Actif");
        table.row().newCell(ouiNon(member.isActive()));
        table.newRow().newHead("Saint-Hubert");
        table.row().newCell(ouiNon(member.isSentToMyKKusch()));
        return table;
    }

    private String affiliation(Membre member) {
        String result;
        if (member.isComite()) {
            result = "Comité";
        } else if (member.getAffiliation() != null) {
            result = member.getAffiliation().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        } else {
            result = ouiNon(false);
        }
        return result;
    }

    private AbstractHTMLElement addressTable(Membre member) {
        HTMLTable table = new HTMLTable().width(100);
        table.newRow().newHead("Rue");
        table.row().newHead("Numéro");
        table.row().newHead("Code Postal");
        table.row().newHead("Localité");
        table.row().newHead("Pays");

        table.newRow().newCell(member.getRue());
        table.row().newCell(member.getNum());
        table.row().newCell(member.getCodePostal());
        table.row().newCell(member.getLocalite());
        table.row().newCell(member.getCodePays());
        return table;
    }

    private AbstractHTMLElement familyTable(List<Membre> input) {
        HTMLTable table = new HTMLTable().width(100);
        table.newRow().newHead("Nom");
        table.row().newHead("Prénom");
        table.row().newHead("E-Mail");
        table.row().newHead("G.S.M.");
        table.row().newHead("Date de naissance");
        table.row().newHead("Comité ?");

        for (Membre member : input) {
            table.newRow().newCell(member.getNom());
            table.row().newCell(member.getPrenom());
            table.row().newCell(member.getEmail().getAdresse());
            table.row().newCell(member.getGsm());
            table.row().newCell(member.getDateDeNaissance().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
            table.row().newCell(ouiNon(member.isComite()));
        }
        return table;
    }

    private String ouiNon(boolean condition) {
        return condition ? OUI : NON;
    }

    @Override
    public String output() {
        return htmlContent.toString();
    }
}
