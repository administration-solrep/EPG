ALTER TABLE FICHE_LOI ADD "REFUSENGAGEMENTPROCASS1" NVARCHAR2(2000);
ALTER TABLE FICHE_LOI ADD "DATEREFUSENGPROCASS1" TIMESTAMP(6);
ALTER TABLE FICHE_LOI ADD "REFUSENGAGEMENTPROCASS2" NVARCHAR2(2000);
ALTER TABLE FICHE_LOI ADD "DECISIONENGAGEMENTASSEMBLEE2" NVARCHAR2(2000);
COMMIT;

  CREATE TABLE "VOC_DEC_PROC_ACC" 
   (	
    "id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
   ) ;

COMMIT;

INSERT INTO VOC_DEC_PROC_ACC ("id","obsolete","ordering","label") values ('ACCEPTATION',0,10000000,'Acceptation');
INSERT INTO VOC_DEC_PROC_ACC ("id","obsolete","ordering","label") values ('REFUS',0,10000000,'Refus');
commit;
