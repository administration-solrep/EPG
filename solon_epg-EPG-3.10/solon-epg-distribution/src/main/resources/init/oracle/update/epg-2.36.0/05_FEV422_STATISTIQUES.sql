CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_COM  AS
SELECT DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, DOS.MINISTEREATTACHE, DOS.MINISTERERESP, 
      DES.NOMDESTINATAIRECOMMUNICATION, TO_CHAR(DES.DATEENVOICOMMUNICATION, 'DD/MM/YYYY')DATEENVOICOMMUNICATION,
      acte."label" TYPEACTE, cet.numeroisa
      , (select regexp_replace(listagg(vp.vpintitule,', ') within group (order by vp.vpintitule), '[^[:alnum:] ,]', '') from DOS_VECTEURPUBLICATION VEC, vecteur_publication vp WHERE VEC.ID = DOS.ID AND VEC.ITEM = vp.id and VEC.id=dos.id) VECTEURPUBLICATION
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP, HIERARCHY H, DESTINATAIRECOMMUNICATION DES, voc_acte_type acte, conseil_etat CET
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND dos.typeacte=acte."id"
and DOS.ID= CET.ID (+)
AND TP.TEXTESOUMISAVALIDATION = 1
AND DOS.ID = TP.ID
AND DOS.ID = H.PARENTID
AND   H.ID = DES.ID 
AND   DES.DATERETOURCOMMUNICATION IS NULL 
AND   DES.DATEENVOICOMMUNICATION IS NOT NULL
ORDER BY DES.DATEENVOICOMMUNICATION ASC;


CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_ARRIVE_SGG  AS
SELECT DISTINCT DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, TP.DATEARRIVEPAPIER,
      acte."label" TYPEACTE, cet.numeroisa
      , (select regexp_replace(listagg(vp.vpintitule,', ') within group (order by vp.vpintitule), '[^[:alnum:] ,]', '') from DOS_VECTEURPUBLICATION VEC, vecteur_publication vp WHERE VEC.ID = DOS.ID AND VEC.ITEM = vp.id and VEC.id=dos.id) VECTEURPUBLICATION
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP, voc_acte_type acte, conseil_etat CET
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
 AND DOS.ID = TP.ID
AND dos.typeacte=acte."id"
and DOS.ID= CET.ID (+);


CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_SIG_PM_PR  AS
SELECT DISTINCT DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, NVL(V."label", '') TYPEACTELABEL,
    MAX(SIG.DATEENVOISIGNATURE)AS DATESIGNATURE, LISTE.DATEGENERATIONLISTE,
      cet.numeroisa
      , (select regexp_replace(listagg(vp.vpintitule,', ') within group (order by vp.vpintitule), '[^[:alnum:] ,]', '') from DOS_VECTEURPUBLICATION VEC, vecteur_publication vp WHERE VEC.ID = DOS.ID AND VEC.ITEM = vp.id and VEC.id=dos.id) VECTEURPUBLICATION
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
  FROM DOSSIER_SOLON_EPG DOS, HIERARCHY H, DONNEESSIGNATAIRE SIG, VOC_ACTE_TYPE V,
    LIS_IDSDOSSIER LISDOS, LISTE_TRAITEMENT_PAPI_144E75F6 LISTE, conseil_etat CET
  WHERE DOS.STATUTARCHIVAGE   IN ('1', '2')
  AND DOS.TYPEACTE             = V."id" (+)
  AND H.NAME                  IN ('tp:signaturePm', 'tp:signaturePr')
  AND H.PARENTID               = DOS.ID
  AND H.ID                     = SIG.ID
  AND SIG.DATEENVOISIGNATURE  IS NOT NULL
  AND SIG.DATERETOURSIGNATURE IS NULL
  AND DOS.ID                   =LISDOS.ITEM (+)
  AND LISDOS.ID                =LISTE.ID (+)
  and DOS.ID                   = CET.ID (+)
  AND (LISTE.TYPELISTE         = '1'
  OR LISTE.TYPELISTE          IS NULL)
  GROUP BY DOS.ID,
    DOS.NUMERONOR,
    DOS.TITREACTE,
    NVL(V."label", ''),
    LISTE.DATEGENERATIONLISTE,
    LISTE.TYPELISTE,
    cet.numeroisa
  ORDER BY LISTE.DATEGENERATIONLISTE;


CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_AMPLIATION  AS
SELECT  DOS.ID, 
		DOS.NUMERONOR, 
		DOS.TITREACTE, 
    acte."label" TYPEACTE, cet.numeroisa,
		LISTAGG (AMP.ITEM, ', ') WITHIN GROUP (ORDER BY AMP.ITEM) ITEM,
    (
      SELECT MAX(I.DATEENVOIAMPLIATION) AS DATEENVOIE FROM INFOHISTORIQUEAMPLIATION I, HIERARCHY H 
      WHERE H.PARENTID = DOS.ID AND H.NAME = 'tp:ampliationHistorique'
      AND I.ID = H.ID GROUP BY H.PARENTID
    )AS DATEENVOIE
      , (select regexp_replace(listagg(vp.vpintitule,', ') within group (order by vp.vpintitule), '[^[:alnum:] ,]', '') from DOS_VECTEURPUBLICATION VEC, vecteur_publication vp WHERE VEC.ID = DOS.ID AND VEC.ITEM = vp.id and VEC.id=dos.id) VECTEURPUBLICATION
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
FROM DOSSIER_SOLON_EPG DOS, TP_AMPLIATIONDESTINATAIREMAILS AMP, voc_acte_type acte, conseil_etat CET
where 
DOS.ID = AMP.ID
AND DOS.ARCHIVE = 0
and dos.typeacte=acte."id"
and DOS.ID= CET.ID (+)
GROUP BY DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, acte."label", cet.numeroisa
order by DATEENVOIE desc;

CREATE OR REPLACE VIEW V_STATS_LISTE_TEXTE_DILA_PUB  AS
SELECT '0' AS IS_TP, DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, DOS.DELAIPUBLICATION, RD.MODEPARUTION, 
  DOS.MINISTERERESP, TO_CHAR (DOS.DATEPRECISEEPUBLICATION, 'DD/MM/YYYY') AS DATEPRECISEPUB,
  NVL(MP."MODE",' ') AS MODEPARUTIONLABEL,
  NVL(VPD."label",' ') AS DELAIPUBLICATIONLABEL,
  ( select acte."label" from voc_acte_type acte where acte."id"=dos.typeacte) TYPEACTE,  
  cet.numeroisa
    , (select regexp_replace(listagg(vp.vpintitule,', ') within group (order by vp.vpintitule), '[^[:alnum:] ,]', '') from DOS_VECTEURPUBLICATION VEC, vecteur_publication vp WHERE VEC.ID = DOS.ID AND VEC.ITEM = vp.id and VEC.id=dos.id) VECTEURPUBLICATION
    , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
FROM DOSSIER_SOLON_EPG DOS, RETOUR_DILA RD, VOC_PUBLICATION_DELAI VPD, MODE_PARUTION MP , conseil_etat CET
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND RD.MODEPARUTION =  MP.id(+)
AND DOS.DELAIPUBLICATION = VPD."id" (+)
AND RD.ID = DOS.ID
and DOS.ID= CET.ID (+)
AND EXISTS ( 	SELECT 1 
				FROM HIERARCHY H, ROUTING_TASK ROU
				WHERE DOS.LASTDOCUMENTROUTE = H.PARENTID
				AND   H.ID = ROU.ID
				AND   TO_CHAR (ROU.DATEDEBUTETAPE, 'DD/MM/YYYY') = TO_CHAR(SYSTIMESTAMP,'DD/MM/YYYY')
        		AND   ROU.DATEFINETAPE IS NULL
				AND   ROU.TYPE IN (13) )
