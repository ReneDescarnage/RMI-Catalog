package server.startup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.controller.Controller;

/**
 * Starts the bank server.
 */
public class Server {
    private static final String USAGE = "java bankjpa.Server [bank name in rmi registry]";

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startRMIServant();
            System.out.println("Catalog server started.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Failed to start catalog server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRMIServant() throws IOException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller contr = new Controller();
        Naming.rebind("catalog", contr);





    }
}