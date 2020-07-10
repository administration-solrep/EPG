package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;

public enum EPGFulltextIndexEnum {
        
        TXT_ACTE(VocabularyConstants.FILETYPEID_ACTE){
            public Boolean isNeededBy(final RequeteDossierSimple requete){ 
                return requete.getTIDansActe();
            }
         },
        TXT_EXTRAIT(VocabularyConstants.FILETYPEID_EXTRAIT){ 
             public Boolean isNeededBy(final RequeteDossierSimple requete){ 
                 return requete.getTIDansExtrait(); 
             }
         },
         TXT_AUTRE_PIECE_PARAPHEUR(VocabularyConstants.FILETYPEID_AUTREPARAPHEUR){ 
            public Boolean isNeededBy(final RequeteDossierSimple requete){ 
                return requete.getTIDansAutrePieceParapheur();
            }
         },
         TXT_FOND_DOSSIER(VocabularyConstants.FILETYPEID_FONDDOSSIER){ 
            public Boolean isNeededBy(final RequeteDossierSimple requete){ 
                return requete.getTIDansFondDossier();
            }
        };
        
        private Integer filetypeId;
        
        EPGFulltextIndexEnum(Integer filetypeId){
            this.filetypeId = filetypeId;
        }
        
        public Integer getFiletypeId(){
            return this.filetypeId;
        }
        
        public abstract Boolean isNeededBy(final RequeteDossierSimple requete);
}  
