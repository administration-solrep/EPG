-- A passer après une injection de feuille de route --

--== RECUPERATION REPERTOIRE CONTENANT LES MODELES DE FDR ==--
--== select * from HIERARCHY where PRIMARYTYPE= 'FeuilleRouteModelFolder'; ==--

--== RECUPERATION DES MODELES DE FDR ==--
--== select * from HIERARCHY where PRIMARYTYPE= 'FeuilleRoute' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRouteModelFolder'); ==--

--== RECUPERATION DES STATUTS DES MODELES DE FDR ==--
--== select * from MISC where id IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRoute' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRouteModelFolder'));  ==--

--== RECUPERATION DES ETAPES DES MODELES DE FDR ==--
--== select * from HIERARCHY where PRIMARYTYPE= 'RouteStep' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRoute' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRouteModelFolder')); ==--

--== RECUPERATION DES STATUTS DES ETAPES DES MODELES DE FDR ==--
--== select * from MISC where id IN (select id from HIERARCHY where PRIMARYTYPE= 'RouteStep' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRoute' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRouteModelFolder'))); ==--

--== UPDATE STATUTS ETAPES MODELE FDR ==--
UPDATE MISC SET LIFECYCLESTATE = 'validated' WHERE LIFECYCLESTATE ='draft' and ID IN (select id from HIERARCHY where PRIMARYTYPE= 'RouteStep' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRoute' and parentid IN (select id from HIERARCHY where PRIMARYTYPE= 'FeuilleRouteModelFolder')));

--== NUMERO VERSION ETAPE FEUILLE ROUTE ==--
UPDATE ROUTING_TASK SET numeroversion= NULL WHERE numeroversion = 0;

--== ECHEANCE ETAPE FEUILLE ROUTE ==--
UPDATE ROUTING_TASK SET deadline= NULL WHERE deadline = 0;

--== note : DC TITLE vide lors de l'import : pas de mise � jour car non utilise ==--
--== note : rtsk:actionnable false lors de l'import au lieu de null : pas de mise � jour car non utilise ==--

COMMIT;

--== On effecture la dénormalisation du documentrouteId sur les étapes ==--
create or replace
PROCEDURE generateDocumentRouteId AS
 pragma autonomous_transaction;
  idfdr VARCHAR2(2000);
 BEGIN
  DBMS_OUTPUT.PUT_LINE('start');
	FOR fr in (select fr.id as id from feuille_route fr  ) LOOP
		FOR s_H_0 in (SELECT s_H_0."ID" as id  FROM HIERARCHY s_H_0  WHERE s_H_0."PRIMARYTYPE" IN ( 'DocumentRouteStep', 'RouteStep', 'GenericDistributionTask', 'DistributionStep', 'DistributionTask', 'PersonalDistributionTask') START WITH s_H_0."ID" IN (SELECT f_H_0."ID" FROM HIERARCHY f_H_0 WHERE f_H_0."PRIMARYTYPE" IN ( 'FeuilleRoute', 'DocumentRoute' ) AND f_H_0."ID" = fr.id) CONNECT BY PRIOR s_H_0."ID" = s_H_0."PARENTID") LOOP    
			UPDATE ROUTING_TASK SET documentrouteid = fr.id WHERE id = s_H_0.id;
		END LOOP;
	END LOOP;
  DBMS_OUTPUT.PUT_LINE('end');
  commit;
END;
/

set serveroutput on
BEGIN
  GENERATEDOCUMENTROUTEID();
END;
/


