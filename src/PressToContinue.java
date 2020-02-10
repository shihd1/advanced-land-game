import java.util.Scanner;

public class PressToContinue {
    public static void pressToContinue() {
        Scanner c = new Scanner(System.in);
        try
        {
            c.nextLine();
        }
        catch(Exception e){
            System.out.println("");
        }
    }
}
