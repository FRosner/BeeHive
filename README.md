BeeHive
=======

Bee population simulation using Tortuga.

JVM Tuning
----------

Config on my Windows machine (32bit jdk1.6.0_20): -Xmx64m -Xss6k -XX:MaxPermSize=8m

The less Xmx available, the more threads can be spawned. The smaller the Xss the more threads can be spawned. MaxPermSize is also reduced to make more space for threads.

I am able to spawn up to 24443 flower threads with this config.
