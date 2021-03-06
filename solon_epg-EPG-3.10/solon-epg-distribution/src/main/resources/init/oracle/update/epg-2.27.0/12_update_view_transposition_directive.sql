CREATE OR REPLACE VIEW V_AN_TRANSPOSITION_DIRECTIVE  AS
SELECT 
TM_DIR.NUMERO , TM_DIR.DATEDIRECTIVE, TM_DIR.TITREACTE AS TITRE, TM_DIR.DATEPROCHAINTABAFFICHAGE, TM_DIR.MINISTEREPILOTE AS TD_MINISTEREPILOTE, 
TM_DIR.TABAFFICHAGEMARCHEINT, TM_DIR.ACHEVEE, TM_DIR.OBSERVATION AS TD_OBSERVATION, TM_DIR.DATEECHEANCE, TM_DIR.CHAMPLIBRE, TM_DIR.DATETRANSPOSITIONEFFECTIVE,
VOC."label" AS NATURE, TM_TT.NUMERONOR, TM_TT.MINISTEREPILOTE AS TT_MINISTERE_PILOTE, TM_TT.ETAPESOLON, TM_TT.NUMEROTEXTEPUBLIE,
TM_TT.TITRETEXTEPUBLIE, TM_TT.DATEPUBLICATION, VOC_DIR_ETAT."label" as DIRECTIVEETAT
FROM 
(((((
 TEXTE_MAITRE TM_DIR 
 INNER JOIN      ACTIVITE_NORMATIVE AN ON TM_DIR.ID = AN.ID AND AN.TRANSPOSITION = '1' ) 
 LEFT OUTER JOIN TEXM_DOSSIERSNOR TEXN ON TM_DIR.ID = TEXN.ID)
 LEFT OUTER JOIN TEXTE_MAITRE TM_TT    ON TEXN.ITEM = TM_TT.NUMERONOR)
 LEFT OUTER JOIN VOC_ACTE_TYPE VOC     ON TM_TT.NATURETEXTE = VOC."id")
 LEFT OUTER JOIN VOC_DIRECTIVE_ETAT VOC_DIR_ETAT     ON TM_DIR.DIRECTIVEETAT = VOC_DIR_ETAT."id");
 