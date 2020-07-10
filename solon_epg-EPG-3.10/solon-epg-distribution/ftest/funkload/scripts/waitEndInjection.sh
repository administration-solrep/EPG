#!/usr/bin/bash

INJECTION_LOG_FILE=$1
echo "Attend la fin de l'injection, pour monitorer : tail -f $INJECTION_LOG_FILE"

grep -q 'End job in' $INJECTION_LOG_FILE
while [[ $? -ne 0 ]] ; do
    sleep 1
    grep -q 'End job in' $INJECTION_LOG_FILE
done
echo "Fin de l'injection"