AND 1 IN (SELECT vp.vppos
        FROM DOS_VECTEURPUBLICATION VEC, vecteur_publication vp
        WHERE VEC.ID = DOS.ID
        AND VEC.ITEM = vp.id)  -- 1 = Journal officiel
UNION
SELECT '1' AS IS_TP, DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, TP.PUBLICATIONDELAI DELAIPUBLICATION, DOS.MINISTERERESP,
  TP.PUBLICATIONMODEPUBLICATION MODEPARUTION, TO_CHAR (TP.PUBLICATIONDATEDEMANDE, 'DD/MM/YYYY')  AS DATEPRECISEPUB,
  NVL(MP."MODE",' ') AS MODEPARUTIONLABEL,
  NVL(VPD."label",' ') AS DELAIPUBLICATIONLABEL,
  acte."label" TYPEACTE, cet.numeroisa
      , (select regexp_replace(listagg(vp.vpintitule,', ') within group (order by vp.vpintitule), '[^[:alnum:] ,]', '') from DOS_VECTEURPUBLICATION VEC, vecteur_publication vp WHERE VEC.ID = DOS.ID AND VEC.ITEM = vp.id and VEC.id=dos.id) VECTEURPUBLICATION
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
FROM DOSSIER_SOLON_EPG DOS, TRAITEMENT_PAPIER TP, MODE_PARUTION MP, VOC_PUBLICATION_DELAI VPD, VECTEUR_PUBLICATION VEC , voc_acte_type acte, conseil_etat CET 
WHERE DOS.STATUTARCHIVAGE IN ('1', '2')
AND TO_CHAR (TP.PUBLICATIONDATEENVOIJO, 'DD/MM/YYYY') = TO_CHAR(SYSTIMESTAMP,'DD/MM/YYYY')
AND TP.PUBLICATIONMODEPUBLICATION = MP."ID" (+)
AND TP.PUBLICATIONDELAI = VPD."id" (+)
AND TP.PUBLICATIONNOMPUBLICATION = VEC.ID
and VEC.vppos = 1 -- 1 = Journal Officiel
and dos.typeacte=acte."id"
and DOS.ID= CET.ID (+)
AND DOS.ID = TP.ID;


  CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_DILA_EPR" ("ID", "NUMERONOR", "TITREACTE", "DELAIPUBLICATION", "MODEPARUTION", "POURFOURNITUREEPREUVE", "MINISTERERESP", 
"DATEPRECISEPUB","DATETRANSMISSIONEPREUVE", "MODEPARUTIONLABEL", "DELAIPUBLICATIONLABEL", "VECTEURPUBLICATION", "TYPEACTE","NUMEROISA","PUBLICATIONSCONJOINTES")
AS
  SELECT DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, DOS.DELAIPUBLICATION, RD.MODEPARUTION,
    TO_CHAR (DOS.POURFOURNITUREEPREUVE, 'DD/MM/YYYY') AS POURFOURNITUREEPREUVE,
    DOS.MINISTERERESP,
    TO_CHAR (DOS.DATEPRECISEEPUBLICATION, 'DD/MM/YYYY') AS DATEPRECISEPUB,
    TO_CHAR (ROU.DATEDEBUTETAPE, 'DD/MM/YYYY') AS DATETRANSMISSIONEPREUVE,
    NVL(MP."MODE",' ')                                  AS MODEPARUTIONLABEL,
    NVL(VPD."label",' ')                                AS DELAIPUBLICATIONLABEL,
    (
    CASE
      WHEN vp.vpintitule IS NOT NULL
      THEN vp.vpintitule
      ELSE VEC.ITEM
    END) AS VECTEURPUBLICATION,
    acte."label" TYPEACTE, cet.numeroisa
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
  FROM DOSSIER_SOLON_EPG DOS, DOS_VECTEURPUBLICATION VEC, RETOUR_DILA RD, MODE_PARUTION MP,
    VOC_PUBLICATION_DELAI VPD, VECTEUR_PUBLICATION vp, ROUTING_TASK ROU, hierarchy H, voc_acte_type acte, conseil_etat CET
  WHERE  (DOS.STATUTARCHIVAGE='1' OR DOS.STATUTARCHIVAGE='2')
  AND RD.MODEPARUTION        = MP."ID"
  AND DOS.DELAIPUBLICATION   = VPD."id"
  AND DOS.ID                 = VEC.ID
  AND RD.ID                  = DOS.ID
  AND VEC.ITEM               = VP.ID 
  and dos.typeacte=acte."id"
  and DOS.ID= CET.ID (+)
  AND DOS.LASTDOCUMENTROUTE                    = H.PARENTID
  AND H.ID                                       = ROU.ID
  AND ROU.DATEFINETAPE                          IS NULL
  AND ROU.TYPE=12;

