/* Nettoyage des dossiers dans les textes signalés */

UPDATE DOSSIER_SOLON_EPG SET DATEVERSEMENTTS = NULL;

/* Ajout de la colonne type_ts de type xs:string */
ALTER TABLE TEXTE_SIGNALE ADD TYPE_TS VARCHAR2(1);
