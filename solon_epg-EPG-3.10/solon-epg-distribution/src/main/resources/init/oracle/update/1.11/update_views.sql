/* Update des views*/

CREATE OR REPLACE VIEW V_BORDEREAU_SIGNATURE_LISTE  AS
SELECT DOS.MINISTERERESP, 
        DOS.NUMERONOR,
        LISTE.TYPELISTE,
        LISTE.NUMEROLISTE,
        LISTE.DATEGENERATIONLISTE,
        LISTE.TYPESIGNATURE,
        DOS.TITREACTE,
        LISTE.ID
  FROM DOSSIER_SOLON_EPG DOS,LIS_IDSDOSSIER LISDOS,LISTE_TRAITEMENT_PAPI_144E75F6 LISTE
  WHERE DOS.ID=LISDOS.ITEM 
	AND LISDOS.ID=LISTE.ID 
	AND LISTE.TYPELISTE = '1' ;


CREATE OR REPLACE VIEW V_BORDEREAU_AUTRE_LISTE  AS
SELECT DOS.MINISTERERESP, DOS.NUMERONOR, 
       DOS.TITREACTE, 
       TP.PUBLICATIONEPREUVEENRETOUR,
       TP.PUBLICATIONDELAI,
       TP.PUBLICATIONDATEDEMANDE,
       LISTE.TYPELISTE, LISTE.NUMEROLISTE, 
       LISTE.ID,
       TP.ID as DOCID,
       LISTE.DATEGENERATIONLISTE, 
       LISTE.TYPESIGNATURE,
       NVL(VOCPUBTP."label",' ') AS PUBTPLABEL, 
       NVL(VOCMODEPARU."label",' ') AS MODPARULABEL,
       NVL(VOCPUBDEL."label",' ') AS PUBDELAILABEL
  FROM DOSSIER_SOLON_EPG DOS,
       TRAITEMENT_PAPIER TP,
       LIS_IDSDOSSIER LISDOS,
       LISTE_TRAITEMENT_PAPI_144E75F6 LISTE,
       VOC_VECTEUR_PUBLICATION_TP VOCPUBTP,
       VOC_MODE_PARUTION VOCMODEPARU,
       VOC_PUBLICATION_DELAI VOCPUBDEL
 WHERE DOS.ID = TP.ID
   AND DOS.ID = LISDOS.ITEM
   AND LISDOS.ID = LISTE.ID
   AND (LISTE.TYPELISTE = '2' OR LISTE.TYPELISTE = '3')
   AND TP.PUBLICATIONNOMPUBLICATION = VOCPUBTP."id"(+)
   AND TP.PUBLICATIONMODEPUBLICATION = VOCMODEPARU."id"(+)
   AND TP.PUBLICATIONDELAI = VOCPUBDEL."id"(+);

CREATE OR REPLACE VIEW  V_BORDEREAU_COMMUNICATION AS 
SELECT
    1 AS JOIN_ID,
    DOS.ID,
    TP.SIGNATAIRE,    
    DOS.NUMERONOR,
    DOS.TITREACTE,
    TP.PRIORITE,
    NVL(VOCPRIORITE."label",' ') AS PRIORITELABEL,
    NVL(DOS.PRENOMRESPDOSSIER,' ') || ' ' || NVL(DOS.NOMRESPDOSSIER,' ') AS RESPDOSSIER,
    NVL(DOS.TELRESPDOSSIER,' ') AS TELRESPDOSSIER,
    TP.PERSONNESNOMMEES,
	  DOS.TYPEACTE,
	  TP.TEXTESOUMISAVALIDATION,
	  TP.TEXTEAPUBLIER
FROM
    DOSSIER_SOLON_EPG DOS,
    TRAITEMENT_PAPIER TP,
    VOC_PAPIER_PRIORITE  VOCPRIORITE
WHERE DOS.ID = TP.ID
AND	  TP.PRIORITE=VOCPRIORITE."id"(+);

