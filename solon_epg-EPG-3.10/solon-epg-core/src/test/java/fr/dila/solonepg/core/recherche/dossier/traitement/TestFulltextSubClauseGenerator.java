package fr.dila.solonepg.core.recherche.dossier.traitement;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestFulltextSubClauseGenerator extends TestCase {

	public void testIndexActe() {
		EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_ACTE;
		List<String> filetypeIds = new ArrayList<String>();
		filetypeIds.add(index.getFiletypeId().toString());
		FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
//		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 1 AND f.ver:islatest = 1)",
		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 1)",
				generator.getFullText("toto", filetypeIds));
	}

	public void testIndexExtrait() {
		EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_EXTRAIT;
		List<String> filetypeIds = new ArrayList<String>();
		filetypeIds.add(index.getFiletypeId().toString());
		FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
//		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 2 AND f.ver:islatest = 1)",
		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 2)",
				generator.getFullText("toto", filetypeIds));
	}

	public void testIndexAutrePieceParapheur() {
		EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_AUTRE_PIECE_PARAPHEUR;
		List<String> filetypeIds = new ArrayList<String>();
		filetypeIds.add(index.getFiletypeId().toString());
		FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 3)",
//		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 3 AND f.ver:islatest = 1)",
				generator.getFullText("toto", filetypeIds));
	}

	public void testIndexFondDossier() {
		EPGFulltextIndexEnum index = EPGFulltextIndexEnum.TXT_FOND_DOSSIER;
		List<String> filetypeIds = new ArrayList<String>();
		filetypeIds.add(index.getFiletypeId().toString());
		FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 4)",
//		assertEquals("(f.ecm:fulltext = \"${toto}\" AND f.filepg:filetypeId  = 4 AND f.ver:islatest = 1)",
				generator.getFullText("toto", filetypeIds));
	}

}
