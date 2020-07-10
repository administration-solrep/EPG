#!/bin/bash

sh createDossier.sh $1 $2
INJECTION_LOG_FILE=`sh inject.sh`
sh waitEndInjection.sh $3/$INJECTION_LOG_FILE

