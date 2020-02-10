import java.util.Scanner;

public class getLetter {
    public static String getLetter(int x){
        Scanner c = new Scanner(System.in);
        String s;
        int charX;
        try{
            s = c.nextLine().toUpperCase();
            charX = s.charAt(0);
        }catch(Exception e){
            System.out.println("Please enter in a letter");
            return getLetter(x);
        }
        if(charX >= 65 && charX <= x+65){
            return s;
        }else{
            System.out.println("Please enter in a letter that is listed in one of the boxes");
            return getLetter(x);
        }
    }
}
