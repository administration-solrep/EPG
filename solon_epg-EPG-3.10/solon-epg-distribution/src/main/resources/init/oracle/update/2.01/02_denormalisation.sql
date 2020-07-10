-- A passer apres 01_drop_index.sql
-- denormalisation de created

ALTER TABLE DOSSIER_SOLON_EPG ADD CREATED TIMESTAMP(6);
ALTER TABLE ACTIONNABLE_CASE_LINK ADD CREATED TIMESTAMP(6);
commit;

update DOSSIER_SOLON_EPG d set d.created = (select du.created from dublincore du where du.id = d.id);
update ACTIONNABLE_CASE_LINK a set a.created = (select du.created from dublincore du where du.id = a.id);
commit;


-- denormalisation de caseDocumentId
ALTER TABLE ACTIONNABLE_CASE_LINK ADD CASEDOCUMENTID VARCHAR2(50 BYTE);
commit;

update ACTIONNABLE_CASE_LINK a set a.CASEDOCUMENTID = (select c.CASEDOCUMENTID from CASE_LINK c where c.id = a.id);
commit;