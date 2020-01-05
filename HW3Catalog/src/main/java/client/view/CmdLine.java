package client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java. util. Scanner;

class CmdLine {

    private    Scanner scanner = new Scanner( System. in);
    private ArrayList<String> arguments = new ArrayList<>();
    private Command cmd;

    /**
     * Constructor for CmdLine is responsible for processing entered text
     * @param rawText User input.
     */
    CmdLine(String rawText) {
        parseRawText(rawText);
    }

    /**
     * @param rawText User input.
     */

    private void parseRawText(String rawText) {
        StringTokenizer tokenizer = new StringTokenizer(rawText);

        if (tokenizer.countTokens() == 0) {
            this.cmd = Command.NO_COMMAND;
        }

        String command = tokenizer.nextToken().toUpperCase();
        this.cmd = Command.valueOf(command);
        switch (command) {
            case "REGISTER":
            case "UNREGISTER":
                case "LOGIN":
            case"LOGOUT":
                System.out.print("Enter username: ");
                arguments.add( scanner.nextLine());
                System.out.print("Enter password: ");
                arguments.add( scanner.nextLine());
                break;
            case "UPLOAD":
            case "DOWNLOAD":
                System.out.print("Enter filename: ");
                arguments.add( scanner.nextLine());
                break;


            case "QUIT":
                this.cmd = Command.QUIT;
                break;
            case "START":
                this.cmd = Command.START;
                break;
        }
    }

    /**
     * @return The stored command type.
     */
    Command getCmd() {
        return cmd;
    }

    /**
     * @return one of the arguments stored in the line. Different
     * arguments have different index codes
     * A guess string = 0
     */

    public String getArgument(int index){
        return arguments.get(index);
    }

}
