-- FEV576 - Ajout de colonne statut pour migration detail

ALTER TABLE MIGRATION_DETAIL ADD STATUT NVARCHAR2(2000) DEFAULT 'OK';

COMMIT;