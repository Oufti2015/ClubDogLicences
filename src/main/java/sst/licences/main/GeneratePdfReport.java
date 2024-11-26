package sst.licences.main;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class GeneratePdfReport {
    public static void main(String[] args) {
        String dest = "d:/rapport.pdf";

        try {
            // Initialiser le document PDF
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Ajouter un titre
            document.add(new Paragraph("Rapport PDF Exemple").setBold().setFontSize(18));

            // Ajouter du contenu
            document.add(new Paragraph("Ceci est un exemple de rapport généré en PDF avec Java."));

            // Ajouter une table
            float[] columnWidths = {1, 5};
            Table table = new Table(columnWidths);

            table.addCell("ID");
            table.addCell("Nom");

            for (int i = 1; i <= 5; i++) {
                table.addCell(String.valueOf(i));
                table.addCell("Exemple " + i);
            }
            document.add(table);

            // Fermer le document
            document.close();
            System.out.println("Le rapport PDF a été généré avec succès : " + dest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

