import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by Gennady Stanchik.
 */

public class SecondTaskThread extends Thread {

    private ArrayList<String> allStrings;

    public SecondTaskThread(ArrayList<String> allStrings) {
        this.allStrings = allStrings;
    }

    public void run(){
        fillingOut2(allStrings);
    }

    private static void fillingOut2(ArrayList<String> allStrings) {
        TreeSet<String> ts = new TreeSet<String>(Main.getComparatorByAlphabet());
        Scanner sc;
        for (String allString : allStrings) {
            sc = new Scanner(allString);
            while (sc.hasNext()) {
                ts.add(sc.next());
            }
        }
        Iterator<String> it = ts.iterator();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("juice2.out"));
            while (it.hasNext()) {
                out.write(it.next());
                out.write(" ");
            }
            out.close();
        }
        catch (IOException io){
            System.err.println("Error writing to file juice2.out");
        }

    }
}
