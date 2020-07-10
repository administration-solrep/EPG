alter table CONSEIL_ETAT ADD PRIORITE NVARCHAR2(50);
Insert into VOC_BORDEREAU_LABEL ("id","obsolete","ordering","label") values ('priorite',0,10000000,'Priorité');
commit;