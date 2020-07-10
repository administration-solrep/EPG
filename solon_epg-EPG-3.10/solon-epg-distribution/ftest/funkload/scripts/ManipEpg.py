# -*- coding: utf-8 -*-

import os
import re
import random
import urllib
from Utils import extractJsfState, extractToken, extractLastStartToken, extractJsfId
from time import gmtime, strftime
import time
from webunit.utility import Upload

from funkload.utils import Data

class TabAction:
    
    def __init__(self,index,id,url,view, assertionActionSucceed):
        self.id = id
        self.index = index 
        self.url = url
        self.view = view
        self.assertionActionSucceed = assertionActionSucceed
        

class ContentView:
    
    def __init__(self, id, action_id, description):
        self.action_id = action_id
        self.id = id
        self.description = description

class ManipEpg :
  
    def __init__(self, ftest):
        self.ftest = ftest
        self.actions = {}
        self.miscActions = {}
        
        # --- Actions pour le menu principal ---
        self.addTabAction(0,"espace_traitement","/espace_traitement/view_espace_traitement.faces","view_espace_traitement",self.makeAssertBody('traitement'))
        self.addTabAction(1,"espace_creation","/espace_creation/view_espace_creation.faces","view_espace_creation",self.makeAssertBody('creation'))
        self.addTabAction(2,"espace_suivi","/espace_suivi/view_espace_suivi.faces","",self.makeAssertBody('suivi'))
        self.addTabAction(3,"espace_recherche","/requeteur/requeteur_dossier.faces","",self.makeAssertBody('recherche'))
        self.addTabAction(4,"espace_espace_parlementaire","/view_empty.faces","",self.makeAssertBody(''))
        self.addTabAction(5,"espace_pilotage_activite_normative", "/view_empty.faces","",self.makeAssertBody(''))
        self.addTabAction(6,"espace_administration", "/view_empty.faces", "",self.makeAssertBody(''))
        
        self.addMiscAction(0,"resultats_recherche_nor","/recherche/requete_resultats.faces","view_recherche_resultats_nor",self.makeAssertBody('R&eacute;sultats '))
        
        self.actionIdToContentView = {}
        self.addContentView("espace_traitement_dossier_content","espace_traitement","Ouvrir dossier espace de traitement")
        self.addContentView('espace_creation_dossier_content',"espace_creation", "Ouvrir dossier espace de création")
        self.addContentView("recherche_nor","resultats_recherche_nor", "Ouvrir dossier recherche")
        
        self.urlEspaceTraitement = self.actions['espace_traitement'].url
        self.urlResultatsRechercheNor = self.miscActions['resultats_recherche_nor'].url
        
        self.urlCourante = self.urlEspaceTraitement
        
        
    def addTabAction(self,*actions_args):
        action =  TabAction(*actions_args)
        self.actions[action.id] = action
        
    def addMiscAction(self,*actions_args):
        action =  TabAction(*actions_args)
        self.miscActions[action.id] = action
        
    def addContentView(self,*contentview_args):
        contentview =  ContentView(*contentview_args)
        self.actionIdToContentView[contentview.action_id] = contentview
        
    def makeAssertBody(self, keyword):
        return lambda f: f.ftest.assert_(keyword in f.ftest.getBody() and '<h1>Erreur</h1>' not in f.ftest.getBody())
    
    def getActionFromUrl(self,ref_url):
        for action in self.actions.values():
            if (ref_url == action.url):
                return action
        for action in self.miscActions.values():
            if (ref_url == action.url):
                return action
        
    def getCurrentContentView(self):
        action = self.getActionFromUrl(self.urlCourante)
        contentview = self.actionIdToContentView[action.id]
        return contentview
    
    # -- login / logout ---
    # accueil
    # login
    # logout
    
    # Permet d atteindre la page login.jsp
    def accueil(self):
        self.ftest.get(self.ftest.server_url + "/login.jsp",
                 description="Recuperation de la page login.jsp")
        self.ftest.assert_('user_password' in self.ftest.getBody())
	
    def login(self, login, password):
        assertText = 'Mon identifiant dans S.O.L.O.N. : ' + login
	self.ftest.post(self.ftest.server_url + "/nxstartup.faces",
                  params=[
                ['user_name', login],
                ['user_password', password],
                ['language', 'fr'],
                ['requestedUrl', ''],
                ['form_submitted_marker', ''],
                ['Submit', 'Connexion']],
                  description="Login " + login)
	#print(self.ftest.getBody())
	self.ftest.assert_('Mon identifiant dans S.O.L.O.N. : ' + login in self.ftest.getBody(), 'login échoue pour : ' + login)
        self.urlCourante = self.urlEspaceTraitement
	return self.urlEspaceTraitement

    def logout(self, login):
        self.ftest.get(self.ftest.server_url + "/logout",
                 description="Logout " + login)
        self.ftest.assert_('user_password' in self.ftest.getBody())
    
    def checkLogin(self, login, password):
        self.ftest.logd("checkLogin")
        self.login(login, password)
        self.logout(login)

        
    ''' 
    La navigation dans le menu est fait via des requetes de type POST.
    L'URL de la requete de type POST qui mene a une page depend de la page source.
    C'est a cela que sert le parametre de type chaine de caracteres 'urlSource'
    Toutes les requetes de navigation retournent une nouvelle valeur d'urlSource (une chaine de caracteres)
    qui doit être utilise dans la requete suivante.
    '''   
    
    def clickOnMainTab(self,action):
        self.ftest.post(self.ftest.server_url + self.urlCourante, params=[
                                                                   ['userServicesForm_SUBMIT', '1'],
                                                                   ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
                                                                   ['userServicesForm:userServicesActionsTable:'+ str(action.index) + ':userServicesActionCommandLink', 'userServicesForm:userServicesActionsTable:'+ str(action.index) + ':userServicesActionCommandLink'],
                                                                   ['id', action.id]],
                                                           description="Post go to " + action.id)
      
    # ----------------------------------------
    # MANIPULATION DES ACTIONS DU MENU PRINCIPAL
    # ----------------------------------------
        
    def goTo(self,action_id):
        action = self.actions[action_id]
        self.clickOnMainTab(action)
        self.urlCourante = action.url
        action.assertionActionSucceed(self)
        return action.url
    
    def goToEspaceTraitement(self):
       return self.goTo("espace_traitement")
            
    def goToEspaceRecherche(self):
       return self.goTo("espace_recherche")
    
    def goToEspaceSuivi(self):
        return self.goTo("espace_suivi")
    
    def goToEspaceCreation(self):
        return self.goTo("espace_creation")
      
    def goToEspacePilotageActiviteNormative(self):
        return self.goTo("espace_pilotage_activite_normative")
    
    def goToEspaceEspaceParlementaire(self):
        return self.goTo("espace_espace_parlementaire")
    
    def goToEspaceAdministration(self):
     return self.goTo("espace_administration")
 
    def openResult(self, index, content_view,description):
        strIndex = str(index)
        dossierListingId = extractJsfId(self.ftest.getBody(),"nxw_dossier_listing_titre_dto_" + strIndex)       
        self.ftest.post(self.ftest.server_url +  self.urlCourante, params=[
            [content_view + '_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            [content_view + ':nxl_dossier_listing_dto_' + strIndex +':nxw_dossier_listing_titre_dto_' + strIndex + ':' + dossierListingId, content_view + ':nxl_dossier_listing_dto_' + strIndex + ':nxw_dossier_listing_titre_dto_' + strIndex + ':' + dossierListingId]],
            description=description)
 
    # ----------------------------------------
    # MANIPULATION DEPUIS LA RECHERCHE SIMPLE
    # ----------------------------------------
    
    
    def rs_chercherDossier(self, nor):
        self.ftest.post(self.ftest.server_url + self.urlCourante, params=[
            ['userMetaServicesSearchForm:numeroNor', nor],
            ['userMetaServicesSearchForm:rechercheNumeroSubmitButton', 'Atteindre'],
            ['userMetaServicesSearchForm_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
            description="Atteindre (" + nor + ")")
        self.urlCourante = self.urlResultatsRechercheNor
        
    def rs_chercherUniqueDossier(self, nor):
        self.rs_chercherDossier(nor)
        self.ftest.assert_("<h4>" + nor + "</h4>")
        self.urlCourante = self.urlResultatsRechercheNor        
        

    def openResultInEspace(self, index):
        contentview = self.getCurrentContentView()
        self.openResult(index,contentview.id, contentview.description)
        #self.ftest.assert_((" <h4>" + nor + "</h4>") in self.ftest.getBody())
    
    ''' Prend un dossier dans la première page des résultats'''
    def openOneRandomResultInEspace(self):
        dossiers = self.getFirstPageDossiers()
        if len(dossiers) > 0:
            dossierIndex = random.randrange(1, len(dossiers) + 1)
            self.openResultInEspace(dossierIndex)
        else:
            self.ftest.logd("Pas de dossiers trouvés")
    
    '''
    getFirstPageDossiers
    Cette methode ne doit etre invoquee que depuis un espace qui affiche une liste de résultats
    Retourne la liste des nor des dossiers visibles sous forme de tableau de chaines de car
    '''
    def getFirstPageDossiers(self):
      dossiers = []
      contentview = self.getCurrentContentView()
      '''
          On utilise le fait que les nors sont écrits avec docRef="<Nor>"
      '''
      html = self.ftest.getBody()
      dossiers = re.findall('docRef="(\w+)"', html)
      return dossiers

            
    # ajouterFichierParapheur
    # Suit toute la procedure pour ajouter un fichier au parapheur.
    # renvoie une exeption si le fichier a uploader n'est pas trouve ou si il n'a pas ete ajoute.
    # A cause des appels AJAX, a la fin de cette methode la page recuperable via getBody n'est pas une page complete.
    # afin de pouvoir recuperer certaines informations dans la page, la page html d'origine est retournee.
	# pour l'espace de création l'url courante est : '/espace_creation/view_espace_creation.faces'
    def ajouterFichierParapheur(self, urlCourante, filepath): #, htmlPageOrigine):
      
      # verification que le fichier existe
      if not os.path.exists(filepath):
        print('File not found : ' + filepath)
	
      # Il faut conserver le html de la page d'origine pour recuperer des information a l'interieur.
      #  les futur appels a la methode getBody vont retourner le resultat des requetes AJAX.
      htmlPageOrigine = self.ftest.getBody()
      
      
      # selection du répertoire Acte Intégral.
      '''
      exemple de requete : 
      self.post(server_url + urlCourante, params=[
	  ['AJAXREQUEST', 'document_properties:documentViewRegion'],
	  ['document_properties:parapheurTree:1::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
	  ['document_properties:parapheurTree:2::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
	  ['document_properties:parapheurTree:3::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
	  ['document_properties:parapheurTree:input', 'document_properties:parapheurTree:1::parapheurFolderVideDocAjoutable'],
	  ['document_properties_SUBMIT', '1'],
	  ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
	  ['document_properties:parapheurTree:selectedNode', 'document_properties:parapheurTree:1::parapheurFolderVideDocAjoutable']],
	  description="Post /solonepg/..._view.faces")
      '''
      idNode = extractToken(self.ftest.getBody(), 'document_properties:parapheurTree:1::', '\"><tbody>')
      
      self.ftest.post(self.ftest.server_url + urlCourante, params=[
        ['AJAXREQUEST', 'document_properties:documentViewRegion'],
        ['document_properties:parapheurTree:1::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
        ['document_properties:parapheurTree:2::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
        ['document_properties:parapheurTree:3::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
        ['document_properties:parapheurTree:input', 'document_properties:parapheurTree:1::parapheurFolderVideDocAjoutable'],
        ['document_properties_SUBMIT', '1'],
        ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
        ['document_properties:parapheurTree:selectedNode', 'document_properties:parapheurTree:1::parapheurFolderVideDocAjoutable']],
        description="AJAX : Select bouton Acte intégral")
	
      # Clic sur 'Ajouter document'
      '''
      exemple de requete : 
      self.post(server_url + urlCourante, params=[
          ['AJAXREQUEST', 'document_properties:documentViewRegion'],
          ['document_properties:parapheurTree:1::parapheurFolderVideDocAjoutableNodeExpanded, 'true'],
          ['document_properties:parapheurTree:2::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
          ['document_properties:parapheurTree:3::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
          ['document_properties:parapheurTree:input', 'document_properties:parapheurTree:1::parapheurFolderVideDocAjoutable'],
          ['document_properties_SUBMIT', '1'],
          ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
          ['SelectedNodeId', '9881bbf1-7c04-42b8-90e9-9f5f37bdf7be'],
          ['SelectedNodeType', 'ParapheurFolder'],
          ['document_properties:j_id1101', 'document_properties:j_id1101'],
          ['ajaxSingle', 'document_properties:j_id1101']],
	  description="Post /solonepg/..._view.faces")
      '''
      # idNode a la même valeur que dans la requete precedente
      #idDocumentProperties = extractToken(htmlPageOrigine, 'document_properties:j_id', '\',\'parameters')
      SelectedNodeId = extractToken(htmlPageOrigine, "'show',{'id': '", "', 'type':")
      #print(SelectedNodeId)
      
      SelectedNodeId = SelectedNodeId.replace('\\x2D', '-')
     # print(SelectedNodeId)
      
      self.ftest.post(self.ftest.server_url + urlCourante, params=[
        ['AJAXREQUEST', 'document_properties:documentViewRegion'],
        ['document_properties:parapheurTree:1::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
        ['document_properties:parapheurTree:2::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
        ['document_properties:parapheurTree:3::parapheurFolderVideDocAjoutableNodeExpanded', 'true'],
        ['document_properties:parapheurTree:input', 'document_properties:parapheurTree:1::parapheurFolderVideDocAjoutable'],
        ['document_properties_SUBMIT', '1'],
        ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
        ['SelectedNodeId', SelectedNodeId],
        ['SelectedNodeType', 'ParapheurFolder'],
        ['document_properties:j_id1787', 'document_properties:j_id1787'],
        ['ajaxSingle', 'document_properties:j_id1787']],
        description="AJAX : Ajouter document'")
      
      # ajout du fichier
          
      #print("### Upload du fichier : " + filepath)
      filename = os.path.basename(filepath) 
      
      # rich face random uid
      upload_uid = str(random.random()) 
     
      #Attention, ne pas descendre les parametres passés dans la string dans le tableau
      self.ftest.post(self.ftest.server_url + urlCourante +"?_richfaces_upload_uid="+upload_uid+"&editFileForm:uploadParapheurFiles=editFileForm:uploadParapheurFiles&_richfaces_upload_file_indicator=true&AJAXREQUEST=editFileRegion", params=[
            ['editFileForm:uploadParapheurFiles:file', Upload(filepath)], # adresse du fichier
            ['editFileForm_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
            description="AJAX : Upload du fichier")
            
      self.ftest.post(self.ftest.server_url + urlCourante, params=[
            ['AJAXREQUEST', 'editFileRegion'],
            ['editFileForm:uploadParapheurFiles:file', ''],
            ['editFileForm_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            ['ajaxSingle', 'editFileForm:uploadParapheurFiles'],
            ['editFileForm:uploadParapheurFiles', 'editFileForm:uploadParapheurFiles'], 
            ['AJAX:EVENTS_COUNT', '1']],
            description="AJAX : Requete post-upload : Reaffichage de la boite de telechargement.")

      idEditForm = extractToken(htmlPageOrigine, '\"editFileForm:j_id', '\"')            

      self.ftest.post(self.ftest.server_url + urlCourante, params=[
            ['AJAXREQUEST', 'editFileRegion'],
            ['editFileForm:uploadParapheurFiles:file', ''],
            ['editFileForm_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            ['editFileForm:j_id' + idEditForm, 'editFileForm:j_id' + idEditForm], 
            ['AJAX:EVENTS_COUNT', '1']],
            description="AJAX prévalidation upload")
    
	  #Validation de l'upload du fichier
      self.ftest.post(self.ftest.server_url + urlCourante, params=[
            ['AJAXREQUEST', 'editFileRegion'],
            ['editFileForm:uploadParapheurFiles:file', ''],
            ['editFileForm_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            ['ajaxSingle', 'editFileForm:editFileButtonText'],
            ['editFileForm:editFileButtonText', 'editFileForm:editFileButtonText'],
            ['AJAX:EVENTS_COUNT', '1']],
            description="AJAX : Validation definitive de l'upload")
            
      # les reponses AJAX ne permettent pas de savoir si le document a bien ete uploade
      
      # Verification que le dossier dispose bien d'un document ayant le nom de celui que l'on vient d'ajouter.
      # Attention, si on ajoute 2 documents avec le meme nom, un seul d'entre eux est bien uploade.

      self.ftest.assert_('\'name\': \'' + filename + '\', \'type\': \'ParapheurFichier\'' in self.ftest.getBody())
    
      return htmlPageOrigine

    def ajouterFichierParapheurEspaceCreation(self, filepath): #, htmlPageOrigine):
      return self.ajouterFichierParapheur('/espace_creation/view_espace_creation.faces', filepath);#, htmlPageOrigine);
 
# -- ajout etape feuille de route --- #
   
    # ajoutEtapeFDRApresEtapeCourante
    # Cette methode ajoute une etape a la feuille de route du dossier apres l'etape en cours.
    # Ele doit être appellee depuis l'onglet feuille de route et le dossier doit être verrouillé.
    # parametres : 
    #   CommentaireNouvelleEtape : chaine de caracteres
    #   idPoste : chaine de caracteres de l'identifiant ldap du poste (ex : 'poste-50000656')
    #   deadLine : chaine de caracteres representant un nomre (ex : '3')
    #   validationAutomatique : chaine de caracteres : 'on' ou 'off'
    #   obligatoire : chaine de caracteres : chaine de caracteres : 'on' ou 'off'
    # si un de ces parametres est passe a None, il prend par defaut la valeur '' ou 'off'
    def ajoutEtapeFDRApresEtapeCourante(self, CommentaireNouvelleEtape, idPoste, deadLine, validationAutomatique, obligatoire, urlCourante):
      if CommentaireNouvelleEtape == None: 
        CommentaireNouvelleEtape = ''

      if deadLine == None:
        deadLine = ''

      dossierPath = self.__extractDossierPath(self.ftest.getBody())
      
      # recuperation de l'etape courante en utilisant l'image
      htmlEtapeCourante = extractToken(self.ftest.getBody(), '<li class=\"ctxMenuItemStyle\">', 'img/icons/bullet_ball_glass_yellow_16.png')
      # recuperation des differents elements de la requete
      hiddenSourceDocId = extractToken(htmlEtapeCourante, 'hiddenSourceDocId\').value = \'', '\';')
      #print(hiddenSourceDocId)
      # ajouter apres
      self.ftest.post(self.ftest.server_url + urlCourante, params=[
          ['selectRouteElementsTypeForCreationForm:hiddenSourceDocId', hiddenSourceDocId], # 865380c5-337d-4aa9-827e-7d5f2ae1875b
          ['selectRouteElementsTypeForCreationForm:hiddenDocOrder', 'after'],
          ['selectRouteElementsTypeForCreationForm:selectRouteElementsTypePanelOpenedState', ''],
          ['selectRouteElementsTypeForCreationForm_SUBMIT', '1'],
          ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
          ['selectRouteElementsTypeForCreationForm:selectRouteElementsTypeForCreationTable:0:selectRouteElementTypeForCreationCategory:0:selectRouteElementsTypeForCreationCategoryTable:0:createRouteElementLink', 'selectRouteElementsTypeForCreationForm:selectRouteElementsTypeForCreationTable:0:selectRouteElementTypeForCreationCategory:0:selectRouteElementsTypeForCreationCategoryTable:0:createRouteElementLink']],
          description="AJAX : insertion d'une etape apres l'etape courante")
      
      
      # creation de l'etape
      self.ftest.post(self.ftest.server_url + "/feuilleroute/create_route_element.faces", params=[
          ['document_create:nxl_routing_task_detail:routing_task_type', '4'], # Pour attribution : '4', ...
          ['document_create:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_nodeId', idPoste], # 'poste-50000656' ou 'poste-bureau-des-cabinets'
          ['document_create:nxl_routing_task_detail:nxw_routing_task_description', CommentaireNouvelleEtape], # 'Commentaire nouvelle \xc3\xa9tape.'
          ['document_create:nxl_routing_task_detail:nxw_routing_task_deadline:nxw_routing_task_deadline_from', deadLine],
          ['document_create:nxl_routing_task_detail:nxw_routing_task_automatic_validation', validationAutomatique],
          ['document_create:nxl_routing_task_detail:nxw_routing_task_obligatoire_ministere', obligatoire],
          ['parentDocumentPath', dossierPath],
          ['document_create:button_create', 'Cr\xc3\xa9er'], # bouton de validation
          ['document_create_SUBMIT', '1'],
          ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
          description="Creation de l'etape de la feuille de route")
   
    
    def ajoutEtapeFDRApresEtapeCouranteCreation(self, CommentaireNouvelleEtape, idPoste, deadLine, validationAutomatique, obligatoire):
        self.ajoutEtapeFDRApresEtapeCourante(CommentaireNouvelleEtape, idPoste, deadLine, validationAutomatique, obligatoire, '/espace_creation/view_espace_creation.faces');

        # A partir de la feuille de route d'un dossier, permet d'aller vers la page de modification de l'étape à venir
        # nécessite un dossier verrouillé
    def goToModifierEtape(self, urlCourante, stepNum):
         # recuperation de l'etape a venir en utilisant l'image
	# Attention, le ctxMenuItemStyle rencontré en premier est celui de l'étape en cours
	idLink = '<a id=\"dm_route_elements:nxl_feuille_route_instance_listing_'+stepNum+':nxw_dr_listing_step_actions_'+stepNum+':titleref\"'
        htmlEtapeAVenir = extractToken(self.ftest.getBody(), idLink, 'img/icons/bullet_ball_glass_grey_16.png')
        # recuperation des differents elements de la requete
        hiddenSourceDocId = extractToken(htmlEtapeAVenir, 'hiddenSourceDocId\').value = \'', '\';')

	parameterStep = 'dm_route_elements:nxl_feuille_route_instance_listing_'+stepNum+':nxw_dr_listing_step_actions_'+stepNum+':nxw_dr_listing_step_actions_'+stepNum+'_edit:editStepListTable:2:documentActionSubviewUpperListLink'

        self.ftest.post(self.ftest.server_url + urlCourante, params=[
        ['stepId', hiddenSourceDocId], # 865380c5-337d-4aa9-827e-7d5f2ae1875b
        [parameterStep, parameterStep],
        ['dm_route_elements_SUBMIT', '1'],
        ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
	description="Aller vers modification de l'étape à venir")

    def goToModifierEtapeCreation(self, stepNum):
	self.goToModifierEtape('/espace_creation/view_espace_creation.faces', stepNum);


         # A partir de la page de modification d'un étape
    def modifyStep(self, deadline, poste, task):
        if deadline == None:
                deadline = ''
        if task == None:
		task = '2' #tache "pour avis"

        self.ftest.post(self.ftest.server_url + '/edit_route_element.faces', params=[
        ['edit_step:nxl_routing_task_detail:nxw_routing_task_deadline:nxw_routing_task_deadline_from',deadline],
        ['edit_step:nxl_routing_task_detail:nxw_routing_task_distribution_mailbox_nodeId', poste],
        ['edit_step:nxl_routing_task_detail:routing_task_type', task],
        ['edit_step:update_step', 'Enregistrer'],
	['edit_step_SUBMIT', '1'],
	['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
	description="Modifier l'étape à venir")

    def deleteStep(self, urlCourante, stepNum):
         # recuperation de l'etape a venir en utilisant l'image
	# Attention, le ctxMenuItemStyle rencontré en premier est celui de l'étape en cours
        idLink = '<a id=\"dm_route_elements:nxl_feuille_route_instance_listing_'+stepNum+':nxw_dr_listing_step_actions_'+stepNum+':titleref\"'
	htmlEtapeAVenir = extractToken(self.ftest.getBody(), idLink, 'img/icons/bullet_ball_glass_grey_16.png')
         # recuperation des differents elements de la requete
        hiddenSourceDocId = extractToken(htmlEtapeAVenir, 'hiddenSourceDocId\').value = \'', '\';')

	parameterStep = 'dm_route_elements:nxl_feuille_route_instance_listing_'+stepNum+':nxw_dr_listing_step_actions_'+stepNum+':nxw_dr_listing_step_actions_'+stepNum+'_remove:removeStepListTable:0:documentActionSubviewUpperListLink'

        self.ftest.post(self.ftest.server_url + urlCourante, params=[
        [parameterStep, parameterStep],
        ['stepId', hiddenSourceDocId],
	['dm_route_elements_SUBMIT', '1'],
	['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
	description="Supprimer l'étape à venir")

    def deleteStepCreation(self,stepNum):
 	self.deleteStep('/espace_creation/view_espace_creation.faces',stepNum);


   ############################################################################# --- fin feuille de route ---  #####

   ############################################################################# --- Début bordereau ---  #####

	# Cette méthode ajoute les données nécessaires à un dossier pour être lancé
    # et sauvegarde => deverrouillage du dossier
	# Elle doit être appelé depuis l'onglet bordereau d'un dossier verrouillé
    def ajoutDonneesBordereau(self, urlCourante):
        # Il faut conserver le html de la page d'origine pour recuperer des information a l'interieur.
        # les futur appels a la methode getBody vont retourner le resultat des requetes AJAX.
        htmlPageOrigine = self.ftest.getBody()

        idNode = extractToken(htmlPageOrigine, '\'parameters\':{\'bordereauForm:nxl_bordereau_indexation:', '\':\'bordereauForm:nxl_bordereau_indexation:')
        print idNode
        # Requete ajax d'ajout de la donnée d'indexation
        self.ftest.post(self.ftest.server_url + urlCourante, params=[
            ['AJAXREQUEST', 'bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_ajax_region'],
            ['bordereauForm:nxl_bordereau_donnees_principales:nxw_categorie_acte_bord', '1'],
            ['bordereauForm:nxl_bordereau_date_publication:nxw_date_publication_fieldInputCurrentDate', '03/2013'],
            ['bordereauForm:nxl_bordereau_date_signature:nxw_date_signature_fieldInputCurrentDate', '03/2013'],
            ['bordereauForm:nxl_bordereau_parution:nxw_pour_fourniture_epreuve_fieldInputCurrentDate', '03/2013'], 
            ['bordereauForm:nxl_bordereau_parution:nxw_vecteur_publication_field_selectVecteurs', 'Journal officiel'],
            ['bordereauForm:nxl_bordereau_parution:nxw_date_precisee_publication_fieldInputCurrentDate', '03/2013'],
            ['bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_select_index_menu', 'Energie, environnement'], 
            ['bordereauForm:nxl_bordereau_parution:nxw_publication_integrale_ou_extrait_field', '1'],  
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            ['bordereauForm:nxl_bordereau_indexation:'+ idNode, 'bordereauForm:nxl_bordereau_indexation:' + idNode], 
            ['bordereauForm_SUBMIT', '1']],
            description="AJAX : ajout de la donnée d'indexation du dossier")
	print "Ajout autres donnees"
        # ajout données texte + sauvegarde
        self.ftest.post(self.ftest.server_url + urlCourante, params=[
            ['bordereauForm:nxl_bordereau_donnees_principales:nxw_titre_acte_field', 'titre test funkload '], 
            ['bordereauForm:nxl_bordereau_donnees_principales:nxw_categorie_acte_bord', '1'], 
            ['bordereauForm:nxl_bordereau_responsable_acte:nxw_nom_resp_dossier_field', 'DUPONT'],
            ['bordereauForm:nxl_bordereau_responsable_acte:nxw_qualite_resp_dossier_field', 'M'],
            ['bordereauForm:nxl_bordereau_responsable_acte:nxw_tel_resp_dossier_field', '0102030405'],
            ['bordereauForm:nxl_bordereau_date_publication:nxw_date_publication_fieldInputCurrentDate', '03/2013'],
            ['bordereauForm:nxl_bordereau_date_signature:nxw_date_signature_fieldInputCurrentDate', '03/2013'],
            ['bordereauForm:nxl_bordereau_parution:nxw_pour_fourniture_epreuve_fieldInputCurrentDate', '03/2013'],
            ['bordereauForm:nxl_bordereau_parution:nxw_vecteur_publication_field_selectVecteurs', 'Journal officiel'], 
            ['bordereauForm:nxl_bordereau_indexation:nxw_indexation_rubriques_select_index_menu', 'Energie, environnement'],
            ['bordereauForm:nxl_bordereau_parution:nxw_publication_integrale_ou_extrait_field', '1'],
            ['bordereauForm:nxl_bordereau_parution:nxw_date_precisee_publication_fieldInputCurrentDate', '03/2013'],	      
            ['bordereauForm_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            ['bordereauForm:idSaveBordereauButton', 'bordereauForm:idSaveBordereauButton']],
            description="Ajout et sauvegarde des données du bordereau")
        
        return htmlPageOrigine
 
    def ajoutDonneesBordereauCreation(self):
        self.ajoutDonneesBordereau('/espace_creation/view_espace_creation.faces')

   ############################################################################# --- Fin bordereau ---  #####    
 
    # Navigation dans les onglets du dossier selectionne #
    # Ces methodes recupere des informations dans le contenu de la page courante.
    # Pour le cas où la page courante n'est pas la page complete (a cause des requetes AJAX), des methodes existent où l'on passe le contenu de la page en parametre.
    # goToBordereauTab
    # goToParapheurDeRouteTab
    # goToFondDeDossierTab
    # goToFeuilleDeRouteTab
    # goToJournalTab
    # goToTraitementPapierTab
    
    def goToParapheurTab(self):
        tab = 'TAB_DOSSIER_PARAPHEUR'
        self.__goToDossierTab(tab,'Onglet parapheur')        

    def goToFondDeDossierTab(self):
        tab = 'TAB_DOSSIER_FDD'
        self.__goToDossierTab(tab,'Onglet dossier')        

    def goToBordereauTab(self):
        tab = 'TAB_DOSSIER_BORDEREAU'
        self.__goToDossierTab(tab,'Onglet bordereau')        
        
    def goToFeuilleDeRouteTab(self):
        tab = 'TAB_CASE_MANAGEMENT_VIEW_RELATED_ROUTE'
        self.__goToDossierTab(tab,'Onglet feuille de route')        
        
    def goToJournalTab(self):
        tab = 'TAB_DOSSIER_JOURNAL'
        self.__goToDossierTab(tab,'Onglet journal')
         
    def goToTraitementPapierTab(self):
        tab = 'TAB_PAPIER'
        self.__goToDossierTab(tab,'Onglet traitement papier')
        
    
    def openSubTabs(self):
        self.goToParapheurTab()
        self.goToFondDeDossierTab()
        self.goToBordereauTab()
        self.goToFeuilleDeRouteTab()
        self.goToJournalTab()
        self.goToTraitementPapierTab()
        
        
    def openMonInfoCentre(self):
        pass
    
    def extractLockTableParam(self, html):
        m = re.search('formFdr:dossierActionsLockTable:1:\w+:\w+', html)
        paramFound = m.group(0)
        if not paramFound:
            raise ValueError("No dossierActionsLockTable found")
        else:
            return paramFound

    def extractFdrActionsParam(self, html):
        m = re.search('formFdr:dossierActionsTable:0:\w+', html)
        paramFound = m.group(0)
        if not paramFound:
            raise ValueError("No formFdr found")
        else:
            return paramFound

        
    def verrouillerDossier(self):
      lockTableParam = self.extractLockTableParam(self.ftest.getBody())
      self.ftest.post(self.ftest.server_url + self.urlCourante, params=[
            ['formFdr:Avalider_non_concerne_panelOpenedState',''],
            [lockTableParam,lockTableParam],
            ['formFdr_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
          ],
            description="Verrouille le dossier")
      self.ftest.assert_("Verrouill&eacute; le " in self.ftest.getBody(), "chaine verrouillé le non trouve")
 
     
    def validerDossier(self):
         fdrParam = self.extractLockTableParam(self.ftest.getBody())
         idNode = extractToken(self.ftest.getBody(), 'formFdr:j_id', '\':\'formFdr:j_id')
         self.ftest.post(self.ftest.server_url + self.urlCourante, params=[
            ['formFdr_SUBMIT', '1'],
            ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())],
            ['formFdr:j_id'+idNode,'formFdr:j_id'+idNode],
            ['formFdr:fdrActionsTable:valider_non_concerne_panelOpenedState', ''],
            ],
            description="Valide le dossier")
 
    
    
    def creation_ajouterUnDossier(self):
       self.ftest.post(self.ftest.server_url +"/espace_creation/view_espace_creation.faces", params=[
        ['formCreationDossier:buttonCreationDossier', 'Ajouter un dossier'],
        ['formCreationDossier_SUBMIT', '1'],
        ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
        description="Ajouter un dossier")
        
    
    def creation_goTerminer(self):
        # Type acte Amnistie, Circulaire, decret simple
        type_acte_array = ["1"]
        type_acte_index = random.randrange(0, len(type_acte_array))
        type_acte = type_acte_array[type_acte_index]
        self.ftest.post(self.ftest.server_url +"/espace_creation/view_creation_dossier_100.faces", params=[
        ['creation_dossier_100:nxl_creation_dossier_layout_100_a:nxw_type_acte_field_select_one_menu',type_acte],
        ['creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_ministere_resp_field_nodeId','50002618'],
        ['creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_nodeId','50001814'],
        ['creation_dossier_100:nxl_creation_dossier_layout_100_b:nxw_direction_resp_field_nodeNor',''],
        ['creation_dossier_100:buttonTerminer', 'Terminer'],
        ['creation_dossier_100_SUBMIT', '1'],
        ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
        description="Terminer la création d'un dossier")
        self.ftest.assert_("<title>SOLON EPG - Espace de cr&eacute;ation")
    
    
    # Pour se rendre dans l'onglet d'un dossier --- 
    # tab : correspond a l'onglet que l'on souahite ouvrir. Il prend pour valeur TAB_DOSSIER_BORDEREAU/TAB_DOSSIER_PARAPHEUR/TAB_DOSSIER_FDD/TAB_CASE_MANAGEMENT_VIEW_RELATED_ROUTE
    # urlMenu : correspond a l'url du menu courant. Il prend les valeurs possibles des variables urlMailBox, urlPlanDeClassement, ...
    def __goToDossierTab(self, tab, descrip=None):
        #dossierPath = "2011/12/28/EFIC1030148A"
        dossierPath = self.__extractDossierPath(self.ftest.getBody())
        view = self.getActionFromUrl(self.urlCourante).view
        if descrip == None:
            descrip = "Ouvrir onglet " + tab + " pour le dossierpath[" + str(dossierPath) + "] et l'utilisateur : " + self.ftest.login
        self.ftest.get(self.ftest.server_url + "/nxpath/default/case-management/case-root/" + dossierPath + '@' + view + '?tabId=' + tab + "&conversationId=0NXMAIN", description=descrip)
    
    
    ''' 
    extractDossierPath
    Cette fonction extrait le path du dossier affiche et le retourne
    Le path est utilise pour la construction des url de navigation dans les onglets d'un dossier
    ex de path : 2011/05/24/EFIB1014698A.1306223817352
    '''
    def __extractDossierPath(self, html):
        dossierpath = extractToken(html, '<li class=\"selected\"><a href=\"' + self.ftest.server_url + '/nxpath/default/case-management/case-root/', '@') 
        if not dossierpath:
            raise ValueError('No dossier path found in the page')
        if not dossierpath.startswith('20'):
            raise ValueError('Invalid dossier path %s' % str(dossierpath))
        return dossierpath
    
    # navigation dans la liste des questions #
    # goToLastPageOfCorbeille
    # goToPreviousPageofCorbeille
    # goToNextPageofCorbeille
    # goToFirstPageofCorbeille
    
    def goToLastPageOfCorbeille(self):
      self.__navigateById(self.__extractLastNavigationId(), "last page")
            
    def goToPreviousPageofCorbeille(self):
        self.__navigateById(self.__extractPrevNavigationId(), "prev page")

    def goToNextPageofCorbeille(self):
        self.__navigateById(self.__extractNextNavigationId(), "next page")

    def goToFirstPageofCorbeille(self):
        self.__navigateById(self.__extractFirstNavigationId(), "first page")
        
    def __extractLastNavigationId(self):
      return self.__extractNavigation('action_page_fastforward.gif')
    
    def __extractPrevNavigationId(self):
        return self.__extractNavigation('action_page_previous.gif')

    def __extractNextNavigationId(self):
        return self.__extractNavigation('action_page_next.gif')

    def __extractFirstNavigationId(self):
        return self.__extractNavigation('action_page_rewind.gif')

    def hasNavigationNext(self):
        return self.__extractNavigation('action_page_next.gif') != None
    
    def hasNavigationPrevious(self):
        return self.__extractNavigation('action_page_previous.gif') != None
        
    def hasNavigationLast(self):
        return self.__extractNavigation('action_page_fastforward.gif') != None
    
    def hasNavigationFirst(self):
        return self.__extractNavigation('action_page_rewind.gif') != None    
    
    
    def __extractNavigation(self, imgname):
      html = self.ftest.getBody()
      id = extractToken(html, imgname + '" name="', '" alt')
      return id
        
    def __navigateById(self, id, title):
      self.ftest.post(self.ftest.server_url + self.urlEspaceTraitement, params=[         
        [id + '.x', '13'], # la valeur de ce parametre varie, mais ne semble pas avoir d'effet
        [id + '.y', '7'], # la valeur de ce parametre varie, mais ne semble pas avoir d'effet
        ['corbeille_content_SUBMIT', '1'],
        ['javax.faces.ViewState', extractJsfState(self.ftest.getBody())]],
        description="Go to " + title + " for : " + self.ftest.login)          

    
  
  
