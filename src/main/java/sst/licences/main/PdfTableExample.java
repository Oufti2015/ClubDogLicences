package sst.licences.main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

public class PdfTableExample {
    public static void main(String[] args) {
        String dest = "d:/rapport_table.pdf";

        try (PDDocument document = new PDDocument()) {
            // Ajouter une page
            PDPage page = new PDPage();
            document.addPage(page);

            // Créer un flux pour écrire dans la page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                // Coordonnées de départ pour le tableau
                float margin = 50;
                float yStart = 750; // Position verticale de départ
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;
                float rowHeight = 20;
                float cellMargin = 5;

                // Données du tableau
                String[][] content = {
                        {"ID", "Nom", "Description"},
                        {"1", "Élément A", "Description de l'élément A"},
                        {"2", "Élément B", "Description de l'élément B"},
                        {"3", "Élément C", "Description de l'élément C"}
                };

                // Largeur des colonnes
                float[] columnWidths = {50, 150, 300};

                // Dessiner les lignes et colonnes
                for (int i = 0; i < content.length; i++) {
                    float nextY = yPosition - rowHeight;
                    // Dessiner la ligne horizontale
                    contentStream.moveTo(margin, yPosition);
                    contentStream.lineTo(margin + tableWidth, yPosition);
                    contentStream.stroke();

                    // Dessiner le contenu des colonnes
                    float xPosition = margin;
                    for (int j = 0; j < content[i].length; j++) {
                        // Dessiner la bordure verticale
                        contentStream.moveTo(xPosition, yPosition);
                        contentStream.lineTo(xPosition, nextY);
                        contentStream.stroke();

                        // Ajouter le texte dans la cellule
                        contentStream.beginText();
                        contentStream.setFont(PDType1Font.HELVETICA, 10);
                        contentStream.newLineAtOffset(xPosition + cellMargin, nextY + cellMargin);
                        contentStream.showText(content[i][j]);
                        contentStream.endText();

                        // Déplacer la position horizontale
                        xPosition += columnWidths[j];
                    }

                    // Dessiner la dernière bordure verticale de la ligne
                    contentStream.moveTo(xPosition, yPosition);
                    contentStream.lineTo(xPosition, nextY);
                    contentStream.stroke();

                    // Déplacer la position verticale pour la prochaine ligne
                    yPosition = nextY;
                }

                // Dessiner la dernière ligne horizontale
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(margin + tableWidth, yPosition);
                contentStream.stroke();
            }

            // Enregistrer le document
            document.save(dest);
            System.out.println("Le rapport avec tableau a été généré : " + dest);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
