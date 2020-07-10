-- A passer dans sqlplus ! avant 02_denormalisation.sql

SPOOL drop_index.sql
SELECT 'DROP INDEX ' || index_name ||';' FROM USER_INDEXES WHERE table_name = 'DOSSIER_SOLON_EPG';
SPOOL OFF;
@drop_index;

SPOOL drop_index.sql
SELECT 'DROP INDEX ' || index_name ||';' FROM USER_INDEXES WHERE table_name = 'HIERARCHY';
SPOOL OFF;
@drop_index;

SPOOL drop_index.sql
SELECT 'DROP INDEX ' || index_name ||';' FROM USER_INDEXES WHERE table_name = 'DUBLINCORE';
SPOOL OFF;
@drop_index;

SPOOL drop_index.sql
SELECT 'DROP INDEX ' || index_name ||';' FROM USER_INDEXES WHERE table_name = 'CASE_LINK';
SPOOL OFF;
@drop_index;

SPOOL drop_index.sql
SELECT 'DROP INDEX ' || index_name ||';' FROM USER_INDEXES WHERE table_name = 'ACTIONNABLE_CASE_LINK';
SPOOL OFF;
@drop_index;

SPOOL drop_index.sql
SELECT 'DROP INDEX ' || index_name ||';' FROM USER_INDEXES WHERE table_name = 'MISC';
SPOOL OFF;
@drop_index;

commit;