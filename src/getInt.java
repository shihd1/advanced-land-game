import java.util.Scanner;

public class getInt {
    public static int getInt(){
        Scanner c = new Scanner(System.in);
        int a;
        try{
            a = c.nextInt();
        } catch(Exception e){
            System.out.println("Please enter a number");
            return getInt();
        }
        return a;
    }
}
