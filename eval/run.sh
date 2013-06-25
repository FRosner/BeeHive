#!/bin/sh

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
echo "Compiling helpclass..."
javac -source "1.6" -target "1.6" factor.java
echo "Compilation complete."
rm log.txt
hiveNumbers=$1
for i in `java factor $hiveNumbers`
do
	hiveValue=`echo $i | sed -e 's/^ *//g' -e 's/ *$//g'`
	hivesPerGroup=`expr $hiveNumbers / $hiveValue`
	collapsedFile=collapsed.n$hiveValue.s$hivesPerGroup.in.txt
	rm $collapsedFile
	
	for ((j=1;j<=$reps;j++));
	do
		echo "n = $hiveValue, s = $hivesPerGroup, rep=$j"
		java -jar BeeSimulation.jar -n $hiveValue -s $hivesPerGroup -r >> log.txt
		tail -n 1 report.csv | cut -d ";" -f 1 >> $collapsedFile
	done
done