CREATE OR REPLACE VIEW V_BORDEREAU_ENVOI_EPR_RELC AS 
SELECT
	DOS.ID,
	TP.SIGNATAIRE,
	DOS.NUMERONOR,
	DOS.TITREACTE,
	TP.PRIORITE,
	NVL(VOCPRIORITE."label",' ') AS PRIORITELABEL,
	NVL(DOS.PRENOMRESPDOSSIER,' ') || ' ' || NVL(DOS.NOMRESPDOSSIER,' ') AS RESPDOSSIER,
	NVL(DOS.TELRESPDOSSIER,' ') AS TELRESPDOSSIER
FROM 
	DOSSIER_SOLON_EPG DOS,
	TRAITEMENT_PAPIER TP,
	VOC_PAPIER_PRIORITE VOCPRIORITE
WHERE DOS.ID   = TP.ID
AND   TP.PRIORITE=VOCPRIORITE."id"(+);

CREATE OR REPLACE VIEW V_BORDEREU_RETOUR AS 
SELECT  DOS.ID,
        DOS.NUMERONOR, 
        NVL(DOS.TITREACTE,' ') AS TITREACTE, 
        NVL(TP.MOTIFRETOUR,' ') AS MOTIFRETOUR, 
        NVL(TP.RETOURA,' ') AS RETOURA,
        NVL(DOS.PRENOMRESPDOSSIER,' ') || ' ' || NVL(DOS.NOMRESPDOSSIER,' ') AS RESPDOSSIER,
        NVL(DOS.TELRESPDOSSIER,' ') AS TELRESPDOSSIER
FROM DOSSIER_SOLON_EPG DOS,
     TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID;

--********
--V_STATS
--********

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_COM  AS
SELECT DOS.*
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND TP.TEXTESOUMISAVALIDATION = 1
AND   EXISTS ( 	SELECT * 
				FROM HIERARCHY H, DESTINATAIRECOMMUNICATION DES
				WHERE DOS.ID = H.PARENTID
				AND   H.ID = DES.ID 
				AND   DES.DATERETOURCOMMUNICATION IS NULL 
				AND   DES.DATEENVOICOMMUNICATION IS NOT NULL );

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_SIG_PM_PR  AS
SELECT DISTINCT DOS.*
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND EXISTS ( 	SELECT * 
				FROM HIERARCHY H, DONNEESSIGNATAIRE SIG
				WHERE DOS.ID = H.PARENTID
				AND   H.NAME IN ('dos:signaturePm', 'dos:signaturePr')
				AND   H.ID = SIG.ID
				AND   SIG.DATEENVOISIGNATURE IS NOT NULL
				AND   SIG.DATERETOURSIGNATURE IS NULL);

CREATE OR REPLACE VIEW V_STATS_LISTE_EPREUVE_RELEC  AS
SELECT DISTINCT DOS.*
FROM DOSSIER_SOLON_EPG DOS, 
     TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND EXISTS (
				SELECT * 
				FROM INFOEPREUVE EPR, HIERARCHY H
				WHERE DOS.ID = H.PARENTID
				AND   H.ID = EPR.ID
				AND   EPR.EPREUVEENVOIRELECTURELE IS NOT NULL
				AND   TP.RETOURDUBONATITRERLE IS NULL );

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_AMPLIATION  AS
SELECT  DOS.ID, 
		DOS.NUMERONOR, 
		DOS.TITREACTE, 
		LISTAGG (AMP.ITEM, ', ') WITHIN GROUP (ORDER BY AMP.ITEM) ITEM
