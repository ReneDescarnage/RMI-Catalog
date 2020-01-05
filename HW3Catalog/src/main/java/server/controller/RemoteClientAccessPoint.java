package server.controller;

import common.FileDTO;
import common.RemoteNotifier;
import common.UserDTO;
import server.model.Account;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class RemoteClientAccessPoint  {

    public UserDTO clientAccount;
    public RemoteNotifier notifier;

    public RemoteClientAccessPoint(UserDTO account, RemoteNotifier notifier){
        clientAccount = account;
        this.notifier = notifier;
    }
}
