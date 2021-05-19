package Utility;

import Model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class FileLogger {

    /**
     * Method that creates(if not created already) a file with the designated fileName of "fileLog.txt" and inputs the user that logged in and the current time
     * @param user - passes a User object.
     * @throws IOException
     */
    public static void fileLog(User user) throws IOException {
        String fileName = "login_activity.txt";
        LocalTime currTime = LocalTime.now();

        FileWriter outputFile = new FileWriter(fileName, true);
        PrintWriter printWriter = new PrintWriter(outputFile);

        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("hh:mm a");
        printWriter.println("The user: " + user.getUserName() + " has logged in at " + currTime.format(myFormat));
        outputFile.close();
    }
}
