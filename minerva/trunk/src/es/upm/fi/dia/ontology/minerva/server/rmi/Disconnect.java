package es.upm.fi.dia.ontology.minerva.server.rmi;

public interface Disconnect extends java.rmi.Remote
{
    void disconnect() throws java.rmi.RemoteException;

    void sendBeacon () throws java.rmi.RemoteException;
}
