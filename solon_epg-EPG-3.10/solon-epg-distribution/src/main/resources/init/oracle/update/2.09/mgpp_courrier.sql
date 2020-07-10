--Changer le contenu du courier

delete from VOC_COURRIER;
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('0',0,10000000,'10 - Procedure accéléré');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('1',0,10000000,'10 - Procedure accéléré lettre rectificative');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('2',0,10000000,'13 - Première lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('3',0,10000000,'13 - Première lecture modifiée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('4',0,10000000,'13 - Première lecture rejetée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('5',0,10000000,'13 - Première lecture procédure accéleré');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('6',0,10000000,'13 - Première lecture procédure accéleré modifiée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('7',0,10000000,'13 - Première lecture procédure accéleré rejetée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('8',0,10000000,'13 - Deuxième lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('9',0,10000000,'13 - Deuxième lecture modifiée');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('10',0,10000000,'13 - Nouvelle lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('11',0,10000000,'19 - CMP (45-2)');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('12',0,10000000,'19 - CMP (45-2) texte commun');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('13',0,10000000,'23 - CMP (45-3)');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('14',0,10000000,'23 bis - Echec CMP');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('15',0,10000000,'23 bis - Echec CMP texte commun');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('16',0,10000000,'23 bis - Rejet par l''Assemblée nationale');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('17',0,10000000,'23 bis - Rejet par le Sénat');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('18',0,10000000,'23 bis - Rejet par le Sénat des amendements du Gouvernement');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('19',0,10000000,'23 - Dernière lecture');
insert into VOC_COURRIER ("id","obsolete","ordering","label") values ('20',0,10000000,'23 - Dernière lecture rejet');

commit;