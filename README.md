BeeHive
=======

Bee population simulation using Tortuga.

Documentation
=============

Running simulation
------------------

This project can be downloaded as ZIP or checked out using Git and imported as existing project to Eclipse IDE.

The simulation could be exported as jar file or started directly in Eclipse (main class is BeeSimulation.java).

The configuration is done by commandline parameters and property files. The commandline parameters are the following:

  h - prints usage information \n
  n - number of hive groups, [int], required
  s - number of hives in each group, [int], required
  c - display control panel
  g - display graphical user interface
  r - generate report (into report.csv).

The default.properties file contains further input data. This file should not be changed. Changes should be applied 
to the custom.properties file.

JVM Tuning
----------

The Tortuga framework creates a new thread for each entity. The JVM preserves system resources for every thread.
So it is recommended to use the default input data in the default.properties file and increase number of bees per hive
or the number of hives in small steps.

Config on my Windows machine (32bit jdk1.6.0_20): -Xmx64m -Xss6k -XX:MaxPermSize=8m

The less Xmx available, the more threads can be spawned. The smaller the Xss the more threads can be spawned. MaxPermSize is also reduced to make more space for threads.

I am able to spawn up to 24443 flower threads with this config.
