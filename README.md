# Jobs4J
A simple multi-threaded pure Java job management library.

A job is a self-contained unit of work that can be queued up and run on a seperate thread without holding up the rest of the Java application/service.
Job management is simply the process of allowing code to queue and execute jobs - in our case to run them on a pool of threads within the JVM.

I have found myself writing and rewriting custom versions of this for multiple projects, and while its simple enough - sometimes it nice to have a tested implementation ready to go. It might also be convienent to have it open source so that anyone can add features as we go. To be clear, this is not intended to be a replacement for a proper cluster provisioning system - it is just threads within a single JVM.
