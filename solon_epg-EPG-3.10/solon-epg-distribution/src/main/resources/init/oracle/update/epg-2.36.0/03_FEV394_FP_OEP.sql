ALTER TABLE fiche_presentation_oep
ADD(  
  "IDSANATLIES" NVARCHAR2(2000)
  );
  
UPDATE fiche_presentation_oep set "IDSANATLIES" = "IDDOSSIER";
  
commit;

