-- A passer apres 02_denormalisation.sql

-- INDEX ACTIONNABLE_CASE_LINK
CREATE UNIQUE INDEX "IDX_ACT_CL_LIFE_STATUS" ON "ACTIONNABLE_CASE_LINK" ("CASELIFECYCLESTATE", "STATUTARCHIVAGE", "ID");
CREATE UNIQUE INDEX "ACTIONNABLE_CASE_LINK_PK" ON "ACTIONNABLE_CASE_LINK" ("ID");
CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_1" ON "ACTIONNABLE_CASE_LINK" ("STEPDOCUMENTID", "ID");
CREATE INDEX "IDX_ACTIONNABLE_CASE_LINK_2" ON "ACTIONNABLE_CASE_LINK" ("AUTOMATICVALIDATION");
CREATE INDEX "IDX_ACTIONNABLE_CASE_LINK_3" ON "ACTIONNABLE_CASE_LINK" ("DUEDATE");
CREATE UNIQUE INDEX "IDX_ACTCLINK_ARCHIVAGE" ON "ACTIONNABLE_CASE_LINK" ("ID", "STATUTARCHIVAGE");
CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_4" ON "ACTIONNABLE_CASE_LINK" ("ID" ,"CASELIFECYCLESTATE");
CREATE UNIQUE INDEX "IDX_ACTCLINK_CREATED" ON "ACTIONNABLE_CASE_LINK" ("ID", "CREATED");
CREATE UNIQUE INDEX "IDX_ACTCLINK_CASE" ON "ACTIONNABLE_CASE_LINK" ("CASEDOCUMENTID", "ID");
CREATE UNIQUE INDEX "IDX_ACTCLINK_CASE_INV" ON "ACTIONNABLE_CASE_LINK" ("ID", "CASEDOCUMENTID");
CREATE UNIQUE INDEX "IDX_ACTIONNABLE_CASE_LINK_12" ON "ACTIONNABLE_CASE_LINK" ("CASEDOCUMENTID" ,"CASELIFECYCLESTATE", "ID");

-- INDEX CASE_LINK
CREATE UNIQUE INDEX "CASE_LINK_PK" ON "CASE_LINK" ("ID");
CREATE UNIQUE INDEX "IDX_CASE_LINK_1" ON "CASE_LINK" ("ID", "DATE");
CREATE UNIQUE INDEX "IDX_CASE_LINK_2" ON "CASE_LINK" ("CASEDOCUMENTID", "ID");

-- INDEX DOSSIER_SOLON_EPG
CREATE UNIQUE INDEX "DOSSIER_ARCH_ID" ON "DOSSIER_SOLON_EPG" ("STATUTARCHIVAGE", "ID");
CREATE UNIQUE INDEX "DOSSIER_STAT_ID" ON "DOSSIER_SOLON_EPG" ("STATUT", "ID");
CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_1" ON "DOSSIER_SOLON_EPG" ("ID", "STATUTARCHIVAGE");
CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_2" ON "DOSSIER_SOLON_EPG" ("ID", "CATEGORIEACTE");
CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_3" ON "DOSSIER_SOLON_EPG" ("ID", "NUMERONOR");
CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_5" ON "DOSSIER_SOLON_EPG" ("ID", "TYPEACTE");
CREATE INDEX "IDX_DOSSIER_EPG_6" ON "DOSSIER_SOLON_EPG" ("LASTDOCUMENTROUTE");
CREATE UNIQUE INDEX "DOSSIER_SOLON_EPG_PK" ON "DOSSIER_SOLON_EPG" ("ID");
CREATE UNIQUE INDEX "DOSSIER_SOLON_EPG_NOR" ON "DOSSIER_SOLON_EPG" ("NUMERONOR");
CREATE INDEX "IDX_DOSSIER_EPG_CREATOR" ON "DOSSIER_SOLON_EPG" ("POSTECREATOR");
CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_CREATED" ON "DOSSIER_SOLON_EPG" ("ID", "CREATED");
CREATE UNIQUE INDEX "IDX_DOSSIER_EPG_CREATED_INV" ON "DOSSIER_SOLON_EPG" ("CREATED", "ID");

-- INDEX HIERARCHY
CREATE UNIQUE INDEX "IDX_HIER_1" ON "HIERARCHY" ("PRIMARYTYPE", "ID");
CREATE INDEX "IDX_HIER_2" ON "HIERARCHY" ("PARENTID", "ISPROPERTY");
CREATE UNIQUE INDEX "IDX_HIER_4" ON "HIERARCHY" ("ID", "PRIMARYTYPE");
CREATE UNIQUE INDEX "IDX_HIER_5" ON "HIERARCHY" ("ID", "MIXINTYPES");
CREATE UNIQUE INDEX "IDX_HIER_6" ON "HIERARCHY" ("ID", "ISVERSION");
CREATE UNIQUE INDEX "HIERARCHY_PK" ON "HIERARCHY" ("ID");
CREATE INDEX "HIER_B0ED9092_IDX" ON "HIERARCHY" ("PARENTID", "NAME");
CREATE INDEX "HIERARCHY_PARENTID_IDX" ON "HIERARCHY" ("PARENTID", "ID");
CREATE INDEX "HIERARCHY_ISVERSION_IDX" ON "HIERARCHY" ("ISVERSION");

-- INDEX MISC
CREATE UNIQUE INDEX "MISC_PK" ON "MISC" ("ID");
CREATE UNIQUE INDEX "IDX_MISC_1" ON "MISC" ("ID", "LIFECYCLESTATE");
CREATE UNIQUE INDEX "IDX_MISC_2" ON "MISC" ("LIFECYCLESTATE", "ID");

-- INDEX DUBLINCORE
CREATE UNIQUE INDEX "DUBLINCORE_PK" ON "DUBLINCORE" ("ID"); 
CREATE UNIQUE INDEX "IDX_DUBLINCORE_1" ON "DUBLINCORE" ("ID", "TITLE");
CREATE UNIQUE INDEX "IDX_DUBLINCORE_2" ON "DUBLINCORE" ("ID", "CREATOR");
CREATE UNIQUE INDEX "IDX_DUBLINCORE_CREATED" ON "DUBLINCORE" ("ID", "CREATED");

commit;