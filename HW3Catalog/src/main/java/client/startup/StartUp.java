package client.startup;

import client.view.ClientShell;
import common.Catalog;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class StartUp {

    public static void main(String[] args) {
        try {
            Catalog catalog = (Catalog) Naming.lookup("catalog");
            System.out.println("Catalog acquired");
            new ClientShell().start(catalog);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println("Could not start catalog shell!");
        }
    }
}

