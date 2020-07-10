TASKS="$TASKS injection"

INJECTEURARCHIVENAMEPATTERN="solrep-injection-solonEPG-*-bin.zip"
INJECTEURARCHIVEPATH="../lib/"

function process_injection(){
  local processdir="$(readlink -f "$1")"
  echo "Injection on [$1] --> [$processdir]"
  local currentDir=$(pwd)

  local RESULTDIR=$BASERESULTDIR/result-$(basename $processdir)
  mkdir -p $RESULTDIR
  local logfile=$RESULTDIR/log.txt

  echo "$(date) == PREPARE INJECTEUR ==" | tee $logfile

  local injecteurzip=$(find $INJECTEURARCHIVEPATH -name "$INJECTEURARCHIVENAMEPATTERN")
  echo "injecteurzip=$injecteurzip" | tee $logfile

  if [ -z "$injecteurzip" ]; then
    echo "Cannot found injecteur" | tee $logfile
  elif [ ! -r "$injecteurzip" ]; then
    echo "Cannot read injecteur [$injecteurzip]" | tee $logfile
  else
     local TMPDIR=$BASERESULTDIR/tmp-$(basename $processdir)
     mkdir -p $TMPDIR

    echo "== UNZIP INJECTEUR ==" | tee $logfile

    cp $injecteurzip $TMPDIR/
    (cd $TMPDIR && unzip -o $(basename $injecteurzip) ) 2>&1 | tee $logfile
    rm $TMPDIR/$(basename $injecteurzip)
    local INJECTDIR=$TMPDIR/$(basename $injecteurzip "-bin.zip")

    echo "$(date) == PROCESS DATA TO INJECT =="

    local aline=
    cat $1/inject.txt | (
      while read aline; do
        if [ -n "$aline" ]; then
          private_doinject "$currentdir" "$INJECTDIR" "${APP_URL}" "$processdir/datas" "$logfile" $aline
        fi
      done
    )

    rm -rf $TMPDIR
  fi
echo "==========================================="
  echo "Injecton done."
}

# param <currentdir> <injectdir> <url> <processdir> <logfile> <file> <feuil> <file2>
function private_doinject(){
  local currentdir="$1"
  local injectdir="$2"
  local url="$3"
  local processdir="$4"
  local logfile="$5"
  local file1="$6"
  local feuille="$7"
  local file2="$8"

  echo "$(date)== DO INJECT ==" | tee $logfile
  echo "ARGS : $@" | tee $logfile
  if [ ! -r "$processdir/$file1" ]; then 
    echo "ERROR Cannot read [$processdir/$file1]" | tee $logfile
  elif [ ! -r "$processdir/$file2" ]; then
    echo "ERROR Cannot read [$processdir/$file2]" | tee $logfile
  else 

    cd $injectdir
    echo doInjectOEP.sh "$url" "$processdir/$file1" "$feuille" "$processdir/$file2" | tee $logfile
    sh doInjectOEP.sh "$url" "$processdir/$file1" "$feuille" "$processdir/$file2" 

    # wait for the end of the injection

    local END=0
    while [ $END -lt 1 ]
    do
        grep -R  "Job completed" ./log/
        if [ $? == 1 ]; then
                echo -----Attente fin injection OEP  | tee $logfile
                echo -----sleep 10  | tee $logfile
                sleep 10
        else
                echo -----Fin injection OEP  | tee $logfile
                END=1
        fi
    done

    #kill injection
    ps aux | grep java | grep xdb | grep -v grep | awk '{print $2}' | xargs kill

    mv $(find ./log/ -name "*.txt") $(dirname $logfile)/

    cd $currentDir

  fi
}

function postprocess_injection(){
  echo "No postprocess for injection"
}