FROM (DOSSIER_SOLON_EPG DOS INNER JOIN TP_AMPLIATIONDESTINATAIREMAILS AMP ON DOS.ID = AMP.ID AND   DOS.STATUTARCHIVAGE IN ('1', '2'))
GROUP BY DOS.ID, DOS.NUMERONOR, DOS.TITREACTE;

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_DILA_RIG  AS
SELECT DISTINCT DOS.*
FROM DOSSIER_SOLON_EPG DOS,
     TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND( 
	EXISTS
	(
		SELECT *
		FROM ACTIONNABLE_CASE_LINK ACT, CASE_LINK CAS, ROUTING_TASK ROU
		WHERE DOS.ID = CAS.CASEDOCUMENTID
		AND   CAS.ID = ACT.ID
		AND   ACT.ROUTINGTASKID = ROU.ID
		AND   TO_CHAR (ROU.DATEDEBUTETAPE, 'MM/DD/YYYY') = TO_CHAR(SYSTIMESTAMP,'MM/DD/YYYY')
		AND   ROU.TYPE = 13
		AND   ACT.CASELIFECYCLESTATE = 'RUNNING'
		AND   DOS.DELAIPUBLICATION = 3
	)
	 OR
	(
		TO_CHAR (TP.PUBLICATIONDATEENVOIJO, 'MM/DD/YYYY') = TO_CHAR(SYSTIMESTAMP,'MM/DD/YYYY') 
		AND   TP.PUBLICATIONDELAI = 3
	)
);

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_ARRIVE_SGG  AS
SELECT DISTINCT DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, TP.DATEARRIVEPAPIER
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND DOS.ID = TP.ID;

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_CORBEILLE  AS
SELECT DISTINCT DOS.*, ROU.DISTRIBUTIONMAILBOXID
FROM DOSSIER_SOLON_EPG DOS, ACTIONNABLE_CASE_LINK ACT, CASE_LINK CAS, ROUTING_TASK ROU
WHERE DOS.ID = CAS.CASEDOCUMENTID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND CAS.ID = ACT.ID
AND ACT.ROUTINGTASKID = ROU.ID
AND ACT.CASELIFECYCLESTATE = 'running';

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_DILA_PUB  AS
SELECT DOS.*
FROM DOSSIER_SOLON_EPG DOS
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND EXISTS ( 	SELECT * 
				FROM HIERARCHY H, ROUTING_TASK ROU
				WHERE DOS.LASTDOCUMENTROUTE = H.PARENTID
				AND   H.ID = ROU.ID
				AND   TO_CHAR (ROU.DATEDEBUTETAPE, 'MM/DD/YYYY') = TO_CHAR(SYSTIMESTAMP,'MM/DD/YYYY')
				AND   ROU.TYPE IN (13, 14) );

CREATE OR REPLACE VIEW V_STATS_NOMBRE_ACTE_PAR_TYPE  AS
SELECT DOS.TYPEACTE, DOS.ID, DUB.CREATED DATEDOSSIERCREE
FROM DOSSIER_SOLON_EPG DOS, DUBLINCORE DUB
WHERE DOS.ID = DUB.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2');

CREATE OR REPLACE VIEW V_STATS_NBR_ACTE_TYPE_PERIODE  AS
SELECT DOS.ID, DOS.MINISTEREATTACHE, DOS.MINISTERERESP, DOS.DIRECTIONRESP, DOS.MAILRESPDOSSIER, DUB.CREATED DATEDOSSIERCREE
FROM DOSSIER_SOLON_EPG DOS, DUBLINCORE DUB
WHERE DOS.ID = DUB.ID
AND   DOS.STATUTARCHIVAGE IN ('1', '2');

CREATE OR REPLACE VIEW V_STATS_LISTE_ACTE_TYPE_15  AS
SELECT DOS.ID, DOS.MINISTEREATTACHE, TP.DATERETOUR, ROU.DISTRIBUTIONMAILBOXID
FROM DOSSIER_SOLON_EPG DOS, HIERARCHY H, ROUTING_TASK ROU, TRAITEMENT_PAPIER TP
WHERE DOS.LASTDOCUMENTROUTE = H.PARENTID
AND   DOS.STATUTARCHIVAGE IN ('1', '2')
AND   H.ID = ROU.ID
AND TP.ID = DOS.ID
AND   ROU.TYPE = 15;

CREATE OR REPLACE VIEW V_STATS_LISTE_ALL_ACTE  AS
SELECT DOS.ID, DOS.MINISTEREATTACHE, TP.DATERETOUR
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP
WHERE DOS.ID = TP.ID
AND DOS.STATUTARCHIVAGE IN ('1', '2');

CREATE OR REPLACE VIEW V_STATS_LISTE_ALL_ACTE_DETAIL  AS
SELECT  DOS.ID ID, 
		DOS.MINISTEREATTACHE MINISTEREATTACHE,  
		TP.DATERETOUR DATERETOUR, 
		H.POS POS, 
		ROU.DISTRIBUTIONMAILBOXID DISTRIBUTIONMAILBOXID, 
		ROU.TYPE TYPE
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP, HIERARCHY H, ROUTING_TASK ROU
WHERE DOS.ID = TP.ID
AND   DOS.LASTDOCUMENTROUTE = H.PARENTID
And   Dos.Statutarchivage In ('1', '2')
AND   H.ID = ROU.ID;

