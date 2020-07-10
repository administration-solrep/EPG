#!/bin/sh

#
# Ce script exécute les scripts nuxeo-shell d'une version donnée.
# Le premier argument est le chemin vers le serveur
# Le deuxième argument est le numéro de versio
#
USAGE="Usage: executeNxshellScripts /opt/epg-server_1.0.0-SNAPSHOT/ 2.9"
[[ -n "$1" ]] && echo "REP_SERVER=$1" || { echo $USAGE; exit 0; }
[[ -n "$2" ]] && echo "VERSION=$2" || { echo $USAGE; exit 0; }

REP_SERVER=$1
VERSION=$2
INST_1=$REP_SERVER/epg-server_inst1
NUXEOSHELL_HOME=/opt/nuxeo-shell

# Mise à jour du script nxshell.sh
cp $INST_1/tools/nxshell.sh $NUXEOSHELL_HOME

# Exécution de tous les scripts de cette version
SHELLSCRIPTS_DIR=$INST_1/init/nxshell/update/$VERSION
pushd $NUXEOSHELL_HOME
for shell_script in `ls  $SHELLSCRIPTS_DIR`
do
	echo "Exécution de $shell_script";
	sh nxshell.sh -f $SHELLSCRIPTS_DIR/$shell_script;
	echo "Fin d'exécution";
done
popd

