pushd /root/epg/funkload/injecteur/solrep-injection-solonEPG-1.0.0-SNAPSHOT >> /dev/null
sh doInject.sh http://idlv-solon-intej3.lyon-dev2.local:8180/solon-epg/ /tmp/injection-validation | grep log | awk -F'['  '{print $2 }' | tr -d ']'
popd >> /dev/null

