#!/bin/sh

echo ""
echo "BeeSimulation Runscript"
echo "-----------------------"
echo ""
echo "Compiling helpclass..."
javac -source "1.6" -target "1.6" factor.java
echo "Compilation complete."
rm log.txt
hiveNumbers=10
for i in `java factor $hiveNumbers`
do
	hiveValue=`echo $i | sed -e 's/^ *//g' -e 's/ *$//g'`
	hivesPerGroup=`expr $hiveNumbers / $hiveValue`
	echo "n = $hiveValue, s = $hivesPerGroup"
	java -jar BeeSimulation.jar -n $hiveValue -s $hivesPerGroup -r >> log.txt
	collapsedFile=collapsed.n$hiveValue.s$hivesPerGroup.in.txt
	rm $collapsedFile
	tail -n 1 report.csv >> $collapsedFile
done
