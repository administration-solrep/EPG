-- Script pour ajouter une nouvelle colonne dans la table MIGRATION_LOGGER

ALTER TABLE MIGRATION_LOGGER
ADD
(
   STATUS VARCHAR(20)
);
COMMIT;