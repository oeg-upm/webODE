package es.upm.fi.dia.ontology.minerva.server.rmi;

public interface BeaconConstants 
{
    byte[] BEACON = new byte[4];

    int KEEP_ALIVE_THREAD_SLEEP_TIME = 60000; // 1 minute

    int CHECKER_THREAD_SLEEP_TIME   = 2 * KEEP_ALIVE_THREAD_SLEEP_TIME;

    int MAX_QUANTUM = 50000; // 50 seconds
}