--********
--V_AN
--********
CREATE OR REPLACE VIEW V_AN_LOIS_APPELANT_MESURE  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP, 
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION, 
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR
FROM (TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 )
WHERE EXISTS
(SELECT * 
 FROM (TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM ON TM_MES.ID = TEXM.ITEM) 
 WHERE TM_MES.TYPEMESURE = '1'
 AND   TM_LOI.ID = TEXM.ID);
 
CREATE OR REPLACE VIEW V_AN_MESURES_APPELANT_DECRET  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP, 
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION, 
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE, 
TM_LOI.PROCEDUREVOTE AS LOI_PROCEDUREVOTE, 
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ), 
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM)
WHERE TM_MES.TYPEMESURE = '1'
AND   TM_LOI.ID = TEXM.ID
AND EXISTS
(SELECT * 
 FROM TEXM_DECRETIDS TEXD
 WHERE TM_MES.ID = TEXD.ID);

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
TM_DEC.DATEPUBLICATION AS DECRET_DATEPUBLICATION
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ), 
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

CREATE OR REPLACE VIEW V_AN_MESURES_ATTENTE_DECRET  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.MINISTEREPILOTE AS LOI_MINISTERERESP, 
TM_LOI.DATEPUBLICATION AS LOI_DATEPUBLICATION,
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION, 
TM_LOI.DATEENTREEENVIGUEUR AS LOI_DATEENTREEENVIGUEUR,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE, 
TM_LOI.PROCEDUREVOTE AS LOI_PROCEDUREVOTE, 
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ), 
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU <> '1'
AND EXISTS
  (SELECT * 
  FROM TEXM_DECRETIDS TEXD
  WHERE TM_MES.ID = TEXD.ID);
  
CREATE OR REPLACE VIEW V_AN_MESURES_DELAIS_DERNIER  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE,
CASE
  WHEN TM_MES.DATEENTREEENVIGUEUR IS NULL 
  THEN TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_LOI.DATEPUBLICATION) day TO second))) / 30) 
  ELSE TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_MES.DATEENTREEENVIGUEUR) day TO second))) / 30) 
END DELAIS_EN_MOIS
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ), 
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
								
CREATE OR REPLACE VIEW V_AN_MESURES_DELAIS_PREMIER  AS
SELECT 
TM_LOI.ID AS LOI_ID, 
TM_LOI.DATEPROMULGATION AS LOI_DATEPROMULGATION,
TM_LOI.NATURETEXTE AS LOI_NATURETEXTE,
TM_MES.ID AS MESURE_ID,
TM_MES.MINISTEREPILOTE AS MESURE_MINISTEREPILOTE,
CASE
  WHEN TM_MES.DATEENTREEENVIGUEUR IS NULL 
  THEN TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_LOI.DATEPUBLICATION) day TO second))) / 30) 
  ELSE TRUNC(abs(extract(day FROM ((TM_DEC.DATEPUBLICATION - TM_MES.DATEENTREEENVIGUEUR) day TO second))) / 30) 
END DELAIS_EN_MOIS
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ), 
(TEXTE_MAITRE TM_MES INNER JOIN TEXM_MESUREIDS TEXM   ON TM_MES.ID = TEXM.ITEM),
(TEXTE_MAITRE TM_DEC INNER JOIN TEXM_DECRETIDS TEXD   ON TM_DEC.ID = TEXD.ITEM)
WHERE TM_LOI.ID = TEXM.ID
AND   TM_MES.TYPEMESURE = '1'
AND   TM_MES.APPLICATIONRECU = '1'
AND   TM_MES.ID = TEXD.ID
AND   TM_DEC.DATEPUBLICATION = (SELECT MIN(MN_TM_DEC.DATEPUBLICATION)
								FROM TEXM_MESUREIDS MN_TEXM, 
								(TEXTE_MAITRE MN_TM_DEC INNER JOIN TEXM_DECRETIDS MN_TEXD   ON MN_TM_DEC.ID = MN_TEXD.ITEM)
								WHERE TM_MES.ID = MN_TEXM.ITEM
								AND   MN_TEXM.ID = MN_TM_DEC.ID
                                );
								
