#!/bin/sh
#
# Ce script calcule les stats pour la date du jour et les met dans un fichier statistiques_es.csv

# Les différents chemins d'accès aux logs, fichiers .log ou .tar.gz

# Version pour DEV
#LOG_DIRS="/platform/home/user/Documents/stats/log_instance1/vmwdaa-tepg-p01/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/log_instance1/vmwdaa-tepg-p02/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/log_instance1/vmwdaa-tepg-p03/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/log_instance1/vmwdaa-tepg-p04/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/p01_current/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/p02_current/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/p03_current/indexation.log* "
#LOG_DIRS="$LOG_DIRS /platform/home/user/Documents/stats/p04_current/indexation.log*"
# Version pour Recette Sword
#LOG_DIRS="/var/log/epg-server_inst1/indexation.log* "
# Version pour QUALIF
#LOG_DIRS="/EPG/log_instance1/vmwqda-tepg-q01/indexation.log* "
#LOG_DIRS="$LOG_DIRS /var/log/epg-server_inst1/indexation.log*"
# Version pour PREPROD
LOG_DIRS="/EPG/log_instance1/vmwpda-tepg-v01/indexation.log* "
LOG_DIRS="$LOG_DIRS /EPG/log_instance1/vmwpda-tepg-v02/indexation.log* "
LOG_DIRS="$LOG_DIRS /var/log/epg-server_inst1/indexation.log* "
LOG_DIRS="$LOG_DIRS ??????/indexation.log* " # Chemin d'accès depuis TEPG-V01 vers les logs courantes de TEPG-V02
# Version pour PROD 
#LOG_DIRS="/EPG/log_instance1/vmwdaa-tepg-p01/indexation.log* "
#LOG_DIRS="$LOG_DIRS /EPG/log_instance1/vmwdaa-tepg-p02/indexation.log* "
#LOG_DIRS="$LOG_DIRS /EPG/log_instance1/vmwdaa-tepg-p03/indexation.log* "
#LOG_DIRS="$LOG_DIRS /EPG/log_instance1/vmwdaa-tepg-p04/indexation.log* "
#LOG_DIRS="$LOG_DIRS /var/log/epg-server_inst1/indexation.log* "
#LOG_DIRS="$LOG_DIRS ??????/indexation.log* "  # Chemin d'accès depuis TEPG-P01 vers les logs courantes de TEPG-P02
#LOG_DIRS="$LOG_DIRS ??????/indexation.log* "  # Chemin d'accès depuis TEPG-P01 vers les logs courantes de TEPG-P03
#LOG_DIRS="$LOG_DIRS ??????/indexation.log*"  # Chemin d'accès depuis TEPG-P01 vers les logs courantes de TEPG-P04

#Le répertoire où on place le fichier de stats
STATS_FILE_DIR=/root/stats_es #DILA et Recette Sword
#STATS_FILE_DIR=/platform/home/user/Documents/stats # Des SWORD

# Nom du fichier de statistiques
STATS_FILE_NAME="statistiques_es.csv"
STATS_FILE_FULLNAME=${STATS_FILE_DIR}/${STATS_FILE_NAME}

STATS_FILE_HEADER="Date;Indexations;Requêtes ES;Facette vecteur de publication;Facette type d'acte;Facette catégorie d'acte;Facette statut;Facette Ministère responsable;Facette Date de création;Facette Date de publication;"

REGEX_TOTAL_REQUETES="\[fr.dila.solonepg.elastic.rest.ElasticHttpClient\].*\"size\":25"
REGEX_TOTAL_INDEXATIONS="indexation post-commit event"
REGEX_FACET_VECTEURPUB="\"dos:vecteurPublication\":"
REGEX_FACET_TYPEACTE="\"dos:typeActe\":"
REGEX_FACET_CATEGORIEACTE="\"dos:categorieActe\":"
REGEX_FACET_STATUT="\"dos:statut\":"
REGEX_FACET_MINISTERERESP="\"dos:ministereResp\":"
REGEX_FACET_CREATED="\"dos:created\":"
REGEX_FACET_DATEPARUTION="\"retdila:dateParutionJorf\":"

result=0

# Vérifie le fichier de stats
# S'il n'existe pas, le crée
function check_stat_file {
	if [[ ! -f ${STATS_FILE_FULLNAME} ]]; then
    	#echo "The File Does Not Exist -> Init"
    	echo ${STATS_FILE_HEADER} > ${STATS_FILE_FULLNAME}
    fi
}

function compute_one_stat {
	regex="$1.*$2"
	echo $(zgrep -a "$regex" $LOG_DIRS | wc -l)
}

function compute_one_day_stats {
	check_stat_file
 
	echo -n $1";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_TOTAL_INDEXATIONS}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_TOTAL_REQUETES}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_FACET_VECTEURPUB}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_FACET_TYPEACTE}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_FACET_CATEGORIEACTE}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_FACET_STATUT}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_FACET_MINISTERERESP}")";" >> ${STATS_FILE_FULLNAME}
	echo -n $(compute_one_stat $1 "${REGEX_FACET_CREATED}")";" >> ${STATS_FILE_FULLNAME}
	echo $(compute_one_stat $1 "${REGEX_FACET_DATEPARUTION}")";" >> ${STATS_FILE_FULLNAME}
}

function compute_today_stats {
	local today="$(date +%Y-%m-%d)"
	compute_one_day_stats $today 
}

function compute_yesterday_stats {
	local today=$(date -d "yesterday" +%Y-%m-%d)
	compute_one_day_stats $today 
}

function compute_current_month_stats {
	local currentDay=$(date +%d)
	local currentMonth=$(date +%m)
	local currentYear=$(date +%Y)
	
	local day=1
	
	local dateMonth=$currentMonth
	if [ $currentMonth -le 9 ]; then
		dateMonth="0"$currentMonth
	fi
	
	while [ $day -le $currentDay ]
	do
		local dateDay=$day
		if [ $day -le 9 ]; then
			dateDay="0"$day
		fi
		local date="$currentYear-$currentMonth-$dateDay"
		compute_one_day_stats $date
		((day++))
	done
}

function compute_given_month_stats {
	local givenMonth=$1
	local lastDay=$(date -d "$givenMonth/1 + 1 month - 1 day" +%d)
	local givenYear=$2
	
	local day=1
	
	local dateMonth=$givenMonth
	if [ $givenMonth -le 9 ]; then
		dateMonth="0"$givenMonth
	fi
	
	while [ $day -le $lastDay ]
	do
		local dateDay=$day
		if [ $day -le 9 ]; then
			dateDay="0"$day
		fi
		local date="$givenYear-$givenMonth-$dateDay"
		echo "$date"
		compute_one_day_stats $date
		((day++))
	done
}

# Calcul des stats pour le jour précédent
compute_yesterday_stats

# Ex calcul des stats pour le mois courant, jusqu'au jour courant inclus
#compute_current_month_stats

# Ex : Calcul des stats pour le mois d'octobre 2018
#compute_given_month_stats 10 2018