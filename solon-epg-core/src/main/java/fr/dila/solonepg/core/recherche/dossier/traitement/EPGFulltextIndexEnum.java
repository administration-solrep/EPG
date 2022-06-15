package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;

public enum EPGFulltextIndexEnum {
    TXT_ACTE(VocabularyConstants.FILETYPEID_ACTE) {

        @Override
        public Boolean isNeededBy(final RequeteDossierSimple requete) {
            return requete.getTIDansActe();
        }
    },
    TXT_EXTRAIT(VocabularyConstants.FILETYPEID_EXTRAIT) {

        @Override
        public Boolean isNeededBy(final RequeteDossierSimple requete) {
            return requete.getTIDansExtrait();
        }
    },
    TXT_AUTRE_PIECE_PARAPHEUR(VocabularyConstants.FILETYPEID_AUTREPARAPHEUR) {

        @Override
        public Boolean isNeededBy(final RequeteDossierSimple requete) {
            return requete.getTIDansAutrePieceParapheur();
        }
    },
    TXT_FOND_DOSSIER(VocabularyConstants.FILETYPEID_FONDDOSSIER) {

        @Override
        public Boolean isNeededBy(final RequeteDossierSimple requete) {
            return requete.getTIDansFondDossier();
        }
    };

    private long filetypeId;

    EPGFulltextIndexEnum(long filetypeId) {
        this.filetypeId = filetypeId;
    }

    public long getFiletypeId() {
        return this.filetypeId;
    }

    public abstract Boolean isNeededBy(final RequeteDossierSimple requete);
}