CREATE OR REPLACE VIEW V_AN_MESURES_PUBLIEES  AS
SELECT 
TM_MES.ID AS TM_MESURE_ID,
EXTRACT(DAY FROM TM_DEC.DATEPUBLICATION) AS JOUR_DE_PUBLICATION,
EXTRACT(MONTH FROM TM_DEC.DATEPUBLICATION) AS MOIS_DE_PUBLICATION,
EXTRACT(YEAR FROM TM_DEC.DATEPUBLICATION)  AS ANNEE_DE_PUBLICATION
FROM 
(TEXTE_MAITRE TM_LOI INNER JOIN ACTIVITE_NORMATIVE AN ON TM_LOI.ID = AN.ID AND AN.APPLICATIONLOI = 1 ), 
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
								
CREATE OR REPLACE VIEW V_AN_TRANSPOSITION_DIRECTIVE  AS
SELECT 
TM_DIR.NUMERO , TM_DIR.DATEDIRECTIVE, TM_DIR.TITREACTE AS TITRE, TM_DIR.DATEPROCHAINTABAFFICHAGE, TM_DIR.MINISTEREPILOTE AS TD_MINISTEREPILOTE, 
TM_DIR.TABAFFICHAGEMARCHEINT, TM_DIR.ACHEVEE, TM_DIR.OBSERVATION AS TD_OBSERVATION, TM_DIR.DATEECHEANCE,  
VOC."label" AS NATURE, TM_TT.NUMERONOR, TM_TT.MINISTEREPILOTE AS TT_MINISTERE_PILOTE, TM_TT.ETAPESOLON, TM_TT.NUMEROTEXTEPUBLIE,
TM_TT.TITRETEXTEPUBLIE, TM_TT.DATEPUBLICATION
FROM 
((((
 TEXTE_MAITRE TM_DIR 
 INNER JOIN      ACTIVITE_NORMATIVE AN ON TM_DIR.ID = AN.ID AND AN.TRANSPOSITION = 1 ) 
 LEFT OUTER JOIN TEXM_DOSSIERSNOR TEXN ON TM_DIR.ID = TEXN.ID)
 LEFT OUTER JOIN TEXTE_MAITRE TM_TT    ON TEXN.ITEM = TM_TT.NUMERONOR)
 LEFT OUTER JOIN VOC_ACTE_TYPE VOC     ON TM_TT.NATURETEXTE = VOC."id");
 
CREATE OR REPLACE VIEW V_AN_LOI_HAB_ORDONNANCE  AS
SELECT 
TM_RAC.NUMERO AS RAC_NUMERO,
TM_RAC.DATEPUBLICATION AS RAC_DATEPUBLICATION,
TM_RAC.TITREACTE AS RAC_TITREACTE,
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
TM_ORD.TITREACTE AS ORD_TITREACTE,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_ORD.TERMEDEPOT AS ORD_TERMEDEPOT, 
TM_ORD.DATETERME AS ORD_DATETERME,
TM_ORD.DATELIMITEDEPOT AS ORD_DATELIMITEDEPOT,
TM_ORD.OBSERVATION  AS ORD_OBSERVATION
FROM 
 TEXTE_MAITRE TM_RAC 
 INNER JOIN      ACTIVITE_NORMATIVE AN      ON TM_RAC.ID  = AN.ID AND AN.ORDONNANCE38C = 1
 LEFT OUTER JOIN TEXM_HABILITATIONIDS TEXH  ON TM_RAC.ID  = TEXH.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_HAB        ON TEXH.ITEM  = TM_HAB.ID 
 	LEFT OUTER JOIN VOC_TYPE_HABILITATION VOC_THAB ON TM_HAB.TYPEHABILITATION = VOC_THAB."id"
 LEFT OUTER JOIN TEXM_ORDONNANCEIDS TEXO    ON TM_HAB.ID  = TEXO.ID  
 LEFT OUTER JOIN TEXTE_MAITRE TM_ORD        ON TEXO.ITEM  = TM_ORD.ID  
