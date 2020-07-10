-- Les dossiers au status d'archivage 5 passe en statut d'archivage 6
UPDATE
	DOSSIER_SOLON_EPG
SET
	STATUTARCHIVAGE = '6'
WHERE
	STATUTARCHIVAGE = '5';
