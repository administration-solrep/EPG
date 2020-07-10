#!/bin/sh


#
# Ce script prend un fichier ou un répertoire en entrée, copie son contenu dans /tmp
# puis execute le fichier groovy au sein de l'application nuxeo
#
# Exemple, dans le répertoire reparePublication :
# sh ~/groonux.sh reparePublication.groovy .
#

SCRIPT=$1
REP=$2
USER=Administrator
PWD=Administrator
HOST=http://localhost:8180
URL=${HOST}/solon-epg/site/automation    
TMP_NXSCRIPT=/tmp/tmp.nxscript
NUXEO_SHELL_HOME=/opt/nuxeo-shell


echo "COPIE DES FICHIERS NECESSAIRES A L'EXECUTION DU SCRIPT"
cp $2/* /tmp
echo "EXECUTION DE $SCRIPT"

cat > $TMP_NXSCRIPT <<EOF 

connect -u $USER -p $PWD $URL
script /tmp/$SCRIPT

EOF


pushd $NUXEO_SHELL_HOME;
java -cp nuxeo-shell-5.4.2*.jar org.nuxeo.shell.Main -f $TMP_NXSCRIPT;
popd;

echo "FIN D'EXECUTION DE $SCRIPT"

