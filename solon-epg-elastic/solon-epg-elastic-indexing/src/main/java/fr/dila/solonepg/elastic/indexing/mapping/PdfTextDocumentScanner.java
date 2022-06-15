package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PdfTextDocumentScanner extends PDFTextStripper {
    private int characters = 0;

    public PdfTextDocumentScanner(PDDocument pdd) throws IOException {
        super();
        document = pdd;
    }

    /**
     * @return {true} if the pdf document contains the specified number of
     *         caracteres.
     * @throws IOException
     */
    public boolean containsNumberCharacters(int nbCharacters) throws IOException {
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            countPageCharacters(i);
            if (characters >= nbCharacters) {
                return true;
            }
        }

        return false;
    }

    private void countPageCharacters(int pageNr) throws IOException {
        this.setStartPage(pageNr + 1);
        this.setEndPage(pageNr + 1);
        Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
        writeText(document, dummy); // This call starts the parsing process and
        // calls writeString repeatedly.
    }

    @Override
    protected void writePage() throws IOException {
        characters += charactersByArticle.stream().mapToInt(Collection::size).sum();
    }
}
