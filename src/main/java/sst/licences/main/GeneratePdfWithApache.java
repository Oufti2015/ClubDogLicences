package sst.licences.main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class GeneratePdfWithApache {
    public static void main(String[] args) {
        // Nom du fichier de destination
        String dest = "d:/rapport_apache.pdf";

        // Créer un nouveau document PDF
        try (PDDocument document = new PDDocument()) {
            // Ajouter une page
            PDPage page = new PDPage();
            document.addPage(page);

            // Ajouter du contenu à la page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Définir la police et la taille
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(50, 750);

                // Titre du rapport
                contentStream.showText("Rapport PDF - Apache PDFBox");
                contentStream.newLine();
                contentStream.setFont(PDType1Font.HELVETICA, 12);

                // Contenu du rapport
                contentStream.showText("Ceci est un exemple de rapport généré avec la bibliothèque Apache PDFBox.");
                contentStream.newLine();
                contentStream.showText("Vous pouvez ajouter du texte, des images, et bien plus encore.");
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("Liste des éléments:");
                contentStream.newLine();
                contentStream.showText("- Élément 1");
                contentStream.newLine();
                contentStream.showText("- Élément 2");
                contentStream.newLine();
                contentStream.showText("- Élément 3");

                contentStream.endText();
            }

            // Enregistrer le fichier
            document.save(dest);
            System.out.println("Le rapport PDF a été généré avec succès : " + dest);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
