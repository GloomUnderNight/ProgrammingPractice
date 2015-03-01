import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FirstTaskThread extends Thread{

    private ArrayList<String> allStrings;

    public FirstTaskThread(ArrayList<String> allStrings) {
        this.allStrings = allStrings;
    }


    public  void run(){
        fillingOut1(allStrings);
    }

    private static void fillingOut1(ArrayList<String> allStrings) {
        ArrayList<String> arr = new ArrayList<String>();
        Scanner sc;
        String tmpLine;
        for (String allString : allStrings) {
            sc = new Scanner(allString);
            while (sc.hasNext()) {
                tmpLine = sc.next();
                if (!arr.contains(tmpLine)) {
                    arr.add(tmpLine);
                }
            }
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("juice1.out"));
            for (String anArr : arr) {
                out.write(anArr);
                out.write(" ");
            }
            out.close();
        }
        catch (IOException io) {
            System.err.println("Error writing to juice1.out");
        }
    }
}
