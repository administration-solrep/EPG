#!/bin/bash

#
# Script de supression des fichiers trop anciens dans le cache de conversion Nuxeo.
# Mettre un appel dans la crontab :
# 5 * * * * /opt/clean_convert_cache.sh
#

find /opt/epg-server_inst1/tmp/convertcache/ -type f -mmin +50 -exec rm {} \;