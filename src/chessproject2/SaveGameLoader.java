
package chessproject2;

import java.io.File;
import java.util.Scanner;

/** SaveGameLoader is responsible for listing available save files and 
 * allowing the user to choose one from the cui
 * 
 * Save files are stored inside the saves folder
 * It only uses files ending with .txt
 * The user is prompted to pick up a file by entering its number
 * If no save files are found, back is returned which loops back to the mainmenuLoop
 *
 * 
 */
public class SaveGameLoader {
    
    
    public static String chooseSaveFile(Scanner scanner)
    {
        //Locates the saves folder
        File saveFolder = new File("saves");
        
        //Gets all .txt files inside saves
        File[] files = saveFolder.listFiles((dir,name) -> name.endsWith(".txt"));
        
        //if no files exist, returns back
        if(files == null || files.length == 0)
        {
            System.out.println("No save files found.");
            return "BACK";
        }
       
        //Prints out a list of available save files with numbers
        System.out.println("Available save files: ");
        for(int i = 0; i < files.length; i++)
        {
            System.out.println("(" + (i+1) + ") " + files[i].getName());
        }
        
        //Asks repeatedly until a user picks a valid number
        while (true)
        {
            System.out.println("Select a save file number: ");
            String input = scanner.next();
            
            try {
                int choice = Integer.parseInt(input);
                //Checks if choice is within range 
                if(choice > 0 && choice <= files.length)
                {
                    //Returns path of the selected file
                    return files[choice - 1].getPath();
                } else {
                    System.out.println("Select a valid number.");
                }
            } catch (NumberFormatException e)
            {
                //If input wasnt a valid integer
                System.out.println("Integer input needed.");
                scanner.nextLine(); //clear invalid input
            }
        }
    }
}
