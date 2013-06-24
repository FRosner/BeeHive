#!/bin/sh

echo ""
echo "BeeSimulation Runscript"
echo "-----------------------"
echo ""
echo "Compiling helpclass..."
javac factor.java
echo "Compilation complete."
echo ""

hiveNumbers=10
for i in `java factor $hiveNumbers`
do
hivevalue=`echo $i | sed -e 's/^ *//g' -e 's/ *$//g'`
echo $hivevalue
hivespergroup=`expr $hiveNumbers / $hivevalue`
echo $hivespergroup
java -jar BeeSimulation.jar -n $hivevalue -s $hivespergroup -g -r -c
done
