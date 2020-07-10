--------------------------------------------------------
--  DDL for Table NOTIFICATION_DOC
--------------------------------------------------------

CREATE TABLE "NOTIFICATION_DOC"
  (
    "ID" VARCHAR2(36 BYTE),
    "EVENEMENTID" NVARCHAR2(2000),
    "DATEARRIVE" TIMESTAMP (6)
  ) ;
CREATE TABLE "NT_MESSAGECORBEILLELIST"
  (
    "ID"  VARCHAR2(36 BYTE),
    "POS" NUMBER(10,0),
    "ITEM" NVARCHAR2(2000)
  );
commit;