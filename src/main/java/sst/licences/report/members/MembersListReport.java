package sst.licences.report.members;

import com.google.common.base.MoreObjects;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import sst.common.html.*;
import sst.common.html.table.HTMLTable;
import sst.common.html.table.HTMLTableRow;
import sst.licences.main.LicencesConstants;
import sst.licences.model.Membre;
import sst.licences.report.AllMembersReport;
import sst.licences.report.IMembreReport;

import java.util.List;
import java.util.stream.Collectors;

public class MembersListReport implements IMembreReport {
    private final ListType listType;
    private List<Membre> input;
    private final HTML htmlContent = new HTML();

    public MembersListReport(ListType listType) {
        this.listType = listType;
    }

    @Override
    public IMembreReport input(List<Membre> input) {
        this.input = input;
        return this;
    }

    @Override
    public IMembreReport format() {
        htmlContent.head().css("report.css");
        HTMLBody body = htmlContent.body();
        body.addChild(new HTMLHeader(1).textContent(LicencesConstants.APPLICATION_TITLE));
        HTMLTable table = new HTMLTable().width(100);
        HTMLTableRow row = table.newRow();
        row.newCell().width(20).addChild(menuList(listType));
        row.newCell().addChild(content());

        body.addChild(table);
        return this;
    }

    @Override
    public String output() {
        return htmlContent.toString();
    }

    private AbstractHTMLElement content() {
        HTMLDiv div = new HTMLDiv();
        div.addChild(new HTMLHeader(2).textContent(listType.getLabel() + " (" + input.size() + " membres)"));
        HTMLTable table = new HTMLTable().width(100);
        table.newRow().newHead("Nom");
        table.row().newHead("Prénom");
        table.row().newHead("Adresse");
        table.row().newHead("Chien");

        List<Membre> list = input.stream().sorted().collect(Collectors.toList());
        for (Membre member : list) {
            table.newRow().newCell().addChild(attributeLink(member, member.getNom()));
            table.row().newCell().addChild(attributeLink(member, member.getPrenom()));
            table.row().newCell().addChild(attributeLink(member, member.fullAddress()));
            table.row().newCell().addChild(attributeLink(member, MoreObjects.firstNonNull(member.getDescription(), "&nbsp;")));
        }
        div.addChild(table);
        return div;
    }

    private HTMLHyperlinks attributeLink(Membre member, @NotNull @NotEmpty String attribute) {
        HTMLHyperlinks href = new HTMLHyperlinks().href(AllMembersReport.familyFileName(member));
        href.textContent(attribute);
        return href;
    }

    private AbstractHTMLElement menuList(ListType listType) {
        HTMLUnorderedList ul = new HTMLUnorderedList();
        for (ListType l : ListType.values()) {
            if (l.equals(listType)) {
                HTMLListItem item = new HTMLListItem();
                item.textContent("<B>" + l.getLabel() + "</B>");
                ul.addListItem(item);
            } else {
                HTMLListItem item = new HTMLListItem();
                item.addChild(new HTMLHyperlinks().href(l.getFilename()).textContent(l.getLabel()));
                ul.addListItem(item);
            }
        }
        return ul;
    }
}
