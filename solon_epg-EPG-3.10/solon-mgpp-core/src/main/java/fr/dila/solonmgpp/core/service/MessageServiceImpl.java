package fr.dila.solonmgpp.core.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;

import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.CorbeilleDTO;
import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.enumeration.CorbeilleTypeObjet;
import fr.dila.solonmgpp.api.service.MessageService;
import fr.dila.solonmgpp.api.service.RechercheService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.builder.QueryBuilder;
import fr.dila.solonmgpp.core.dto.CritereRechercheDTOImpl;
import fr.dila.solonmgpp.core.dto.MessageDTOImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.sword.xsd.solon.epp.Depot;
import fr.sword.xsd.solon.epp.EtatDossier;
import fr.sword.xsd.solon.epp.EtatEvenement;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.Message;
import fr.sword.xsd.solon.epp.NiveauLecture;
import fr.sword.xsd.solon.epp.RechercherEvenementResponse;

public class MessageServiceImpl implements MessageService {

    private static final String MAX_SUFFIX = "Max";

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(MessageServiceImpl.class);

    private final Map<String, List<MessageDTO>> cacheCorbeille = new HashMap<String, List<MessageDTO>>();
    private final Map<String, List<String>> cachePrincipal = new HashMap<String, List<String>>();
    
 
    

