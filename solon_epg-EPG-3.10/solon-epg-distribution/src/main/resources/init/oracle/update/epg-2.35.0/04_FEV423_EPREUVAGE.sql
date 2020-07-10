-- Suppression du dossier "epreuves" dans les modeles de fond de dossier - FEV 423
DELETE FROM ( SELECT * FROM hierarchy h2 INNER JOIN hierarchy h1 
ON h1.id=h2.parentid 
WHERE h2.name LIKE 'Epreuves' 
AND h1.parentid IN (SELECT ID FROM MODELE_FOND_DOSSIER_SOLON_EPG));

commit;
