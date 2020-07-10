#!/usr/bin/python
# -*- coding: utf-8 -*-

from __future__ import with_statement
from fabric.api import run, local, cd

# Script déploiement et de lancement des benchs

# Mettre dans un fichier de properties
EPG_FUNKLOAD_HOME="/root/epg/funkload/scripts/"

def hello():
    run('echo "Bonjour le monde !"')

def deploy():
    with cd("/opt/"):
        run('epg-deploy-hudson.sh')
    local("sleep 150")

def benchall():
        local("cd " + EPG_FUNKLOAD_HOME +";sh launchall.sh")
               
def deploy_and_benchall():
     deploy()
     benchall()    

