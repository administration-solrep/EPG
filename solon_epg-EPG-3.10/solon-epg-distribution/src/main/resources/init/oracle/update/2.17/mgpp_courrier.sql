--Changer le contenu du courier

delete from VOC_COURRIER;
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('0',0,10000000,'LEX-14 - Procedure accélérée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('1',0,10000000,'LEX-14 - Procedure accélérée lettre rectificative');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('2',0,10000000,'LEX-17 - Première lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('3',0,10000000,'LEX-17 - Première lecture modifiée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('4',0,10000000,'LEX-17 - Première lecture rejetée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('5',0,10000000,'LEX-17 - Première lecture procédure accélerée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('6',0,10000000,'LEX-17 - Première lecture procédure accélerée modifiée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('7',0,10000000,'LEX-17 - Première lecture procédure accélerée rejetée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('8',0,10000000,'LEX-17 - Deuxième lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('9',0,10000000,'LEX-17 - Deuxième lecture modifiée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('10',0,10000000,'LEX-17 - Nouvelle lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('11',0,10000000,'LEX-22 - CMP (45-2)');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('12',0,10000000,'LEX-22 - CMP (45-2) texte commun');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('13',0,10000000,'LEX-26 - CMP (45-3)');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('14',0,10000000,'LEX-27 - Echec CMP');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('15',0,10000000,'LEX-27 - Echec CMP texte commun');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('16',0,10000000,'LEX-27 - Rejet par l''Assemblée nationale');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('17',0,10000000,'LEX-27 - Rejet par le Sénat');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('18',0,10000000,'LEX-27 - Rejet par le Sénat des amendements du Gouvernement');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('19',0,10000000,'LEX-26 - Dernière lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('20',0,10000000,'LEX-26 - Dernière lecture rejet');

insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('21',0,10000000,'Lettre choix multiples - décès');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('22',0,10000000,'Lettre choix multiples - démission');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('23',0,10000000,'Lettre choix multiples - expiration');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('24',0,10000000,'Lettre création OEP');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('25',0,10000000,'Lettres re-création OEP');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('26',0,10000000,'Lettres suite entrée au Gouvernement');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('27',0,10000000,'Lettres suite nomination CCI');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('28',0,10000000,'34-1C : injonction');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('29',0,10000000,'34-1C: mise en cause de la responsabilité');

insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('30',0,10000000,'avenant conventions');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('31',0,10000000,'ouverture session extraordinaire-décret');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('32',0,10000000,'clôture session extraordianaire-décret');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('33',0,10000000,'ouverture session extraordinaire-ampliation');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('34',0,10000000,'clôture session extraordinaire-ampliation');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('35',0,10000000,'ouverture session extraordinaire-lettre');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('36',0,10000000,'clôture session extraordinaire-lettre');

insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('37',0,10000000,'dépôt rapport art 67');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('38',0,10000000,'dépôt rapport');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('39',0,10000000,'dépôt rapport pour information');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('40',0,10000000,'Lettres + décrets');

insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('41',0,10000000,'Lettre intervention extérieure');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('42',0,10000000,'Lettre intervention ext en session extra');

insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('43',0,10000000,'Décret congrès article 18C');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('44',0,10000000,'Lettres + décrets congrès article 18C');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('45',0,10000000,'Décret congrès');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('46',0,10000000,'Ampliation congrès');


ALTER TABLE FICHE_PRESENTATION_DECRET
ADD NOROUVERTURESESSIONEXT NVARCHAR2(2000);

--Ajouter colonnes nom AN/Senat dans parametrage_mgpp
ALTER TABLE PARAMETRAGE_MGPP
ADD
 (
    NOMAN      NVARCHAR2(2000),
    NOMSENAT   NVARCHAR2(2000)
 ) ;
commit;

UPDATE PARAMETRAGE_MGPP SET NOMAN = 'Monsieur Claude BARTOLONE';
UPDATE PARAMETRAGE_MGPP SET NOMSENAT = 'Monsieur Jean-Pierre BEL';

ALTER TABLE FICHE_PRESENTATION_DECRET
ADD NORLOI NVARCHAR2(2000);

commit;