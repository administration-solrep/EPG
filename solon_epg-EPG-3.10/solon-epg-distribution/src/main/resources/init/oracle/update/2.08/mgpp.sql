ALTER TABLE FICHE_PRESENTATION_OEP ADD baseLegale VARCHAR2(4000 CHAR);
ALTER TABLE FICHE_PRESENTATION_OEP DROP COLUMN texteOrganisme;

commit;