    @Override
    public List<MessageDTO> findMessageByCorbeille(SSPrincipal ssPrincipal, CorbeilleDTO corbeilleDTO, CoreSession session, String currentMenu)
            throws ClientException {

        synchronized (this) {

            if (cacheCorbeille.get(ssPrincipal.getName() + corbeilleDTO.getIdCorbeille()) != null) {
                // return cache
                return cacheCorbeille.get(ssPrincipal.getName() + corbeilleDTO.getIdCorbeille());
            }

            List<MessageDTO> listMessage = new ArrayList<MessageDTO>();
            CritereRechercheDTO critereRechercheDTO = new CritereRechercheDTOImpl();

            RechercherEvenementResponse rechercherEvenementResponse = null;

            // tri par defaut sur la date, desc
            SortInfo sortInfo = new SortInfo(QueryBuilder.getDefaultOrderClause(), false);
            critereRechercheDTO.getSortInfos().add(sortInfo);

            RechercheService rechercheService = SolonMgppServiceLocator.getRechercheService();

            if (SolonMgppCorbeilleConstant.CORBEILLE_NODE.equals(corbeilleDTO.getType())) {

                critereRechercheDTO.getIdsCorbeille().add(corbeilleDTO.getIdCorbeille());

                // recuperation de etatMessage par defaut
                critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.FALSE));

                if (SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION.equals(corbeilleDTO.getIdCorbeille())) {
                  critereRechercheDTO.setEnAttente(false);
                }

                rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);

            } else if (SolonMgppCorbeilleConstant.MGPP_NODE.equals(corbeilleDTO.getType())) {

                if (corbeilleDTO.getIdCorbeille().endsWith(SolonMgppCorbeilleConstant.HISTORIQUE_SUFFIXE)) {
                    // corbeille historique
                    // recherche de toutes les corbeilles de type Message pour cet historique

                    List<CorbeilleDTO> listCorbeille = SolonMgppServiceLocator.getCorbeilleService().findCorbeille(currentMenu, ssPrincipal, session);

                    getIdsCorbeilleForHistorique(critereRechercheDTO, listCorbeille);

                    // recuperation de etatMessage par defaut
                    critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.TRUE));

                    // limitation par rapport au parametre MGPP : Nombre de jour de persistance d'affichage des messages traités de l'EPP
                    ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session);
                    critereRechercheDTO.setAncienneteMessage(parametrageMgpp.getNbJourAffichable());

                    rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);

                } else if (corbeilleDTO.getIdCorbeille().equals(SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE)) {

                    critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.FALSE));
                    critereRechercheDTO.getIdsCorbeille().add(SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION);
                    critereRechercheDTO.setEnAttente(true);

                    rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);

                } else if (CorbeilleTypeObjet.MESSAGE.equals(corbeilleDTO.getTypeObjet())) {
                    // corbeille de message rajouté par le MGPP par rapport aux corbeilles EPP
                    critereRechercheDTO.getIdsCorbeille().add(corbeilleDTO.getIdCorbeille());

                    // recuperation de etatMessage par defaut
                    critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.FALSE));

                    rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);

                }
            }

            if (rechercherEvenementResponse != null) {
                for (Message message : rechercherEvenementResponse.getMessage()) {
                    listMessage.add(buildMessageDTOFromMessage(message, session));
                }
            }

          //Ajouter en attente
            if(corbeilleDTO.getIdCorbeille().equals(SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE)) {
              for(MessageDTO crbAttente : listMessage) {
                crbAttente.setEnAttente(Boolean.TRUE);
              }
            }

            // mise en cache des messages
            cacheCorbeille.put(ssPrincipal.getName() + corbeilleDTO.getIdCorbeille(), listMessage);
            List<String> listCorbeille = cachePrincipal.get(ssPrincipal.getName());

            if (listCorbeille == null) {
                listCorbeille = new ArrayList<String>();
            }

            listCorbeille.add(ssPrincipal.getName() + corbeilleDTO.getIdCorbeille());

            cachePrincipal.put(ssPrincipal.getName(), listCorbeille);

            return listMessage;
        }

    }

    private void getIdsCorbeilleForHistorique(CritereRechercheDTO critereRechercheDTO, List<CorbeilleDTO> listCorbeille) {
        for (CorbeilleDTO corbeille : listCorbeille) {
            if (corbeille.getTypeObjet().equals(CorbeilleTypeObjet.MESSAGE) && !SolonMgppCorbeilleConstant.ROOT_NODE.equals(corbeille.getType())
                    && !corbeille.getIdCorbeille().endsWith(SolonMgppCorbeilleConstant.HISTORIQUE_SUFFIXE)) {
                critereRechercheDTO.getIdsCorbeille().add(corbeille.getIdCorbeille());
            }
            if (SolonMgppCorbeilleConstant.ROOT_NODE.equals(corbeille.getType())) {
                // load child
                getIdsCorbeilleForHistorique(critereRechercheDTO, corbeille.getCorbeille());
            }
        }
    }

    /**
     * Construction d'un {@link MessageDTO} à partir d'un {@link Message}
     * 
     * @param message
     * @return
     * @throws ClientException
     */

    @Override
    public MessageDTO buildMessageDTOFromMessage(Message message, CoreSession session) {

        MessageDTO messageDTO = new MessageDTOImpl();

        messageDTO.setEtatMessage(message.getEtatMessage().value());
        messageDTO.setIdEvenement(message.getIdEvenement());
        messageDTO.setIdDossier(message.getIdDossier());
        messageDTO.setObjet(message.getObjet());
        messageDTO.setIdSenat(message.getIdSenat());
        messageDTO.setPresencePieceJointe(message.isPresencePieceJointe());

        buildNiveauLecture(message.getNiveauLecture(), messageDTO);
        buildTypeEvenement(message.getTypeEvenement(), messageDTO, session);
        buildEtatEvenement(message.getEtatEvenement(), messageDTO);
        buildDateEvenement(message.getDateEvenement(), messageDTO);
        buildEmetteurEvenement(message.getEmetteurEvenement(), messageDTO);
        buildDestinataireEvenement(message.getDestinataireEvenement(), messageDTO);
        buildCopieEvenement(message.getCopieEvenement(), messageDTO);
        buildDepot(message.getNumeroDepot(), messageDTO);
        buildEtatDossier(message.getEtatDossier(), messageDTO);

        return messageDTO;
    }

    private void buildDepot(Depot depot, MessageDTO messageDTO) {
        if (depot != null) {
            messageDTO.setNumeroDepot(depot.getNumero());
            messageDTO.setDateDepot(DateUtil.xmlGregorianCalendarToDate(depot.getDate()));
        }
    }

    private void buildEtatEvenement(EtatEvenement etatEvenement, MessageDTO messageDTO) {
        if (etatEvenement != null) {
            messageDTO.setEtatEvenement(etatEvenement.value());
        }

    }

    private void buildEtatDossier(EtatDossier etatDossier, MessageDTO messageDTO) {
        if (etatDossier != null) {
            messageDTO.setEtatDossier(etatDossier.value());
        }

    }

    private void buildDestinataireEvenement(Institution destinataireEvenement, MessageDTO messageDTO) {
        if (destinataireEvenement != null) {
            messageDTO.setDestinataireEvenement(destinataireEvenement.value());
        }
    }

    private void buildEmetteurEvenement(Institution emetteurEvenement, MessageDTO messageDTO) {
        if (emetteurEvenement != null) {
            messageDTO.setEmetteurEvenement(emetteurEvenement.value());
        }
    }

    private void buildCopieEvenement(List<Institution> copieEvenement, MessageDTO messageDTO) {
        if (copieEvenement != null) {
            List<String> copie = new ArrayList<String>();
            for (Institution institution : copieEvenement) {
                copie.add(institution.value());
            }
            messageDTO.setCopieEvenement(copie);
        }

    }

    private void buildDateEvenement(XMLGregorianCalendar dateEvenement, MessageDTO messageDTO) {
        messageDTO.setDateEvenement(DateUtil.xmlGregorianCalendarToDate(dateEvenement));
    }

    private void buildTypeEvenement(EvenementType typeEvenement, MessageDTO messageDTO, CoreSession session) {
        if (typeEvenement != null) {
            try {
                EvenementTypeDescriptor evenementTypeDescriptor = SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(
                        typeEvenement.value());
                if (evenementTypeDescriptor != null) {
                    messageDTO.setTypeEvenement(evenementTypeDescriptor.getName());
                }
            } catch (ClientException e) {                
                LOGGER.error(session, STLogEnumImpl.FAIL_GET_EVENT_TYPE_TEC, e) ;
                messageDTO.setTypeEvenement(typeEvenement.name());
            }
        }

    }

    private void buildNiveauLecture(NiveauLecture niveauLecture, MessageDTO messageDTO) {
        if (niveauLecture != null) {
            if (niveauLecture.getCode() != null) {
                messageDTO.setCodeLecture(niveauLecture.getCode().value());
            }
            messageDTO.setNiveauLecture(niveauLecture.getNiveau());
        }
    }

    @Override
    public void clearCache(SSPrincipal ssPrincipal) {
        List<String> keys = cachePrincipal.get(ssPrincipal.getName());
        if (keys != null) {
            for (String key : keys) {
                cacheCorbeille.remove(key);
            }
        }
        cachePrincipal.remove(ssPrincipal.getName());

    }

    @Override
    public List<MessageDTO> filtrerCorbeille(CorbeilleNode corbeilleNode, CoreSession session, Map<String, Serializable> mapFiltrable,
            List<SortInfo> sortInfos, SSPrincipal ssPrincipal, String currentMenu) throws ClientException {

        List<MessageDTO> listMessage = new ArrayList<MessageDTO>();

        CritereRechercheDTO critereRechercheDTO = new CritereRechercheDTOImpl();

        RechercheService rechercheService = SolonMgppServiceLocator.getRechercheService();

        RechercherEvenementResponse rechercherEvenementResponse = null;

        if (SolonMgppCorbeilleConstant.CORBEILLE_NODE.equals(corbeilleNode.getType())) {

            critereRechercheDTO.getIdsCorbeille().add(corbeilleNode.getId());

            setCritereFromFiltre(critereRechercheDTO, mapFiltrable);

            if (SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION.equals(corbeilleNode.getId())) {
              critereRechercheDTO.setEnAttente(false);
            }

            if (critereRechercheDTO.getEtatMessages().isEmpty()) {
                // pas de filtre sur etatMessage => recuperation de etatMessage par defaut
                critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.FALSE));

            }

            critereRechercheDTO.setSortInfos(sortInfos);

            rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);

        } else if (SolonMgppCorbeilleConstant.MGPP_NODE.equals(corbeilleNode.getType())) {

            if (corbeilleNode.getId().endsWith(SolonMgppCorbeilleConstant.HISTORIQUE_SUFFIXE)) {
                // corbeille historique
                // recherche de toutes les corbeilles de type Message pour cet historique

                List<CorbeilleDTO> listCorbeille = SolonMgppServiceLocator.getCorbeilleService().findCorbeille(currentMenu, ssPrincipal, session);

                getIdsCorbeilleForHistorique(critereRechercheDTO, listCorbeille);

                setCritereFromFiltre(critereRechercheDTO, mapFiltrable);

                if (critereRechercheDTO.getEtatMessages().isEmpty()) {
                    // pas de filtre sur etatMessage => recuperation de etatMessage par defaut
                    critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.TRUE));

                }

                // limitation par rapport au parametre MGPP : Nombre de jour de persistance d'affichage des messages traités de l'EPP
                ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session);
                critereRechercheDTO.setAncienneteMessage(parametrageMgpp.getNbJourAffichable());

                critereRechercheDTO.setSortInfos(sortInfos);

                rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);

            } else if (CorbeilleTypeObjet.MESSAGE.equals(corbeilleNode.getTypeObjet())) {

                if (SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE.equals(corbeilleNode.getId())) {

                  critereRechercheDTO.getIdsCorbeille().add(SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION);
                  critereRechercheDTO.setEnAttente(true);

                } else {

                  // corbeille de message rajouté par le MGPP par rapport aux corbeilles EPP
                  critereRechercheDTO.getIdsCorbeille().add(corbeilleNode.getId());
                }
                
                setCritereFromFiltre(critereRechercheDTO, mapFiltrable);

                if (critereRechercheDTO.getEtatMessages().isEmpty()) {
                    // pas de filtre sur etatMessage => recuperation de etatMessage par defaut
                    critereRechercheDTO.getEtatMessages().addAll(SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(Boolean.FALSE));

                }

                critereRechercheDTO.setSortInfos(sortInfos);

                rechercherEvenementResponse = rechercheService.findMessageWS(critereRechercheDTO, session);
            }

        }

        if (rechercherEvenementResponse != null) {
            for (Message message : rechercherEvenementResponse.getMessage()) {
                listMessage.add(buildMessageDTOFromMessage(message, session));
            }
        }

        //Ajouter en attente
        if(corbeilleNode.getId().equals(SolonMgppCorbeilleConstant.CORBEILLE_GOUVERNEMET_PROC_LEG_ATTENTE)) {
          for(MessageDTO crbAttente : listMessage) {
            crbAttente.setEnAttente(Boolean.TRUE);
          }
        }

        return listMessage;
    }

    private void setCritereFromFiltre(CritereRechercheDTO critereRechercheDTO, Map<String, Serializable> mapFiltrable) {

        if (mapFiltrable.get(MessageDTO.ETAT_MESSAGE) != null) {
            critereRechercheDTO.setEtatMessage((String) mapFiltrable.get(MessageDTO.ETAT_MESSAGE));
        }

        if (mapFiltrable.get(MessageDTO.ETAT_EVENEMENT) != null) {
          critereRechercheDTO.setEtatEvenement((String) mapFiltrable.get(MessageDTO.ETAT_EVENEMENT));
        }

        if (mapFiltrable.get(MessageDTO.OBJET) != null) {
            critereRechercheDTO.setObjet((String) mapFiltrable.get(MessageDTO.OBJET));
        }

        if (mapFiltrable.get(MessageDTO.ID_DOSSIER) != null) {
            critereRechercheDTO.setIdDossier((String) mapFiltrable.get(MessageDTO.ID_DOSSIER));
        }

        if (mapFiltrable.get(MessageDTO.PRESENCE_PIECE_JOINTE) != null) {
            critereRechercheDTO.setPresencePieceJointe((Boolean) mapFiltrable.get(MessageDTO.PRESENCE_PIECE_JOINTE));
        }

        if (mapFiltrable.get(MessageDTO.CODE_LECTURE) != null) {

            critereRechercheDTO.setCodeLecture((String) mapFiltrable.get(MessageDTO.CODE_LECTURE));

            if (mapFiltrable.get(MessageDTO.NIVEAU_LECTURE) != null) {
                critereRechercheDTO.setNiveauLecture(Long.parseLong((String) mapFiltrable.get(MessageDTO.NIVEAU_LECTURE)));
            }
        }

        if (mapFiltrable.get(MessageDTO.EMETTEUR_EVENEMENT) != null) {
            critereRechercheDTO.setEmetteur((String) mapFiltrable.get(MessageDTO.EMETTEUR_EVENEMENT));
        }

        if (mapFiltrable.get(MessageDTO.DESTINATAIRE_EVENEMENT) != null) {
            critereRechercheDTO.setDestinataire((String) mapFiltrable.get(MessageDTO.DESTINATAIRE_EVENEMENT));
        }

        if (mapFiltrable.get(MessageDTO.COPIE_EVENEMENT) != null) {
            critereRechercheDTO.setCopie((String) mapFiltrable.get(MessageDTO.COPIE_EVENEMENT));
        }

        if (mapFiltrable.get(MessageDTO.TYPE_EVENEMENT) != null) {
            critereRechercheDTO.getTypeEvenement().add((String) mapFiltrable.get(MessageDTO.TYPE_EVENEMENT));
        }

        if (mapFiltrable.get(MessageDTO.DATE_EVENEMENT) != null) {
            Date date = (Date) mapFiltrable.get(MessageDTO.DATE_EVENEMENT);

            Calendar calDebut = Calendar.getInstance();
            calDebut.setTime(date);
            calDebut.set(Calendar.HOUR_OF_DAY, 0);
            calDebut.set(Calendar.MINUTE, 0);
            calDebut.set(Calendar.SECOND, 0);
            calDebut.set(Calendar.MILLISECOND, 0);

            critereRechercheDTO.setDateEvenementMin(calDebut.getTime());

        }

        if (mapFiltrable.get(MessageDTO.DATE_EVENEMENT + MAX_SUFFIX) != null) {
            Date date = (Date) mapFiltrable.get(MessageDTO.DATE_EVENEMENT + MAX_SUFFIX);

            Calendar calFin = Calendar.getInstance();
            calFin.setTime(date);
            calFin.set(Calendar.HOUR_OF_DAY, 23);
            calFin.set(Calendar.MINUTE, 59);
            calFin.set(Calendar.SECOND, 59);
            calFin.set(Calendar.MILLISECOND, 999);

            critereRechercheDTO.setDateEvenementMax(calFin.getTime());

        }

        if (mapFiltrable.get(MessageDTO.ID_EVENEMENT) != null) {
            critereRechercheDTO.setIdEvenement((String) mapFiltrable.get(MessageDTO.ID_EVENEMENT));
        }

    }

}
