# -*- coding: utf-8 -*-
"""Reponse FunkLoad test

$Id: $
"""
import unittest
import random
from webunit.utility import Upload
import time
from datetime import datetime

from funkload.FunkLoadTestCase import FunkLoadTestCase
from funkload.utils import Data
from funkload.utils import xmlrpc_get_credential
from funkload.Lipsum import Lipsum

from Utils import extractJsfState
import ManipEpg

class Epg(FunkLoadTestCase):
    """Ce test utilise un fichier Epg.conf.
    """

    def setUp(self):
        """Setting up test."""
        self.logd("setUp")

        self.manip = ManipEpg.ManipEpg(self)

        self.server_url = self.conf_get('main', 'url')
        self.credential_host = self.conf_get('credential', 'host')
        self.credential_port = self.conf_getInt('credential', 'port')
        
        self.recherche_pattern_scenario5 =  self.conf_get('scenario5', 'recherche_pattern')

        utilisateur_groupe = self.conf_get('scenario1', 'connexion_groupe')
	self.login,self.password = xmlrpc_get_credential(self.credential_host, self.credential_port, utilisateur_groupe)        
		
        
        self.nordistrib_host = self.conf_get('nordistrib', 'host')
        self.nordistrib_port = self.conf_getInt('nordistrib', 'port')
        nors = self.conf_get('scenario1', 'nor_group')
        self.nor_a_valider = xmlrpc_get_credential(self.nordistrib_host, self.nordistrib_port, nors)[0]

        # document qui sera uploade
        self.documentLocation = self.conf_get('scenario20', 'documentLocation')
        # poste utilisé pour l'ajout d'étape
        self.poste = self.conf_get('scenario21', 'poste')
        

    ''' 
    Scenario 1 : 
      - Atteinte de la page login.jsp
      - login en tant qu'utilisateur 'bdc'
      - deconnexion
    '''
    def testScenario01(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.logout(self.login)      
      

    ''' 
    Scenario 2 : 
      - atteinte de la page login.jsp
      - login
      - chargement de chacun des onglets du menu principal
      - deconnexion
    '''
    def testScenario02(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)

        ''' Navigation dans les onglets principaux'''
        m.goToEspaceTraitement()
        m.goToEspaceCreation()
        m.goToEspaceSuivi()
        m.goToEspaceRecherche()
        m.goToEspaceEspaceParlementaire()
#        m.goToEspacePilotageActiviteNormative()
        m.goToEspaceAdministration()
        
        m.logout(self.login)
        
    ''' 
    Scenario 3 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de traitement
      - ouverture d'un dossier
      - balade dans chacun des onglets de ce dossier
      - deconnexion
    '''
    def testScenario03(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)

        ''' Navigation dans les onglets de 2 dossiers'''
        m.goToEspaceTraitement()
        for i in range(1,4):
            m.openOneRandomResultInEspace()
            m.openSubTabs()
        
        m.logout(self.login)  
        
    ''' 
    Scenario 4 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - balade dans chacun des onglets de ce dossier
      - deconnexion
    '''
    def testScenario04(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)

        ''' Navigation dans les onglets de 2 dossiers'''
        m.goToEspaceCreation()
        for i in range(1,3):
           m.openOneRandomResultInEspace()
           m.openSubTabs()
        
        m.logout(self.login) 
        
    ''' 
    Scenario 5 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de recherche
      - effectuer une recherche (sur le nor) avec la recherche simple
      - Ouvrir un dossier des résultats
      - balade dans chacun des onglets de ce dossier
      - deconnexion
    '''
    def testScenario05(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)

        ''' Navigation dans les onglets principaux'''
        m.goToEspaceRecherche()
        m.rs_chercherDossier(self.recherche_pattern_scenario5)
        
        ''' Navigation dans les onglets de 2 dossiers'''
        m.openOneRandomResultInEspace()
        m.openSubTabs()
        
        m.logout(self.login)  
         


    ''' 
    Scenario 6 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de traitement
      - ouverture d'un dossier
      - deconnexion
    '''
    def testScenario06(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceTraitement()
        m.openOneRandomResultInEspace()
        m.logout(self.login) 
        
        
    ''' 
    Scenario 7 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - deconnexion
    '''
    def testScenario07(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.logout(self.login) 

    ''' 
    Scenario 8 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - Ouverture de l'onglet FDD
      - deconnexion
    '''
    def testScenario08(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.goToFondDeDossierTab()
        m.logout(self.login) 

    ''' 
    Scenario 9 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - Ouverture de l'onglet Bordereau
      - deconnexion
    '''
    def testScenario09(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.goToBordereauTab()
        m.logout(self.login) 


    ''' 
    Scenario 10 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - Ouverture de l'onglet FDR
      - deconnexion
    '''
    def testScenario10(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.goToFeuilleDeRouteTab()
        m.logout(self.login) 


    ''' 
    Scenario 11 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - Ouverture de l'onglet Journal
      - deconnexion
    '''
    def testScenario11(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.goToJournalTab()
        m.logout(self.login) 


    ''' 
    Scenario 12 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture d'un dossier
      - Ouverture de l'onglet TP
      - deconnexion
    '''
    def testScenario12(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.goToTraitementPapierTab()
        m.logout(self.login) 

    ''' 
    Scenario 13 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - ouverture de 3 dossiers
      - deconnexion
    '''
    def testScenario13(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        for i in range(1,4):
            m.openOneRandomResultInEspace()
        m.logout(self.login) 

    ''' 
    Scenario 14 : 
      - atteinte de la page login.jsp
      - login
      - chargement de l'espace de création
      - Ouverture d'un dossier et ses sous-onglets
      - deconnexion
    '''
    def testScenario14(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.openSubTabs()
        m.logout(self.login) 
        

    ''' 
    Scenario 15: 
      - Atteinte de la page login.jsp
      - Login en tant qu'adminsgg
      - Chargement de l'espace de creation
      - Création d'un dossier parmi Amnistie,Décision, Circulaire
      - logout'''
    def testScenario15(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.creation_ajouterUnDossier()
        m.creation_goTerminer()
        m.logout(self.login) 
        
        
        ''' 
       Scenario 16 : 
      - Atteinte de la page login.jsp
      - Login 
      - Chargement de l'espace de traitement
      - Dévérouillage d'un dossier
      - Validation d'un dossier
      - logout'''
    def testScenario16(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.rs_chercherUniqueDossier(self.nor_a_valider)
        m.goToFeuilleDeRouteTab()
        try:
            m.verrouillerDossier()
        except:
            # Le dossier est déjà verrouiller, on valide directement
            pass
        m.validerDossier()
        m.logout(self.login) 
        
    
    '''
    Scénario mixte 
    20% ecriture : création de dossier
    80% lecture : ouverture d'un  
    '''
    def testScenario17(self):
        rnd = random.random()
        if rnd < 0.20:
            self.testScenario15()
        else:
            self.testScenario14()
    
    ''' 
    
    Scenario 18 : 
      - Atteinte de la page login.jsp
      - Login en tant agriculture_bdc (profil admin min)
      - Chargement de l'espace de création
      - Création d'un dossier logout
      - logout'''
    def testScenario18(self):
        m = self.manip
        m.accueil()
        m.login('agriculture_bdc','agriculture_bdc')
        m.goToEspaceSuivi()
        folders = m.openInfoGeneral()
        #for folder in folders:
        #    m.openFolder(folder)
        m.logout(self.login) 
    
    
    ''' 
    Scenario 19 : 
      - Atteinte de la page login.jsp
      - Login en tant agriculture_bdc (profil admin min)
      - Chargement de l'espace de suivi
      - Ouverture de mon InfoCentre
      - Ouverture des dossiers de mon InfoCentre
      - logout'''
    def testScenario19(self):
        m = self.manip
        m.accueil()
        m.login('agriculture_bdc','agriculture_bdc')
        m.goToEspaceSuivi()
        folders = m.openMonInfoCentre()
        if folders:
            self.logd(folders)
            
        #for folder in folders:
        #    m.openFolder(folder)
        m.logout(self.login) 
    

    ''' 
    Scenario 20 : 
      - Atteinte de la page login.jsp
      - Login 
      - Ouverture d'un dossier dans l'espace de création
      - Verrouillage + ajout fichier parapheur
      - logout
    '''
    def testScenario20(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()
        m.goToParapheurTab()
        try:
            m.verrouillerDossier()
        except:
            # Le dossier est déjà verrouillé
            pass
        m.ajouterFichierParapheurEspaceCreation(self.documentLocation)
        
        m.logout(self.login) 

    ''' 
    Scenario 21 : 
      - Atteinte de la page login.jsp
      - Login 
      - creation d'un dossier dans l'espace de création
      - ajout d'étape de fdr
      - ajout fichier parapheur
      - ajout donnees bordereau
      - lancement dossier
      - logout
    '''
    def testScenario21(self):
        m = self.manip
        self.doLogin()
        self.creerDossier()
        self.verrouillage()
        #htmlOrigine = m.ftest.getBody()       
                            
       #ajout de l'étape suivante
        self.ajouterEtapeDossier()
        self.ajouterDonneesBordereauDossier()

        self.verrouillage()
#        self.ajouterFichierDossier()#htmlOrigine)
      
       #lancement dossier
        m.validerDossier()
        m.logout(self.login) 


	'''
     Scenario 22:
         - Atteinte de la page login.jsp
         - Login
         - Chargement de l'espace de creation
         - Création d'un dossier parmi Amnistie,Décision, Circulaire
	 - Verrouillage dossier
         - Ouverture de l'onglet Feuille de route
         - Ajout d'une étape à la suite de la première
         - Modification de la seconde étape de la feuille de route 
         - Suppression de la seconde étape de la feuille de route (TODO)
         - Deverrouillage du dossier (TODO)
         - logout'''
    def testScenario22(self):
	url = '/espace_creation/view_espace_creation.faces'
	self.doLogin()
	self.creerDossier() 
	self.verrouillage()
	self.ajouterEtapeDossier(url)
	self.modifierEtape(url)
	self.supprimerEtape(url)
	self.manip.logout(self.login)

    
    def doLogin(self):
        self.manip.accueil()
        self.manip.login(self.login, self.password)

    def creerDossier(self):
        self.manip.goToEspaceCreation()
        self.manip.creation_ajouterUnDossier()
        self.manip.creation_goTerminer()

    def ajouterFichierDossier(self):#, htmlOrigine):
#        self.verrouillage()
        self.manip.goToParapheurTab()
        self.manip.ajouterFichierParapheurEspaceCreation(self.documentLocation)#, htmlOrigine)

    def ajouterDonneesBordereauDossier(self):
#        self.verrouillage()
        self.manip.goToBordereauTab()
        self.manip.ajoutDonneesBordereauCreation()

    def ajouterEtapeDossier(self, url):
#        self.verrouillage()
        self.manip.goToFeuilleDeRouteTab()
        self.manip.ajoutEtapeFDRApresEtapeCourante(None, self.poste, None, 'off', 'off', url)

    def modifierEtape(self, url):
        self.manip.goToModifierEtape(url, '1')
        self.manip.modifyStep('10', self.poste, '2')

    def supprimerEtape(self, url):
        self.manip.deleteStep(url, '1')

    def verrouillage(self):
        try:
            self.manip.verrouillerDossier()
        except:
            # Le dossier est déjà verrouillé
            pass


    ''' 
    Scenario 99 : 
         Scénario variable pour mettre en évidence des problématiques de performance.
    ''' 
    def testScenario99(self):
        m = self.manip
        m.accueil()
        m.login(self.login,self.password)
        m.goToEspaceCreation()
        m.openOneRandomResultInEspace()    
        m.openOneRandomResultInEspace() 
        m.openOneRandomResultInEspace()    
        m.openOneRandomResultInEspace() 
        m.openOneRandomResultInEspace() 
        m.openOneRandomResultInEspace()
        m.openOneRandomResultInEspace()    
        m.openOneRandomResultInEspace() 
        m.openOneRandomResultInEspace()    
        m.openOneRandomResultInEspace() 
        m.openOneRandomResultInEspace() 
        m.openOneRandomResultInEspace()  
        m.logout(self.login) 
        
    
        

    def tearDown(self):
        self.logd("tearDown.\n")



if __name__ in ('main', '__main__'):
    unittest.main()
