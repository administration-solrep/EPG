delete from VOC_LEGISLATURE;
insert into VOC_LEGISLATURE ("id","obsolete","dateFin","ordering","label","dateDebut") values ('14',0,TO_TIMESTAMP('20/06/2012', 'dd/MM/yyyy'),10000000,'14ème législature',TO_TIMESTAMP('20/06/2012', 'dd/MM/yyyy'));
insert into VOC_LEGISLATURE ("id","obsolete","dateFin","ordering","label","dateDebut") values ('13',0,TO_TIMESTAMP('20/06/2012', 'dd/MM/yyyy'),10000000,'13ème législature',TO_TIMESTAMP('20/06/2012', 'dd/MM/yyyy'));
insert into VOC_LEGISLATURE ("id","obsolete","dateFin","ordering","label","dateDebut") values ('12',0,TO_TIMESTAMP('20/06/2002', 'dd/MM/yyyy'),10000000,'12ème législature',TO_TIMESTAMP('20/06/2007', 'dd/MM/yyyy'));
commit;


CREATE TABLE "VOC_SESSION" 
 (	"id" NVARCHAR2(2000), 
	"obsolete" NUMBER(19,0) DEFAULT 0, 
	"ordering" NUMBER(19,0) DEFAULT 10000000, 
	"label" NVARCHAR2(2000)
 );

delete from voc_activite_parlementaire;
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('DLDA',0,10000000,'Date Limite de Dépôt des Amendements (DLDA)');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('VS',0,10000000,'Vote Solennel (VS)');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('CMP',0,10000000,'Commission mixte paritaire (CMP)');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('COM',0,10000000,'Commission');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('COM Amdts',0,10000000,'Examen des amendements (COM Amdts)');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('1',0,10000000,'Première lecture');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('2',0,10000000,'Seconde lecture');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('lec CMP',0,10000000,'Lectures CMP (lec CMP)');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('Nelle lec',0,10000000,'Nouvelles lectures (Nelle lec)');
insert into voc_activite_parlementaire ("id", "obsolete", "ordering", "label") values ('lec def',0,10000000,'Lectures définitives (lec def)');
commit;