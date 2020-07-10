-- UPDATE de la donnée ARCHIVE qui doit être à 0 pour les dossiers non archivés
-- Dans le cas d'un dossier en erreur d'archivage cette donnée était assigné à 1 à tord

UPDATE DOSSIER_SOLON_EPG SET ARCHIVE=0 WHERE STATUTARCHIVAGE NOT IN (6,7,8);
UPDATE DOSSIER_SOLON_EPG SET ARCHIVE=1 WHERE STATUTARCHIVAGE IN (6,7,8);
COMMIT;