package server.controller;

import common.*;
import common.exceptions.DuplicateFileException;
import common.exceptions.DuplicateUsernameException;
import common.exceptions.NoUserException;
import common.exceptions.WrongPasswordException;
import server.integration.DataAccessor;
import server.model.Account;
import server.model.File;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller extends UnicastRemoteObject implements Catalog {



    public final DataAccessor DA;
    private final Map<Long, RemoteClientAccessPoint> clients;
    private long sessionId = 0;



    public Controller() throws IOException {
        super();

        clients = Collections.synchronizedMap(new HashMap());
        DA = new DataAccessor();
        //testConstructor();
        //TODO make all of the controller CRUD methods and test them
    }

    private void testConstructor() throws IOException, DuplicateUsernameException {
        registerUser("Kyle","kyle");
        byte[] bytes = "Example String".getBytes();
        storeFile(DA.findAccount("kyle",true),"Example", bytes.length);
        DA.deleteAccount("Kyle");
        if(clients == null){
            System.out.println("The client access map was not initialized");
        }
        else {  System.out.println("Client Map initialized successfully");   };
    }

    @Override
    public void registerUser(String username, String password) throws RemoteException, DuplicateUsernameException {
        if (DA.findAccount(username,true) == null) {
            DA.registerUser(new Account(username, password));
        } else {
            throw new DuplicateUsernameException();
        }
    }

    @Override
    public void unregisterUser(String username, String password) throws RemoteException {


     DA.deleteAccount(username);

    }

    @Override
    public Session loginUser(RemoteNotifier notifier, String username, String password) throws RemoteException, NoUserException, WrongPasswordException {

        try{
            Account account = (DA.findAccount(username, false));

            if(account == null){ throw new NoUserException(); }
            if(!account.getPassword().equals(password)){ throw new WrongPasswordException(); }

            if(account!= null && account.getPassword().equals(password)) {

                Session session = new Session(sessionId);
                account.updateSessionId(session.id);
                clients.put((session.id),new RemoteClientAccessPoint(account, notifier));//add session and client with account to map
                sessionId++;
                return session;
            }
        else { return null; }

        } finally{
            DA.updateEntity();
            }

    }






    public void logoutUser(String username, String password) throws RemoteException {

        Account account = DA.findAccount(username,false);
        clients.remove(account.getSessionId());
    }


    @Override
    public List<? extends FileDTO> findAllFiles() throws RemoteException {
        return DA.getFiles();
    }


    @Override
    public void storeFile(UserDTO owner, String name,  int length) throws IOException, RemoteException {
        //if (fileDAO.findFileByName(name) == null) {
        File file = new File((Account) owner, name, length);
        DA.storeFile(file);

        //FileManager.persistFile(name, content);

    }



    @Override
    public String getFileMetaData(long sessionId, String fileName) throws IOException {
        Account account = (Account) clients.get(sessionId).clientAccount;
        if(clients.containsKey(sessionId)){

        }
        return null;
    }

    @Override
    public void updateFile(UserDTO owner, String name, byte[] content, boolean privateAccess, boolean publicWrite, boolean publicRead) throws IOException {
        //TODO Needs to notify
    }

    @Override
    public void deleteFile(UserDTO user, String name) throws IOException {
        //TODO Needs to notify

    }

    @Override
    public void notify(UserDTO user, String name, RemoteNotifier outputHandler) throws RemoteException {
        //TODO Needs to be done
    }

    @Override
    public void uploadFile(long sessionId, int size, String name) throws IOException, DuplicateFileException {
        if(DA.getFile(name,false) != null ) { throw new DuplicateFileException(); }

        storeFile(clients.get(sessionId).clientAccount,name,size);
    }

    @Override
    public void downloadFile(long id, String fileName) throws RemoteException {
        if(clients.containsKey(id)){
            StringBuilder fileMetaData = new StringBuilder();
            File file = DA.getFile(fileName, false);
            String string;
            if(file == null) {
                clients.get(id).notifier.outputMessage("File not found");   }
            fileMetaData.append("Downloaded File has properties -> File name: ");
            fileMetaData.append(file.getName());
            fileMetaData.append(" File owner: ");
            fileMetaData.append(file.getOwner().getUsername());
            fileMetaData.append(" File size: ");
            fileMetaData.append(file.getDimension());
            clients.get(id).notifier.outputMessage(fileMetaData.toString());
            notifyUser(file);
            }
    }

    private void notifyUser(File file) throws RemoteException {
        Account account = file.getOwner();
        if(clients.containsKey(account.getSessionId())){
            clients.get(account.getSessionId()).notifier.outputMessage("File: " + file.getName() + " Has been accessed");
        }

    }

    @Override
    public byte[] getFile(UserDTO user, String name) throws IOException {

        //TODO Needs to notify
        return new byte[0];
    }
}
