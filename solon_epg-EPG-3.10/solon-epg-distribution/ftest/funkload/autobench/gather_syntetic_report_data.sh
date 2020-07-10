#!/bin/sh

# Rassemble des données synthétiques sur l'ensemble des scénarios exécutés par funkload 
# Exemple d'exécution : 
# sh gather_synthetic_report_data.sh DILA-prod 20120405
#
REPORT_HOME=/root/epg/results-bench/
if [[ $1 ]];
	then FOLDER_NAME=$1;
	else FOLDER_NAME=(date +%Y%m%d);
fi

if [[ $2 ]];
	then DATA_SET=$2
	else 
		DATA_SET="intesword-nightly-benchs";
fi

DATA_HOME=$REPORT_HOME/$DATA_SET/$FOLDER_NAME
PAGE_DATA=pages.data

#echo "Last value : "
#for scenario in `ls -tr $DATA_HOME`
#do
#	cat $DATA_HOME/$scenario/$PAGE_DATA | awk 'END {printf "%.2f;",$2}' 
#done
#echo ""
#echo "Max value : "
for scenario in `ls -tr $DATA_HOME`
do
	shortname_scenario=$(echo $scenario | cut -d\- -f 1 | sed s/testS/s/)
	printf "$shortname_scenario; "
	awk 'NR == 2 {max = 0} {if ($2>max) max=$2} END {printf "%.2f;", max}'  $DATA_HOME/$scenario/$PAGE_DATA;
	printf "\n"
done


