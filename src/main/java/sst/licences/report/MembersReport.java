package sst.licences.report;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import sst.licences.config.ConfigUtil;
import sst.licences.container.MemberEligibility;
import sst.licences.model.Membre;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MembersReport implements IMembreReport {
    private List<Membre> input = null;

    @Override
    public MembersReport input(List<Membre> input) {
        this.input = input.stream().sorted().collect(Collectors.toList());
        return this;
    }

    @Override
    public IMembreReport format() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Create a new blank page and add it to the document
        // Initialiser le document PDF
        try (PdfWriter writer = new PdfWriter(filename(ConfigUtil.me().reportMembers()));
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            // A4 en mode paysage
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

            // Charger une police intégrée
            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.setFont(font);

            // Ajouter un titre
            document.add(new Paragraph("Berger Club Arlonais").setBold().setFontSize(18));

            // Ajouter un titre
            document.add(new Paragraph("Membres (" + LocalDate.now().format(dateTimeFormatter) + ")").setBold().setFontSize(14));

            // Ajouter une table
            float[] columnWidths = {20, 20, 50, 15, 30, 5, 5, 5};

            int year = LocalDate.now().getYear();
            String[] headers = {"Nom", "Prénom", "Address", "Téléphone", "Nom du chien", "Date d'affiliation", "" + year, "" + (year + 1)};
            Table table = new Table(columnWidths);

            for (String header : headers) {
                table.addHeaderCell(header);
            }

            for (Membre member : input) {
                table.addCell(textCell(member.getNom()));
                table.addCell(textCell(member.getPrenom()));
                table.addCell(textCell(member.fullAddress()));
                table.addCell(textCell(member.getGsm()));
                table.addCell(textCell(member.getDescription() == null ? "" : member.getDescription()));
                table.addCell(textCell(member.isComite() ? "Comité" : member.getAffiliation().format(dateTimeFormatter)));
                table.addCell(textCell(member.isComite() || MemberEligibility.isCurrentYearAffiliated(member) ? "X" : ""));
                table.addCell(textCell(member.isComite() || MemberEligibility.isNextYearAffiliated(member) ? "X" : ""));
            }

            document.add(table);
        }
        return this;
    }

    private Cell headCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text).setBold().setFontSize(10));
        return cell;
    }

    private Cell textCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text).setFontSize(8));
        return cell;
    }

    private Cell textRightCell(String text) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text).setFontSize(8));
        cell.setTextAlignment(TextAlignment.RIGHT);
        return cell;
    }

    private String filename(String baseName) {
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMM-dd.HH.mm.ss"));
        return baseName.replace("{date}", formattedDate);
    }

    @Override
    public String output() {
        return null;
    }
}
