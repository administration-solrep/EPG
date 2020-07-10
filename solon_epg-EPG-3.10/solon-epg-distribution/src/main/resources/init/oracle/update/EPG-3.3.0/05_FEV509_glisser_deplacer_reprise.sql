UPDATE HIERARCHY
SET PRIMARYTYPE = 'FileSolonEpg'
WHERE PRIMARYTYPE IN ('ParapheurFichier', 'FondDeDossierFichier');
commit;