ORDER BY TM_RAC.NUMERO, TM_ORD.DATETERME;

CREATE OR REPLACE VIEW V_AN_ORDONNANCE_38  AS
SELECT 
TM_RAC.NUMERO AS RAC_NUMERO,
TM_RAC.TITREACTE AS RAC_TITREACTE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.TITREACTE AS ORD_TITREACTE,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_LRA.DATELIMITEDEPOT AS LRA_DATELIMITEDEPOT,
TM_LRA.CONVENTIONDEPOT AS LRA_CONVENTIONDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.CHAMBREDEPOT AS LRA_CHAMBREDEPOT,
TM_LRA.DATEDEPOT AS LRA_DATEDEPOT,
TM_LRA.NUMERODEPOT AS LRA_NUMERODEPOT,
TM_LRA.TITREOFFICIEL AS LRA_TITREOFFICIEL,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION
FROM 
 (TEXTE_MAITRE TM_ORD 
 INNER JOIN      ACTIVITE_NORMATIVE AN      ON TM_ORD.ID  = AN.ID AND AN.ORDONNANCE = 1 AND  TM_ORD.DISPOSITIONHABILITATION = 1)
 LEFT OUTER JOIN
 ( SELECT TEXO.ITEM, TM_RAC.* FROM
   TEXM_ORDONNANCEIDS TEXO
   INNER JOIN      TEXTE_MAITRE TM_HAB        ON TEXO.ID    = TM_HAB.ID
   INNER JOIN      TEXM_HABILITATIONIDS TEXH  ON TM_HAB.ID  = TEXH.ITEM
   INNER JOIN      TEXTE_MAITRE TM_RAC        ON TEXH.ID    = TM_RAC.ID ) TM_RAC ON TM_ORD.ID = TM_RAC.ITEM
 LEFT OUTER JOIN   TEXM_LOIRATIFICATIONIDS  TEXL   ON TM_ORD.ID = TEXL.ID
 LEFT OUTER JOIN   TEXTE_MAITRE TM_LRA             ON TEXL.ITEM = TM_LRA.ID 
 ORDER BY TM_LRA.DATELIMITEDEPOT;

CREATE OR REPLACE VIEW V_AN_ORDONNANCE_74  AS
SELECT 
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.NUMERO AS ORD_NUMERO,
TM_ORD.TITREACTE AS ORD_TITREACTE,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_LRA.DATELIMITEDEPOT AS LRA_DATELIMITEDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION
FROM 
 (TEXTE_MAITRE TM_ORD 
 INNER JOIN      ACTIVITE_NORMATIVE AN         ON TM_ORD.ID  = AN.ID AND AN.ORDONNANCE = 1 AND  TM_ORD.DISPOSITIONHABILITATION = 0)
 LEFT OUTER JOIN TEXM_LOIRATIFICATIONIDS  TEXL ON TM_ORD.ID  = TEXL.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRA           ON TEXL.ITEM  = TM_LRA.ID 
 ORDER BY TM_LRA.DATELIMITEDEPOT;

