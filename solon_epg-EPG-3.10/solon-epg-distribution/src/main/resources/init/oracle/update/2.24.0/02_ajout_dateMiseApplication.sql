ALTER TABLE TEXTE_MAITRE 
ADD
  (
    "DATEMISEAPPLICATION" TIMESTAMP (6)
  )
;
COMMIT;

CREATE OR REPLACE VIEW V_AN_MESURES_LEGISLATIVE  AS
SELECT
TM_LOI.MOTCLE,
TM_LOI.NUMERO AS NUMEROLOI,
TM_LOI.TITREOFFICIEL,
TO_CHAR(TM_LOI.DATEPUBLICATION , 'DD/MM/YYYY') DATEPUBLICATION,
TM_LOI.LEGISLATUREPUBLICATION,
TM_MES.NUMEROORDRE,
TM_MES.ARTICLE,
TM_MES.CODEMODIFIE,
TM_MES.BASELEGALE,
TM_MES.OBJETRIM,
TM_MES.MINISTEREPILOTE,
TM_MES.DIRECTIONRESPONSABLE,
TM_MES.CONSULTATIONSHCE,
TM_MES.CALENDRIERCONSULTATIONSHCE,
TM_MES.DATEPREVISIONNELLESAISINECE,
TM_MES.DATEENTREEENVIGUEUR,
TM_MES.DATEOBJECTIFPUBLICATION DATEOBJECTIFPUBLICATION,
TM_MES.TYPEMESURE           AS TYPEMESUREID,
NVL(VOC_TM_MES."label", '') AS TYPEMESURE,
TM_MES.OBSERVATION,
TM_DEC.NUMERONOR,
TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL,
NVL(VOC_TM_DEC."label",' ') AS TYPACTE,
TO_CHAR(TM_DEC.DATESAISINECE, 'DD/MM/YYYY') DATESAISINECE,
TO_CHAR(TM_DEC.DATESORTIECE, 'DD/MM/YYYY') DATESORTIECE,
TO_CHAR(TM_DEC.DATEPUBLICATION, 'DD/MM/YYYY') DECRET_DATEPUBLICATION,
TO_CHAR(TM_MES.DATEMISEAPPLICATION, 'DD/MM/YYYY') DATEMISEAPPLICATION,
TM_MES.APPLICATIONRECU
FROM TEXTE_MAITRE TM_LOI, TEXM_MESUREIDS TME, ACTIVITE_NORMATIVE AN, TEXTE_MAITRE TM_MES 
LEFT JOIN VOC_TYPE_MESURE VOC_TM_MES ON TM_MES.TYPEMESURE = VOC_TM_MES."id"
LEFT JOIN TEXM_DECRETIDS TD ON TD.ID = TM_MES.ID
LEFT JOIN TEXTE_MAITRE TM_DEC ON TM_DEC.ID = TD.ITEM
LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC ON TM_DEC.TYPEACTE = VOC_TM_DEC."id"
WHERE TM_LOI.ID = AN.ID
AND AN.APPLICATIONLOI = '1'
AND TM_MES.TYPEMESURE IN ('1','4' ,'6')
AND TME.ID = AN.ID
AND TM_MES.ID = TME.ITEM
ORDER BY NUMEROLOI, TO_NUMBER(TM_MES.NUMEROORDRE);

CREATE OR REPLACE VIEW V_AN_MESURES_PREVISIONNELLE  AS
SELECT
TM_LOI.NUMERO AS NUMEROLOI,  
TM_LOI.TITREOFFICIEL, 
TM_MES.NUMEROORDRE, 
TM_MES.ARTICLE, 
TM_MES.BASELEGALE, 
TM_MES.OBJETRIM, 
TM_MES.MINISTEREPILOTE, 
TM_MES.DIRECTIONRESPONSABLE,
TM_MES.CONSULTATIONSHCE,
TM_MES.CALENDRIERCONSULTATIONSHCE,
TM_MES.DATEPREVISIONNELLESAISINECE,
TM_MES.DATEOBJECTIFPUBLICATION,
TO_CHAR(TM_DEC.DATESAISINECE, 'DD/MM/YYYY') DATESAISINECE, 
TO_CHAR(TM_DEC.DATESORTIECE, 'DD/MM/YYYY') DATESORTIECE, 
TM_MES.OBSERVATION, 
TM_DEC.NUMERONOR,
TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL, 
NVL(VOC_TM_DEC."label", '') AS TYPACTE,
TO_CHAR(TM_DEC.DATEPUBLICATION, 'DD/MM/YYYY') DATEPUBLICATION,
TO_CHAR(TM_MES.DATEMISEAPPLICATION, 'DD/MM/YYYY') DATEMISEAPPLICATION,
TM_MES.APPLICATIONRECU
FROM TEXTE_MAITRE TM_LOI, TEXM_MESUREIDS TME, ACTIVITE_NORMATIVE AN, TEXTE_MAITRE TM_MES
LEFT JOIN TEXM_DECRETIDS TD ON TD.ID = TM_MES.ID
LEFT JOIN TEXTE_MAITRE TM_DEC ON TM_DEC.ID = TD.ITEM
LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC ON TM_DEC.TYPEACTE = VOC_TM_DEC."id"
WHERE TM_LOI.ID = AN.ID
AND AN.APPLICATIONLOI = '1'
AND TM_MES.TYPEMESURE = '1'
AND TME.ID = AN.ID
AND TM_MES.ID = TME.ITEM
AND TM_MES.DATEOBJECTIFPUBLICATION < SYSDATE
ORDER BY NUMEROLOI, TO_NUMBER(TM_MES.NUMEROORDRE);

CREATE OR REPLACE VIEW V_AN_MESURES_RECU_APPLICATION  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP, 
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION, 
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE, 
TM_LOI.PROCEDUREVOTE AS LOI_PROCEDUREVOTE, 
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE,
TM_DEC.ID AS DECRET_ID,
TM_DEC.DATEPUBLICATION AS DECRET_DATEPUBLICATION,
TM_MES.DATEMISEAPPLICATION AS MES_DATEMISEAPPLICATION
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = '1' ), 
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM),
(TEXTE_MAITRE TM_DEC INNER JOIN TEXM_DECRETIDS TEXD   ON TM_DEC.ID = TEXD.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU = '1'
AND   TM_MES.ID = TEXD.ID
AND   TM_DEC.DATEPUBLICATION = (SELECT MAX(MX_TM_DEC.DATEPUBLICATION)
								FROM TEXM_MESUREIDS MX_TEXM, 
								(TEXTE_MAITRE MX_TM_DEC INNER JOIN TEXM_DECRETIDS MX_TEXD   ON MX_TM_DEC.ID = MX_TEXD.ITEM)
								WHERE TM_MES.ID = MX_TEXM.ITEM
								AND   MX_TEXM.ID = MX_TM_DEC.ID
                                );

commit;