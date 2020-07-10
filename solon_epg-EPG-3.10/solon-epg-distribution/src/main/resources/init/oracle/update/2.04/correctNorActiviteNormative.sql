CREATE OR REPLACE PROCEDURE correctNorActiviteNormative(oldNor VARCHAR2, newNor VARCHAR2) AS
BEGIN	
	FOR texm in (select texm.id from TEXTE_MAITRE texm where texm.MINISTEREPILOTE = oldNor) LOOP
		UPDATE TEXTE_MAITRE SET MINISTEREPILOTE = newNor WHERE id = texm.id;
		INSERT INTO CLUSTER_INVALS (SELECT NODEID, texm.id, 'texm:ministerePilote', 1 FROM CLUSTER_NODES);
	END LOOP;
END;
/

BEGIN
  correctNorActiviteNormative('50005632', '50013998');
END;
/
BEGIN
  correctNorActiviteNormative('50000633', '50000976');
END;
/
commit;