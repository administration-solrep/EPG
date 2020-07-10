ALTER TABLE DOSSIER_SOLON_EPG ADD NORATTRIBUE NUMBER(1) DEFAULT 0;
update DOSSIER_SOLON_EPG d set d.NORATTRIBUE =  1 where d.statut ='5';

ALTER TABLE DOSSIER_SOLON_EPG ADD ARCHIVE NUMBER(1) DEFAULT 0;
update DOSSIER_SOLON_EPG d set d.ARCHIVE =  1 where d.STATUTARCHIVAGE not in ('1', '2');

ALTER TABLE DOSSIER_SOLON_EPG ADD PUBLIE NUMBER(1) DEFAULT 0;
update DOSSIER_SOLON_EPG d set d.PUBLIE =  1 where d.statut ='6';

ALTER TABLE ACTIONNABLE_CASE_LINK ADD ARCHIVE NUMBER(1) DEFAULT 0;
update ACTIONNABLE_CASE_LINK d set d.ARCHIVE =  1 where d.STATUTARCHIVAGE not in ('1', '2');

commit;

DROP INDEX IDX_DOSSIER_EPG_1;
DROP INDEX IDX_DOSSIER_EPG_CREATED_INV;
DROP INDEX DOSSIER_ARCH_ID;
DROP INDEX DOSSIER_STAT_ID;

CREATE INDEX "DOSSIER_ARCH_ID" ON "DOSSIER_SOLON_EPG" ("STATUTARCHIVAGE");
CREATE INDEX "DOSSIER_STAT_ID" ON "DOSSIER_SOLON_EPG" ("STATUT");
CREATE INDEX "DOSSIER_SOLON_EPG_NOR" ON "DOSSIER_SOLON_EPG" ("NUMERONOR");
CREATE UNIQUE INDEX "IDX_DOSSIER_ARCHIV_BOOL" ON "DOSSIER_SOLON_EPG" ("ID", "ARCHIVE");
CREATE UNIQUE INDEX "IDX_DOSSIER_PUBLIE_BOOL" ON "DOSSIER_SOLON_EPG" ("PUBLIE", "ID");

CREATE UNIQUE INDEX "IDX_DOSSIER_NORATTRIB_BOOL" ON "DOSSIER_SOLON_EPG" ("ID", "NORATTRIBUE");

CREATE UNIQUE INDEX "IDX_ACTLINK_ARCHIV_BOOL" ON "ACTIONNABLE_CASE_LINK" ("CASELIFECYCLESTATE", "ARCHIVE", "ID");
CREATE UNIQUE INDEX "IDX_ACTLINK_ARCHIV_BOOL_INV" ON "ACTIONNABLE_CASE_LINK" ("ID", "CASELIFECYCLESTATE", "ARCHIVE");

DROP INDEX "IDX_ACTCLINK_CASE";
DROP INDEX "IDX_ACTCLINK_CASE_INV";


CREATE UNIQUE INDEX "NXP_LOGS_DATE" ON "NXP_LOGS" ("LOG_EVENT_DATE", "LOG_ID");
CREATE UNIQUE INDEX "NXP_LOGS_ID_DATE" ON "NXP_LOGS" ("LOG_ID", "LOG_EVENT_DATE");

commit;
