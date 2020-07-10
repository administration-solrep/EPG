-- Réindexation des dossiers dont la catégorie d'acte est 3=convention collective

-- Update des dossierindexationquery existants
update DOSSIERINDEXATIONQUERY set INDEXATIONMASSIVEDATE = current_date where dossierid in (select id from dossier_solon_epg where categorieacte=3);
update DOSSIERINDEXATIONSTATUS set LASTINDEXATIONDATE = null where dossierid in (select id from dossier_solon_epg where categorieacte=3);

-- Ajout des dossierindexationquery manquants
insert into DOSSIERINDEXATIONQUERY(DOSSIERID, EVENTDATE, INDEXATIONMASSIVEDATE)
select id, current_date, current_date from dossier_solon_epg where categorieacte=3 and id not in (select dossierid from dossierindexationquery);
insert into DOSSIERINDEXATIONSTATUS(DOSSIERID, STATUS, LASTINDEXATIONDATE)
select id, 'ERROR', current_date from dossier_solon_epg where categorieacte=3 and id not in (select dossierid from dossierindexationstatus);

commit;