CREATE OR REPLACE VIEW "V_STATS_LISTE_TEXTE_DILA_JO_BO" ("ID", "NUMERONOR", "TITREACTE", "DELAIPUBLICATION", "MODEPARUTION", "POURFOURNITUREEPREUVE", "MINISTERERESP", 
"DATEPRECISEPUB", "MODEPARUTIONLABEL", "DELAIPUBLICATIONLABEL","DATETRANSMISSIONPUBLI", "VECTEURPUBLICATION", "TYPEACTE","NUMEROISA","PUBLICATIONSCONJOINTES")
AS
  SELECT DOS.ID, DOS.NUMERONOR, DOS.TITREACTE, DOS.DELAIPUBLICATION, RD.MODEPARUTION,
    TO_CHAR (DOS.POURFOURNITUREEPREUVE, 'DD/MM/YYYY') AS POURFOURNITUREEPREUVE,
    DOS.MINISTERERESP, TO_CHAR (DOS.DATEPRECISEEPUBLICATION, 'DD/MM/YYYY') AS DATEPRECISEPUB,
    NVL(MP."MODE",' ')                                  AS MODEPARUTIONLABEL,
    NVL(VPD."label",' ')                                AS DELAIPUBLICATIONLABEL,
    TO_CHAR (ROU.DATEDEBUTETAPE, 'DD/MM/YYYY') as DATETRANSMISSIONPUBLI,
    (
    CASE
      WHEN vp.vpintitule IS NOT NULL
      THEN vp.vpintitule
      ELSE VEC.ITEM
    END) AS VECTEURPUBLICATION,
    acte."label" TYPEACTE, cet.numeroisa
      , (select regexp_replace(listagg(PC.item,', ') within group (order by PC.item), '[^[:alnum:] ,]', '') from DOS_PUBLICATIONSCONJOINTES PC where PC.ID=DOS.ID) PUBLICATIONSCONJOINTES
  FROM DOSSIER_SOLON_EPG DOS, DOS_VECTEURPUBLICATION VEC, RETOUR_DILA RD, MODE_PARUTION MP, VOC_PUBLICATION_DELAI VPD,
    VECTEUR_PUBLICATION vp, HIERARCHY H, ROUTING_TASK ROU, voc_acte_type acte, conseil_etat CET
  WHERE (DOS.STATUTARCHIVAGE='1' OR DOS.STATUTARCHIVAGE='2')
  AND RD.MODEPARUTION        = MP."ID"
  AND DOS.DELAIPUBLICATION   = VPD."id"
  AND DOS.ID                 = VEC.ID
  AND RD.ID                  = DOS.ID
  AND VEC.ITEM               = VP.ID 
  and dos.typeacte=acte."id"
  and DOS.ID= CET.ID (+)
  AND DOS.LASTDOCUMENTROUTE                    = H.PARENTID
    AND H.ID                                       = ROU.ID
    AND ROU.DATEFINETAPE                          IS NULL
    AND (ROU.TYPE=13 OR ROU.TYPE=14);

    
commit;
