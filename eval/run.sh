#!/bin/bash

reps=10

if [ $# -ne 1 ]
then
  echo "Usage: `basename $0` {numberOfHives}"
  exit 1
fi

echo ""
echo "BeeSimulation Runscript"
echo "-----------------------"
echo ""
echo "Compiling helpclass"
javac -source "1.6" -target "1.6" factor.java && echo "Compilation complete"
echo "Removing results from last run"
rm log.txt
rm errorlog.txt
rm collapsed*.txt
hiveNumbers=$1
echo "Starting simulation runs"
for i in `java factor $hiveNumbers`
do
	hiveValue=`echo $i | sed -e 's/^ *//g' -e 's/ *$//g'` #remove leading and trailing whitespaces
	hivesPerGroup=`expr $hiveNumbers / $hiveValue`
	collapsedFile=collapsed.n$hiveValue.s$hivesPerGroup.in.txt
	
	for (( j=1; j<=$reps; j++ ))
	do
		echo "n = $hiveValue, s = $hivesPerGroup, rep=$j"
		java -jar BeeSimulation.jar -n $hiveValue -s $hivesPerGroup -r >> log.txt 2>> errorlog.txt
		tail -n 1 report.csv | cut -d ";" -f 1 >> $collapsedFile
	done
done
Rscript summary.R
