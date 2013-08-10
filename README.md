BeeHive
=======

Bee population simulation using Tortuga.

Documentation
-------------

### Running simulation

This project can be downloaded as ZIP or checked out using Git and imported as existing project to Eclipse IDE.

The simulation could be exported as jar file or started directly in Eclipse (main class is BeeSimulation.java).

The configuration is done by commandline parameters and property files. The commandline parameters are the following:

  h - prints usage information  
  n - number of hive groups, [int], required  
  s - number of hives in each group, [int], required  
  c - display control panel  
  g - display graphical user interface  
  r - generate report (into report.csv).  

The default.properties file contains further input data. This file should not be changed. Changes should be applied 
to the custom.properties file. Changes in the custom.properties will overwrite those in the default.properties.

### JVM Tuning

The Tortuga framework creates a new thread for each entity. The JVM preserves system resources for every thread.
So it is recommended to use the default input data in the default.properties file and increase number of bees per hive
or the number of hives in small steps.
