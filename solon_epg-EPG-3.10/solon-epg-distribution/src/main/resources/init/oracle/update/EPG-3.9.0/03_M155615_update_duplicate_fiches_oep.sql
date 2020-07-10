-- Update des fiches oep en doublons : la donnée IdsAnatLiés est vidée pour toutes les fiches ayant le même IdDossier sauf la plus récente

UPDATE fiche_presentation_oep
SET idsanatlies = NULL
WHERE "ID" IN (SELECT "ID" FROM FICHE_PRESENTATION_OEP F1 WHERE F1.IDDOSSIER IN (SELECT IDDOSSIER FROM FICHE_PRESENTATION_OEP GROUP BY IDDOSSIER, IDSANATLIES HAVING count(*) > 1) AND F1."DATE" < (SELECT MAX("DATE") FROM FICHE_PRESENTATION_OEP F2 WHERE F2.IDDOSSIER = F1.IDDOSSIER));

COMMIT;