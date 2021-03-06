CREATE OR REPLACE VIEW V_AN_LOI_HAB_ORDONNANCE  AS
SELECT 
TM_RAC.NUMERO AS RAC_NUMERO,
TM_RAC.DATEPUBLICATION AS RAC_DATEPUBLICATION,
TM_RAC.TITREOFFICIEL AS RAC_TITREACTE,
TM_HAB.NUMEROORDRE AS HAB_NUMEROORDRE,
TM_HAB.ARTICLE AS HAB_ARTICLE,
TM_HAB.OBJETRIM AS HAB_OBJETRIM,
VOC_THAB."label" AS HAB_LBLTYPEHABILITATION,
TM_HAB.TYPEHABILITATION AS HAB_TYPEHABILITATION,
TM_HAB.MINISTEREPILOTE AS HAB_MINISTEREPILOTE,
TM_HAB.DATESAISINECE AS HAB_DATESAISINECE,
TM_HAB.DATEPREVISIONNELLECM AS HAB_DATEPREVISIONNELLECM,
TM_HAB.LEGISLATURE AS HAB_LEGISLATURE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.TITREOFFICIEL AS ORD_TITREACTE,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_ORD.DATEPASSAGECM AS ORD_DATEPASSAGECM,
TM_ORD.DATESAISINECE AS ORD_DATESAISINECE,
TM_HAB.CONVENTIONDEPOT AS ORD_TERMEDEPOT, 
TM_HAB.DATETERME AS ORD_DATETERME,
TM_ORD.DATELIMITEDEPOT AS ORD_DATELIMITEDEPOT,
TM_ORD.OBSERVATION  AS ORD_OBSERVATION
FROM 
 TEXTE_MAITRE TM_RAC 
 INNER JOIN      ACTIVITE_NORMATIVE AN      ON TM_RAC.ID  = AN.ID AND AN.ORDONNANCE38C = '1'
 LEFT OUTER JOIN TEXM_HABILITATIONIDS TEXH  ON TM_RAC.ID  = TEXH.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_HAB        ON TEXH.ITEM  = TM_HAB.ID 
 	LEFT OUTER JOIN VOC_TYPE_HABILITATION VOC_THAB ON TM_HAB.TYPEHABILITATION = VOC_THAB."id"
 LEFT OUTER JOIN TEXM_ORDONNANCEIDS TEXO    ON TM_HAB.ID  = TEXO.ID  
 LEFT OUTER JOIN TEXTE_MAITRE TM_ORD        ON TEXO.ITEM  = TM_ORD.ID  
where TM_HAB.TYPEHABILITATION = '0'
ORDER BY TM_RAC.NUMERO, TM_ORD.DATETERME;


CREATE OR REPLACE VIEW V_AN_ORDONNANCE_38  AS
SELECT 
TM_RAC.NUMERO AS RAC_NUMERO,
TM_RAC.TITREOFFICIEL AS HAB_TITREOFFICIEL,
TM_RAC.HAB_ARTICLE AS RAC_TITREACTE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.TITREOFFICIEL AS ORD_TITREOFFICIEL,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_LRA.DATELIMITEDEPOT AS LRA_DATELIMITEDEPOT,
TM_ORD.CONVENTIONDEPOT AS LRA_CONVENTIONDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.CHAMBREDEPOT AS LRA_CHAMBREDEPOT,
TM_LRA.DATEDEPOT AS LRA_DATEDEPOT,
TM_LRA.NUMERODEPOT AS LRA_NUMERODEPOT,
TM_LRA.TITREOFFICIEL AS LRA_TITREOFFICIEL,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION,
TM_ORD.RATIFIE AS ORD_RATIFIE
FROM 
 (TEXTE_MAITRE TM_ORD 
 INNER JOIN      ACTIVITE_NORMATIVE AN      ON TM_ORD.ID  = AN.ID AND AN.ORDONNANCE = '1' AND  TM_ORD.DISPOSITIONHABILITATION = 1)
 LEFT OUTER JOIN
 ( SELECT TEXO.ITEM, TM_RAC.*, TM_HAB.ARTICLE as HAB_ARTICLE FROM
   TEXM_ORDONNANCEIDS TEXO
   INNER JOIN      TEXTE_MAITRE TM_HAB        ON TEXO.ID    = TM_HAB.ID
   INNER JOIN      TEXM_HABILITATIONIDS TEXH  ON TM_HAB.ID  = TEXH.ITEM
   INNER JOIN      TEXTE_MAITRE TM_RAC        ON TEXH.ID    = TM_RAC.ID ) TM_RAC ON TM_ORD.ID = TM_RAC.ITEM
 LEFT OUTER JOIN   TEXM_LOIRATIFICATIONIDS  TEXL   ON TM_ORD.ID = TEXL.ID
 LEFT OUTER JOIN   TEXTE_MAITRE TM_LRA             ON TEXL.ITEM = TM_LRA.ID 
 ORDER BY TM_LRA.DATELIMITEDEPOT;
 
CREATE OR REPLACE VIEW V_AN_LOIS_APPELANT_MESURE_VIG  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP, 
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION, 
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR
FROM (TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = '1' )
WHERE EXISTS (
    SELECT TM_MES.ID FROM TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM ON TM_MES.ID = TEXM.ITEM 
    WHERE TEXM.ID = TM_LOI.ID) 
AND NOT EXISTS (
    SELECT TM_MES.ID FROM TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM ON TM_MES.ID = TEXM.ITEM 
    WHERE TEXM.ID = TM_LOI.ID AND TM_MES.TYPEMESURE = '1')
AND TM_LOI.APPLICATIONDIRECTE <> 1
AND TM_LOI.DATEENTREEENVIGUEUR IS NOT NULL;