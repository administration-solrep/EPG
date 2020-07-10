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