CREDCTL := fl-credential-ctl conf/credential.conf
NORCTL := fl-credential-ctl conf/nordistrib.conf
MONCTL := fl-monitor-ctl conf/monitor.conf
CREATEANDINJECTDOSSIER := sh createAndInjectDossier.sh
INJECTIONDIR := /root/epg/funkload/injecteur/solrep-injection-solonEPG-1.0.0-SNAPSHOT 

ifdef REPORT_FREQ
	SCENARIO_FREQ =  $(REPORT_FREQ)
else
	SCENARIO_FREQ = 1:2:3:4:5:6:7:8:9:10
endif

LOG_HOME := ./log

ifdef URL
	FLOPS = -u $(URL) $(EXT)
else
	FLOPS = $(EXT)
endif

ifdef REPORT_HOME
	REPORT = $(REPORT_HOME)
else
	REPORT = /root/epg/results-bench/intesword
endif

all: 
	test-scenario1

start:
	-mkdir -p $(REPORT) $(LOG_HOME)
	-$(MONCTL) restart
	 $(CREDCTL) restart
	 $(NORCTL) restart

stop:
	-$(MONCTL) stop
	-$(CREDCTL) stop
	-$(NORCTL) stop
	

injection-validation-start:
	-$(CREATEANDINJECTDOSSIER) 4950 5000 $(INJECTIONDIR)

build-report:
	-fl-build-report $(LOG_HOME)/epg-bench.xml --html -o $(REPORT) --apdex-T=2

clean:
	-find . "(" -name "*~" -or  -name ".#*" -or  -name "*.pyc" ")" -print0 | xargs -0 rm -f

############################# Scenario de login/logout

run-scenario1:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario01 $(FLOPS)


test-scenario1: start run-scenario1 stop

bench-scenario1: start run-scenario1 b-scenario1 build-report stop

b-scenario1:
	-fl-run-bench --label=Login --simple-fetch -c $(SCENARIO_FREQ) -D 300  -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario01 $(FLOPS)

############################# Scenario d'ouverture des onglets du menu principal

run-scenario2:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario02 $(FLOPS)


test-scenario2: start run-scenario2 stop

bench-scenario2: start run-scenario2 b-scenario2 build-report stop

b-scenario2:
	-fl-run-bench --label=Ouverture_Onglets --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario02 $(FLOPS)
	
	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de traitement

run-scenario3:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario03 $(FLOPS)


test-scenario3: start run-scenario3 stop

bench-scenario3: start run-scenario3 b-scenario3 build-report stop

b-scenario3:
	-fl-run-bench --label=Ouverture_Dossier_Dans_Espace_Traitement --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario03 $(FLOPS)
	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création

run-scenario4:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario04 $(FLOPS)


test-scenario4: start run-scenario4 stop

bench-scenario4: start run-scenario4 b-scenario4 build-report stop

b-scenario4:
	-fl-run-bench --label=Ouverture_Dossier_Dans_Espace_Creation --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario04 $(FLOPS)
	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de recherche, recherche d'une liste de dossier à partir de la recherche simple

run-scenario5:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario05 $(FLOPS)


test-scenario5: start run-scenario5 stop

bench-scenario5: start run-scenario5 b-scenario5 build-report stop

b-scenario5:
	-fl-run-bench --label=Recherche_Dossier --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario05 $(FLOPS)
	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de traitement, sans naviguer dans les onglets
run-scenario6:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario06 $(FLOPS)


test-scenario6: start run-scenario6 stop

bench-scenario6: start run-scenario6 b-scenario6 build-report stop

b-scenario6:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Traitement --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario06 $(FLOPS)
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création, sans naviguer dans les onglets
run-scenario7:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario07 $(FLOPS)


test-scenario7: start run-scenario7 stop

bench-scenario7: start run-scenario7 b-scenario7 build-report stop

b-scenario7:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Creation --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario07 $(FLOPS)
	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création, onglet FDD
run-scenario8:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario08 $(FLOPS)


test-scenario8: start run-scenario8 stop

bench-scenario8: start run-scenario8 b-scenario8 build-report stop

b-scenario8:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Creation_FDD --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario08 $(FLOPS)
	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création, onglet Bordereau
run-scenario9:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario09 $(FLOPS)


test-scenario9: start run-scenario9 stop

bench-scenario9: start run-scenario9 b-scenario9 build-report stop

