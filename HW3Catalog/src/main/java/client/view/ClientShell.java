package client.view;

import client.FileUtility;
import client.network.OutputHandler;
import common.*;
import common.exceptions.DuplicateFileException;
import common.exceptions.DuplicateUsernameException;
import common.exceptions.NoUserException;
import common.exceptions.WrongPasswordException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;


public class ClientShell implements Runnable{
    private boolean receivingCmds = false;

    private final Scanner console = new Scanner(System.in);
    private final String PROMPT = ">>";
    private Catalog catalog;
    private Session currentSession;
    private Terminal terminal;
    public RemoteNotifier notifier;
    private static FileUtility fileUtility = new FileUtility();

    private final ConsoleOutput output = new ConsoleOutput();

    public ClientShell() throws RemoteException {
    }

    public void start(Catalog catalog){
        if (receivingCmds) {
            return;
        }
        this.catalog = catalog;

        try {    this.notifier = new NotificationProvider();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println(fileUtility.readFile("example"));

        receivingCmds = true;

        new Thread(this).start();

    }
    public void run() {
        while (receivingCmds) {
            try {
                CmdLine cmdLine = new CmdLine(readNextLine());
                Command cmd = cmdLine.getCmd();

                if(needsToBeLoggedIn(cmd)) { Terminal.notLoggedIn(); continue;  }

                switch (cmdLine.getCmd()) {
                    case LOGIN:
                        login(notifier,cmdLine.getArgument(0),cmdLine.getArgument(1));
                        break;
                    case QUIT:

                        receivingCmds = false;
                        break;


                    case REGISTER:
                        registerUser(cmdLine.getArgument(0),cmdLine.getArgument(1));


                        break;
                    case UNREGISTER:
                        output.handleMsg("Unregistering");
                        catalog.unregisterUser(cmdLine.getArgument(0),cmdLine.getArgument(1));
                        output.handleMsg("user Unregistered");
                        break;
                    case UPLOAD:
                        output.handleMsg("Uploading file");
                        uploadFile(cmdLine);
                        catalog.uploadFile(currentSession.id,fileUtility.readFile(cmdLine.getArgument(0)),cmdLine.getArgument(0));
                        break;
                    case DOWNLOAD:
                        catalog.downloadFile(currentSession.id,cmdLine.getArgument(0));
                        break;
                    case LOGOUT:
                        output.handleMsg("Logging out");
                        UnicastRemoteObject.unexportObject(notifier, false);
                        currentSession = null;


                    default: break;

                }
            } catch (Exception e) {
                //outMgr.println("Operation failed");
            }
        }
    }

    private void registerUser(String username, String password) throws RemoteException {
        try {
            catalog.registerUser(username, password);
        } catch(DuplicateUsernameException DupEx){
            output.handleMsg("A username with that name already exists ");

            System.out.print("Enter username: ");
            username = console.nextLine();
            System.out.print("Enter password: ");
            password = console.nextLine();
            registerUser(username,password);
            }

    }

    private void uploadFile(CmdLine cmdLine) throws IOException, DuplicateFileException {
        try {
            catalog.uploadFile(currentSession.id, fileUtility.readFile(cmdLine.getArgument(0)), cmdLine.getArgument(0));
        }catch(DuplicateFileException DuplicateFile){ output.handleMsg("There is already a file with that name");  }
    }

    private void login(RemoteNotifier notifier, String username, String password) throws RemoteException, WrongPasswordException, NoUserException {
        try {
            currentSession = catalog.loginUser(notifier, username, password);
        }catch (NoUserException noUser) { output.handleMsg("No user with that username"); }
         catch (WrongPasswordException wrongPassword) { output.handleMsg("Incorrect password"); }
        if(currentSession == null) { output.handleMsg("login failed"); }
    }


    private String readNextLine() {
        Terminal.prompt();
        return console.nextLine();
    }

    private boolean needsToBeLoggedIn(Command command) {
        if (currentSession == null && !command.equals(Command.REGISTER) &&
                !command.equals(Command.UNREGISTER) &&
                !command.equals(Command.LOGIN) &&
                !command.equals(Command.QUIT) &&
                !command.equals(Command.HELP) &&
                !command.equals(Command.NO_COMMAND))
        {   return true;    }

        return false;
    }




    private class ConsoleOutput implements OutputHandler {

        public void handleMsg(String msg) {
           System.out.println((String) msg);
            //outputter.print(Definitions.PROMPT);
        }
    }

    private class NotificationProvider extends UnicastRemoteObject implements RemoteNotifier  {

        public NotificationProvider () throws RemoteException {
        }

        @Override
        public void outputMessage(FileDTO file, String message, UserDTO accessor) throws RemoteException {
            output.handleMsg(Terminal.printNotification(file,message,accessor));
        }
        public void outputMessage(String message) throws RemoteException {
            output.handleMsg(Terminal.printMessage(message));
        }
    }

}
