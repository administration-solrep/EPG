alter table FEUILLE_ROUTE add numero_backup VARCHAR2(10 CHAR);

update FEUILLE_ROUTE set numero_backup = numero;

alter table FEUILLE_ROUTE drop column numero;

alter table FEUILLE_ROUTE add numero NUMBER(10);

update FEUILLE_ROUTE set numero = to_number(numero_backup);

commit;
