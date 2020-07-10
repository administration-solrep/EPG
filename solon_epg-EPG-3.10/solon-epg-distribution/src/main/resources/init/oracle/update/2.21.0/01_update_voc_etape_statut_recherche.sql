delete from VOC_ETAPE_STATUT_RECHERCHE;
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('running_all','0',10000000,'label.epg.feuilleRoute.etape.running');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('ready_all','0',10000000,'label.epg.feuilleRoute.etape.ready');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('done_1','0',10000000,'label.epg.feuilleRoute.etape.valide.manuellement');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('done_2','0',10000000,'label.epg.feuilleRoute.etape.valide.refusValidation');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('done_3','0',10000000,'label.epg.feuilleRoute.etape.valide.automatiquement');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('done_4','0',10000000,'label.epg.feuilleRoute.etape.valide.nonConcerne');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('done_10','0',10000000,'label.epg.feuilleRoute.etape.valide.avisFavorableCorrection');
insert into VOC_ETAPE_STATUT_RECHERCHE ("id","obsolete","ordering","label") values ('done_15','0',10000000,'label.epg.feuilleRoute.etape.valide.retourModification');
commit;

