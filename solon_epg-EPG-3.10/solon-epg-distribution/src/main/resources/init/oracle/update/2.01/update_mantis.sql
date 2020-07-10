--------------------------------------------------------
--  update vocabulaire vecteur publication tp
--------------------------------------------------------

UPDATE VOC_VECTEUR_PUBLICATION_TP
SET VOC_VECTEUR_PUBLICATION_TP."label" = 'Journal officiel'
WHERE VOC_VECTEUR_PUBLICATION_TP."id" = '1';

--------------------------------------------------------
-- ajout champ recherche simple pour l'indexation afin de faire des requete simple de type LIKE ('%xxx%')
--------------------------------------------------------
ALTER TABLE REQUETE_DOSSIER_SIMPL_A3A85FF2  ADD INDEXATIONRUBRIQUEMODIFIED NVARCHAR2(2000);
ALTER TABLE REQUETE_DOSSIER_SIMPL_A3A85FF2  ADD INDEXATIONMOTSCLEFSMODIFIED NVARCHAR2(2000);
ALTER TABLE REQUETE_DOSSIER_SIMPL_A3A85FF2  ADD INDEXATIONCHAMPLIBREMODIFIED NVARCHAR2(2000);

--------------------------------------------------------
-- correction label parametre --
--------------------------------------------------------
UPDATE DUBLINCORE SET TITLE = 'Format - Références ordonnances' WHERE TITLE = 'Format - Références ordonnaces';

commit;
