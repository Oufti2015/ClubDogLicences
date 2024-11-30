package sst.licences.report;

import com.itextpdf.io.font.FontProgramFactory;
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
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PaymentReport2 implements IPaymentsReport {
    private List<Payment> input = null;

    @Override
    public IPaymentsReport input(List<Payment> input) {
        this.input = input;

        return this;
    }

    @Override
    public IPaymentsReport format() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Create a new blank page and add it to the document
        // Initialiser le document PDF
        try (PdfWriter writer = new PdfWriter(filename(ConfigUtil.me().reportPayments()));
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
            document.add(new Paragraph("Paiements (" + LocalDate.now().format(dateTimeFormatter) + ")").setBold().setFontSize(14));

            // Ajouter une table
            float[] columnWidths = {5, 5, 30, 30, 30};
            String[] headers = {"Date", "Montant", "Nom", "Nom du chien", "Communication"};
            Table table = new Table(columnWidths);

            for (String header : headers) {
                table.addHeaderCell(header);
            }

            for (Payment payement : input) {
                Optional<Membre> membreOpt = LicencesContainer.me().allMembers()
                        .stream()
                        .filter(m -> !m.getAccounts().isEmpty() && m.getAccounts().contains(payement.getCompte()))
                        .findFirst();

                table.addCell(textCell(payement.getDate().format(dateTimeFormatter)));
                table.addCell(textRightCell(String.format("%4.2f €", payement.getMontant())));
                table.addCell(textCell(String.format("%-30s", payement.getNom())));

                if (membreOpt.isPresent() && membreOpt.get().getDescription() != null) {
                    table.addCell(textCell(membreOpt.get().getDescription().trim()));
                } else {
                    table.addCell(textCell(payement.getCompte()));
                }

                table.addCell(textCell(payement.getCommunications()));
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
