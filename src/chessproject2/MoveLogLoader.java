
package chessproject2;

import java.io.File;
import java.util.Scanner;

/*
Loads all the files with .txt in it to display the moves in each game
and allows users to choose what file they want to look at
*/
public class MoveLogLoader {

    public static String chooseLogFile(Scanner scanner) {
        File moveFolder = new File("moves");
        File[] files = moveFolder.listFiles((dir, name) -> name.endsWith("_moves.txt"));

        if (files == null || files.length == 0) {
            System.out.println("No move logs found.");
            return null;
        }

        System.out.println("Available game logs: ");
        for (int i = 0; i < files.length; i++) {
            System.out.println("(" + (i + 1) + ") " + files[i].getName());
        }

        while (true) {
            System.out.println("Select a log file number (or 'x' to go back): ");
            String input = scanner.next();

            if (input.equalsIgnoreCase("x")) {
                return "BACK";
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice > 0 && choice <= files.length) {
                    // Pass only the gameName, not the full path
                    String filename = files[choice - 1].getName();
                    return filename.replace("_moves.txt", ""); 
                } else {
                    System.out.println("Select a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Integer input needed.");
                scanner.nextLine();
            }
        }
    }
}
