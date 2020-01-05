package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteNotifier extends Remote  {
    void outputMessage(FileDTO file, String message, UserDTO accessor) throws RemoteException;
    void outputMessage( String message) throws RemoteException;
}