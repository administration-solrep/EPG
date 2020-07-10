
update VOC_ACTE_TYPE set "isDecret" = '0' where "id" = 1;

insert into VOC_ACTE_TYPE("id", "activiteNormative", "classification", "isDecret" , "obsolete", "ordering" , "label") values ('45', '0', '0', '0', 0, 10000000, 'Rapport au parlement');

ALTER TABLE DOSSIER_SOLON_EPG ADD periodiciteRapport VARCHAR2(2000);

commit;