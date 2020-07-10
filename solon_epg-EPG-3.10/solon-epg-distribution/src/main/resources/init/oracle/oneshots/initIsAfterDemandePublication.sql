-- Met à jour le champ isAfterDemandePublication pour les étapes en cours et de type publication JO

UPDATE
  DOSSIER_SOLON_EPG update_dossier
  SET update_dossier.ISAFTERDEMANDEPUBLICATION = 1
WHERE
  EXISTS (
SELECT 1 FROM DOSSIER_SOLON_EPG D
INNER JOIN ROUTING_TASK R ON R.DOCUMENTROUTEID = D.LASTDOCUMENTROUTE
INNER JOIN MISC M ON M.ID = R.ID
WHERE 
M.LIFECYCLESTATE = 'running'  AND R.TYPE IN ( 12,13,14) AND update_dossier.ID = D.ID 
);

COMMIT;
/