CREATE OR REPLACE VIEW V_AN_HAB_ORD_LOI  AS
SELECT 
TM_RAC.NUMERONOR AS RAC_NUMERONOR,
TM_RAC.TITREACTE AS RAC_TITREACTE,
TM_RAC.DATEPUBLICATION AS RAC_DATEPUBLICATION,
TM_HAB.NUMEROORDRE AS HAB_NUMEROORDRE,
TM_HAB.ARTICLE AS HAB_ARTICLE,
TM_HAB.OBJETRIM AS HAB_OBJETRIM,
VOC_THAB."label" AS HAB_TYPEHABILITATION,
TM_HAB.MINISTEREPILOTE AS HAB_MINISTEREPILOTE,
TM_HAB.DATETERME AS HAB_DATETERME,
TM_HAB.CONVENTIONDEPOT AS HAB_CONVENTIONDEPOT,
TM_HAB.DATESAISINECE AS HAB_DATESAISINECE,
TM_HAB.DATEPREVISIONNELLECM AS HAB_DATEPREVISIONNELLECM,
TM_HAB.OBSERVATION AS HAB_OBSERVATION,
TM_HAB.CODIFICATION AS HAB_CODIFICATION,
TM_HAB.LEGISLATURE AS HAB_LEGISLATURE,
TM_ORD.DISPOSITIONHABILITATION AS ORD_DISPOSITIONHABILITATION,
TM_ORD.NUMERONOR AS ORD_NUMERONOR,
TM_ORD.MINISTEREPILOTE AS ORD_MINISTEREPILOTE,
TM_ORD.DATESAISINECE AS ORD_DATESAISINECE,
TM_ORD.DATEPREVISIONNELLECM AS ORD_DATEPREVISIONNELLECM,
TM_ORD.DATEPUBLICATION AS ORD_DATEPUBLICATION,
TM_ORD.TITREOFFICIEL AS ORD_TITREOFFICIEL,
TM_ORD.CONVENTIONDEPOT AS ORD_CONVENTIONDEPOT,
TM_ORD.DATELIMITEDEPOT AS ORD_DATELIMITEDEPOT,
TM_LRA.NUMERONOR AS LRA_NUMERONOR,
TM_LRA.TITREACTE AS LRA_TITREACTE,
TM_LRA.DATESAISINECE AS LRA_DATESAISINECE,
TM_LRA.DATEEXAMENCE AS LRA_DATEEXAMENCE,
TM_LRA.SECTIONCE AS LRA_SECTIONCE,
TM_LRA.DATEEXAMENCM AS LRA_DATEEXAMENCM,
TM_LRA.CHAMBREDEPOT AS LRA_CHAMBREDEPOT,
TM_LRA.DATEDEPOT AS LRA_DATEDEPOT,
TM_LRA.NUMERODEPOT AS LRA_NUMERODEPOT,
TM_LRA.TITREOFFICIEL AS LRA_TITREOFFICIEL,
TM_LRA.DATEPUBLICATION AS LRA_DATEPUBLICATION,
TM_LRA.NUMEROJOPUBLICATION AS LRA_NUMEROJOPUBLICATION
FROM 
 TEXTE_MAITRE TM_RAC 
 INNER JOIN      ACTIVITE_NORMATIVE AN        ON TM_RAC.ID  = AN.ID AND AN.ORDONNANCE38C = 1
 LEFT OUTER JOIN TEXM_HABILITATIONIDS TEXH    ON TM_RAC.ID  = TEXH.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_HAB          ON TEXH.ITEM  = TM_HAB.ID 
	LEFT OUTER JOIN VOC_TYPE_HABILITATION VOC_THAB ON TM_HAB.TYPEHABILITATION = VOC_THAB."id"
 LEFT OUTER JOIN TEXM_ORDONNANCEIDS TEXO      ON TM_HAB.ID  = TEXO.ID  
 LEFT OUTER JOIN TEXTE_MAITRE TM_ORD          ON TEXO.ITEM  = TM_ORD.ID  
 LEFT OUTER JOIN TEXM_LOIRATIFICATIONIDS TEXL ON TM_ORD.ID  = TEXL.ID
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRA          ON TEXL.ITEM  = TM_LRA.ID 
ORDER BY TM_RAC.NUMERO, TM_ORD.DATETERME;

