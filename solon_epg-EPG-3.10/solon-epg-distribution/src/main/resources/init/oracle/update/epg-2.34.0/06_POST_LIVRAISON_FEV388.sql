INSERT INTO PARAN_LEGISLATURES (ID,ITEM,POS) 
	SELECT ID, 13,0 FROM ACTIVITE_NORMATIVE_PARAMETRAGE;
INSERT INTO PARAN_LEGISLATURES (ID,ITEM,POS) 
	SELECT ID, 14, 1 FROM ACTIVITE_NORMATIVE_PARAMETRAGE;

commit;