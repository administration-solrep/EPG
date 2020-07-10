package fr.dila.solonmgpp.api.service;

import fr.dila.st.api.recherche.QueryAssembler;

public interface MGPPJointureService {

    /**
     * Renvoie l'objet chargé de construire les jointures entre les documents du projet, à partir de préfixes connus.
     * @return un queryAssembler.
     */
    public QueryAssembler getQueryAssembler();
}
