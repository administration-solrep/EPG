#!/bin/sh

for i in 1 2 3 5 15; do
	make bench-scenario$i
	sleep 10
done



