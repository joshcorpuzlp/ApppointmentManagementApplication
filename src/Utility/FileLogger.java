package Utility;

import Model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FileLogger {

    /**
     * Method that creates(if not created already) a file with the designated fileName of "fileLog.txt" and inputs the user that logged in and the current date and time
     * @param user - passes a User object.
     * @throws IOException
     */
    public static void fileLog(User user) throws IOException {
        String fileName = "login_activity.txt";
        LocalDateTime currTime = LocalDateTime.now();

        FileWriter outputFile = new FileWriter(fileName, true);
        PrintWriter printWriter = new PrintWriter(outputFile);

        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a MM/dd/yyyy");
        printWriter.println("The user: " + user.getUserName() + " has logged in at " + currTime.format(myFormat) );
        outputFile.close();
    }

    /**
     * Method that creates(if not created already) a file with the designated fileName of "fileLog.txt" and inputs the string used to log in and the current date and time
     * @param userName - Passes a string used during the log in attempt.
     * @throws IOException
     */
    public static  void invalidLoginLog(String userName)  throws IOException {
        String fileName = "login_activity.txt";
        LocalDateTime currTime = LocalDateTime.now();

        FileWriter outputFile = new FileWriter(fileName, true);
        PrintWriter printWriter = new PrintWriter(outputFile);

        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a MM/dd/yyyy");
        printWriter.println("A log in attempt with the username input of: " + userName + " occurred at: " + currTime.format(myFormat));
        outputFile.close();
    }
}
