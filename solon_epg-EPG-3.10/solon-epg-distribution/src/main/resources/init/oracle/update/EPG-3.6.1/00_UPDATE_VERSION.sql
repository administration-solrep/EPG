CREATE TABLE DATABASE_VERSION
(
	LIBELLE VARCHAR2(2000),
	VERSION_DATE TIMESTAMP(6)
);

INSERT INTO DATABASE_VERSION VALUES ('EPG 3.6a.1', TO_DATE('2018/10/29', 'yyyy/mm/dd'));