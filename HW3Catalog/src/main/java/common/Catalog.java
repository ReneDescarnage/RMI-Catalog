package common;

import common.exceptions.DuplicateFileException;
import common.exceptions.DuplicateUsernameException;
import common.exceptions.NoUserException;
import common.exceptions.WrongPasswordException;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Catalog extends Remote {

    void registerUser(String username, String password) throws RemoteException, DuplicateUsernameException;

    void unregisterUser(String username, String password) throws RemoteException;

    Session loginUser(RemoteNotifier notifier, String username, String password) throws RemoteException, NoUserException, WrongPasswordException;

    List<? extends FileDTO> findAllFiles() throws RemoteException;

    void storeFile(UserDTO owner, String name, int length) throws IOException;

    byte[] getFile(UserDTO user, String name) throws IOException;

    String getFileMetaData(long sessionId, String fileName) throws IOException;

    void updateFile(UserDTO owner, String name, byte[] content, boolean privateAccess, boolean publicWrite, boolean publicRead) throws IOException;

    void deleteFile(UserDTO user, String name) throws IOException;

    void notify(UserDTO user, String name, RemoteNotifier outputHandler) throws RemoteException;

    void uploadFile(long sessionId, int readFile, String name) throws IOException, DuplicateFileException;

    void downloadFile(long id, String argument) throws RemoteException;
}