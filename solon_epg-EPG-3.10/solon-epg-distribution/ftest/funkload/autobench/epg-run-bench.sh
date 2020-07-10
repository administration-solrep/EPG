mkdir -p /root/epg/results-bench/intesword-nightly-benchs/$(date +%Y%m%d)
export REPORT_HOME=/opt/epg-bench-reports/intesword-nightly-benchs/$(date +%Y%m%d)
export REPORT_FREQ=1:10:15:20:25:30
rm -fr /tmp/tmp*funkload*
rm -f /tmp/fl*.xml
cd /root/epg/funkload/autobench
fab -H idlv-solon-intej3.lyon-dev2.local deploy_and_benchall
