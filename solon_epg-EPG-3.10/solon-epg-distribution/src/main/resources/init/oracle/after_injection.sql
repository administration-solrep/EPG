-- Reporte l'identifiant de feuille de route dans les dossiers --
-- A passer après une injection --

create or replace
PROCEDURE generateLastDocumentRouteId AS
 pragma autonomous_transaction;
 idfdr VARCHAR2(2000);
 BEGIN
  DBMS_OUTPUT.PUT_LINE('start');
	FOR d in (select d.id as id, d.NUMERONOR as NUMERONOR from DOSSIER_SOLON_EPG d  ) LOOP
    BEGIN
      SELECT s_H_0."ID" as fid INTO idfdr FROM HIERARCHY s_H_0  WHERE s_H_0."PRIMARYTYPE" =  'FeuilleRoute' AND s_H_0."NAME" = 'FDR_INJECTE_'||d.NUMERONOR;
      UPDATE DOSSIER_SOLON_EPG SET LASTDOCUMENTROUTE = idfdr WHERE id = d.id;
    EXCEPTION WHEN NO_DATA_FOUND THEN
      /* on change rien */
       NULL;
    END;
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('end');
  commit;
END;
/

BEGIN
  generateLastDocumentRouteId();
END;
/

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


CREATE INDEX "FULL_02E1B52F_IDX" ON "FULLTEXT" ("FULLTEXT_TITREACTE") 
   INDEXTYPE IS "CTXSYS"."CONTEXT"  PARAMETERS ('LEXER EPG_LEXER WORDLIST EPG_WORDLIST SYNC (ON COMMIT) TRANSACTIONAL');

