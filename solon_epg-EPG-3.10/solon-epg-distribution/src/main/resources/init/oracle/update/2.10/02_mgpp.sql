
alter table FICHE_LOI ADD NUMERODEPOT_OLD VARCHAR2(2000);

update FICHE_LOI SET NUMERODEPOT_OLD = '||NUMERODEPOT||';

alter table FICHE_LOI DROP COLUMN NUMERODEPOT;

alter table FICHE_LOI ADD NUMERODEPOT VARCHAR2(2000);

update FICHE_LOI SET NUMERODEPOT = NUMERODEPOT_OLD;

alter table FICHE_LOI DROP COLUMN NUMERODEPOT_OLD;

alter table FICHE_LOI ADD DATECREATION TIMESTAMP(6);

alter table FICHE_PRESENTATION_OEP ADD DATEFIN TIMESTAMP(6);

commit;