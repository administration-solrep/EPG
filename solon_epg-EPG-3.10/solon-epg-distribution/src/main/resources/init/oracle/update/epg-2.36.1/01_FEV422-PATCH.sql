CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_DILA_JO_BO" ("IS_TP","ID", "NUMERONOR", "TITREACTE", "DELAIPUBLICATION", "MINISTERERESP", "MODEPARUTION", "MODEPARUTIONLABEL", "POURFOURNITUREEPREUVE", 
"DATEPRECISEPUB","DATETRANSMISSIONPUBLI",  "DELAIPUBLICATIONLABEL", "VECTEURPUBLICATION", "TYPEACTE","NUMEROISA","PUBLICATIONSCONJOINTES")
AS
SELECT        '0'                                                   AS IS_TP, 
              DOS.ID                                                AS ID, 
              DOS.NUMERONOR                                         AS NUMERONOR, 
              DOS.TITREACTE                                         AS TITREACTE, 
              DOS.DELAIPUBLICATION                                  AS DELAIPUBLICATION, 
              DOS.MINISTERERESP                                     AS MINISTERERESP, 
              RD.MODEPARUTION                                       AS MODEPARUTION, 
		NVL(MP."MODE",' ')                                    AS MODEPARUTIONLABEL,
    TO_CHAR (DOS.POURFOURNITUREEPREUVE, 'DD/MM/YYYY') AS POURFOURNITUREEPREUVE,
              TO_CHAR (DOS.DATEPRECISEEPUBLICATION, 'DD/MM/YYYY')   AS DATEPRECISEPUB,
              TO_CHAR (ROU.DATEDEBUTETAPE, 'DD/MM/YYYY') AS DATETRANSMISSIONPUBLI,
              NVL(VPD."label",' ')                                  AS DELAIPUBLICATIONLABEL,
              (
                CASE
                  WHEN vp.vpintitule IS NOT NULL
                  THEN vp.vpintitule
                  ELSE VEC.ITEM
                END)                                                AS VECTEURPUBLICATION,
              ACTE."label"                                          AS TYPEACTE, 
              CET.NUMEROISA                                         AS NUMEROISA, 
              (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) 
                                                                    AS PUBLICATIONSCONJOINTES
FROM DOSSIER_SOLON_EPG DOS 
  INNER JOIN          RETOUR_DILA RD                ON  RD.ID = DOS.ID
  INNER JOIN          DOS_VECTEURPUBLICATION VEC    ON  VEC.ID = DOS.ID
  INNER JOIN          VOC_ACTE_TYPE ACTE            ON  DOS.TYPEACTE = ACTE."id"
  INNER JOIN          VECTEUR_PUBLICATION VP        ON  VEC.ITEM = VP.ID
  INNER JOIN          HIERARCHY H                   ON  DOS.LASTDOCUMENTROUTE = H.PARENTID
  INNER JOIN           ROUTING_TASK ROU             ON  H.ID = ROU.ID
  LEFT OUTER JOIN     VOC_PUBLICATION_DELAI VPD     ON  DOS.DELAIPUBLICATION = VPD."id"
  LEFT OUTER JOIN     CONSEIL_ETAT CET              ON  DOS.ID= CET.ID
  LEFT OUTER JOIN     MODE_PARUTION MP              ON  RD.MODEPARUTION =  MP.id                 
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND   ROU.TYPE IN (13,14) 
UNION
SELECT 		'1' 							AS IS_TP, 
		DOS.ID 							AS ID, 
		DOS.NUMERONOR 						AS NUMERONOR, 
		DOS.TITREACTE 						AS TITREACTE, 
		TP.PUBLICATIONDELAI 					AS DELAIPUBLICATION, 
		DOS.MINISTERERESP 					AS MINISTERERESP,
  		TP.PUBLICATIONMODEPUBLICATION 				AS MODEPARUTION,
  		NVL(MP."MODE",' ') 					AS MODEPARUTIONLABEL, 
TO_CHAR (DOS.POURFOURNITUREEPREUVE, 'DD/MM/YYYY') AS POURFOURNITUREEPREUVE,
		TO_CHAR (TP.PUBLICATIONDATEDEMANDE, 'DD/MM/YYYY')  	AS DATEPRECISEPUB,
		TO_CHAR (TP.PUBLICATIONDATEENVOIJO, 'DD/MM/YYYY') AS DATETRANSMISSIONPUBLI,
  		NVL(VPD."label",' ') 					AS DELAIPUBLICATIONLABEL,
  		(
    		CASE
      			WHEN vp.vpintitule IS NOT NULL
      			THEN vp.vpintitule
      			ELSE VEC.ITEM
    		END) 							AS VECTEURPUBLICATION, 
		ACTE."label" 						AS TYPEACTE, 
		CET.NUMEROISA 						AS NUMEROISA,
(select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) 
                                                                    AS PUBLICATIONSCONJOINTES
FROM                DOSSIER_SOLON_EPG DOS 
  INNER JOIN        TRAITEMENT_PAPIER TP        ON DOS.ID = TP.ID 
  INNER JOIN        VOC_ACTE_TYPE ACTE          ON dos.typeacte=acte."id"
  INNER JOIN        DOS_VECTEURPUBLICATION VEC  ON DOS.ID = VEC.ID
  INNER JOIN        VECTEUR_PUBLICATION vp      ON VEC.ITEM = VP.ID
  LEFT OUTER JOIN   CONSEIL_ETAT CET            ON DOS.ID= CET.ID
  LEFT OUTER JOIN   MODE_PARUTION MP            ON TP.PUBLICATIONMODEPUBLICATION = MP."ID"
  LEFT OUTER JOIN   VOC_PUBLICATION_DELAI VPD   ON TP.PUBLICATIONDELAI = VPD."id"
WHERE DOS.STATUTARCHIVAGE IN ('1', '2') ;

commit;

