package sst.licences.report;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import sst.licences.container.LicencesContainer;
import sst.licences.model.Membre;
import sst.licences.model.Payment;

import java.io.IOException;
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

    public void header(PDPage page) throws IOException {

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA_BOLD;

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Define a text content stream using the selected font, moving the cursor and drawing the text "Hello World"
        contentStream.beginText();
        contentStream.setFont(font, 12);
        contentStream.newLineAtOffset(10, START_OFFSET);
        contentStream.showText("Nom    Chien    Montant    Normal 37 €    Familiale 50 €");
        contentStream.lineTo(100, START_OFFSET);
        contentStream.showText("Cours 25 €");
        contentStream.endText();

        // Make sure that the content stream is closed:
        contentStream.close();

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
                    .filter(m -> m.getAccountId() != null && m.getAccountId().equals(payement.getCompte()))
                    .findFirst();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateString = payement.getDate().format(dateTimeFormatter);  //17-02-2022
            String line = String.format("%s %4.2f € %-30s",
                    dateString,
                    payement.getMontant(),
                    payement.getNom());

            contentStream.showText(line);
            lineOffset += 1;
            contentStream.newLine();
            if (membreOpt.isPresent()) {
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
//                contentStream.setFont(font, 12);

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
        document.save("BlankPage.pdf");

        // finally make sure that the document is properly closed.
        document.close();
        return this;
    }

    @Override
    public String output() {
        return null;
    }
}
