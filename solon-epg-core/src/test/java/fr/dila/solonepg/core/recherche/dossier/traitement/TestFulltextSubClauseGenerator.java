package fr.dila.solonepg.core.recherche.dossier.traitement;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class TestFulltextSubClauseGenerator {

    @Test
    public void testIndexActe() {
        EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_ACTE;
        List<String> filetypeIds = new ArrayList<>();
        filetypeIds.add(String.valueOf(index.getFiletypeId()));
        FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
        //		Assert.assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 1 AND f.ver:islatest = 1)",
        Assert.assertEquals(
            "(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 1)",
            generator.getFullText("toto", filetypeIds)
        );
    }

    @Test
    public void testIndexExtrait() {
        EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_EXTRAIT;
        List<String> filetypeIds = new ArrayList<>();
        filetypeIds.add(String.valueOf(index.getFiletypeId()));
        FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
        //		Assert.assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 2 AND f.ver:islatest = 1)",
        Assert.assertEquals(
            "(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 2)",
            generator.getFullText("toto", filetypeIds)
        );
    }

    @Test
    public void testIndexAutrePieceParapheur() {
        EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_AUTRE_PIECE_PARAPHEUR;
        List<String> filetypeIds = new ArrayList<>();
        filetypeIds.add(String.valueOf(index.getFiletypeId()));
        FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
        Assert.assertEquals(
            "(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 3)",
            //		Assert.assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 3 AND f.ver:islatest = 1)",
            generator.getFullText("toto", filetypeIds)
        );
    }

    @Test
    public void testIndexFondDossier() {
        EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_FOND_DOSSIER;
        List<String> filetypeIds = new ArrayList<>();
        filetypeIds.add(String.valueOf(index.getFiletypeId()));
        FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
        Assert.assertEquals(
            "(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 4)",
            //		Assert.assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 4 AND f.ver:islatest = 1)",
            generator.getFullText("toto", filetypeIds)
        );
    }
}
