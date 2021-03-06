CREATE TABLE "FPDR_POLE"
(
    "ID"  VARCHAR2(36 BYTE),
    "POS" NUMBER(10,0),
    "ITEM" NVARCHAR2(2000),
    CONSTRAINT "FPDR_CM_ID_HIERARCHY_FK" FOREIGN KEY ("ID")
    	REFERENCES "HIERARCHY" ("ID") ON DELETE CASCADE ENABLE
);

commit;