-- Création des index fulltext --

DROP INDEX "FULLTEXT_FULLTEXT_IDX";
DROP INDEX "FULL_02E1B52F_IDX";

-- Création d'un index fulltext --
CREATE INDEX "FULLTEXT_FULLTEXT_IDX" ON "FULLTEXT" ("FULLTEXT") INDEXTYPE IS "CTXSYS"."CONTEXT"  PARAMETERS (' SYNC (ON COMMIT) TRANSACTIONAL');


-- require 
-- GRANT EXECUTE ON CTXSYS.CTX_DDL TO "SOLONEPG_INTE";

-- Configuration de du lexer ORACLE utilisé pour l'analyseur fulltext
CREATE PROCEDURE "INIT_INDEX"
IS
BEGIN
        CTXSYS.CTX_DDL.CREATE_PREFERENCE ('EPG_LEXER', 'BASIC_LEXER');
        CTXSYS.CTX_DDL.SET_ATTRIBUTE ('EPG_LEXER', 'base_letter', 'YES');
        CTXSYS.CTX_DDL.SET_ATTRIBUTE ('EPG_LEXER', 'index_stems', 'FRENCH');
        CTXSYS.CTX_DDL.CREATE_PREFERENCE('EPG_WORDLIST', 'BASIC_WORDLIST');
        CTXSYS.CTX_DDL.SET_ATTRIBUTE('EPG_WORDLIST', 'stemmer', 'FRENCH');
        CTXSYS.CTX_DDL.SET_ATTRIBUTE('EPG_WORDLIST', 'substring_index', 'NO');
        CTXSYS.CTX_DDL.SET_ATTRIBUTE('EPG_WORDLIST', 'prefix_index', 'NO');
END;
/

CALL INIT_INDEX();


CREATE INDEX "FULL_02E1B52F_IDX" ON "FULLTEXT" ("FULLTEXT_TITREACTE") INDEXTYPE IS "CTXSYS"."CONTEXT"  PARAMETERS ('LEXER EPG_LEXER WORDLIST EPG_WORDLIST SYNC (ON COMMIT) TRANSACTIONAL');