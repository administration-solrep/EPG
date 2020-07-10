sh gather_syntetic_report_data.sh 20120606 | python -c "import sys; print('\n'.join(' '.join(c) for c in zip(*(l.split() for l in sys.stdin.readlines() if l.strip()))))" | sed 's/\./,/g'
