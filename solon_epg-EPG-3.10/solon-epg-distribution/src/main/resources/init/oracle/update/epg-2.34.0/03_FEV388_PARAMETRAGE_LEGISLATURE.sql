CREATE TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE" (
"ID" VARCHAR(36) NOT NULL, 
"EC_TP_PROMUL_LOIS_DEBUT" TIMESTAMP (6), 
"PRE_TP_PUBLI_DECRETS_FIN" TIMESTAMP (6),
"EC_BS_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"PRE_TL_PROMUL_LOIS_FIN" TIMESTAMP (6),
"PRE_BS_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"EC_BS_PROMUL_LOIS_FIN" TIMESTAMP (6),
"EC_TL_PROMUL_LOIS_FIN" TIMESTAMP (6),
"PRE_BS_PROMUL_LOIS_DEBUT" TIMESTAMP (6),
"EC_TL_PROMUL_LOIS_DEBUT" TIMESTAMP (6),
"PRE_TP_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"EC_TP_PROMUL_LOIS_FIN" TIMESTAMP (6),
"EC_DEBUT_LEGISLATURE" TIMESTAMP (6),
"PRE_DEBUT_LEGISLATURE" TIMESTAMP (6),
"LEGIS_EC" VARCHAR(512), 
"PRE_TL_PROMUL_LOIS_DEBUT" TIMESTAMP (6), 
"EC_BS_PROMUL_LOIS_DEBUT" TIMESTAMP (6),
"PRE_TL_PUBLI_DECRETS_DEBUT" TIMESTAMP (6),
"PRE_BS_PROMUL_LOIS_FIN" TIMESTAMP (6), 
"EC_TL_PUBLI_DECRETS_FIN" TIMESTAMP (6), 
"PRE_TP_PROMUL_LOIS_DEBUT" TIMESTAMP (6), 
"PRE_TL_PUBLI_DECRETS_FIN" TIMESTAMP (6), 
"PRE_BS_PUBLI_DECRETS_FIN" TIMESTAMP (6), 
"EC_TL_PUBLI_DECRETS_DEBUT" TIMESTAMP (6), 
"EC_BS_PUBLI_DECRETS_FIN" TIMESTAMP (6),
"EC_TP_PUBLI_DECRETS_DEBUT" TIMESTAMP (6), 
"EC_TP_PUBLI_DECRETS_FIN" TIMESTAMP (6),
"PRE_TP_PROMUL_LOIS_FIN" TIMESTAMP (6),
"LEGISLATURE_PUBLICATION" VARCHAR(512),
"LEGISLATURE_PREC_PUBLICATION" VARCHAR(512));
CREATE TABLE "PARAN_LEGISLATURES" ("ID" VARCHAR(36) NOT NULL, "POS" INTEGER, "ITEM" VARCHAR(512));
ALTER TABLE "PARAN_LEGISLATURES" ADD CONSTRAINT "PARAN_LEGIS_ID_HIERARCHY_FK" FOREIGN KEY ("ID") REFERENCES "HIERARCHY" ON DELETE CASCADE;
CREATE INDEX "PARAN_LEGISLATURES_ID_IDX" ON "PARAN_LEGISLATURES" ("ID");

ALTER TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE" ADD CONSTRAINT "AN_PARAMETRAGE_PK" PRIMARY KEY ("ID");
ALTER TABLE "ACTIVITE_NORMATIVE_PARAMETRAGE" ADD CONSTRAINT "AN_PARAMETRAGE_ID_HIERARCHY_FK" FOREIGN KEY ("ID") REFERENCES "HIERARCHY" ON DELETE CASCADE;

ALTER TABLE "EXPORT_PAN_STAT" ADD ("LEGISLATURES" VARCHAR(512));

ALTER TABLE "TEXTE_MAITRE" modify (legislaturepublication default ' ' );
update "TEXTE_MAITRE" set legislaturepublication=' ' where legislaturepublication is null;

CREATE OR REPLACE FORCE VIEW "V_AN_MESURES_PREVISIONNELLE" ("NUMEROLOI", "TITREOFFICIEL", "NUMEROORDRE", "ARTICLE", "BASELEGALE", "OBJETRIM", "MINISTEREPILOTE", "DIRECTIONRESPONSABLE", "CONSULTATIONSHCE", "CALENDRIERCONSULTATIONSHCE", "DATEPREVISIONNELLESAISINECE", "DATEOBJECTIFPUBLICATION", "DATESAISINECE", "DATESORTIECE", "OBSERVATION", "NUMERONOR", "DECRET_TITREOFFICIEL", "TYPACTE", "DATEPUBLICATION", "DATEMISEAPPLICATION", "DATEPUBLICATIONLOI", "APPLICATIONRECU","LEGISLATUREPUBLICATION")
                       AS
  SELECT TM_LOI.NUMERO AS NUMEROLOI,
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
    TM_DEC.DATESAISINECE,
    TM_DEC.DATESORTIECE,
    TM_MES.OBSERVATION,
    TM_DEC.NUMERONOR,
    TM_DEC.TITREOFFICIEL DECRET_TITREOFFICIEL,
    NVL(VOC_TM_DEC."label", '') AS TYPACTE,
    TM_DEC.DATEPUBLICATION,
    TM_MES.DATEMISEAPPLICATION,
    TM_LOI.DATEPUBLICATION DATEPUBLICATIONLOI,
    TM_MES.APPLICATIONRECU,    
    TM_LOI.LEGISLATUREPUBLICATION LEGISLATUREPUBLICATION
  FROM TEXTE_MAITRE TM_LOI,
    TEXM_MESUREIDS TME,
    ACTIVITE_NORMATIVE AN,
    TEXTE_MAITRE TM_MES
  LEFT JOIN TEXM_DECRETIDS TD
  ON TD.ID = TM_MES.ID
  LEFT JOIN TEXTE_MAITRE TM_DEC
  ON TM_DEC.ID = TD.ITEM
  LEFT JOIN VOC_ACTE_TYPE VOC_TM_DEC
  ON TM_DEC.TYPEACTE                 = VOC_TM_DEC."id"
  WHERE TM_LOI.ID                    = AN.ID
  AND AN.APPLICATIONLOI              = '1'
  AND TM_MES.TYPEMESURE              = '1'
  AND TME.ID                         = AN.ID
  AND TM_MES.ID                      = TME.ITEM
  AND TM_MES.DATEOBJECTIFPUBLICATION < SYSDATE;
  
commit;
