-- Script à partir de la 2.17.0 
-- Les données du script du 2.17.1 ont été reportées dans celui-ci

DROP TABLE "CLUSTER_NODES";
DROP TABLE "CLUSTER_INVALS";

CREATE TABLE "CLUSTER_NODES" ("NODEID" VARCHAR(25), "CREATED" TIMESTAMP);
CREATE TABLE "CLUSTER_INVALS" ("NODEID" VARCHAR(25), "ID" VARCHAR2(36), "FRAGMENTS" VARCHAR2(4000), "KIND" NUMBER(3,0));
CREATE INDEX "CLUSTER_INVALS_NODEID_IDX" ON "CLUSTER_INVALS" ("NODEID");

-- SUPNXP-6451 --
CREATE OR REPLACE FUNCTION NX_NODEID
RETURN VARCHAR
IS
  cursid NUMBER := SYS_CONTEXT('USERENV', 'SID');
  curser NUMBER;
BEGIN
  SELECT SERIAL# INTO curser FROM GV$SESSION WHERE SID = cursid AND INST_ID = SYS_CONTEXT('USERENV', 'INSTANCE');
  RETURN cursid || ',' || curser;
END;
/

CREATE OR REPLACE PROCEDURE "NX_CLUSTER_INVAL" (i VARCHAR2, f VARCHAR2, k INTEGER)
IS
  nid VARCHAR(4000) := NX_NODEID();
BEGIN
  FOR c IN (SELECT nodeid FROM cluster_nodes WHERE nodeid <> nid) LOOP
    INSERT INTO cluster_invals (nodeid, id, fragments, kind) VALUES (c.nodeid, i, f, k);
  END LOOP;
END;
/

  CREATE OR REPLACE
FUNCTION NX_ANCESTORS(ids NX_STRING_TABLE)
RETURN NX_STRING_TABLE PIPELINED
IS
  id hierarchy.id%TYPE;
  curid hierarchy.id%TYPE;
BEGIN
  FOR i IN ids.FIRST .. ids.LAST LOOP
    curid := ids(i);
    LOOP
      SELECT parentid INTO curid FROM hierarchy WHERE hierarchy.id = curid;
      EXIT WHEN curid IS NULL;
      PIPE ROW(curid);
    END LOOP;
  END LOOP;
END;
/