CREATE OR REPLACE VIEW V_AN_TRAITE_ACCORD  AS
SELECT
TM_TRAIT.CATEGORIE AS TRAIT_CATEGORIE,
TM_TRAIT.ORGANISATION AS TRAIT_ORGANISATION,
TM_TRAIT.THEMATIQUE AS TRAIT_THEMATIQUE,
TM_TRAIT.DEGREPRIORITE AS TRAIT_DEGREPRIORITE,
TM_TRAIT.TITREACTE AS TRAIT_TITREACTE,
TM_TRAIT.DATESIGNATURE AS TRAIT_DATESIGNATURE,
TM_TRAIT.DATEENTREEENVIGUEUR AS TRAIT_DATEENTREEENVIGUEUR,
TM_TRAIT.MINISTEREPILOTE AS TRAIT_MINISTEREPILOTE,
TM_TRAIT.DATEPJL AS TRAIT_DATEPJL,
TM_TRAIT.DISPOETUDEIMPACT AS TRAIT_DISPOETUDEIMPACT,
TM_TRAIT.DATEDEPOTRATIFICATION AS TRAIT_DATEDEPOTRATIFICATION,
TM_TRAIT.AUTORISATIONRATIFICATION AS TRAIT_AUTORISATIONRATIFICATION,
TM_LRAT.NUMERONOR AS LRAT_NUMERONOR,
TM_LRAT.ETAPESOLON AS LRAT_ETAPESOLON,
TM_LRAT.DATEPUBLICATION AS LRAT_DATEPUBLICATION,
TM_LRAT.TITREACTE AS LRAT_TITREACTE,
TM_TRAIT.NUMERONOR AS DECRET_NUMERONOR,
TM_TRAIT.ETAPESOLON AS DECRET_ETAPESOLON,
TM_TRAIT.NUMERO AS DECRET_NUMERO,
TM_TRAIT.DATEPUBLICATION AS DECRET_DATEPUBLICATION,
TM_TRAIT.TITREACTE AS DECRET_TITREACTE,
TM_TRAIT.OBSERVATION AS DECRET_OBSERVATION
FROM 
 TEXTE_MAITRE TM_TRAIT 
 INNER JOIN      ACTIVITE_NORMATIVE AN ON TM_TRAIT.ID = AN.ID AND AN.TRAITE = 1 
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRAT  ON TM_TRAIT.NORLOIRATIFICATION = TM_LRAT.NUMERONOR;
 
CREATE OR REPLACE VIEW V_AN_TRAITE_ACCORD  AS
SELECT
TM_TRAIT.ID AS TRAIT_ID,
TM_TRAIT.CATEGORIE AS TRAIT_CATEGORIE,
TM_TRAIT.ORGANISATION AS TRAIT_ORGANISATION,
TM_TRAIT.THEMATIQUE AS TRAIT_THEMATIQUE,
TM_TRAIT.DEGREPRIORITE AS TRAIT_DEGREPRIORITE,
TM_TRAIT.TITREACTE AS TRAIT_TITREACTE,
TM_TRAIT.DATESIGNATURE AS TRAIT_DATESIGNATURE,
TM_TRAIT.DATEENTREEENVIGUEUR AS TRAIT_DATEENTREEENVIGUEUR,
TM_TRAIT.MINISTEREPILOTE AS TRAIT_MINISTEREPILOTE,
TM_TRAIT.DATEPJL AS TRAIT_DATEPJL,
TM_TRAIT.DISPOETUDEIMPACT AS TRAIT_DISPOETUDEIMPACT,
TM_TRAIT.ETUDEIMPACT AS TRAIT_ETUDEIMPACT,
TM_TRAIT.DATEDEPOTRATIFICATION AS TRAIT_DATEDEPOTRATIFICATION,
TM_TRAIT.AUTORISATIONRATIFICATION AS TRAIT_AUTORISATIONRATIFICATION,
TM_LRAT.ID AS LRAT_ID,
TM_LRAT.NUMERONOR AS LRAT_NUMERONOR,
TM_LRAT.ETAPESOLON AS LRAT_ETAPESOLON,
TM_LRAT.DATEPUBLICATION AS LRAT_DATEPUBLICATION,
TM_LRAT.TITREACTE AS LRAT_TITREACTE,
TM_TRAIT.NUMERONOR AS DECRET_NUMERONOR,
TM_TRAIT.ETAPESOLON AS DECRET_ETAPESOLON,
TM_TRAIT.NUMERO AS DECRET_NUMERO,
TM_TRAIT.DATEPUBLICATION AS DECRET_DATEPUBLICATION,
TM_TRAIT.TITREACTE AS DECRET_TITREACTE,
TM_TRAIT.OBSERVATION AS DECRET_OBSERVATION
FROM 
 TEXTE_MAITRE TM_TRAIT 
 INNER JOIN      ACTIVITE_NORMATIVE AN ON TM_TRAIT.ID = AN.ID AND AN.TRAITE = 1 
 LEFT OUTER JOIN TEXTE_MAITRE TM_LRAT  ON TM_TRAIT.NORLOIRATIFICATION = TM_LRAT.NUMERONOR;