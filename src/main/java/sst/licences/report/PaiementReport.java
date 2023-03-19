package sst.licences.report;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import sst.licences.config.ConfigUtil;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PaiementReport implements IPaymentsReport {
    public static final int START_OFFSET = 750;
    private List<Payment> input = null;
    // Create a new empty document
    protected PDDocument document = new PDDocument();

    @Override
    public IPaymentsReport input(List<Payment> input) {
        this.input = input;

        return this;
    }

    @Override
    public IPaymentsReport format() throws IOException {
        // Create a new blank page and add it to the document
        PDPage page = new PDPage();
        document.addPage(page);

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();

        //Setting the font to the Content stream
        contentStream.setFont(PDType1Font.COURIER, 12);
        //Setting the leading
        contentStream.setLeading(14.5f);

        float lineOffset = 1;
        // Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
        contentStream.newLineAtOffset(25, START_OFFSET);

        for (Payment payement : input) {
            Optional<Membre> membreOpt = LicencesContainer.me().allMembers()
                    .stream()
                    .filter(m -> !m.getAccounts().isEmpty() && m.getAccounts().contains(payement.getCompte()))
                    .findFirst();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateString = payement.getDate().format(dateTimeFormatter);  //17-02-2022
            String line = String.format("%s %4.2f EUROS %-30s",
                    dateString,
                    payement.getMontant(),
                    payement.getNom());

            contentStream.showText(line);
            lineOffset += 1;
            contentStream.newLine();
            if (membreOpt.isPresent() && membreOpt.get().getDescription() != null) {
                contentStream.showText(membreOpt.get().getDescription().trim());
                contentStream.newLine();
            } else {
                contentStream.showText(payement.getCompte());
                contentStream.newLine();
            }
            contentStream.showText(payement.getCommunications());
            contentStream.newLine();
            contentStream.showText("-------------------------------------------------------------------");
            contentStream.newLine();

            if (lineOffset > 12) {
                contentStream.endText();
                // Make sure that the content stream is closed:
                contentStream.close();

                page = new PDPage();
                document.addPage(page);
                // Start a new content stream which will "hold" the to be created content
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();

                //Setting the font to the Content stream
                contentStream.setFont(PDType1Font.COURIER, 12);
                //Setting the leading
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, START_OFFSET);


                lineOffset = 1;
            }
        }
        contentStream.endText();
        // Make sure that the content stream is closed:
        contentStream.close();

        // Save the newly created document
        document.save(filename(ConfigUtil.me().reportPayments()));

        // finally make sure that the document is properly closed.
        document.close();
        return this;
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
