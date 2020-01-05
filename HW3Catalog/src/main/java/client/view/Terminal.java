package client.view;

import common.FileDTO;
import common.UserDTO;

public class Terminal {
    public static void notLoggedIn() {
        System.out.println("You need to be logged in to access the catalog");

    }

    public static void prompt() {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Catalog >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.print("--->>>> Enter Command: ");

    }

    public static String logInFailed() {

        return "Login Failed";


    }

    public static String printMessage(String message) {
        return message;
    }

    public static String printNotification(FileDTO file, String message, UserDTO accessor) {
        return ("File " + file.getName() + " was " + message + " by " + accessor.getUsername());
    }

    //TODO metadata upload prompt
}