b-scenario9:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Creation_Borderau --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario09 $(FLOPS)	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création, onglet FDR
run-scenario10:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario10 $(FLOPS)


test-scenario10: start run-scenario10 stop

bench-scenario10: start run-scenario10 b-scenario10 build-report stop

b-scenario10:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Creation_FDR --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario10 $(FLOPS)	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création, onglet Journal
run-scenario11:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario11 $(FLOPS)


test-scenario11: start run-scenario11 stop

bench-scenario11: start run-scenario11 b-scenario11 build-report stop

b-scenario11:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Creation_Journal --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario11 $(FLOPS)	
	
############################# Scenario d'ouverture d'un dossier dans l'espace de création, onglet TP
run-scenario12:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario12 $(FLOPS)


test-scenario12: start run-scenario12 stop

bench-scenario12: start run-scenario12 b-scenario12 build-report stop

b-scenario12:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Creation_TP --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario12 $(FLOPS)			

############################# Scenario d'ouverture de 3 dossiers dans l'espace de création
run-scenario13:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario13 $(FLOPS)


test-scenario13: start run-scenario13 stop

bench-scenario13: start run-scenario13 b-scenario13 build-report stop

b-scenario13:
	-fl-run-bench --label=Ouverture_Dossier_Multiple_Creation --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario13 $(FLOPS)	
	

############################# Scenario d'ouverture d'un dossier et des sous-onglets dans l'espace de création
run-scenario14:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario14 $(FLOPS)


test-scenario14: start run-scenario14 stop

bench-scenario14: start run-scenario14 b-scenario14 build-report stop

b-scenario14:
	-fl-run-bench --label=Ouverture_Dossier_Simple_Sous_Onglet_Creation --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario14 $(FLOPS)			
			
############################# Scenario de création de dossier
run-scenario15:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario15 $(FLOPS)


test-scenario15: start run-scenario15 stop

bench-scenario15: start run-scenario15 b-scenario15 build-report stop

b-scenario15:
	-fl-run-bench --label=CreationDossier --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario15 $(FLOPS)			
	
############################# Scenario de validation d'étape
run-scenario16:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario16 $(FLOPS)


test-scenario16: start run-scenario16 stop

bench-scenario16: start injection-validation-start run-scenario16 b-scenario16 build-report stop

b-scenario16:
	-fl-run-bench --label=ValidationDossier --simple-fetch -c $(SCENARIO_FREQ) -D 150 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario16 $(FLOPS)			


############################# Scenario mixte
run-scenario17:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario17 $(FLOPS)


test-scenario17: start run-scenario17 stop

bench-scenario17: start run-scenario17 b-scenario17 build-report stop

b-scenario17:
	-fl-run-bench --label=ScenarioMixte --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario17 $(FLOPS)		
		

############################# Scenario ajout document parapheur
run-scenario20:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario20 $(FLOPS)


test-scenario20: start run-scenario20 stop

bench-scenario20: start run-scenario20 b-scenario20 build-report stop

b-scenario20:
	-fl-run-bench --label=ScenarioMixte --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario20 $(FLOPS)		

############################# Scenario ajout Ã©tape
run-scenario21:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario21 $(FLOPS)

test-scenario21: start run-scenario21 stop

bench-scenario21: start run-scenario21 b-scenario21 build-report stop

b-scenario21:
	-fl-run-bench --label=ScenarioMixte --simple-fetch -c $(SCENARIO_FREQ) -D 3600 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario21 $(FLOPS)
		

############################# Scenario ajout Ã©tape
run-scenario22:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario22 $(FLOPS)

test-scenario22: start run-scenario22 stop

bench-scenario22: start run-scenario22 b-scenario22 build-report stop

b-scenario22:
	-fl-run-bench --label=ScenarioMixte --simple-fetch -c $(SCENARIO_FREQ) -D 7200 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario22 $(FLOPS)



############################# Scenario variable pour tester les performances
run-scenario99:
	fl-run-test -V -dv --simple-fetch testEpg.py -e testScenario99 $(FLOPS)


test-scenario99: start run-scenario99 stop

bench-scenario99: start run-scenario15 b-scenario99 build-report stop

b-scenario99:
	-fl-run-bench --label=Test_Performance --simple-fetch -c $(SCENARIO_FREQ) -D 300 -m 0.5 -M 1 -s 0.1 testEpg.py Epg.testScenario99 $(FLOPS)	
	
	
